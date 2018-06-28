package com.example.dev.barcodedetect.barcode

import android.app.Activity
import android.content.Context
import android.util.SparseArray
import android.view.SurfaceView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

interface BarCodeContract {
    interface MvpView {
        fun retornarBarcodeLido(barcodes: SparseArray<Barcode?>?)
        fun exibirMensagemPermissao()
    }

    interface Presenter {
        fun configurarBarcodeDetector(context: Context): BarcodeDetector
        fun configurarCameraSource(context: Context, barcodeDetector: BarcodeDetector): CameraSource
        fun requisitarPermissaoCamera(activity: Activity)
        fun habilitarCameraSource(cameraView: SurfaceView, activity: Activity)
        fun habilitarLeituraBarcode()
        fun verificarPermissaoCamera(cameraView: SurfaceView, grantResults: IntArray)
        fun limparConfiguracoes()
    }
}
