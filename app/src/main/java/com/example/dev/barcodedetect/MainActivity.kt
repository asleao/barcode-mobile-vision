package com.example.dev.barcodedetect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_main.barcode

class MainActivity : AppCompatActivity() {

    private val PERMISSION_CAMERA = 1000

    companion object {
        private const val REQUEST_BARCODE = 100
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)

        requestCameraPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(Intent(this, BarCodeActivity::class.java), REQUEST_BARCODE)
                } else {
                    Toast.makeText(this, "Acesso da camera não autorizado", Toast.LENGTH_SHORT).show()
                    requestCameraPermission()
                }
            }
        }

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BARCODE) {
            val barcodeData = data?.getParcelableExtra<Barcode>("barcode")
            barcode.setText(barcodeData?.displayValue)
        }

    }
}
