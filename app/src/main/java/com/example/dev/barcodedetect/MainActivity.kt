package com.example.dev.barcodedetect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val PERMISSION_CAMERA = 1000

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_CAMERA)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, BarCodeActivity::class.java))
                } else{
                    Toast.makeText(this, "Acesso da camera não autorizado", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.CAMERA),
                            PERMISSION_CAMERA)
                }
            }
        }

    }
}
