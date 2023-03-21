package com.coyotwilly.nomad

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private val login: String = "username"
    private val password: String = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btLogin = findViewById<Button>(R.id.button)

        val pref: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        if (pref.contains(login)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btLogin.setOnClickListener {
            val loginText: String = findViewById<EditText>(R.id.email_login_form).text.toString()
            val passwordText: String = findViewById<EditText>(R.id.passwd_login_form).text.toString()

            if ((loginText == "admin") and (passwordText == "1234")) {
                pref.edit().putString(login, loginText)
                    .putString(password, passwordText)
                    .apply()
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, "Bad log on", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.email_login_form).text.clear()
                findViewById<EditText>(R.id.email_login_form).requestFocus()
                findViewById<EditText>(R.id.passwd_login_form).text.clear()
            }
        }
    }
}