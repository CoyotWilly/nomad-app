package com.coyotwilly.nomad

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf

class SignInActivity : AppCompatActivity() {
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<ImageButton>(R.id.back_arrow).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)){
            val availableViews: Set<Int> = arraySetOf(R.id.first_name_in, R.id.last_name_in,R.id.email_in, R.id.passwd_in, R.id.passwd_repeat_in,
            R.id.login_in, R.id.app_pin_in, R.id.passport_no_in, R.id.document_no_in, R.id.street_in,R.id.home_no_in, R.id.apartment_no_in, R.id.city_in,R.id.country_in)
            for (element in availableViews){
                val navController = findViewById<EditText>(element)
                ThemeWatcher(navController)
            }
            themeChanged = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }
    }

}