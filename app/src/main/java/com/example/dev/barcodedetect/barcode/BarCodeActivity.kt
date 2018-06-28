package com.example.dev.barcodedetect.barcode

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.widget.Toast
import com.example.dev.barcodedetect.MainActivity
import com.example.dev.barcodedetect.R
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_bar_code.camera_view

class BarCodeActivity : AppCompatActivity(), BarCodeContract.MvpView {

    val mPresenter: BarCodePresenter = BarCodePresenter(this)

    private val PERMISSION_CAMERA = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)
        mPresenter.configurarCameraSource(this, mPresenter.configurarBarcodeDetector(this))
        mPresenter.habilitarCameraSource(camera_view, this)
        mPresenter.habilitarLeituraBarcode()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                mPresenter.verificarPermissaoCamera(camera_view, grantResults)
            }
        }

    }

    override fun retornarBarcodeLido(barcodes: SparseArray<Barcode?>?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("barcode", barcodes?.valueAt(0))
        startActivity(intent)
    }

    override fun exibirMensagemPermissao() {
        Toast.makeText(this, "Acesso da camera não autorizado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.limparConfiguracoes()
    }
}
