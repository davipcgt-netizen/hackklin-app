package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.HACKKLIN_RANKS
import com.example.data.model.getLevelEmoji
import com.example.ui.auth.GoogleSignInHelper
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.TerminalErrorCoral
import com.example.ui.viewmodel.HackklinUiState
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileTabScreen(
    uiState: HackklinUiState,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onGoogleAuthSuccess: (FirebaseUser) -> Unit,
    onGoogleSignOut: () -> Unit,
    onSyncCloudNow: () -> Unit,
    onReopenOnboardingTour: () -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEn = uiState.language == AppLanguage.EN
    var showResetDialog by remember { mutableStateOf(false) }
    var isAuthLoading by remember { mutableStateOf(false) }
    var authErrorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    // Silent auto-sign-in check on first composition if not signed in yet
    LaunchedEffect(Unit) {
        if (!uiState.isSignedInWithGoogle) {
            GoogleSignInHelper.attemptAutoSignIn(
                context = context,
                credentialManager = credentialManager,
                onAuthSuccess = { user -> onGoogleAuthSuccess(user) },
                onUnauthenticated = {},
                scope = coroutineScope
            )
        }
    }

    val totalTopicsCount = uiState.levels.sumOf { it.topics.size }
    val completedTopicsCount = uiState.levels.sumOf { level ->
        level.topics.count { uiState.topicProgressMap[it.id]?.isFullyCompleted == true }
    }
    val completedChallengesCount = uiState.challenges.count {
        uiState.challengeProgressMap[it.id]?.completed == true
    }

    val bgColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surface
    val cardVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val borderColor = MaterialTheme.colorScheme.outline
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = cardColor,
            titleContentColor = textPrimary,
            textContentColor = textSecondary,
            title = {
                Text(
                    text = if (isEn) "Reset Local Progress" else "Reiniciar Progreso Local",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isEn) {
                        "Do you want to clear all completed levels, challenges, XP and daily streak stored on this device?"
                    } else {
                        "¿Deseas borrar todo el progreso de niveles, retos, experiencia XP y racha diaria guardado en este dispositivo?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetProgress()
                    },
                    modifier = Modifier.testTag("confirm_reset_button")
                ) {
                    Text(
                        text = if (isEn) "Reset All" else "Reiniciar Todo",
                        color = TerminalErrorCoral,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(
                        text = if (isEn) "Cancel" else "Cancelar",
                        color = textSecondary
                    )
                }
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .testTag("profile_screen_list")
    ) {
        // 1. HERO IDENTITY & RANK EMOJI BANNER CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.5.dp, MatteEmerald),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MatteEmerald.copy(alpha = 0.14f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Glowing Rank Emoji Medallion
                            Surface(
                                color = MatteEmeraldContainer,
                                border = BorderStroke(2.5.dp, MatteEmerald),
                                shape = CircleShape,
                                modifier = Modifier.size(74.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = uiState.currentRank.emoji,
                                        fontSize = 36.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = MatteEmeraldContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "${uiState.currentRank.emoji} ${uiState.currentRank.badgeCode} • ${if (isEn) "CURRENT RANK" else "RANGO ACTUAL"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OnMatteEmeraldContainer,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = uiState.currentRank.localizedTitle(uiState.language),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = textPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = if (uiState.isSignedInWithGoogle) {
                                        "${uiState.authDisplayName ?: "Agente"} • ${uiState.authEmail ?: ""}"
                                    } else {
                                        uiState.currentRank.localizedMotto(uiState.language)
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = borderColor)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Next Rank Progress Bar with Emoji
                        val nextRank = uiState.nextRank
                        if (nextRank != null) {
                            val xpFraction = (uiState.userStats.totalXp.toFloat() / nextRank.minXp.toFloat())
                                .coerceIn(0f, 1f)
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isEn) {
                                        "Next Rank: ${nextRank.emoji} ${nextRank.localizedTitle(uiState.language)}"
                                    } else {
                                        "Próximo Rango: ${nextRank.emoji} ${nextRank.localizedTitle(uiState.language)}"
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    color = textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${uiState.userStats.totalXp} / ${nextRank.minXp} XP",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MatteEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { xpFraction },
                                color = MatteEmerald,
                                trackColor = cardVariantColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )
                        } else {
                            Text(
                                text = if (isEn) {
                                    "👑 Maximum Rank Achieved: Elite Hacker!"
                                } else {
                                    "👑 ¡Has alcanzado el rango máximo de la plataforma (Hacker)!"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = MatteEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. GOOGLE ACCOUNT & CLOUD BACKUP CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (uiState.isSignedInWithGoogle) MatteEmerald else borderColor
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MatteEmeraldContainer,
                                shape = CircleShape,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (uiState.isSignedInWithGoogle) {
                                            Icons.Default.CloudDone
                                        } else {
                                            Icons.Default.AccountCircle
                                        },
                                        contentDescription = null,
                                        tint = MatteEmerald,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (uiState.isSignedInWithGoogle) {
                                        if (isEn) "Google Account Connected" else "Cuenta de Google Conectada"
                                    } else {
                                        if (isEn) "Register / Sign in with Google" else "Registrarse con Google"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (uiState.isSignedInWithGoogle) {
                                        uiState.authEmail ?: (uiState.authDisplayName ?: "Google Cloud Sync")
                                    } else {
                                        if (isEn) {
                                            "Link your Google account to back up your rank, XP & streak"
                                        } else {
                                            "Vincula tu cuenta para respaldar tu rango, XP y racha en la nube"
                                        }
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (uiState.isSignedInWithGoogle) {
                        if (uiState.cloudSyncStatus != null) {
                            Surface(
                                color = MatteEmeraldContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = "☁️ ${uiState.cloudSyncStatus}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MatteEmerald,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onSyncCloudNow,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MatteEmerald,
                                    contentColor = Color(0xFF022C22)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("sync_cloud_profile_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudSync,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEn) "Sync Cloud" else "Sincronizar",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    GoogleSignInHelper.signOut(
                                        credentialManager = credentialManager,
                                        scope = coroutineScope,
                                        onSignOutComplete = onGoogleSignOut
                                    )
                                },
                                border = BorderStroke(1.dp, borderColor),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("google_signout_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = textSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEn) "Sign out" else "Salir",
                                    color = textPrimary
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                isAuthLoading = true
                                authErrorMessage = null
                                GoogleSignInHelper.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = { user ->
                                        isAuthLoading = false
                                        onGoogleAuthSuccess(user)
                                    },
                                    onAuthError = { err ->
                                        isAuthLoading = false
                                        authErrorMessage = err
                                    },
                                    scope = coroutineScope,
                                    onAuthCancelled = {
                                        isAuthLoading = false
                                    }
                                )
                            },
                            enabled = !isAuthLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MatteEmerald,
                                contentColor = Color(0xFF022C22)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("google_signin_button")
                        ) {
                            if (isAuthLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color(0xFF022C22),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isEn) "Sign in with Google" else "Continuar con Google",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (authErrorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = authErrorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = TerminalErrorCoral
                            )
                        }
                    }
                }
            }
        }

        // 3. APPEARANCE & LANGUAGE SETTINGS CARD (Dark/Light + ES/EN)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isEn) "🎨 APPEARANCE & LANGUAGE" else "🎨 APARIENCIA E IDIOMA",
                        style = MaterialTheme.typography.labelMedium,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Theme Selector Row (Dark vs Light)
                    Text(
                        text = if (isEn) "Visual Theme" else "Tema de la Interfaz",
                        style = MaterialTheme.typography.titleSmall,
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            val selected = uiState.themeMode == mode
                            Surface(
                                color = if (selected) MatteEmeraldContainer else cardVariantColor,
                                border = BorderStroke(
                                    width = if (selected) 2.dp else 1.dp,
                                    color = if (selected) MatteEmerald else borderColor
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectThemeMode(mode) }
                                    .testTag("theme_option_${mode.code}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp)
                                ) {
                                    Text(text = mode.emoji, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isEn) mode.labelEn else mode.labelEs,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (selected) OnMatteEmeraldContainer else textPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Language Selector Row (Español vs English)
                    Text(
                        text = if (isEn) "App Language" else "Idioma de la App",
                        style = MaterialTheme.typography.titleSmall,
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            val selected = uiState.language == lang
                            Surface(
                                color = if (selected) MatteEmeraldContainer else cardVariantColor,
                                border = BorderStroke(
                                    width = if (selected) 2.dp else 1.dp,
                                    color = if (selected) MatteEmerald else borderColor
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectLanguage(lang) }
                                    .testTag("lang_option_${lang.code}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp)
                                ) {
                                    Text(text = lang.flagEmoji, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = lang.label,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (selected) OnMatteEmeraldContainer else textPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. 24-HOUR STREAK & TACTICAL TELEMETRY (2x2 Visual Cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EmojiMetricCard(
                        emoji = "🔥",
                        value = if (isEn) "${uiState.userStats.currentStreakDays} Days" else "${uiState.userStats.currentStreakDays} Días",
                        label = if (isEn) "Active Streak (${uiState.hoursRemainingInStreakWindow}h left)" else "Racha Activa (${uiState.hoursRemainingInStreakWindow}h rest.)",
                        cardColor = cardColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    EmojiMetricCard(
                        emoji = "⚡",
                        value = "${uiState.userStats.totalXp} XP",
                        label = if (isEn) "Total Experience" else "Experiencia Total",
                        cardColor = cardColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EmojiMetricCard(
                        emoji = "🎯",
                        value = "$completedTopicsCount / $totalTopicsCount",
                        label = if (isEn) "Classes Completed" else "Clases Dominadas",
                        cardColor = cardColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    EmojiMetricCard(
                        emoji = "🏆",
                        value = "$completedChallengesCount / ${uiState.challenges.size}",
                        label = if (isEn) "Challenges Won" else "Retos Superados",
                        cardColor = cardColor,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. GALLERY OF RANKS WITH EMOJIS (🌱 NOVATO TO 👑 HACKER)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isEn) {
                            "🎖️ RANK INSIGNIA & EMOJIS (ROOKIE TO HACKER)"
                        } else {
                            "🎖️ ESCALA DE RANGOS CON INSIGNIAS (NOVATO A HACKER)"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val currentRankIdx = HACKKLIN_RANKS.indexOf(uiState.currentRank)

                    // Horizontal Quick Showcase of Emoji Badges
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        itemsIndexed(HACKKLIN_RANKS) { idx, rankInfo ->
                            val achieved = idx <= currentRankIdx
                            val isCurrent = idx == currentRankIdx
                            Surface(
                                color = when {
                                    isCurrent -> MatteEmeraldContainer
                                    achieved -> cardVariantColor
                                    else -> cardColor
                                },
                                border = BorderStroke(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (achieved) MatteEmerald else borderColor
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.width(114.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = rankInfo.emoji,
                                        fontSize = 28.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = rankInfo.localizedTitle(uiState.language),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (achieved) textPrimary else textSecondary,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${rankInfo.minXp} XP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (achieved) MatteEmerald else textSecondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = borderColor)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Detailed Rank Rows
                    HACKKLIN_RANKS.forEachIndexed { idx, rankInfo ->
                        val achieved = idx <= currentRankIdx
                        val isCurrent = idx == currentRankIdx
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isCurrent) MatteEmeraldContainer.copy(alpha = 0.35f) else Color.Transparent
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = rankInfo.emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${rankInfo.localizedTitle(uiState.language)} (${rankInfo.badgeCode})",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = if (achieved) textPrimary else textSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (isCurrent) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = MatteEmerald,
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = if (isEn) "ACTIVE" else "ACTUAL",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color(0xFF022C22),
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = rankInfo.localizedMotto(uiState.language),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = textSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = if (achieved) Icons.Default.CheckCircle else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (achieved) MatteEmerald else textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6. CURRICULUM MASTERY BY LEVEL (WITH EMOJIS)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isEn) "🗺️ PROGRESS ACROSS THE 8 LEVELS" else "🗺️ DOMINIO EN LOS 8 NIVELES DE RUTA",
                        style = MaterialTheme.typography.labelMedium,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    uiState.levels.forEach { level ->
                        val frac = uiState.getLevelProgressFraction(level.number)
                        val emoji = getLevelEmoji(level.number)
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "$emoji ${level.title}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${(frac * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MatteEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator(
                                progress = { frac },
                                color = MatteEmerald,
                                trackColor = cardVariantColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }
        }

        // 7. REPLAY ONBOARDING TOUR & RESET BUTTONS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onReopenOnboardingTour,
                    border = BorderStroke(1.5.dp, MatteEmerald),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("reopen_onboarding_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null,
                        tint = MatteEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEn) {
                            "Open Intro Tour & Level Calibration"
                        } else {
                            "Ver Introducción y Guía de Niveles"
                        },
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { showResetDialog = true },
                    border = BorderStroke(1.dp, borderColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = textSecondary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("reset_progress_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = TerminalErrorCoral,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEn) "Reset Local Progress" else "Reiniciar Progreso Local Guardado"
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmojiMetricCard(
    emoji: String,
    value: String,
    label: String,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = cardColor,
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Surface(
                color = MatteEmeraldContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
