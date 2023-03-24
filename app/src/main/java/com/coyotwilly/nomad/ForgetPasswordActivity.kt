package com.coyotwilly.nomad

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.coyotwilly.nomad.service.LoginCredentials
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        findViewById<EditText>(R.id.password_reset_new_in).setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                sendPasswordResetRequest()
                resetDone()
            }
            return@setOnKeyListener false
        }

        findViewById<Button>(R.id.password_reset_confirm_bt).setOnClickListener {
            sendPasswordResetRequest()
            resetDone()
        }

        findViewById<ImageButton>(R.id.password_reset_back_ic).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun sendPasswordResetRequest() {
        val email = findViewById<EditText>(R.id.password_reset_email_in).text.toString()
        val newPassword = findViewById<EditText>(R.id.password_reset_new_in).text.toString()
        runBlocking {
            val job = launch {
                UserService.create().userPasswordReset(LoginCredentials(email, newPassword))
            }
            job.join()
        }
    }

    private fun resetDone() {
        findViewById<TextView>(R.id.success_msg_password_reset).visibility = View.VISIBLE
        object: CountDownTimer(500,1){
            override fun onTick(millisUntilFinished: Long) { }

            override fun onFinish() {
                startActivity(Intent(this@ForgetPasswordActivity, LoginActivity::class.java))
                finish()
            }
        }.start()
    }
}