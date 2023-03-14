package com.coyotwilly.nomad


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
/**
 * A simple [Fragment] subclass.
 * Use the [PersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    Toast.makeText(context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()

                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    System.err.println("AUTH successful")
                    val temp = arguments?.getBoolean("wasSuccessful") ?: false
                    if ((!temp) or (arguments?.getInt("id") == R.layout.fragment_person)){
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.body_container, newInstance(R.layout.fragment_person,true))
                            .commit()
                    }

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
        System.err.println("onCreate")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        System.err.println("onCreate View")
        return inflater.inflate(arguments?.getInt("currentLayout") ?: R.layout.fragment_pin_auth, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param id Layout view parameter.
         * @param wasSuccessful Was the previous object authenticated successfully.
         * @return A new instance of fragment PersonFragment.
         */
        @JvmStatic
        fun newInstance(id: Int, wasSuccessful: Boolean) =
            PersonFragment().apply {
                arguments = Bundle().apply {
                    putInt("currentLayout", id)
                    putBoolean("wasSuccessful", wasSuccessful)
                }
            }
    }
}