package com.coyotwilly.nomad


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.collection.arraySetOf
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.coyotwilly.nomad.service.User
import com.coyotwilly.nomad.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * A simple [Fragment] subclass.
 * Use the [PersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonFragment : Fragment(){
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private var userData: User = User()
    private var userId = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = requireArguments().getLong("UserId")
        if (arguments?.getBoolean("wasSuccessful") == false){
            biometricAuth()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        GET REQUEST FOR USER DATA
        runBlocking {
            val job = launch(Dispatchers.Default) {
                withTimeout(500L){
                    userData = UserService.create().getUser(userId)
                }
            }
            job.join()
        }
        return inflater.inflate(arguments?.getInt("currentLayout") ?: R.layout.fragment_pin_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO) and (arguments?.getInt("currentLayout") == R.layout.fragment_person)){
            var availableViews: Set<Int> = arraySetOf(R.id.login_details_box, R.id.email_details_box, R.id.name_details_box, R.id.birth_details_box, R.id.address_details_box, R.id.document_details_box, R.id.passport_details_box, R.id.danger_zone_details_box)
            for (element in availableViews){
                val navController = view.findViewById<ConstraintLayout>(element)
                ThemeWatcher(navController)
            }
            availableViews = arraySetOf(R.id.log_out_bt, R.id.account_delete_bt)
            for (ele in availableViews) {
                val navController = view.findViewById<AppCompatButton>(ele)
                ThemeWatcher(navController).imgBackground(navController)
            }
            themeChanged = view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }
        if (arguments?.getInt("currentLayout") == R.layout.fragment_person){
            if ((userData.id != 0L) and (userData.passportNo != "")) {
                view.findViewById<TextView>(R.id.full_login).text = userData.login
                view.findViewById<TextView>(R.id.email_data).text = userData.emailAddress
                view.findViewById<TextView>(R.id.full_name).text = getString(R.string.full_name, userData.firstName, userData.lastName)
                if (userData.apartmentNo == 0){
                    view.findViewById<TextView>(R.id.address_data).text = getString(R.string.address_home, userData.street , userData.homeNo, userData.city, userData.country)
                } else {
                    view.findViewById<TextView>(R.id.address_data).text = getString(R.string.address_apart, userData.street , userData.homeNo, userData.apartmentNo, userData.city, userData.country)
                }
                view.findViewById<TextView>(R.id.passport_data).text = userData.passportNo
                view.findViewById<TextView>(R.id.birth_data).text = userData.documentNo
            }
            view.findViewById<Button>(R.id.log_out_bt).setOnClickListener {
                this.requireContext().applicationContext.getSharedPreferences("com.coyotwilly.app", Context.MODE_PRIVATE).edit()
                    .remove("com.coyotwilly.app.user.Username")
                    .remove("com.coyotwilly.app.user.Id")
                    .apply()
                startActivity(Intent(this.context, LoginActivity::class.java))
            }
            view.findViewById<Button>(R.id.account_delete_bt).setOnClickListener {
                runBlocking {
                    var resp = ""
                    val job = launch {
                        resp = UserService.create().deleteUserAccount(userId)
                    }
                    job.join()
                    Toast.makeText(context, resp, Toast.LENGTH_SHORT)
                }
                startActivity(Intent(this.context, LoginActivity::class.java))
            }
        }

    }

    private fun pinAuth(view: View){
        // First PIN input box controller
        view.findViewById<EditText>(R.id.pin_no_1).requestFocus()
        view.findViewById<EditText>(R.id.pin_no_1).doAfterTextChanged {
            view.findViewById<EditText>(R.id.pin_no_2).requestFocus()
        }
        view.findViewById<EditText>(R.id.pin_no_2).setOnKeyListener { _, keyCode, _ ->
            if ((keyCode == KeyEvent.KEYCODE_DEL) and (view.findViewById<EditText>(R.id.pin_no_2).text.toString().isEmpty())){
                view.findViewById<EditText>(R.id.pin_no_1).requestFocus()
            }
            return@setOnKeyListener false
        }

        view.findViewById<EditText>(R.id.pin_no_3).setOnKeyListener { _, keyCode, _ ->
            if ((keyCode == KeyEvent.KEYCODE_DEL) and (view.findViewById<EditText>(R.id.pin_no_3).text.toString().isEmpty())){
                view.findViewById<EditText>(R.id.pin_no_2).requestFocus()
            }
            return@setOnKeyListener false
        }

        view.findViewById<EditText>(R.id.pin_no_4).setOnKeyListener { _, keyCode, _ ->
            if ((keyCode == KeyEvent.KEYCODE_DEL) and (view.findViewById<EditText>(R.id.pin_no_4).text.toString().isEmpty())){
                view.findViewById<EditText>(R.id.pin_no_3).requestFocus()
            }
            return@setOnKeyListener false
        }

        // Second PIN input box controller
        view.findViewById<EditText>(R.id.pin_no_2).doAfterTextChanged {
            view.findViewById<EditText>(R.id.pin_no_3).requestFocus()
        }

        // Third PIN input box controller
        view.findViewById<EditText>(R.id.pin_no_3).doAfterTextChanged {
            view.findViewById<EditText>(R.id.pin_no_4).requestFocus()
        }

        // Forth PIN input box controller
        view.findViewById<EditText>(R.id.pin_no_4).doAfterTextChanged {
            var pin: String = view.findViewById<EditText>(R.id.pin_no_1).text.toString() + view.findViewById<EditText>(R.id.pin_no_2).text.toString() + view.findViewById<EditText>(R.id.pin_no_3).text.toString() + view.findViewById<EditText>(R.id.pin_no_4).text.toString()
            if (pin == userData.pin.toString()){
                parentFragmentManager.beginTransaction()
                    .replace(R.id.body_container, newInstance(R.layout.fragment_person,true, userId))
                    .commit()
            } else {
                // full PIN clear timer
                object: CountDownTimer(200,1){
                    override fun onTick(millisUntilFinished: Long) { }

                    override fun onFinish() {
                        view.findViewById<EditText>(R.id.pin_no_1).text.clear()
                        view.findViewById<EditText>(R.id.pin_no_2).text.clear()
                        view.findViewById<EditText>(R.id.pin_no_3).text.clear()
                        view.findViewById<EditText>(R.id.pin_no_4).text.clear()
                        pin = ""
                        view.findViewById<EditText>(R.id.pin_no_1).requestFocus()
                    }
                }.start()
            }
        }
    }
    private fun biometricAuth() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> Log.d("Nomad","App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.e("Nomad","No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.e("Nomad","Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.d("Nomad","No fingerprint assigned")
            else -> Log.e("Nomad","Value not recognized. Biometric authentication failed.")
        }
        val executor = ContextCompat.getMainExecutor(this.requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED){
                        pinAuth(requireView())
                    } else {
                        Toast.makeText(context,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.body_container, newInstance(R.layout.fragment_person,true, userId))
                        .commit()
                }
                override fun onAuthenticationFailed(){
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        val promptInfo = PromptInfo.Builder()
            .setTitle("Verify your identity")
            .setDescription("Use your fingerprint to verify your identity.")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param id Layout view parameter.
         * @param wasSuccessful Was the previous object authenticated successfully.
         * @param userId User ID given from activity, in most cases received from app server or using getSharedPreferences method.
         * @return A new instance of fragment PersonFragment.
         */
        @JvmStatic
        fun newInstance(id: Int, wasSuccessful: Boolean,userId: Long) =
            PersonFragment().apply {
                arguments = Bundle().apply {
                    putInt("currentLayout", id)
                    putBoolean("wasSuccessful", wasSuccessful)
                    putLong("UserId", userId)
                }
            }
    }
}