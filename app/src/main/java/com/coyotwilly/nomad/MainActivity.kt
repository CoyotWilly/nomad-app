package com.coyotwilly.nomad

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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
        ThemeWatcher(navigationView)
        supportFragmentManager.beginTransaction().replace(R.id.body_container, HomeFragment()).commit()
        navigationView.setOnItemSelectedListener setNavigationItemSelectedListener@{ menuItem ->
            var fragment: Fragment = HomeFragment()
            val userId = getSharedPreferences("com.coyotwilly.app",Context.MODE_PRIVATE).getLong("com.coyotwilly.app.user.Id", 0L)
            when (menuItem.itemId) {
                R.id.nav_home -> fragment = HomeFragment.newInstance(userId)
                R.id.nav_like -> fragment = LikeFragment()
                R.id.nav_search -> fragment = SearchFragment()
                R.id.nav_shop -> fragment = ShopFragment()
                R.id.nav_person -> fragment = PersonFragment.newInstance(R.layout.fragment_pin_auth, false, userId)
            }

            supportFragmentManager.beginTransaction().replace(R.id.body_container, fragment).commit()
            return@setNavigationItemSelectedListener true
        }
    }
}