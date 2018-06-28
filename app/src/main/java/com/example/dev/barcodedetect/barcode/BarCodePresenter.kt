package com.example.dev.barcodedetect.barcode

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class BarCodePresenter(val mView: BarCodeContract.MvpView) : BarCodeContract.Presenter {

    override fun habilitarLeituraBarcode(barcodeDetector: BarcodeDetector) {
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
    override fun verificarPermissaoCamera(grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mView.inicializarCamera()
        } else {
            mView.exibirMensagemPermissao()
        }
    }

}
