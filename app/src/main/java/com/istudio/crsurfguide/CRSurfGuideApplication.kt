package com.istudio.crsurfguide

import android.app.Application
import com.istudio.crsurfguide.ui.debug.LogBuffer
import com.istudio.crsurfguide.ui.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CRSurfGuideApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            LogBuffer.e("CRITICAL", "Application level crash on thread ${thread.name}", throwable)
            // Intentar persistir si fuera necesario, pero por ahora solo log
        }

        LogBuffer.d("CRSurfGuideApplication", "Application onCreate: Inicializando componentes...")
        ThemeManager.init(this)
        LogBuffer.d("CRSurfGuideApplication", "Application onCreate: Tema inicializado con éxito.")
    }
}
