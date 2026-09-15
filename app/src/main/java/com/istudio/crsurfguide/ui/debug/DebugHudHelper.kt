package com.istudio.crsurfguide.ui.debug

import androidx.compose.ui.platform.ComposeView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

object DebugHudHelper {
    @JvmStatic
    fun attachHud(composeView: ComposeView) {
        composeView.setContent {
            MaterialTheme {
                DebugHud()
            }
        }
    }
}
