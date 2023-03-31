package com.coyotwilly.nomad

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.arraySetOf
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.coyotwilly.nomad.model.ActiveTrips
import com.coyotwilly.nomad.model.FutureTrips
import com.coyotwilly.nomad.model.PastTrips
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private val MIN_SWIPE_DISTANCE = 550

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
        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)){
            val availableViews: Set<Int> = arraySetOf(R.id.active_bg, R.id.past_bg, R.id.community_bg)
            for (element in availableViews){
                val navController = view.findViewById<ConstraintLayout>(element)
                ThemeWatcher(navController)
            }
            themeChanged = view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }
        val activeCards = view.findViewById<View>(R.id.active_trip)
        view.findViewById<ImageButton>(R.id.edit_pen).setOnClickListener {
            startActivity(Intent(this.context, FutureTripCreate::class.java))
        }
        activeCards.setOnTouchListener { v, event ->
            val displayMetrics = resources.displayMetrics
            val cardWidth = activeCards.width
            val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)
            when(event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX

                    if (newX - cardWidth < cardStart) {
                        activeCards.animate()
                            .x(min(cardStart, newX - (cardWidth / 2)))
                            .setDuration(0)
                            .start()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    var currentX = activeCards.x
                    activeCards.animate()
                        .x(cardStart)
                        .setDuration(150)
                        .setListener(
                            object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                                        delay(100)
                                        if ( currentX < MIN_SWIPE_DISTANCE) {
                                            currentX = 0f
                                        }
                                    }
                                }
                            }
                        )
                }
            }
            v.performClick()
            return@setOnTouchListener true
        }
    }
    private fun fetchView() {
        var futureTrips: List<FutureTrips> = arrayListOf()
        var activeTrips: List<ActiveTrips> = arrayListOf()
        var pastTrips: List<PastTrips> = arrayListOf()
        runBlocking {
            val job = launch {
                //val userId = requireArguments().getLong("userId") to replace 1
                val service = UserService.create()
                futureTrips = service.getFutureTrips(1)
                activeTrips = service.getActiveTrips(1)
                pastTrips = service.getPastTrips(1)
            }
            job.join()
        }
        cardMaker(futureTrips)
        requireView().findViewById<TextView>(R.id.upcoming_trips_counter).text = getString(R.string.trip_counter, futureTrips.size)
        requireView().findViewById<TextView>(R.id.trip_counter).text = getString(R.string.trip_counter, activeTrips.size)
        requireView().findViewById<TextView>(R.id.past_trips_counter).text = getString(R.string.trip_counter, pastTrips.size)
    }

    private fun cardMaker(trips: List<FutureTrips>) {
        for(trip in trips){
            val array = Base64.decode(trip.imgBackground!!.content, Base64.DEFAULT)
            trip.imgBackground.bitmap = BitmapFactory.decodeByteArray(array, 0, array.size)

            requireView().findViewById<ImageView>(R.id.destination_background_img).setImageBitmap(trip.imgBackground.bitmap)
            requireView().findViewById<TextView>(R.id.destination).text = trip.destination
            requireView().findViewById<TextView>(R.id.travel_period).text = getString(R.string.travel_period_template, trip.startDate, trip.endDate)
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