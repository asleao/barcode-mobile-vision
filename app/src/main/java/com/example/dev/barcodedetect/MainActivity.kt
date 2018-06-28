package com.example.dev.barcodedetect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)
        val barcode = intent.getParcelableExtra<Barcode>("barcode")
        Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show()
    }


}
