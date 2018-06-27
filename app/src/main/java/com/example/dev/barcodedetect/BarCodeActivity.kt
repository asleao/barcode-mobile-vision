package com.example.dev.barcodedetect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_bar_code.barcode_textView
import kotlinx.android.synthetic.main.activity_bar_code.surface_view
import java.io.IOException

class BarCodeActivity : AppCompatActivity() {
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private var barcodeValue: TextView? = null

    private val PERMISSION_CAMERA = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)
        cameraView = surface_view
        barcodeValue = barcode_textView

        barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build()

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode?> {

            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode?>?) {
                val barcodes = detections?.detectedItems
                if (barcodes?.size() != 0) {
                    val intent = Intent(this@BarCodeActivity, MainActivity::class.java)
                    intent.putExtra("barcode", barcodes?.valueAt(0))
                    startActivity(intent)
                }
            }
        })

        cameraView?.holder?.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ContextCompat.checkSelfPermission(this@BarCodeActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission()
                    } else {
                        cameraSource?.start(cameraView?.holder)
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraSource?.start(cameraView?.holder)
                } else {
                    Toast.makeText(this, "Acesso da camera não autorizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        barcodeDetector?.release()
    }
}
