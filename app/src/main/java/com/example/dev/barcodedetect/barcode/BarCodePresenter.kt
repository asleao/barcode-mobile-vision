package com.example.dev.barcodedetect.barcode

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class BarCodePresenter(val mView: BarCodeContract.MvpView) : BarCodeContract.Presenter {

    private val PERMISSION_CAMERA = 1000
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    override fun configurarBarcodeDetector(context: Context): BarcodeDetector {
        barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        return barcodeDetector
    }

    override fun configurarCameraSource(context: Context, barcodeDetector: BarcodeDetector): CameraSource {
        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build()
        return cameraSource
    }

    override fun requisitarPermissaoCamera(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA)
    }

    override fun habilitarCameraSource(cameraView: SurfaceView, activity: Activity) {
        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requisitarPermissaoCamera(activity)
                    } else {
                        cameraSource.start(cameraView.holder)

                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
    }

    override fun habilitarLeituraBarcode() {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {

            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode?>?) {
                val barcodes = detections?.detectedItems
                if (barcodes?.size() != 0) {
                    mView.retornarBarcodeLido(barcodes)
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun verificarPermissaoCamera(cameraView: SurfaceView, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraSource.start(cameraView.holder)
        } else {
            mView.exibirMensagemPermissao()
        }
    }

    override fun limparConfiguracoes() {
        cameraSource.release()
        barcodeDetector.release()
    }
}
