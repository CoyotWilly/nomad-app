package com.coyotwilly.nomad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Text at the top of app hiding property
         supportActionBar?.hide()

        navigationView = findViewById(R.id.bottom_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.body_container, HomeFragment()).commit()
        navigationView.setOnItemSelectedListener setNavigationItemSelectedListener@{ menuItem ->
            var fragment: Fragment = Fragment()
            when (menuItem.itemId) {
                R.id.nav_home -> fragment = HomeFragment()
                R.id.nav_like -> fragment = LikeFragment()
                R.id.nav_search -> fragment = SearchFragment()
                R.id.nav_shop -> fragment = ShopFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.body_container, fragment).commit()
            return@setNavigationItemSelectedListener true
        }
    }
}