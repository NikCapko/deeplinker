package com.nikcapko.deeplinker.deeplinker.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Cross: ImageVector
    get() {
        if (_Cross != null) {
            return _Cross!!
        }
        _Cross = ImageVector.Builder(
            name = "Cross",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveToRelative(256f, 760f)
                lineToRelative(-56f, -56f)
                lineToRelative(224f, -224f)
                lineToRelative(-224f, -224f)
                lineToRelative(56f, -56f)
                lineToRelative(224f, 224f)
                lineToRelative(224f, -224f)
                lineToRelative(56f, 56f)
                lineToRelative(-224f, 224f)
                lineToRelative(224f, 224f)
                lineToRelative(-56f, 56f)
                lineToRelative(-224f, -224f)
                lineToRelative(-224f, 224f)
                close()
            }
        }.build()

        return _Cross!!
    }

@Suppress("ObjectPropertyName")
private var _Cross: ImageVector? = null
