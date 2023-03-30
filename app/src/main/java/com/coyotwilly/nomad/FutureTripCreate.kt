package com.coyotwilly.nomad

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.arraySetOf
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.*

class FutureTripCreate : AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 1
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var imgUri: Uri
    private val galleryImg = registerForActivityResult(ActivityResultContracts.GetContent()
    ) {
        findViewById<ImageView>(R.id.background_preview_future).setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_trip_create)
        themeCheck()

        imgUri = createUri()
        registerPictureLauncher()
        // gallery image listener
        findViewById<FloatingActionButton>(R.id.gallery_bnt).setOnClickListener {
            galleryImg.launch("image/*")
        }
        // photo snap button
        findViewById<FloatingActionButton>(R.id.photo_snap).setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }
        // start date picker
        findViewById<EditText>(R.id.trip_start_date).setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                datePickerDialog(R.id.trip_start_date)
            }
        }
        findViewById<EditText>(R.id.trip_end_date).setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                datePickerDialog(R.id.trip_end_date)
            }
        }

    }

    private fun datePickerDialog(element: Int) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, year_lambda, monthOfYear, dayOfMonth ->
                var tempDay = dayOfMonth.toString()
                var tempMonth = monthOfYear.toString()
                if (dayOfMonth < 10) {
                    tempDay = "0$tempDay"
                }
                if (monthOfYear < 10) {
                    tempMonth = "0$tempMonth"
                }
                findViewById<EditText>(element).setText(
                    getString(
                        R.string.date_template,
                        year_lambda,
                        tempMonth,
                        tempDay
                    )
                )
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun themeCheck() {
        if ((themeChanged != Configuration.UI_MODE_NIGHT_YES) or (themeChanged != Configuration.UI_MODE_NIGHT_NO)) {
            val availableViews: Set<Int> = arraySetOf(
                R.id.card_of_bg_maker,
                R.id.photo_snap,
                R.id.gallery_bnt,
                R.id.destination_place,
                R.id.trip_start_date,
                R.id.trip_end_date
            )
            for (element in availableViews) {
                val navController = findViewById<View>(element)
                ThemeWatcher(navController)
            }
            themeChanged =
                applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        }
    }

    private fun createUri() : Uri {
        val imgFile = File(applicationContext.filesDir, "camera_photo.jpg")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.coyotwilly.nomad.fileProvider",
            imgFile
        )
    }
    private fun registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()
        ) { result ->
            try {
                if (result) {
                    findViewById<ImageView>(R.id.background_preview_future).setImageURI(null)
                    findViewById<ImageView>(R.id.background_preview_future).setImageURI(imgUri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun checkCameraPermissionAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(this ,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            takePictureLauncher.launch(imgUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if((grantResults.isNotEmpty()) and (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                takePictureLauncher.launch(imgUri)
            } else {
                Toast.makeText(this, "Camera permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}