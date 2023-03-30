package com.coyotwilly.nomad

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class FutureTripCreate : AppCompatActivity() {
    private val galleryImg = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            findViewById<ImageView>(R.id.background_preview_future).setImageURI(it)
        })
    private val cameraContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.background_preview_future).setImageBitmap(bitmap)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_trip_create)

        findViewById<ImageView>(R.id.background_preview_future).setOnClickListener {
            galleryImg.launch("image/*")
        }
        findViewById<Button>(R.id.gallery_bnt).setOnClickListener {
            cameraContract.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }
}