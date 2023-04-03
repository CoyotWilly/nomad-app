package com.coyotwilly.nomad

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.collection.arraySetOf
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.coyotwilly.nomad.model.FutureTrips
import com.coyotwilly.nomad.service.UserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.util.*

class FutureTripCreate : AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 1
    private var themeChanged: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private val galleryImg = registerForActivityResult(ActivityResultContracts.GetContent()
    ) {
        findViewById<ImageView>(R.id.background_preview_future).setImageURI(it)
    }
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var imgUri: Uri

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
        findViewById<ImageButton>(R.id.back_arrow_to_home).setOnClickListener {
            startActivity(Intent(this.applicationContext, MainActivity::class.java))
            finish()
        }

        findViewById<AppCompatButton>(R.id.save_future_trip).setOnClickListener {
            val userId = getSharedPreferences("com.coyotwilly.app", Context.MODE_PRIVATE).getLong("com.coyotwilly.app.user.Id", 0L)
            val trip = FutureTrips(0, "1999-02-19", "1999-02-29", "Mount Blanc", null)
            runBlocking {
                val job = launch {
                    val bitmap = findViewById<ImageView>(R.id.background_preview_future).drawToBitmap()
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                    val service = UserService.create()
                    val imgId: Long = service.postImg(userId, stream.toByteArray())
                    service.postFutureTrip(userId,imgId, trip)
                }
                job.join()
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
                    findViewById<ImageView>(R.id.background_preview_future).setImageURI(imgUri)
                    val bitmap = findViewById<ImageView>(R.id.background_preview_future).drawToBitmap()
                    saveImageToStorage(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap) {
        val fos: OutputStream
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder")

                val resolver = contentResolver
                val imgUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = resolver.openOutputStream(Objects.requireNonNull(imgUri!!))!!
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                Objects.requireNonNull(fos)
            }
        } catch (e: IOException) {
            Log.e("SAVE_ERROR", e.message.toString())
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