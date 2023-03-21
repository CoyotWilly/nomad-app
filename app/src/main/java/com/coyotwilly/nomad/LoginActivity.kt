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

class LoginActivity : AppCompatActivity() {
    private val login: String = "username"
    private val password: String = "password"
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)){
            val availableViews: Set<Int> = arraySetOf(R.id.email_login_form, R.id.passwd_login_form)
            for (element in availableViews){
                val navController = findViewById<EditText>(element)
                ThemeWatcher(navController)
            }
            themeChanged = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }

        val btLogin = findViewById<Button>(R.id.button)

        val pref: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

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

        if ((loginText == "admin") and (passwordText == "1234")) {
            pref.edit().putString(login, loginText)
                .putString(password, passwordText)
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