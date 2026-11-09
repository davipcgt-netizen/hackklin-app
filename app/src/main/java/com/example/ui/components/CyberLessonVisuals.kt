package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MatteEmerald
import com.example.ui.theme.MatteEmeraldContainer
import com.example.ui.theme.OnMatteEmeraldContainer
import com.example.ui.theme.SubtleBorderSlate
import com.example.ui.theme.TacticalSlateLight
import com.example.ui.theme.TerminalAmber
import com.example.ui.theme.TerminalBackground
import com.example.ui.theme.TextPrimary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedCyberStageBanner(
    sceneIndex: Int,
    badgeLabel: String,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "cyberStage")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        color = TerminalBackground,
        border = BorderStroke(1.dp, SubtleBorderSlate),
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val cx = w / 2f
                val cy = h / 2f

                // Subtle Tactical Dot Grid
                val step = 28.dp.toPx()
                var x = step
                while (x < w) {
                    var y = step
                    while (y < h) {
                        drawCircle(
                            color = Color(0xFF1E293B).copy(alpha = 0.55f),
                            radius = 1.6f,
                            center = Offset(x, y)
                        )
                        y += step
                    }
                    x += step
                }

                // Soft radial emerald atmosphere
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MatteEmerald.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        center = Offset(cx, cy),
                        radius = h * 0.85f
                    ),
                    radius = h * 0.85f,
                    center = Offset(cx, cy)
                )

                when (sceneIndex % 3) {
                    0 -> {
                        // SCENE 0: Tactical Shield & Radar Sweep
                        val r1 = h * 0.24f
                        val r2 = h * 0.38f
                        drawCircle(
                            color = SubtleBorderSlate,
                            radius = r2,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                        )
                        drawCircle(
                            color = MatteEmerald.copy(alpha = 0.45f),
                            radius = r1 * pulse,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2.5f)
                        )

                        // Rotating radar beam
                        val angleDeg = phase * 360f
                        rotate(degrees = angleDeg, pivot = Offset(cx, cy)) {
                            drawLine(
                                brush = Brush.linearGradient(
                                    colors = listOf(MatteEmerald, Color.Transparent),
                                    start = Offset(cx, cy),
                                    end = Offset(cx + r2, cy)
                                ),
                                start = Offset(cx, cy),
                                end = Offset(cx + r2, cy),
                                strokeWidth = 4f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Orbiting security nodes
                        for (i in 0..3) {
                            val a = (i * 90.0 + phase * 360.0) * (Math.PI / 180.0)
                            val nx = cx + (r2 * cos(a)).toFloat()
                            val ny = cy + (r2 * sin(a)).toFloat()
                            drawCircle(
                                color = MatteEmerald,
                                radius = 6f,
                                center = Offset(nx, ny)
                            )
                        }
                    }

                    1 -> {
                        // SCENE 1: Live Network Packet Transmission (Client -> Firewall -> Server)
                        val leftX = w * 0.18f
                        val midX = w * 0.50f
                        val rightX = w * 0.82f

                        // Circuit Line
                        drawLine(
                            color = SubtleBorderSlate,
                            start = Offset(leftX, cy),
                            end = Offset(rightX, cy),
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )

                        // Traveling glowing data packet
                        val packetX = leftX + (rightX - leftX) * phase
                        drawCircle(
                            color = MatteEmerald.copy(alpha = 0.35f),
                            radius = 18f * pulse,
                            center = Offset(packetX, cy)
                        )
                        drawCircle(
                            color = MatteEmerald,
                            radius = 8f,
                            center = Offset(packetX, cy)
                        )

                        // 3 Nodes
                        listOf(leftX, midX, rightX).forEachIndexed { idx, nx ->
                            drawCircle(
                                color = DeepCharcoal,
                                radius = 26f,
                                center = Offset(nx, cy)
                            )
                            drawCircle(
                                color = if (idx == 1) TerminalAmber else MatteEmerald,
                                radius = 26f,
                                center = Offset(nx, cy),
                                style = Stroke(width = 4f)
                            )
                        }
                    }

                    else -> {
                        // SCENE 2: Cryptographic Blocks & Signal Telemetry
                        val barCount = 11
                        val spacing = w / (barCount + 4)
                        val startX = spacing * 2.5f
                        for (i in 0 until barCount) {
                            val wave = sin((phase * 2 * Math.PI) + i * 0.6).toFloat()
                            val barH = (h * 0.22f) + (h * 0.28f * ((wave + 1f) / 2f))
                            val bx = startX + i * spacing
                            drawRoundRect(
                                color = if (i % 2 == 0) MatteEmerald else MatteEmerald.copy(alpha = 0.45f),
                                topLeft = Offset(bx - 6f, cy - barH / 2f),
                                size = Size(12f, barH),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                        }
                    }
                }
            }

            // Center Floating Icon Emblem for Scene 0
            if (sceneIndex % 3 == 0) {
                Surface(
                    color = MatteEmeraldContainer,
                    border = BorderStroke(2.dp, MatteEmerald),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.Center)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MatteEmerald,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Top-left tactical badge
            Surface(
                color = DeepCharcoal.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, SubtleBorderSlate),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = when (sceneIndex % 3) {
                            0 -> Icons.Default.Radar
                            1 -> Icons.Default.Bolt
                            else -> Icons.Default.Lock
                        },
                        contentDescription = null,
                        tint = MatteEmerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = badgeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnMatteEmeraldContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Typewriter card that reveals text character-by-character ("letrica por letrica")
 * with a blinking emerald cursor. Tapping the card completes the current animation immediately.
 */
@Composable
fun TypewriterStoryCard(
    stepKey: String,
    speakerTitle: String,
    fullText: String,
    onTypingFinishedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var visibleChars by remember(stepKey, fullText) { mutableIntStateOf(0) }
    val isFinished = visibleChars >= fullText.length

    val infinite = rememberInfiniteTransition(label = "cursorBlink")
    val cursorAlpha by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    LaunchedEffect(stepKey, fullText) {
        visibleChars = 0
        onTypingFinishedChange(false)
        while (visibleChars < fullText.length) {
            delay(16L)
            visibleChars = (visibleChars + 2).coerceAtMost(fullText.length)
        }
        onTypingFinishedChange(true)
    }

    Surface(
        color = DeepCharcoal,
        border = BorderStroke(1.5.dp, if (isFinished) MatteEmerald.copy(alpha = 0.7f) else SubtleBorderSlate),
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                if (!isFinished) {
                    visibleChars = fullText.length
                    onTypingFinishedChange(true)
                }
            }
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(MatteEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = speakerTitle.uppercase(),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.5.sp,
                        color = MatteEmerald,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!isFinished) {
                    Text(
                        text = "Toca para completar texto",
                        style = MaterialTheme.typography.labelSmall,
                        color = TacticalSlateLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val shownSubstring = fullText.take(visibleChars)
            val annotated = buildAnnotatedString {
                append(shownSubstring)
                withStyle(
                    SpanStyle(
                        color = MatteEmerald.copy(alpha = if (isFinished) cursorAlpha else 1f),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" ▋")
                }
            }

            Text(
                text = annotated,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                ),
                color = TextPrimary
            )
        }
    }
}
