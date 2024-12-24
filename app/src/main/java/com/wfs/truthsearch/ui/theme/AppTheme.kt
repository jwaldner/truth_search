package com.wfs.truthsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.wfs.truthsearch.R

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) {
        darkColors(
            background = colorResource(id = R.color.black), // Background for Night mode
            surface = colorResource(id = R.color.black), // Surface matches Night mode background
            onBackground = colorResource(id = android.R.color.primary_text_dark), // Text on background
            onSurface = colorResource(id = android.R.color.primary_text_dark) // Text on surfaces
        )
    } else {
        lightColors(
            background = colorResource(id = R.color.white), // Background for Day mode
            surface = colorResource(id = R.color.white), // Surface matches Day mode background
            onBackground = colorResource(id = android.R.color.primary_text_light), // Text on background
            onSurface = colorResource(id = android.R.color.primary_text_light) // Text on surfaces
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography, // Use the default typography or customize
        shapes = Shapes, // Use the default shapes or customize
        content = content
    )
}
