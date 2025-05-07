package com.tecknobit.brownie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.localSession
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Dark
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Light

/**
 * **lightScheme** default light colors scheme
 */
private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

/**
 * **darkScheme** default dark colors scheme
 */
private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

/**
 * Method used to retrieve the correct green tone for the theme
 *
 * @return the correct green tone as [Color]
 */
@Composable
fun green(): Color {
    return if (isDarkThemeApplied())
        darkGreen
    else
        lightGreen
}

/**
 * Method used to retrieve the correct red tone for the theme
 *
 * @return the correct red tone as [Color]
 */
@Composable
fun red(): Color {
    return if (isDarkThemeApplied())
        errorContainerDark
    else
        errorLight
}

/**
 * Method used to retrieve the correct yellow tone for the theme
 *
 * @return the correct yellow tone as [Color]
 */
@Composable
fun yellow(): Color {
    return if (isDarkThemeApplied())
        darkYellow
    else
        lightYellow
}

/**
 * Method used to retrieve the correct violet tone for the theme
 *
 * @return the correct violet tone as [Color]
 */
@Composable
fun violet(): Color {
    return if (isDarkThemeApplied())
        darkViolet
    else
        lightViolet
}

/**
 * Method used to retrieve the correct light green tone for the theme
 *
 * @return the light green tone as [Color]
 */
@Composable
fun serviceAddedColor(): Color {
    return if (isDarkThemeApplied())
        serviceAddedDark
    else
        serviceAddedLight
}

/**
 * Method used to retrieve the correct blue for the theme
 *
 * @return the correct blue tone as [Color]
 */
@Composable
fun serviceRemovedColor(): Color {
    return if (isDarkThemeApplied())
        serviceRemovedDark
    else
        serviceRemovedLight
}

/**
 * Method used to retrieve the correct red for the theme
 *
 * @return the correct red tone as [Color]
 */
@Composable
fun serviceDeletedColor(): Color {
    return if (isDarkThemeApplied())
        serviceDeletedDark
    else
        serviceDeletedLight
}

/**
 * Function to set the Refy theme to the content
 *
 * @param darkTheme Whether to use the dark or light theme
 * @param content The content to display
 */
@Composable
fun BrownieTheme(
    darkTheme: Boolean = isDarkThemeApplied(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

/**
 * Method used to determine whether to apply the dark theme
 *
 * @return whether to apply the dark theme as [Boolean]
 */
@Composable
private fun isDarkThemeApplied(): Boolean {
    return when (localSession.theme) {
        Light -> false
        Dark -> true
        else -> isSystemInDarkTheme()
    }
}

