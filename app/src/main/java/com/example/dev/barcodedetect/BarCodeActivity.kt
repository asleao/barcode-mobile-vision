package com.example.dev.barcodedetect

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_bar_code.barcode_value
import kotlinx.android.synthetic.main.activity_bar_code.surface_view
import java.io.IOException

class BarCodeActivity : AppCompatActivity() {
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private var barcodeValue: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)
        cameraView = surface_view
        barcodeValue = barcode_value

        barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector!!)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()
        cameraView?.holder?.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource?.start(cameraView?.holder)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode?> {

            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode?>?) {
                val barcodes = detections?.detectedItems
                if (barcodes?.size() != 0) {
                    barcodeValue?.post {
                        //Update barcode value to TextView
                        barcodeValue?.setText(barcodes?.valueAt(0)?.displayValue)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        barcodeDetector?.release()
    }
}
