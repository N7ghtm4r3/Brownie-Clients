package com.tecknobit.brownie.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val RuleSettings: ImageVector
    get() {
        if (_RuleSettings != null) {
            return _RuleSettings!!
        }
        _RuleSettings = ImageVector.Builder(
            name = "RuleSettings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(120f, 800f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(110f)
                lineToRelative(-16f, -14f)
                quadToRelative(-52f, -46f, -73f, -105f)
                reflectiveQuadToRelative(-21f, -119f)
                quadToRelative(0f, -111f, 66.5f, -197.5f)
                reflectiveQuadTo(360f, 170f)
                verticalLineToRelative(84f)
                quadToRelative(-72f, 26f, -116f, 88.5f)
                reflectiveQuadTo(200f, 482f)
                quadToRelative(0f, 45f, 17f, 87.5f)
                reflectiveQuadToRelative(53f, 78.5f)
                lineToRelative(10f, 10f)
                verticalLineToRelative(-98f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(240f)
                close()
                moveToRelative(717f, -360f)
                horizontalLineToRelative(-81f)
                quadToRelative(-5f, -35f, -21.5f, -67f)
                reflectiveQuadTo(690f, 312f)
                lineToRelative(-10f, -10f)
                verticalLineToRelative(98f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-240f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(80f)
                horizontalLineTo(730f)
                lineToRelative(16f, 14f)
                quadToRelative(41f, 42f, 63f, 89f)
                reflectiveQuadToRelative(28f, 97f)
                moveTo(680f, 920f)
                lineToRelative(-12f, -60f)
                quadToRelative(-12f, -5f, -22.5f, -10.5f)
                reflectiveQuadTo(624f, 836f)
                lineToRelative(-58f, 18f)
                lineToRelative(-40f, -68f)
                lineToRelative(46f, -40f)
                quadToRelative(-2f, -14f, -2f, -26f)
                reflectiveQuadToRelative(2f, -26f)
                lineToRelative(-46f, -40f)
                lineToRelative(40f, -68f)
                lineToRelative(58f, 18f)
                quadToRelative(11f, -8f, 21.5f, -13.5f)
                reflectiveQuadTo(668f, 580f)
                lineToRelative(12f, -60f)
                horizontalLineToRelative(80f)
                lineToRelative(12f, 60f)
                quadToRelative(12f, 5f, 22.5f, 11f)
                reflectiveQuadToRelative(21.5f, 15f)
                lineToRelative(58f, -20f)
                lineToRelative(40f, 70f)
                lineToRelative(-46f, 40f)
                quadToRelative(2f, 12f, 2f, 25f)
                reflectiveQuadToRelative(-2f, 25f)
                lineToRelative(46f, 40f)
                lineToRelative(-40f, 68f)
                lineToRelative(-58f, -18f)
                quadToRelative(-11f, 8f, -21.5f, 13.5f)
                reflectiveQuadTo(772f, 860f)
                lineToRelative(-12f, 60f)
                close()
                moveToRelative(40f, -120f)
                quadToRelative(33f, 0f, 56.5f, -23.5f)
                reflectiveQuadTo(800f, 720f)
                reflectiveQuadToRelative(-23.5f, -56.5f)
                reflectiveQuadTo(720f, 640f)
                reflectiveQuadToRelative(-56.5f, 23.5f)
                reflectiveQuadTo(640f, 720f)
                reflectiveQuadToRelative(23.5f, 56.5f)
                reflectiveQuadTo(720f, 800f)
            }
        }.build()
        return _RuleSettings!!
    }

private var _RuleSettings: ImageVector? = null
