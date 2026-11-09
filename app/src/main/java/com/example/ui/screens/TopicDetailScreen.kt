package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TopicProgressEntity
import com.example.data.model.CurriculumTopic
import com.example.data.model.QuizQuestion
import com.example.data.repository.CliExecutionResult
import com.example.ui.components.AnimatedCyberStageBanner
import com.example.ui.components.InteractiveCliTerminal
import com.example.ui.components.TypewriterStoryCard
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.ElevatedGraphite
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SlateCardSurface
import com.example.ui.theme.SubtleBorderSlate
import com.example.ui.theme.TacticalSlateLight
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalErrorCoral
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private sealed class InteractiveMicroStep {
    data class TypewriterLine(
        val stepId: String,
        val sceneIndex: Int,
        val badgeLabel: String,
        val heading: String,
        val textChunk: String,
        val codeVisual: String? = null,
        val takeawayTip: String? = null,
        val isLastTheoryChunk: Boolean = false
    ) : InteractiveMicroStep()

    data class CliMission(val topic: CurriculumTopic) : InteractiveMicroStep()
    data class QuizStep(val qIndex: Int, val totalQ: Int, val question: QuizQuestion) : InteractiveMicroStep()
    data class VictoryStep(val topic: CurriculumTopic) : InteractiveMicroStep()
}

@Composable
fun TopicDetailScreen(
    topic: CurriculumTopic,
    progress: TopicProgressEntity?,
    onBack: () -> Unit,
    onCompleteTheory: () -> Unit,
    onExecuteCli: (String) -> CliExecutionResult,
    onCompleteQuiz: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    // Break paragraphs into short, bite-sized typewriter steps so the user never sees a wall of text!
    val microSteps = remember(topic.id) {
        buildList {
            // Opening welcome step
            add(
                InteractiveMicroStep.TypewriterLine(
                    stepId = "${topic.id}_intro",
                    sceneIndex = 0,
                    badgeLabel = "${topic.code} • INICIO",
                    heading = topic.title,
                    textChunk = topic.summary
                )
            )

            topic.theorySections.forEachIndexed { secIdx, sec ->
                val paragraphs = sec.body
                    .split("\n\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                paragraphs.forEachIndexed { pIdx, paragraph ->
                    val isLastParagraphOfSection = pIdx == paragraphs.lastIndex
                    val isVeryLastTheory = secIdx == topic.theorySections.lastIndex && isLastParagraphOfSection
                    add(
                        InteractiveMicroStep.TypewriterLine(
                            stepId = "${topic.id}_s${secIdx}_p${pIdx}",
                            sceneIndex = secIdx + pIdx + 1,
                            badgeLabel = "PASO TEÓRICO ${secIdx + 1}.${pIdx + 1}",
                            heading = sec.heading,
                            textChunk = paragraph,
                            codeVisual = if (isLastParagraphOfSection) sec.codeSnippet else null,
                            takeawayTip = if (isLastParagraphOfSection) sec.keyTakeaway else null,
                            isLastTheoryChunk = isVeryLastTheory
                        )
                    )
                }
            }

            add(InteractiveMicroStep.CliMission(topic))

            topic.quizQuestions.forEachIndexed { qIdx, q ->
                add(InteractiveMicroStep.QuizStep(qIdx + 1, topic.quizQuestions.size, q))
            }

            add(InteractiveMicroStep.VictoryStep(topic))
        }
    }

    var currentIndex by remember(topic.id) { mutableIntStateOf(0) }
    val currentStep = microSteps[currentIndex]
    var typingFinished by remember(currentIndex) { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = (currentIndex + 1).toFloat() / microSteps.size.toFloat(),
        label = "lessonStepProgress"
    )

    var localLabSolved by remember(topic.id, progress?.labCompleted) {
        mutableStateOf(progress?.labCompleted == true)
    }
    val selectedQuizAnswers = remember(topic.id) { mutableStateMapOf<String, Int>() }
    val verifiedQuizQuestions = remember(topic.id) { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // 1. Sleek Minimalist Top Bar (Close + Progress + Step Counter)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Surface(
                color = DeepCharcoal,
                border = BorderStroke(1.dp, SubtleBorderSlate),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("topic_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Salir",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                color = MatteEmerald,
                trackColor = DeepCharcoal,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
            )

            Surface(
                color = MatteEmeraldContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "${currentIndex + 1}/${microSteps.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnMatteEmeraldContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // 2. Main Stage Content
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val step = currentStep) {
                is InteractiveMicroStep.TypewriterLine -> {
                    // Visual Animated Stage Banner at the top
                    item {
                        AnimatedCyberStageBanner(
                            sceneIndex = step.sceneIndex,
                            badgeLabel = step.badgeLabel
                        )
                    }

                    // Short Heading
                    item {
                        Text(
                            text = step.heading,
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Letter-by-Letter Typewriter Card!
                    item {
                        TypewriterStoryCard(
                            stepKey = step.stepId,
                            speakerTitle = "Instructor Táctico Hackklin",
                            fullText = step.textChunk,
                            onTypingFinishedChange = { finished ->
                                typingFinished = finished
                            }
                        )
                    }

                    // Visual Code / Diagram Box appears once typing finishes!
                    if (!step.codeVisual.isNullOrBlank()) {
                        item {
                            AnimatedVisibility(visible = typingFinished) {
                                Surface(
                                    color = TerminalBackground,
                                    border = BorderStroke(1.dp, SubtleBorderSlate),
                                    shape = RoundedCornerShape(18.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .horizontalScroll(rememberScrollState())
                                            .padding(18.dp)
                                    ) {
                                        Text(
                                            text = step.codeVisual,
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 13.sp,
                                            lineHeight = 21.sp,
                                            color = Color(0xFFA7F3D0)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Key Takeaway Pill appears once typing finishes!
                    if (!step.takeawayTip.isNullOrBlank()) {
                        item {
                            AnimatedVisibility(visible = typingFinished) {
                                Surface(
                                    color = MatteEmeraldContainer.copy(alpha = 0.4f),
                                    border = BorderStroke(1.dp, MatteEmerald),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = MatteEmerald,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = step.takeawayTip,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is InteractiveMicroStep.CliMission -> {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MatteEmeraldContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        tint = MatteEmerald,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LABORATORIO EN VIVO • CONSOLA CLI",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OnMatteEmeraldContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        InteractiveCliTerminal(
                            scenario = step.topic.cliLab,
                            isCompleted = localLabSolved || progress?.labCompleted == true,
                            onExecuteCommand = { cmd ->
                                val res = onExecuteCli(cmd)
                                if (res.missionAccomplished) {
                                    localLabSolved = true
                                }
                                res
                            }
                        )
                    }
                }

                is InteractiveMicroStep.QuizStep -> {
                    item {
                        val q = step.question
                        val chosenOption = selectedQuizAnswers[q.id]
                        val isVerified = verifiedQuizQuestions[q.id] == true

                        QuizStepCard(
                            questionIndex = step.qIndex,
                            totalQuestions = step.totalQ,
                            question = q,
                            chosenOption = chosenOption,
                            isVerified = isVerified,
                            onSelectOption = { optIdx ->
                                selectedQuizAnswers[q.id] = optIdx
                                verifiedQuizQuestions[q.id] = true
                            }
                        )
                    }
                }

                is InteractiveMicroStep.VictoryStep -> {
                    item {
                        VictoryCelebrationCard(
                            topic = step.topic,
                            onFinish = {
                                onCompleteQuiz(100)
                                onBack()
                            }
                        )
                    }
                }
            }
        }

        // 3. Bottom Action Bar ("Siguiente" Button)
        if (currentStep !is InteractiveMicroStep.VictoryStep) {
            Surface(
                color = DeepCharcoal,
                border = BorderStroke(1.dp, SubtleBorderSlate),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    if (currentIndex > 0) {
                        OutlinedButton(
                            onClick = { currentIndex-- },
                            border = BorderStroke(1.dp, SubtleBorderSlate),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(54.dp)
                                .testTag("prev_slide_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    val canProceed = when (val step = currentStep) {
                        is InteractiveMicroStep.QuizStep -> {
                            selectedQuizAnswers[step.question.id] == step.question.correctIndex
                        }
                        else -> true
                    }

                    Button(
                        onClick = {
                            if (currentStep is InteractiveMicroStep.TypewriterLine && currentStep.isLastTheoryChunk) {
                                onCompleteTheory()
                            }
                            if (currentIndex < microSteps.lastIndex) {
                                val nextIdx = currentIndex + 1
                                if (microSteps[nextIdx] is InteractiveMicroStep.VictoryStep) {
                                    onCompleteQuiz(100)
                                }
                                currentIndex = nextIdx
                            }
                        },
                        enabled = canProceed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MatteEmerald,
                            contentColor = Color(0xFF022C22)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("next_slide_button")
                    ) {
                        Text(
                            text = when (currentStep) {
                                is InteractiveMicroStep.QuizStep -> if (canProceed) "Siguiente" else "Elige la respuesta correcta"
                                is InteractiveMicroStep.CliMission -> if (localLabSolved || progress?.labCompleted == true) {
                                    "Siguiente ➔ Ir al Quiz"
                                } else {
                                    "Siguiente"
                                }
                                else -> "Siguiente"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizStepCard(
    questionIndex: Int,
    totalQuestions: Int,
    question: QuizQuestion,
    chosenOption: Int?,
    isVerified: Boolean,
    onSelectOption: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Surface(
            color = MatteEmeraldContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    tint = MatteEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PREGUNTA RÁPIDA $questionIndex DE $totalQuestions",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnMatteEmeraldContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            question.options.forEachIndexed { optIndex, optionText ->
                val isSelected = chosenOption == optIndex
                val isCorrect = optIndex == question.correctIndex

                val borderColor = when {
                    isVerified && isSelected && isCorrect -> MatteEmerald
                    isVerified && isSelected && !isCorrect -> TerminalErrorCoral
                    isSelected -> MatteEmerald
                    else -> SubtleBorderSlate
                }
                val bgColor = when {
                    isVerified && isSelected && isCorrect -> MatteEmeraldContainer.copy(alpha = 0.65f)
                    isVerified && isSelected && !isCorrect -> TerminalErrorCoral.copy(alpha = 0.18f)
                    isSelected -> SlateCardSurface
                    else -> DeepCharcoal
                }

                Surface(
                    color = bgColor,
                    border = BorderStroke(1.5.dp, borderColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectOption(optIndex) }
                        .testTag("quiz_opt_$optIndex")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = when {
                                isVerified && isSelected && !isCorrect -> TerminalErrorCoral
                                isSelected -> MatteEmerald
                                else -> TacticalSlateLight
                            },
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (isVerified && chosenOption != null) {
            val right = chosenOption == question.correctIndex
            Surface(
                color = if (right) MatteEmeraldContainer.copy(alpha = 0.4f) else Color(0xFF281114),
                border = BorderStroke(1.dp, if (right) MatteEmerald else TerminalErrorCoral),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (right) "✓ ¡Respuesta Correcta!" else "✗ Intenta con otra opción",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (right) MatteEmerald else TerminalErrorCoral,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun VictoryCelebrationCard(
    topic: CurriculumTopic,
    onFinish: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DeepCharcoal),
        border = BorderStroke(2.dp, MatteEmerald),
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(28.dp)
        ) {
            Surface(
                color = MatteEmeraldContainer,
                border = BorderStroke(2.dp, MatteEmerald),
                shape = CircleShape,
                modifier = Modifier.size(88.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MatteEmerald,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¡CLASE SUPERADA!",
                style = MaterialTheme.typography.labelLarge,
                color = MatteEmerald
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.title,
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Desbloqueaste el siguiente nodo de la ruta y mantuviste activa tu racha de 24 horas.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                color = MatteEmeraldContainer,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "+${topic.xpReward} XP GANADOS",
                    style = MaterialTheme.typography.titleMedium,
                    color = OnMatteEmeraldContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatteEmerald,
                    contentColor = Color(0xFF022C22)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("finish_lesson_victory_button")
            ) {
                Text(
                    text = "Volver al Mapa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
