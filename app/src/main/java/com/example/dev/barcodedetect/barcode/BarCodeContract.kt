package com.example.dev.barcodedetect.barcode

import android.util.SparseArray
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

interface BarCodeContract {
    interface MvpView {
        fun retornarBarcodeLido(barcodes: SparseArray<Barcode?>?)
        fun exibirMensagemPermissao()
        fun configurarBarcodeDetector(): BarcodeDetector
        fun configurarCameraSource(barcodeDetector: BarcodeDetector): CameraSource
        fun requisitarPermissaoCamera()
        fun habilitarCameraSource()
        fun inicializarCamera()
        fun pararCamera()
    }

    interface Presenter {
        fun habilitarLeituraBarcode(barcodeDetector: BarcodeDetector)
        fun verificarPermissaoCamera(grantResults: IntArray)
    }
}
