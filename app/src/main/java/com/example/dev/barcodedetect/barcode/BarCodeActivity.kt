package com.example.dev.barcodedetect.barcode


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import com.example.dev.barcodedetect.MainActivity
import com.example.dev.barcodedetect.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_bar_code.camera_view
import java.io.IOException


class BarCodeActivity : AppCompatActivity(), BarCodeContract.MvpView {

    val mPresenter: BarCodePresenter = BarCodePresenter(this)
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    private val PERMISSION_CAMERA = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)
        barcodeDetector = configurarBarcodeDetector()
        configurarCameraSource(barcodeDetector)
        habilitarCameraSource()
        mPresenter.habilitarLeituraBarcode(barcodeDetector)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                mPresenter.verificarPermissaoCamera(grantResults)
            }
        }
    }

    override fun configurarBarcodeDetector(): BarcodeDetector {
        barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        return barcodeDetector
    }

    override fun configurarCameraSource(barcodeDetector: BarcodeDetector): CameraSource {
        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build()
        return cameraSource
    }

    override fun habilitarCameraSource() {
        camera_view.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ContextCompat.checkSelfPermission(this@BarCodeActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requisitarPermissaoCamera()
                    } else {
                        inicializarCamera()
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                pararCamera()
            }
        })
    }

    override fun pararCamera() {
        cameraSource.stop()
    }

    override fun requisitarPermissaoCamera() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA)
    }

    @SuppressLint("MissingPermission")
    override fun inicializarCamera() {
        cameraSource.start(camera_view.holder)
    }

    override fun retornarBarcodeLido(barcodes: SparseArray<Barcode?>?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("barcode", barcodes?.valueAt(0))
        startActivity(intent)
//        Toast.makeText(this, barcodes?.valueAt(0).toString(), Toast.LENGTH_SHORT).show()
    }

    override fun exibirMensagemPermissao() {
        Toast.makeText(this, "Acesso da camera não autorizado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
        barcodeDetector.release()
    }
}
