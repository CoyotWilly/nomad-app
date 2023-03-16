package com.coyotwilly.nomad

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationBarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // sets transparent background for status bar
        // REQUIRES SDK >= 24
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        navigationView = findViewById(R.id.bottom_navigation)
        var bgColor: Int = R.color.white
        when(navigationView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> bgColor = R.color.shape_bg_dark
            Configuration.UI_MODE_NIGHT_NO -> bgColor = R.color.shape_bg
            Configuration.UI_MODE_NIGHT_UNDEFINED -> println("NIGHT undefined, JavaSpript Dev KEeeee")
        }

        when(navigationView.background){
            is ShapeDrawable -> (navigationView.background as ShapeDrawable).paint.color = ContextCompat.getColor(navigationView.context, bgColor)
            is GradientDrawable -> (navigationView.background as GradientDrawable).setColor(ContextCompat.getColor(navigationView.context,bgColor))
            is ColorDrawable -> (navigationView.background as ColorDrawable).color = ContextCompat.getColor(navigationView.context, bgColor)
        }

        supportFragmentManager.beginTransaction().replace(R.id.body_container, HomeFragment()).commit()
        navigationView.setOnItemSelectedListener setNavigationItemSelectedListener@{ menuItem ->
            var fragment: Fragment = HomeFragment()
            when (menuItem.itemId) {
                R.id.nav_home -> fragment = HomeFragment()
                R.id.nav_like -> fragment = LikeFragment()
                R.id.nav_search -> fragment = SearchFragment()
                R.id.nav_shop -> fragment = ShopFragment()
                R.id.nav_person -> fragment = PersonFragment.newInstance(R.layout.fragment_pin_auth, false)
            }
            supportFragmentManager.beginTransaction().replace(R.id.body_container, fragment).commit()
            return@setNavigationItemSelectedListener true
        }
    }
}