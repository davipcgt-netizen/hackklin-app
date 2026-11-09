package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.ui.screens.ChallengesTabScreen
import com.example.ui.screens.OnboardingSplashScreen
import com.example.ui.screens.ProfileTabScreen
import com.example.ui.screens.RoutesTabScreen
import com.example.ui.screens.TopicDetailScreen
import com.example.ui.theme.HackklinTheme
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.viewmodel.BottomNavTab
import com.example.ui.viewmodel.HackklinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackklinApp()
        }
    }
}

@Composable
fun HackklinApp(
    viewModel: HackklinViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HackklinTheme(themeMode = uiState.themeMode) {
        if (!uiState.onboardingCompleted) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets.safeDrawing,
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    OnboardingSplashScreen(
                        levels = uiState.levels,
                        language = uiState.language,
                        themeMode = uiState.themeMode,
                        isSignedInWithGoogle = uiState.isSignedInWithGoogle,
                        signedInName = uiState.authDisplayName,
                        onToggleLanguage = { lang -> viewModel.setLanguage(lang) },
                        onToggleTheme = { mode -> viewModel.setThemeMode(mode) },
                        onGoogleAuthSuccess = { user -> viewModel.onGoogleAuthSuccess(user) },
                        onFinishOnboarding = { expLevel -> viewModel.completeOnboarding(expLevel) }
                    )
                }
            }
            return@HackklinTheme
        }

        // Handle back navigation from secondary tabs back to Rutas (Home)
        BackHandler(
            enabled = uiState.activeTopic == null &&
                uiState.activeChallenge == null &&
                uiState.activeTab != BottomNavTab.RUTAS
        ) {
            viewModel.selectTab(BottomNavTab.RUTAS)
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                HackklinBottomBar(
                    activeTab = uiState.activeTab,
                    language = uiState.language,
                    onSelectTab = { tab -> viewModel.selectTab(tab) }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val activeTopic = uiState.activeTopic
                if (activeTopic != null) {
                    TopicDetailScreen(
                        topic = activeTopic,
                        progress = uiState.topicProgressMap[activeTopic.id],
                        onBack = { viewModel.closeTopic() },
                        onCompleteTheory = { viewModel.markTopicTheoryCompleted(activeTopic) },
                        onExecuteCli = { cmd -> viewModel.executeTopicCliCommand(activeTopic, cmd) },
                        onCompleteQuiz = { score -> viewModel.markTopicQuizCompleted(activeTopic, score) }
                    )
                } else {
                    when (uiState.activeTab) {
                        BottomNavTab.RUTAS -> {
                            RoutesTabScreen(
                                uiState = uiState,
                                onSelectLevel = { lvl -> viewModel.selectLevel(lvl) },
                                onOpenTopic = { topic -> viewModel.openTopic(topic) },
                                onDismissStreakBanner = { viewModel.dismissStreakResetBanner() }
                            )
                        }
                        BottomNavTab.RETOS -> {
                            ChallengesTabScreen(
                                uiState = uiState,
                                onOpenChallenge = { chg -> viewModel.openChallenge(chg) },
                                onCloseChallenge = { viewModel.closeChallenge() },
                                onExecuteChallengeCli = { chg, cmd ->
                                    viewModel.executeChallengeCliCommand(chg, cmd)
                                },
                                onCompleteChallengeQuiz = { chg ->
                                    viewModel.markChallengeQuizCompleted(chg)
                                }
                            )
                        }
                        BottomNavTab.PERFIL -> {
                            ProfileTabScreen(
                                uiState = uiState,
                                onSelectThemeMode = { mode -> viewModel.setThemeMode(mode) },
                                onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
                                onGoogleAuthSuccess = { user -> viewModel.onGoogleAuthSuccess(user) },
                                onGoogleSignOut = { viewModel.onGoogleSignOutComplete() },
                                onSyncCloudNow = { viewModel.syncToCloudIfAuthenticated() },
                                onReopenOnboardingTour = { viewModel.reopenOnboardingTour() },
                                onResetProgress = { viewModel.resetAllProgress() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HackklinBottomBar(
    activeTab: BottomNavTab,
    language: AppLanguage,
    onSelectTab: (BottomNavTab) -> Unit
) {
    val isEn = language == AppLanguage.EN
    val routesLabel = if (isEn) "Paths" else "Rutas"
    val challengesLabel = if (isEn) "Challenges" else "Retos"
    val profileLabel = if (isEn) "Profile" else "Perfil"

    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = activeTab == BottomNavTab.RUTAS,
            onClick = { onSelectTab(BottomNavTab.RUTAS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomNavTab.RUTAS) Icons.Filled.Map else Icons.Outlined.Map,
                    contentDescription = routesLabel
                )
            },
            label = {
                Text(
                    text = routesLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (activeTab == BottomNavTab.RUTAS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnMatteEmeraldContainer,
                selectedTextColor = MatteEmerald,
                indicatorColor = MatteEmeraldContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_routes")
        )

        NavigationBarItem(
            selected = activeTab == BottomNavTab.RETOS,
            onClick = { onSelectTab(BottomNavTab.RETOS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomNavTab.RETOS) Icons.Filled.Flag else Icons.Outlined.Flag,
                    contentDescription = challengesLabel
                )
            },
            label = {
                Text(
                    text = challengesLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (activeTab == BottomNavTab.RETOS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnMatteEmeraldContainer,
                selectedTextColor = MatteEmerald,
                indicatorColor = MatteEmeraldContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_challenges")
        )

        NavigationBarItem(
            selected = activeTab == BottomNavTab.PERFIL,
            onClick = { onSelectTab(BottomNavTab.PERFIL) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomNavTab.PERFIL) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = profileLabel
                )
            },
            label = {
                Text(
                    text = profileLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (activeTab == BottomNavTab.PERFIL) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnMatteEmeraldContainer,
                selectedTextColor = MatteEmerald,
                indicatorColor = MatteEmeraldContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_profile")
        )
    }
}
