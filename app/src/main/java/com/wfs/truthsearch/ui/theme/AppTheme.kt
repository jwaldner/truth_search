package com.wfs.truthsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.wfs.truthsearch.R

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme(
            background = colorResource(id = R.color.black), // Background for Night mode
            surface = colorResource(id = R.color.black), // Surface for Night mode
            onBackground = colorResource(id = R.color.colorOnBackground), // Text on background
            onSurface = colorResource(id = R.color.colorOnSurface) // Text on surfaces
        )
    } else {
        lightColorScheme(
            background = colorResource(id = R.color.white), // Background for Day mode
            surface = colorResource(id = R.color.white), // Surface for Day mode
            onBackground = colorResource(id = R.color.colorOnBackground), // Text on background
            onSurface = colorResource(id = R.color.colorOnSurface) // Text on surfaces
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography =  MaterialTheme.typography, // Use the default typography or customize
        shapes = MaterialTheme.shapes, // Use the default shapes or customize
        content = content
    )
}
