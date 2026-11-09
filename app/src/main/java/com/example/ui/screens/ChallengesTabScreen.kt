package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.ChallengeItem
import com.example.data.model.ChallengeType
import com.example.data.repository.CliExecutionResult
import com.example.ui.components.AnimatedCyberStageBanner
import com.example.ui.components.InteractiveCliTerminal
import com.example.ui.components.TypewriterStoryCard
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.ElevatedGraphite
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SlateCardSurface
import com.example.ui.theme.SubtleBorderSlate
import com.example.ui.theme.TacticalSlateContainer
import com.example.ui.theme.TacticalSlateLight
import com.example.ui.theme.TerminalErrorCoral
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.HackklinUiState

@Composable
fun ChallengesTabScreen(
    uiState: HackklinUiState,
    onOpenChallenge: (ChallengeItem) -> Unit,
    onCloseChallenge: () -> Unit,
    onExecuteChallengeCli: (ChallengeItem, String) -> CliExecutionResult,
    onCompleteChallengeQuiz: (ChallengeItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeChallenge = uiState.activeChallenge
    if (activeChallenge != null) {
        val currentIdx = uiState.challenges.indexOfFirst { it.id == activeChallenge.id }
        val nextChallengeInChain = uiState.challenges.getOrNull(currentIdx + 1)
        val isDone = uiState.challengeProgressMap[activeChallenge.id]?.completed == true

        ChallengeWorkspaceScreen(
            stepNumber = currentIdx + 1,
            totalSteps = uiState.challenges.size,
            challenge = activeChallenge,
            isCompleted = isDone,
            nextChallenge = nextChallengeInChain,
            onBack = onCloseChallenge,
            onExecuteCli = { cmd -> onExecuteChallengeCli(activeChallenge, cmd) },
            onCompleteQuiz = { onCompleteChallengeQuiz(activeChallenge) },
            onAdvanceToNextChallenge = { nextChg -> onOpenChallenge(nextChg) }
        )
        return
    }

    val completedCount = uiState.challenges.count {
        uiState.challengeProgressMap[it.id]?.completed == true
    }
    val (activeStepNumber, activeChainChallenge) = uiState.getCurrentChainChallenge()
    val chainFraction = completedCount.toFloat() / uiState.challenges.size.toFloat().coerceAtLeast(1f)

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .testTag("challenges_list")
    ) {
        // 1. Minimalist Hero Card for Active Challenge
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepCharcoal),
                border = BorderStroke(1.5.dp, MatteEmerald),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = MatteEmeraldContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Flag,
                                    contentDescription = null,
                                    tint = MatteEmerald,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MISIÓN #$activeStepNumber DE ${uiState.challenges.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnMatteEmeraldContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "$completedCount / ${uiState.challenges.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MatteEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = activeChainChallenge.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { chainFraction },
                        color = MatteEmerald,
                        trackColor = ElevatedGraphite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onOpenChallenge(activeChainChallenge) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MatteEmerald,
                            contentColor = Color(0xFF022C22)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_active_chain_challenge_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Iniciar Reto #$activeStepNumber (+${activeChainChallenge.xpReward} XP)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Step-by-Step Sequential Challenge Chain Nodes (Ultra-Clean Visual Nodes)
        itemsIndexed(uiState.challenges, key = { _, chg -> chg.id }) { index, challenge ->
            val stepNum = index + 1
            val isUnlocked = uiState.isChallengeUnlockedByIndex(index)
            val isCompleted = uiState.challengeProgressMap[challenge.id]?.completed == true
            val isCurrentActive = isUnlocked && !isCompleted
            val isLast = index == uiState.challenges.lastIndex

            SequentialChallengeChainRow(
                stepNumber = stepNum,
                challenge = challenge,
                isUnlocked = isUnlocked,
                isCompleted = isCompleted,
                isCurrentActive = isCurrentActive,
                isLastItem = isLast,
                onClick = {
                    if (isUnlocked) {
                        onOpenChallenge(challenge)
                    }
                }
            )
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }
    }
}

@Composable
private fun SequentialChallengeChainRow(
    stepNumber: Int,
    challenge: ChallengeItem,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    isCurrentActive: Boolean,
    isLastItem: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("challenge_card_${challenge.id}")
    ) {
        // Left Step Number & Vertical Chain Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(44.dp)
        ) {
            Surface(
                color = when {
                    isCompleted -> MatteEmerald
                    isCurrentActive -> MatteEmeraldContainer
                    else -> Color(0xFF101318)
                },
                border = BorderStroke(
                    width = if (isCurrentActive) 2.5.dp else 1.5.dp,
                    color = when {
                        isCompleted || isCurrentActive -> MatteEmerald
                        else -> SubtleBorderSlate
                    }
                ),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when {
                        isCompleted -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Superado",
                            tint = Color(0xFF022C22),
                            modifier = Modifier.size(20.dp)
                        )
                        isUnlocked -> Text(
                            text = "%02d".format(stepNumber),
                            style = MaterialTheme.typography.labelMedium,
                            color = OnMatteEmeraldContainer,
                            fontWeight = FontWeight.Bold
                        )
                        else -> Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Bloqueado",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (!isLastItem) {
                Canvas(
                    modifier = Modifier
                        .width(4.dp)
                        .height(58.dp)
                ) {
                    drawLine(
                        color = if (isCompleted) MatteEmerald else SubtleBorderSlate,
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, size.height),
                        strokeWidth = 5f,
                        pathEffect = if (isCompleted) null else PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Right Compact Visual Card (No clutter, just clean mission title & badge)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isCurrentActive -> SlateCardSurface
                    isUnlocked -> DeepCharcoal
                    else -> Color(0xFF0A0C0F)
                }
            ),
            border = BorderStroke(
                width = if (isCurrentActive) 1.5.dp else 1.dp,
                color = when {
                    isCurrentActive || isCompleted -> MatteEmerald
                    else -> SubtleBorderSlate
                }
            ),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 14.dp)
                .clip(RoundedCornerShape(18.dp))
                .clickable(enabled = isUnlocked) { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = if (isUnlocked) MatteEmeraldContainer else ElevatedGraphite,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (challenge.type == ChallengeType.CLI_SIMULATOR) {
                                    Icons.Default.Terminal
                                } else {
                                    Icons.Default.Quiz
                                },
                                contentDescription = null,
                                tint = if (isUnlocked) MatteEmerald else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = challenge.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isUnlocked) TextPrimary else TextMuted,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isUnlocked) {
                                challenge.type.label
                            } else {
                                "Requiere Reto #%02d".format(stepNumber - 1)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUnlocked) MatteEmerald else TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = if (isUnlocked) TacticalSlateContainer else ElevatedGraphite,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = if (isUnlocked) MatteEmerald else TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${challenge.xpReward}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUnlocked) TextPrimary else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeWorkspaceScreen(
    stepNumber: Int,
    totalSteps: Int,
    challenge: ChallengeItem,
    isCompleted: Boolean,
    nextChallenge: ChallengeItem?,
    onBack: () -> Unit,
    onExecuteCli: (String) -> CliExecutionResult,
    onCompleteQuiz: () -> Unit,
    onAdvanceToNextChallenge: (ChallengeItem) -> Unit
) {
    BackHandler { onBack() }

    // Step 0 = Mission Typewriter Briefing, Step 1 = Interactive Lab / Quiz
    var stageIndex by remember(challenge.id) { mutableIntStateOf(0) }
    var briefingTypedDone by remember(challenge.id, stageIndex) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // Minimalist Header
        Surface(
            color = DeepCharcoal,
            border = BorderStroke(1.dp, SubtleBorderSlate),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("challenge_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver a la cadena de retos",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RETO #$stepNumber DE $totalSteps • +${challenge.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = challenge.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            if (stageIndex == 0) {
                AnimatedCyberStageBanner(
                    sceneIndex = stepNumber,
                    badgeLabel = "BRIEFING DE OPERACIÓN #$stepNumber"
                )

                TypewriterStoryCard(
                    stepKey = "${challenge.id}_briefing",
                    speakerTitle = challenge.title,
                    fullText = challenge.description,
                    onTypingFinishedChange = { briefingTypedDone = it }
                )
            } else {
                if (challenge.type == ChallengeType.CLI_SIMULATOR && challenge.cliScenario != null) {
                    InteractiveCliTerminal(
                        scenario = challenge.cliScenario,
                        isCompleted = isCompleted,
                        onExecuteCommand = onExecuteCli
                    )
                } else {
                    ChallengeScenarioQuizContent(
                        challenge = challenge,
                        isCompleted = isCompleted,
                        onCompleteQuiz = onCompleteQuiz
                    )
                }

                if (isCompleted) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DeepCharcoal),
                        border = BorderStroke(2.dp, MatteEmerald),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = MatteEmerald,
                                    shape = CircleShape,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Flag,
                                            contentDescription = null,
                                            tint = Color(0xFF022C22)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "¡Reto #$stepNumber Superado!",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (nextChallenge != null) {
                                            "Siguiente desbloqueado: ${nextChallenge.title}"
                                        } else {
                                            "¡Completaste todos los retos!"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (nextChallenge != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { onAdvanceToNextChallenge(nextChallenge) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MatteEmerald,
                                        contentColor = Color(0xFF022C22)
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("next_chain_challenge_button")
                                ) {
                                    Text(
                                        text = "Siguiente Reto #${stepNumber + 1}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (stageIndex == 0) {
            Surface(
                color = DeepCharcoal,
                border = BorderStroke(1.dp, SubtleBorderSlate),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = { stageIndex = 1 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MatteEmerald,
                            contentColor = Color(0xFF022C22)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("challenge_next_to_action_button")
                    ) {
                        Text(
                            text = if (briefingTypedDone) "Siguiente • Abrir Laboratorio" else "Continuar al Laboratorio",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeScenarioQuizContent(
    challenge: ChallengeItem,
    isCompleted: Boolean,
    onCompleteQuiz: () -> Unit
) {
    val selectedMap = remember(challenge.id) { mutableStateMapOf<String, Int>() }
    var currentQuestionIdx by remember(challenge.id) { mutableIntStateOf(0) }
    var submitted by remember(challenge.id) { mutableStateOf(isCompleted) }

    val questions = challenge.quizQuestions
    val q = questions.getOrNull(currentQuestionIdx) ?: return
    val chosen = selectedMap[q.id]

    val allCorrect = questions.all { item ->
        selectedMap[item.id] == item.correctIndex
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TypewriterStoryCard(
            stepKey = "chg_q_${q.id}",
            speakerTitle = "Decisión Táctica ${currentQuestionIdx + 1} de ${questions.size}",
            fullText = q.question,
            onTypingFinishedChange = {}
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = DeepCharcoal),
            border = BorderStroke(1.dp, SubtleBorderSlate),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                q.options.forEachIndexed { optIdx, optText ->
                    val isSelected = chosen == optIdx
                    val isRight = optIdx == q.correctIndex
                    val border = when {
                        submitted && isRight -> MatteEmerald
                        submitted && isSelected && !isRight -> TerminalErrorCoral
                        isSelected -> MatteEmerald
                        else -> SubtleBorderSlate
                    }
                    Surface(
                        color = if (isSelected) SlateCardSurface else ElevatedGraphite,
                        border = BorderStroke(1.5.dp, border),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectedMap[q.id] = optIdx
                                submitted = false
                            }
                            .testTag("challenge_q${currentQuestionIdx}_opt${optIdx}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) MatteEmerald else TacticalSlateLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                        }
                    }
                }

                if (submitted) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = TacticalSlateContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = q.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        if (currentQuestionIdx < questions.lastIndex) {
            Button(
                onClick = { currentQuestionIdx++ },
                enabled = chosen != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatteEmerald,
                    contentColor = Color(0xFF022C22)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Siguiente Decisión",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = {
                    submitted = true
                    if (allCorrect) {
                        onCompleteQuiz()
                    }
                },
                enabled = selectedMap.size == questions.size,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatteEmerald,
                    contentColor = Color(0xFF022C22)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_challenge_quiz_button")
            ) {
                Text(
                    text = "Validar Reto",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
