package com.example.dev.barcodedetect

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.imgview
import kotlinx.android.synthetic.main.activity_main.txtContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val myBitmap = BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.puppy)
            imgview.setImageBitmap(myBitmap)

            val detector = BarcodeDetector.Builder(applicationContext)
                    .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE)
                    .build()

            if (!detector.isOperational) {
                txtContent.setText("Could not set up the detector!")
            }

            val frame = Frame.Builder().setBitmap(myBitmap).build()
            val barcodes = detector.detect(frame)

            val thisCode = barcodes.valueAt(0)
            txtContent.text = thisCode.displayValue
        }

    }
}
