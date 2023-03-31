package com.coyotwilly.nomad

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import com.coyotwilly.nomad.model.User
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            var isPasswd = false
            var isDocumentNo = false
            var isPassportNo = false
            var isEmail = false
            var isPin = false
            if (findViewById<EditText>(R.id.passport_no_in).text.toString().isBlank()){
                findViewById<EditText>(R.id.passport_no_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.passport_no_in))
            } else {
                isPassportNo = true
            }
            if (findViewById<EditText>(R.id.document_no_in).text.toString().isBlank()) {
                findViewById<EditText>(R.id.document_no_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.document_no_in))
            } else {
                isDocumentNo = true
            }
            if ((findViewById<EditText>(R.id.app_pin_in).text.toString().length != 4) or (findViewById<EditText>(R.id.app_pin_in).text.toString().isBlank())) {
                findViewById<EditText>(R.id.app_pin_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.app_pin_in))
            } else {
                isPin = true
            }
            if (findViewById<EditText>(R.id.passwd_repeat_in).text.toString() != findViewById<EditText>(R.id.passwd_in).text.toString()) {
                findViewById<EditText>(R.id.passwd_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.passwd_in))
                findViewById<EditText>(R.id.passwd_repeat_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.passwd_repeat_in))
            } else {
                isPasswd = true
            }
            if (findViewById<EditText>(R.id.email_in).text.toString().isBlank()){
                findViewById<EditText>(R.id.email_in).setBackgroundResource(R.drawable.background_danger_zone)
                ThemeWatcher(findViewById<EditText>(R.id.email_in))
            } else {
                isEmail = true
            }

            if (isPasswd and isDocumentNo and isPin and isPassportNo and isEmail){
                var apartmentNo = 0
                if (findViewById<EditText>(R.id.apartment_no_in).text.toString().isNotBlank()) {
                    apartmentNo = Integer.parseInt(findViewById<EditText>(R.id.apartment_no_in).text.toString())
                }
                runBlocking {
                    val job = launch {
                        val request = UserService.create()
                        request.createUser(
                            User(
                                0,
                                Integer.parseInt(findViewById<EditText>(R.id.app_pin_in).text.toString()),
                                findViewById<EditText>(R.id.login_in).text.toString(),
                                findViewById<EditText>(R.id.email_in).text.toString(),
                                findViewById<EditText>(R.id.passwd_in).text.toString(),
                                findViewById<EditText>(R.id.first_name_in).text.toString(),
                                findViewById<EditText>(R.id.last_name_in).text.toString(),
                                findViewById<EditText>(R.id.passport_no_in).text.toString(),
                                findViewById<EditText>(R.id.document_no_in).text.toString(),
                                findViewById<EditText>(R.id.street_in).text.toString(),
                                findViewById<EditText>(R.id.home_no_in).text.toString(),
                                apartmentNo,
                                findViewById<EditText>(R.id.city_in).text.toString(),
                                findViewById<EditText>(R.id.country_in).text.toString(),
                            )
                        )
                    }
                    job.join()
                }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}