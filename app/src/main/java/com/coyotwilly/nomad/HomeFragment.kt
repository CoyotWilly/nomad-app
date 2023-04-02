package com.coyotwilly.nomad

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.collection.arraySetOf
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.coyotwilly.nomad.model.ActiveTrips
import com.coyotwilly.nomad.model.FutureTrips
import com.coyotwilly.nomad.model.Image
import com.coyotwilly.nomad.model.PastTrips
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private var destinationList = mutableListOf<String>()
    private var smallDestinationListFuture = mutableListOf<String>()
    private var smallDestinationListPast = mutableListOf<String>()
    private var travelPeriodList = mutableListOf<String>()
    private var backgroundImgList = mutableListOf<Bitmap>()
    private var backgroundImgListCommunity = mutableListOf<Bitmap>()
    private var smallBackgroundImgListFuture = mutableListOf<Bitmap>()
    private var smallBackgroundImgListPast = mutableListOf<Bitmap>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchView()

        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)) {
            val availableViews: Set<Int> = arraySetOf(R.id.active_bg, R.id.past_bg, R.id.community_bg)
            for (element in availableViews) {
                val paintedElement = view.findViewById<ConstraintLayout>(element)
                ThemeWatcher(paintedElement)
            }
            themeChanged = view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }

        view.findViewById<ImageButton>(R.id.edit_pen).setOnClickListener {
            startActivity(Intent(this.context, FutureTrips::class.java))
        }
        // active trips adapter
        val viewPager = view.findViewById<ViewPager2>(R.id.active_trip_page_viewer)
        viewPager.adapter = ViewPagerAdapter(destinationList, travelPeriodList, backgroundImgList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //future trips adapter
        val futureTripsViewPager = view.findViewById<ViewPager2>(R.id.future_trip_view_pager)
        futureTripsViewPager.adapter = SmallViewPagerAdapter(smallDestinationListFuture, smallBackgroundImgListFuture)
        futureTripsViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        val pastTripsViewPager = view.findViewById<ViewPager2>(R.id.past_trip_page_viewer)
        pastTripsViewPager.adapter = SmallViewPagerAdapter(smallDestinationListPast, smallBackgroundImgListPast)
        pastTripsViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        val dummyString = mutableListOf("","","","","")
        val communityPager = view.findViewById<ViewPager2>(R.id.community_exploration)
        communityPager.adapter = ViewPagerAdapter(dummyString, dummyString, backgroundImgListCommunity)
        communityPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
    private fun fetchView() {
        var futureTrips: List<FutureTrips> = arrayListOf()
        var activeTrips: List<ActiveTrips> = arrayListOf()
        var pastTrips: List<PastTrips> = arrayListOf()
        var memoTrips: List<Image> = arrayListOf()
        runBlocking {
            val job = launch {
                //val userId = requireArguments().getLong("userId") to replace 1
                val service = UserService.create()
                futureTrips = service.getFutureTrips(1)
                activeTrips = service.getActiveTrips(1)
                pastTrips = service.getPastTrips(1)
                memoTrips = service.getCommunityPhotos()
            }
            job.join()
        }
        cardMaker(activeTrips)
        cardMakerImages(memoTrips)
        smallCardMakerFuture(futureTrips)
        smallCardMakerPast(pastTrips)
        requireView().findViewById<TextView>(R.id.upcoming_trips_counter).text = getString(R.string.trip_counter, futureTrips.size)
        requireView().findViewById<TextView>(R.id.trip_counter).text = getString(R.string.trip_counter, activeTrips.size)
        requireView().findViewById<TextView>(R.id.past_trips_counter).text = getString(R.string.trip_counter, pastTrips.size)
        requireView().findViewById<TextView>(R.id.explorer_counter).text = getString(R.string.new_memories_counter, memoTrips.size)
    }

    private fun smallCardMakerFuture(futureTrips: List<FutureTrips>) {
        for (trip in futureTrips) {
            val array = Base64.decode(trip.imgBackground!!.content, Base64.DEFAULT)

            smallDestinationListFuture.add(trip.destination)
            smallBackgroundImgListFuture.add(BitmapFactory.decodeByteArray(array,0,array.size))
        }
    }
    private fun smallCardMakerPast(pastTrips: List<PastTrips>) {
        for (trip in pastTrips) {
            val array = Base64.decode(trip.memories[0].content, Base64.DEFAULT)

            smallDestinationListPast.add(trip.destination)
            smallBackgroundImgListPast.add(BitmapFactory.decodeByteArray(array,0,array.size))
        }
    }
    private fun cardMaker(trips: List<ActiveTrips>) {
        for(trip in trips){
            val array = Base64.decode(trip.imgBackground.content, Base64.DEFAULT)

            destinationList.add(trip.destination)
            travelPeriodList.add(getString(R.string.travel_period_template, trip.startDate.subSequence(5,10), trip.endDate))
            backgroundImgList.add(BitmapFactory.decodeByteArray(array, 0, array.size))
        }
    }
    private fun cardMakerImages(shots: List<Image>){
        for (trip in shots) {
            val array = Base64.decode(trip.content, Base64.DEFAULT)
            backgroundImgListCommunity.add(BitmapFactory.decodeByteArray(array,0,array.size))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: Long) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putLong("userId", userId)
                }
            }
    }
}