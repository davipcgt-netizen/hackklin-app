package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.getLevelEmoji
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.ElevatedGraphite
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.MatteEmeraldPressed
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SlateCardSurface
import com.example.ui.theme.SubtleBorderSlate
import com.example.ui.theme.TacticalSlate
import com.example.ui.theme.TacticalSlateLight
import com.example.ui.theme.TerminalAmber
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.HackklinUiState

@Composable
fun RoutesTabScreen(
    uiState: HackklinUiState,
    onSelectLevel: (Int?) -> Unit,
    onOpenTopic: (CurriculumTopic) -> Unit,
    onDismissStreakBanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEn = uiState.language == AppLanguage.EN
    val isLight = uiState.themeMode == AppThemeMode.LIGHT
    val activeLevelNumber = uiState.selectedLevelNumber ?: uiState.getHighestUnlockedLevel()
    val currentLevel = uiState.levels.find { it.number == activeLevelNumber } ?: uiState.levels.first()
    val isCurrentLevelUnlocked = uiState.isLevelUnlocked(currentLevel.number)
    val nextMissionTopic = uiState.getNextActiveTopic()

    val bgColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textPrimary = MaterialTheme.colorScheme.onSurface

    val infiniteTransition = rememberInfiniteTransition(label = "mapAnim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val floatOffsetY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // Subtle Background Tactical Grid on Canvas
        val dotColor = if (isLight) Color(0xFFCBD5E1) else Color(0xFF161D26)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 36.dp.toPx()
            var x = step / 2f
            while (x < size.width) {
                var y = step / 2f
                while (y < size.height) {
                    drawCircle(
                        color = dotColor,
                        radius = 1.5f,
                        center = Offset(x, y)
                    )
                    y += step
                }
                x += step
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Minimalist HUD Top Bar (Rank Emoji + Streak + XP)
            MinimalistHudBar(
                uiState = uiState,
                onDismissStreakBanner = onDismissStreakBanner
            )

            // 2. Sleek Compact Chapter Switcher Banner
            ChapterSwitcherBanner(
                level = currentLevel,
                totalLevels = uiState.levels.size,
                isUnlocked = isCurrentLevelUnlocked,
                progressFraction = uiState.getLevelProgressFraction(currentLevel.number),
                onPrevLevel = {
                    if (currentLevel.number > 1) {
                        onSelectLevel(currentLevel.number - 1)
                    }
                },
                onNextLevel = {
                    if (currentLevel.number < uiState.levels.size) {
                        onSelectLevel(currentLevel.number + 1)
                    }
                },
                onSelectLevelDot = { lvl -> onSelectLevel(lvl) },
                uiState = uiState
            )

            // 3. Centerpiece Winding Game Map
            LazyColumn(
                contentPadding = PaddingValues(top = 24.dp, bottom = 110.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("routes_map_list")
            ) {
                item {
                    WindingGamePathCanvas(
                        level = currentLevel,
                        isLevelUnlocked = isCurrentLevelUnlocked,
                        uiState = uiState,
                        pulseScale = pulseScale,
                        floatOffsetY = floatOffsetY,
                        onOpenTopic = onOpenTopic
                    )
                }
            }
        }

        // 4. Floating Bottom CTA Pill ("Continuar Clase")
        Surface(
            color = cardColor.copy(alpha = 0.96f),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isEn) {
                            "NEXT LESSON • ${nextMissionTopic.code}"
                        } else {
                            "SIGUIENTE CLASE • ${nextMissionTopic.code}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = nextMissionTopic.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = textPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { onOpenTopic(nextMissionTopic) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MatteEmerald,
                        contentColor = Color(0xFF022C22)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("continue_mission_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isEn) "Study" else "Estudiar",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MinimalistHudBar(
    uiState: HackklinUiState,
    onDismissStreakBanner: () -> Unit
) {
    val cardColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textPrimary = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Rank Pill with Emoji
            Surface(
                color = cardColor,
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = uiState.currentRank.emoji,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = uiState.currentRank.localizedTitle(uiState.language),
                        style = MaterialTheme.typography.labelLarge,
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Streak Counter
                Surface(
                    color = cardColor,
                    border = BorderStroke(
                        1.dp,
                        if (uiState.userStats.currentStreakDays > 0) MatteEmerald else borderColor
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Racha",
                            tint = if (uiState.userStats.currentStreakDays > 0) MatteEmerald else TacticalSlate,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${uiState.userStats.currentStreakDays}",
                            style = MaterialTheme.typography.labelLarge,
                            color = textPrimary
                        )
                    }
                }

                // XP Counter
                Surface(
                    color = cardColor,
                    border = BorderStroke(1.dp, borderColor),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "XP",
                            tint = MatteEmerald,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${uiState.userStats.totalXp}",
                            style = MaterialTheme.typography.labelLarge,
                            color = textPrimary
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = uiState.streakWasResetBanner) {
            Surface(
                color = Color(0xFF1F160A),
                border = BorderStroke(1.dp, TerminalAmber),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Racha reiniciada (+24h inactivo). ¡Completa una clase hoy!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismissStreakBanner,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color(0xFFFDE68A),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterSwitcherBanner(
    level: CurriculumLevel,
    totalLevels: Int,
    isUnlocked: Boolean,
    progressFraction: Float,
    onPrevLevel: () -> Unit,
    onNextLevel: () -> Unit,
    onSelectLevelDot: (Int) -> Unit,
    uiState: HackklinUiState
) {
    val isEn = uiState.language == AppLanguage.EN
    val cardColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val emoji = getLevelEmoji(level.number)

    Surface(
        color = cardColor,
        border = BorderStroke(1.dp, if (isUnlocked) MatteEmerald.copy(alpha = 0.5f) else borderColor),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onPrevLevel,
                    enabled = level.number > 1,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Nivel anterior",
                        tint = if (level.number > 1) textPrimary else borderColor
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isUnlocked) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = TerminalAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = if (isEn) {
                                "$emoji LEVEL ${level.number} OF $totalLevels • ${level.rankName.uppercase()}"
                            } else {
                                "$emoji NIVEL ${level.number} DE $totalLevels • ${level.rankName.uppercase()}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUnlocked) MatteEmerald else TerminalAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = level.subtitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = textPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onNextLevel,
                    enabled = level.number < totalLevels,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Siguiente nivel",
                        tint = if (level.number < totalLevels) TextPrimary else SubtleBorderSlate
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 8 Level Indicator Dots + Progress Bar
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (lvlNum in 1..totalLevels) {
                    val selected = lvlNum == level.number
                    val unlocked = uiState.isLevelUnlocked(lvlNum)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(7.dp)
                            .width(if (selected) 24.dp else 10.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                when {
                                    selected -> MatteEmerald
                                    unlocked -> TacticalSlateLight
                                    else -> SubtleBorderSlate
                                }
                            )
                            .clickable { onSelectLevelDot(lvlNum) }
                            .testTag("level_selector_$lvlNum")
                    )
                }
            }
        }
    }
}

@Composable
private fun WindingGamePathCanvas(
    level: CurriculumLevel,
    isLevelUnlocked: Boolean,
    uiState: HackklinUiState,
    pulseScale: Float,
    floatOffsetY: Float,
    onOpenTopic: (CurriculumTopic) -> Unit
) {
    val offsetsDp: List<Dp> = listOf(0.dp, (-66).dp, 0.dp, 66.dp, 0.dp)
    val nodeIcons: List<ImageVector> = listOf(
        Icons.Default.Security,
        Icons.Default.Radar,
        Icons.Default.Router,
        Icons.Default.Dns,
        Icons.Default.Hub,
        Icons.Default.Key,
        Icons.Default.Terminal
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        level.topics.forEachIndexed { index, topic ->
            val prog = uiState.topicProgressMap[topic.id]
            val isNodeUnlocked = isLevelUnlocked && uiState.isTopicUnlocked(topic)
            val isNodeCompleted = prog?.isFullyCompleted == true
            val isNodeCurrent = isNodeUnlocked && !isNodeCompleted
            val xOffset = offsetsDp[index % offsetsDp.size]
            val icon = nodeIcons[(level.number + index) % nodeIcons.size]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(x = xOffset)
                    .testTag("topic_item_${topic.id}")
            ) {
                // Floating "EMPEZAR" Callout Bubble above the active node
                if (isNodeCurrent) {
                    Surface(
                        color = MatteEmerald,
                        shape = RoundedCornerShape(50),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .offset(y = floatOffsetY.dp)
                            .padding(bottom = 6.dp)
                            .clickable { onOpenTopic(topic) }
                    ) {
                        Text(
                            text = "¡EMPEZAR!",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF022C22),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }
                }

                // 3D-Styled Circular Game Node
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(96.dp)
                ) {
                    if (isNodeCurrent) {
                        Canvas(modifier = Modifier.size(96.dp)) {
                            drawCircle(
                                color = MatteEmerald.copy(alpha = 0.24f),
                                radius = (size.minDimension / 2.1f) * pulseScale
                            )
                        }
                    }

                    // Bottom 3D lip shadow for tactile depth
                    Box(
                        modifier = Modifier
                            .offset(y = 5.dp)
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isNodeCompleted || isNodeCurrent -> MatteEmeraldPressed
                                    else -> Color(0xFF0D1117)
                                }
                            )
                    )

                    // Top Main Node Circle
                    Surface(
                        color = when {
                            isNodeCompleted -> MatteEmerald
                            isNodeCurrent -> MatteEmeraldContainer
                            isNodeUnlocked -> SlateCardSurface
                            else -> DeepCharcoal
                        },
                        border = BorderStroke(
                            width = if (isNodeCurrent) 3.dp else 2.dp,
                            color = when {
                                isNodeCompleted || isNodeCurrent -> MatteEmerald
                                isNodeUnlocked -> TacticalSlateLight
                                else -> SubtleBorderSlate
                            }
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .clickable(enabled = isNodeUnlocked) {
                                onOpenTopic(topic)
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            when {
                                isNodeCompleted -> Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completado",
                                    tint = Color(0xFF022C22),
                                    modifier = Modifier.size(34.dp)
                                )
                                isNodeUnlocked -> Icon(
                                    imageVector = icon,
                                    contentDescription = topic.title,
                                    tint = MatteEmerald,
                                    modifier = Modifier.size(30.dp)
                                )
                                else -> Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Bloqueado",
                                    tint = TextMuted,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                // 3 Stars Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                ) {
                    val stages = listOf(
                        prog?.theoryCompleted == true,
                        prog?.labCompleted == true,
                        prog?.quizCompleted == true
                    )
                    stages.forEach { starDone ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (starDone) MatteEmerald else SubtleBorderSlate,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Short Clean Node Title Pill (Max 1 line, crisp)
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isNodeUnlocked) TextPrimary else TextMuted,
                    fontWeight = if (isNodeCurrent) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(175.dp)
                        .padding(top = 2.dp)
                )
            }

            // Curved Bezier Path to next node
            if (index < level.topics.lastIndex) {
                val nextOffset = offsetsDp[(index + 1) % offsetsDp.size]
                SmoothPathConnector(
                    startXOffsetDp = xOffset,
                    endXOffsetDp = nextOffset,
                    isCompleted = isNodeCompleted
                )
            }
        }
    }
}

@Composable
private fun SmoothPathConnector(
    startXOffsetDp: Dp,
    endXOffsetDp: Dp,
    isCompleted: Boolean
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        val centerX = size.width / 2f
        val startX = centerX + startXOffsetDp.toPx()
        val endX = centerX + endXOffsetDp.toPx()

        val path = Path().apply {
            moveTo(startX, 0f)
            cubicTo(
                startX,
                size.height * 0.55f,
                endX,
                size.height * 0.45f,
                endX,
                size.height
            )
        }

        drawPath(
            path = path,
            color = if (isCompleted) MatteEmerald else SubtleBorderSlate,
            style = Stroke(
                width = 9f,
                cap = StrokeCap.Round,
                pathEffect = if (isCompleted) null else PathEffect.dashPathEffect(floatArrayOf(14f, 14f), 0f)
            )
        )
    }
}
