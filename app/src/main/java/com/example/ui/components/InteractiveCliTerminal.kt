package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CliLabScenario
import com.example.data.model.TerminalLine
import com.example.data.model.TerminalLineType
import com.example.data.repository.CliExecutionResult
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.ElevatedGraphite
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.SubtleBorderSlate
import com.example.ui.theme.TacticalSlateLight
import com.example.ui.theme.TerminalAmber
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TerminalChipBg
import com.example.ui.theme.TerminalErrorCoral
import com.example.ui.theme.TerminalHeaderBg
import com.example.ui.theme.TerminalOutputGray
import com.example.ui.theme.TerminalPromptGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveCliTerminal(
    scenario: CliLabScenario,
    isCompleted: Boolean,
    onExecuteCommand: (String) -> CliExecutionResult,
    modifier: Modifier = Modifier
) {
    var commandText by remember(scenario.missionTitle) { mutableStateOf("") }
    var showHint by remember(scenario.missionTitle) { mutableStateOf(false) }
    val terminalLines = remember(scenario.missionTitle) {
        mutableStateListOf<TerminalLine>().apply {
            scenario.initialBanner.forEach { line ->
                add(TerminalLine(line, TerminalLineType.BANNER))
            }
        }
    }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun runCommand(cmdToRun: String) {
        val result = onExecuteCommand(cmdToRun)
        if (result.isClearCommand) {
            terminalLines.clear()
            scenario.initialBanner.forEach { line ->
                terminalLines.add(TerminalLine(line, TerminalLineType.BANNER))
            }
        } else {
            terminalLines.addAll(result.newLines)
        }
        commandText = ""
        keyboardController?.hide()
    }

    LaunchedEffect(terminalLines.size) {
        if (terminalLines.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Mission Briefing Card
        Card(
            colors = CardDefaults.cardColors(containerColor = ElevatedGraphite),
            border = BorderStroke(1.dp, if (isCompleted) MatteEmerald else SubtleBorderSlate),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Misión CLI",
                            tint = MatteEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = scenario.missionTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (isCompleted) {
                        Surface(
                            color = MatteEmeraldContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Superado",
                                    tint = MatteEmerald,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SUPERADO",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnMatteEmeraldContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = scenario.briefing,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CLI Window Frame
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(TerminalBackground)
                .border(1.dp, SubtleBorderSlate, RoundedCornerShape(14.dp))
        ) {
            // Terminal Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TerminalHeaderBg)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF64748B))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF475569))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MatteEmerald)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${scenario.promptUser}:${scenario.workingDir}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = TacticalSlateLight
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showHint = !showHint },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("cli_hint_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Mostrar pista",
                            tint = if (showHint) TerminalAmber else TacticalSlateLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { runCommand("clear") },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("cli_clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Limpiar consola",
                            tint = TacticalSlateLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = showHint) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1F1A10))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = TerminalAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = scenario.hint,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = Color(0xFFFDE68A)
                    )
                }
            }

            // Console Output Buffer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 190.dp, max = 280.dp)
                    .verticalScroll(scrollState)
                    .horizontalScroll(rememberScrollState())
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                terminalLines.forEach { line ->
                    val textColor = when (line.type) {
                        TerminalLineType.BANNER -> TacticalSlateLight
                        TerminalLineType.INPUT -> TerminalPromptGreen
                        TerminalLineType.OUTPUT -> TerminalOutputGray
                        TerminalLineType.SUCCESS -> MatteEmerald
                        TerminalLineType.ERROR -> TerminalErrorCoral
                        TerminalLineType.HINT -> TerminalAmber
                    }
                    val weight = when (line.type) {
                        TerminalLineType.INPUT, TerminalLineType.SUCCESS -> FontWeight.Bold
                        else -> FontWeight.Normal
                    }
                    Text(
                        text = line.text,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp,
                        color = textColor,
                        fontWeight = weight
                    )
                }
            }

            // Hybrid Quick Chips Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepCharcoal)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "CHIPS TÁCTICOS DE ENTRADA RÁPIDA (MODO HÍBRIDO):",
                    style = MaterialTheme.typography.labelSmall,
                    color = TacticalSlateLight,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    scenario.quickChips.forEachIndexed { idx, chipCommand ->
                        Surface(
                            color = TerminalChipBg,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, SubtleBorderSlate),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    commandText = chipCommand
                                }
                                .testTag("cli_quick_chip_$idx")
                        ) {
                            Text(
                                text = "$ $chipCommand",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.5.sp,
                                color = MatteEmerald,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Command Input Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = commandText,
                        onValueChange = { commandText = it },
                        placeholder = {
                            Text(
                                text = "Escribe un comando o toca un chip…",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                color = TacticalSlateLight
                            )
                        },
                        prefix = {
                            Text(
                                text = "$ ",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = MatteEmerald
                            )
                        },
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            color = TextPrimary,
                            fontSize = 13.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = { runCommand(commandText) }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MatteEmerald,
                            unfocusedBorderColor = SubtleBorderSlate,
                            focusedContainerColor = TerminalBackground,
                            unfocusedContainerColor = TerminalBackground,
                            cursorColor = MatteEmerald
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cli_command_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { runCommand(commandText) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MatteEmerald,
                            contentColor = Color(0xFF022C22)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("cli_run_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Ejecutar comando",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ejecutar",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
