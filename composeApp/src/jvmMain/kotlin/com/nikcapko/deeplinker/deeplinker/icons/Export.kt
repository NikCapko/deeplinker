package com.nikcapko.deeplinker.deeplinker.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Export: ImageVector
    get() {
        if (_Export != null) {
            return _Export!!
        }
        _Export = ImageVector.Builder(
            name = "Export",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(440f, 640f)
                verticalLineToRelative(-326f)
                lineTo(336f, 418f)
                lineToRelative(-56f, -58f)
                lineToRelative(200f, -200f)
                lineToRelative(200f, 200f)
                lineToRelative(-56f, 58f)
                lineToRelative(-104f, -104f)
                verticalLineToRelative(326f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(240f, 800f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(160f, 720f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(720f, 800f)
                lineTo(240f, 800f)
                close()
            }
        }.build()

        return _Export!!
    }

@Suppress("ObjectPropertyName")
private var _Export: ImageVector? = null
