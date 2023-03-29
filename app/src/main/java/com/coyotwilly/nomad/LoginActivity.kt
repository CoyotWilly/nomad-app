package com.coyotwilly.nomad

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import com.coyotwilly.nomad.service.LoginCredentials
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private val login: String = "com.coyotwilly.app.user.Username"
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)){
            val availableViews: Set<Int> = arraySetOf(R.id.email_login_form, R.id.passwd_login_form)
            ThemeWatcher(findViewById(R.id.destination_background_img)).imgBackground(findViewById(R.id.destination_background_img))
            for (element in availableViews){
                val navController = findViewById<EditText>(element)
                ThemeWatcher(navController)
            }
            themeChanged = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }

        val btLogin = findViewById<Button>(R.id.button)

        val pref: SharedPreferences = getSharedPreferences("com.coyotwilly.app",Context.MODE_PRIVATE)

        if (pref.contains(login)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btLogin.setOnClickListener {
            viewChangeHandler(pref)
        }

        findViewById<EditText>(R.id.passwd_login_form).setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                viewChangeHandler(pref)
            }
            return@setOnKeyListener false
        }

        findViewById<TextView>(R.id.sign_in_redirect).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // forget password text ACTION
        findViewById<TextView>(R.id.textView2).setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }
    }

    private fun viewChangeHandler(pref: SharedPreferences) {
        val loginText: String = findViewById<EditText>(R.id.email_login_form).text.toString()
        val passwordText: String = findViewById<EditText>(R.id.passwd_login_form).text.toString()
        var canLogin = false
        var id = 0L
        runBlocking {
            val job = launch {
                val service =  UserService.create()
                val credentials = LoginCredentials(loginText, passwordText)
                canLogin = service.canLogin(credentials)
                id = service.idChecker(credentials)
            }
            job.join()
        }

        if (canLogin) {
            pref.edit()
                .putString(login, loginText)
                .putString("com.coyotwilly.app.user.Password", passwordText)
                .putLong("com.coyotwilly.app.user.Id", id)
                .apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            findViewById<EditText>(R.id.email_login_form).text.clear()
            findViewById<EditText>(R.id.email_login_form).requestFocus()
            findViewById<EditText>(R.id.passwd_login_form).text.clear()
        }
    }
}