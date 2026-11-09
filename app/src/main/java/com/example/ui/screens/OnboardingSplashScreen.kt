package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.CurriculumLevel
import com.example.data.model.getLevelEmoji
import com.example.ui.auth.GoogleSignInHelper
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay

private data class SimpleExpOption(
    val levelNumber: Int,
    val emoji: String,
    val titleEs: String,
    val titleEn: String,
    val subtitleEs: String,
    val subtitleEn: String
)

private val SIMPLE_EXP_OPTIONS = listOf(
    SimpleExpOption(
        levelNumber = 1,
        emoji = "🌱",
        titleEs = "Principiante • Desde Cero",
        titleEn = "Beginner • From Scratch",
        subtitleEs = "Quiero aprender qué es una IP, puertos y hacking ético paso a paso.",
        subtitleEn = "Learn what an IP, ports and ethical hacking are step by step."
    ),
    SimpleExpOption(
        levelNumber = 3,
        emoji = "🛡️",
        titleEs = "Intermedio • Redes y Linux",
        titleEn = "Intermediate • Networks & Linux",
        subtitleEs = "Conozco lo básico y quiero dominar terminal CLI, SSH y firewalls.",
        subtitleEn = "I know the basics and want to master CLI, SSH and firewalls."
    ),
    SimpleExpOption(
        levelNumber = 5,
        emoji = "🦅",
        titleEs = "Avanzado • Red Team y DFIR",
        titleEn = "Advanced • Red Team & DFIR",
        subtitleEs = "Busco retos de Pentesting, Forense Digital y Seguridad Cloud.",
        subtitleEn = "Looking for Pentesting, Digital Forensics and Cloud Security labs."
    )
)

@Composable
fun OnboardingSplashScreen(
    levels: List<CurriculumLevel>,
    language: AppLanguage,
    themeMode: AppThemeMode,
    isSignedInWithGoogle: Boolean,
    signedInName: String?,
    onToggleLanguage: (AppLanguage) -> Unit,
    onToggleTheme: (AppThemeMode) -> Unit,
    onGoogleAuthSuccess: (FirebaseUser) -> Unit,
    onFinishOnboarding: (Int) -> Unit
) {
    val isEn = language == AppLanguage.EN
    var currentSlide by remember { mutableIntStateOf(0) }
    var selectedExpLevel by remember { mutableIntStateOf(1) }
    var inspectedLevelNum by remember { mutableIntStateOf(1) }
    var isSigningIn by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val bgColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surface
    val cardVariant = MaterialTheme.colorScheme.surfaceVariant
    val borderColor = MaterialTheme.colorScheme.outline
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant

    val infinite = rememberInfiniteTransition(label = "splashPulse")
    val pulse by infinite.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .testTag("onboarding_splash_screen")
    ) {
        // Soft radial emerald glow in background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MatteEmerald.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    center = Offset(size.width / 2f, size.height * 0.32f),
                    radius = size.minDimension * 0.72f
                ),
                radius = size.minDimension * 0.72f,
                center = Offset(size.width / 2f, size.height * 0.32f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 18.dp)
        ) {
            // 1. Minimalist Top Bar (Back Arrow / Step Dots / Skip)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.width(64.dp), contentAlignment = Alignment.CenterStart) {
                    if (currentSlide > 0) {
                        IconButton(
                            onClick = { currentSlide-- },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (isEn) "Back" else "Atrás",
                                tint = textPrimary
                            )
                        }
                    }
                }

                // 3 Clean Progress Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0..2) {
                        val active = i == currentSlide
                        val passed = i < currentSlide
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (active) 28.dp else 8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (active || passed) MatteEmerald else borderColor)
                        )
                    }
                }

                Box(modifier = Modifier.width(64.dp), contentAlignment = Alignment.CenterEnd) {
                    TextButton(
                        onClick = { onFinishOnboarding(selectedExpLevel) },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = if (isEn) "Skip" else "Saltar",
                            style = MaterialTheme.typography.labelLarge,
                            color = textSecondary
                        )
                    }
                }
            }

            // 2. Centered Slide Content (Compact, Balanced & Clean)
            AnimatedContent(
                targetState = currentSlide,
                label = "cleanSplashSlide",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { slide ->
                when (slide) {
                    0 -> {
                        // SLIDE 1: Iconic Centered Splash
                        val welcomeText = if (isEn) {
                            "Learn ethical hacking and cybersecurity step by step, from Rookie 🌱 to Elite Hacker 👑."
                        } else {
                            "Aprende ciberseguridad y hacking ético paso a paso, desde Novato 🌱 hasta Hacker 👑."
                        }
                        var typedCount by remember(welcomeText) { mutableIntStateOf(0) }
                        LaunchedEffect(welcomeText) {
                            typedCount = 0
                            while (typedCount < welcomeText.length) {
                                delay(18L)
                                typedCount = (typedCount + 2).coerceAtMost(welcomeText.length)
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Glowing Hero Medallion
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(148.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(
                                        color = MatteEmerald.copy(alpha = 0.22f),
                                        radius = (size.minDimension / 2.2f) * pulse
                                    )
                                    drawCircle(
                                        color = MatteEmerald.copy(alpha = 0.45f),
                                        radius = (size.minDimension / 2.5f),
                                        style = Stroke(width = 3f)
                                    )
                                }

                                Surface(
                                    color = MatteEmeraldContainer,
                                    border = BorderStroke(2.5.dp, MatteEmerald),
                                    shape = CircleShape,
                                    modifier = Modifier.size(96.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = null,
                                            tint = MatteEmerald,
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            Surface(
                                color = MatteEmeraldContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = if (isEn) "CYBERSECURITY ACADEMY" else "ACADEMIA DE CIBERSEGURIDAD",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnMatteEmeraldContainer,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "HACKKLIN",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontSize = 36.sp,
                                    letterSpacing = 2.sp
                                ),
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${welcomeText.take(typedCount)} ▋",
                                style = MaterialTheme.typography.bodyLarge,
                                color = textSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            // 3 Minimalist Feature Badges
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val pills = if (isEn) {
                                    listOf("⚡ Step-by-Step", "💻 Live CLI", "🏆 8 Ranks")
                                } else {
                                    listOf("⚡ Paso a Paso", "💻 Consola CLI", "🏆 8 Rangos")
                                }
                                pills.forEach { pill ->
                                    Surface(
                                        color = cardColor,
                                        border = BorderStroke(1.dp, borderColor),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = pill,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = textPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // SLIDE 2: Clean Experience Selector (3 Crisp Cards)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "🎯",
                                fontSize = 38.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isEn) "What is your current level?" else "¿Cuál es tu nivel actual?",
                                style = MaterialTheme.typography.headlineMedium,
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isEn) {
                                    "Choose your profile to personalize your starting experience."
                                } else {
                                    "Elige tu perfil para calibrar tu punto de partida."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = textSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                SIMPLE_EXP_OPTIONS.forEach { opt ->
                                    val selected = selectedExpLevel == opt.levelNumber
                                    Surface(
                                        color = if (selected) MatteEmeraldContainer.copy(alpha = 0.35f) else cardColor,
                                        border = BorderStroke(
                                            width = if (selected) 2.dp else 1.dp,
                                            color = if (selected) MatteEmerald else borderColor
                                        ),
                                        shape = RoundedCornerShape(18.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .clickable {
                                                selectedExpLevel = opt.levelNumber
                                                inspectedLevelNum = opt.levelNumber
                                            }
                                            .testTag("exp_option_${opt.levelNumber}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Surface(
                                                color = if (selected) MatteEmerald else cardVariant,
                                                shape = CircleShape,
                                                modifier = Modifier.size(46.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(text = opt.emoji, fontSize = 22.sp)
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = if (isEn) opt.titleEn else opt.titleEs,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = textPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = if (isEn) opt.subtitleEn else opt.subtitleEs,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = textSecondary,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            if (selected) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MatteEmerald,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // SLIDE 3: Clean 8-Rank Grid Preview + Optional Google Account
                        val activeLevel = levels.find { it.number == inspectedLevelNum } ?: levels.first()
                        val activeEmoji = getLevelEmoji(activeLevel.number)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = if (isEn) "Your 8-Level Roadmap" else "Tu Ruta de 8 Niveles",
                                style = MaterialTheme.typography.headlineMedium,
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isEn) {
                                    "Tap any badge to see what you will master:"
                                } else {
                                    "Toca cualquier insignia para ver qué dominarás:"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Clean 2x4 Grid of the 8 Levels
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                levels.chunked(4).forEach { rowLevels ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        rowLevels.forEach { lvl ->
                                            val isSelected = lvl.number == inspectedLevelNum
                                            Surface(
                                                color = if (isSelected) MatteEmeraldContainer else cardColor,
                                                border = BorderStroke(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) MatteEmerald else borderColor
                                                ),
                                                shape = RoundedCornerShape(14.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .clickable { inspectedLevelNum = lvl.number }
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                                                ) {
                                                    Text(text = getLevelEmoji(lvl.number), fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "N${lvl.number}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = if (isSelected) MatteEmerald else textSecondary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = lvl.rankName,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = textPrimary,
                                                        fontWeight = FontWeight.SemiBold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Compact Spotlight Card for the Selected Level
                            Surface(
                                color = cardColor,
                                border = BorderStroke(1.5.dp, MatteEmerald),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Surface(
                                        color = MatteEmeraldContainer,
                                        shape = CircleShape,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = activeEmoji, fontSize = 24.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = activeLevel.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = textPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = activeLevel.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MatteEmerald,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = activeLevel.topics.joinToString(" • ") { it.title },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = textSecondary,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Clean Optional Google Sign-In Button
                            if (isSignedInWithGoogle) {
                                Surface(
                                    color = MatteEmeraldContainer.copy(alpha = 0.45f),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MatteEmerald,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isEn) {
                                                "Google Account Linked: ${signedInName ?: "Agent"}"
                                            } else {
                                                "Cuenta Google Conectada: ${signedInName ?: "Agente"}"
                                            },
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MatteEmerald,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                OutlinedButton(
                                    onClick = {
                                        isSigningIn = true
                                        GoogleSignInHelper.onGoogleSignInClicked(
                                            context = context,
                                            credentialManager = credentialManager,
                                            onAuthSuccess = { user ->
                                                isSigningIn = false
                                                onGoogleAuthSuccess(user)
                                            },
                                            onAuthError = { isSigningIn = false },
                                            scope = scope,
                                            onAuthCancelled = { isSigningIn = false }
                                        )
                                    },
                                    enabled = !isSigningIn,
                                    border = BorderStroke(1.dp, borderColor),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("onboarding_google_signin_button")
                                ) {
                                    if (isSigningIn) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = MatteEmerald,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = MatteEmerald,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isEn) {
                                                "Link Google Account (Optional)"
                                            } else {
                                                "Vincular Cuenta con Google (Opcional)"
                                            },
                                            color = textPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Single Primary Bottom CTA Button
            Button(
                onClick = {
                    if (currentSlide < 2) {
                        currentSlide++
                    } else {
                        onFinishOnboarding(selectedExpLevel)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatteEmerald,
                    contentColor = Color(0xFF022C22)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_next_button")
            ) {
                Text(
                    text = when {
                        currentSlide < 2 -> if (isEn) "Continue" else "Continuar"
                        else -> if (isEn) "Start My Path" else "¡Comenzar Mi Ruta!"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (currentSlide < 2) {
                        Icons.AutoMirrored.Filled.ArrowForward
                    } else {
                        Icons.Default.RocketLaunch
                    },
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
