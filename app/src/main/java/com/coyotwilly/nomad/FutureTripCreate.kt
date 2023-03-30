package com.coyotwilly.nomad

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File

class FutureTripCreate : AppCompatActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private val CAMERA_PERMISSION_CODE = 1
    private lateinit var imgUri: Uri
    private val galleryImg = registerForActivityResult(ActivityResultContracts.GetContent()
    ) {
        findViewById<ImageView>(R.id.background_preview_future).setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_trip_create)
        imgUri = createUri()
        registerPictureLauncher()

        findViewById<ImageView>(R.id.background_preview_future).setOnClickListener {
            galleryImg.launch("image/*")
        }
        findViewById<Button>(R.id.gallery_bnt).setOnClickListener {
            checkCameraPermissionAndOpenCamera()
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