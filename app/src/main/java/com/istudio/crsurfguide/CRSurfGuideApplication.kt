package com.istudio.crsurfguide

import android.app.Application
import com.istudio.crsurfguide.ui.debug.LogBuffer
import com.istudio.crsurfguide.ui.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CRSurfGuideApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogBuffer.d("CRSurfGuideApplication", "Application onCreate: Inicializando componentes...")
        ThemeManager.init(this)
        LogBuffer.d("CRSurfGuideApplication", "Application onCreate: Tema inicializado con éxito.")
    }
}
