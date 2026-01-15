package com.nikcapko.deeplinker.deeplinker

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.onDoubleClick(
    onDoubleClick: () -> Unit,
    delayMillis: Long = 250L
): Modifier = composed {
    var lastClickTime by androidx.compose.runtime.remember {
        mutableStateOf(0L)
    }

    this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < delayMillis) {
                    // Двойной клик
                    onDoubleClick()
                    lastClickTime = 0L
                } else {
                    // Запоминаем время первого клика
                    lastClickTime = currentTime
                }
            }
        )
    }
}
