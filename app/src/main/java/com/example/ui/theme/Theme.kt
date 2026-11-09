package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.data.model.AppThemeMode

private val HackklinDarkColorScheme = darkColorScheme(
    primary = MatteEmerald,
    onPrimary = Color(0xFF022C22),
    primaryContainer = MatteEmeraldContainer,
    onPrimaryContainer = OnMatteEmeraldContainer,
    secondary = TacticalSlateLight,
    onSecondary = PureBlack,
    secondaryContainer = TacticalSlateContainer,
    onSecondaryContainer = OnTacticalSlateContainer,
    tertiary = MatteEmeraldSoft,
    onTertiary = PureBlack,
    background = PureBlack,
    onBackground = TextPrimary,
    surface = DeepCharcoal,
    onSurface = TextPrimary,
    surfaceVariant = ElevatedGraphite,
    onSurfaceVariant = TextSecondary,
    outline = SubtleBorderSlate,
    outlineVariant = Color(0xFF1C222B),
    error = TerminalErrorCoral,
    onError = PureBlack
)

private val HackklinLightColorScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF475569),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF0F172A),
    tertiary = Color(0xFF10B981),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF)
)

val HackklinShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun HackklinTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isLight = themeMode == AppThemeMode.LIGHT
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = isLight
                    isAppearanceLightNavigationBars = isLight
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = if (isLight) HackklinLightColorScheme else HackklinDarkColorScheme,
        typography = Typography,
        shapes = HackklinShapes,
        content = content
    )
}
