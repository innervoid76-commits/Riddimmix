package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DialSetting
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractiveStudioDial(
    dial: DialSetting,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonGreen
) {
    var isDragging by remember { mutableStateOf(false) }
    val normalizedValue = remember(value, dial.min, dial.max) {
        ((value - dial.min) / (dial.max - dial.min)).coerceIn(0f, 1f)
    }

    // Angle from 135 deg to 405 deg (270 deg sweep)
    val startAngle = 135f
    val sweepAngle = 270f
    val currentAngle = startAngle + (normalizedValue * sweepAngle)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, if (isDragging) accentColor else DarkCardBorder, RoundedCornerShape(12.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dial.name,
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .pointerInput(dial.id) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false }
                    ) { change, dragAmount ->
                        change.consume()
                        val deltaNormalized = -dragAmount.y / 200f
                        val newNorm = (normalizedValue + deltaNormalized).coerceIn(0f, 1f)
                        val computedVal = dial.min + (newNorm * (dial.max - dial.min))
                        onValueChange(computedVal)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 5.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val centerOffset = Offset(size.width / 2, size.height / 2)

                // Background track
                drawArc(
                    color = Color(0xFF263044),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active value arc
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to accentColor.copy(alpha = 0.6f),
                        1.0f to accentColor
                    ),
                    startAngle = startAngle,
                    sweepAngle = normalizedValue * sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth + 1.dp.toPx(), cap = StrokeCap.Round)
                )

                // Inner knob disc
                drawCircle(
                    color = Color(0xFF131824),
                    radius = radius - 7.dp.toPx(),
                    center = centerOffset
                )

                // Indicator line
                val rad = (currentAngle * PI / 180.0).toFloat()
                val innerR = radius - 14.dp.toPx()
                val outerR = radius - 4.dp.toPx()
                val p1 = Offset(centerOffset.x + innerR * cos(rad), centerOffset.y + innerR * sin(rad))
                val p2 = Offset(centerOffset.x + outerR * cos(rad), centerOffset.y + outerR * sin(rad))

                drawLine(
                    color = accentColor,
                    start = p1,
                    end = p2,
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Value inside knob
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val formatted = if (dial.formatDecimals == 0) {
                    value.toInt().toString()
                } else {
                    String.format("%.1f", value)
                }
                Text(
                    text = formatted,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = dial.unit,
                    color = accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Quick micro-step buttons for accessibility & precision
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    val step = (dial.max - dial.min) / 20f
                    onValueChange((value - step).coerceIn(dial.min, dial.max))
                },
                modifier = Modifier
                    .size(24.dp)
                    .testTag("dial_minus_${dial.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease ${dial.name}",
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = {
                    val step = (dial.max - dial.min) / 20f
                    onValueChange((value + step).coerceIn(dial.min, dial.max))
                },
                modifier = Modifier
                    .size(24.dp)
                    .testTag("dial_plus_${dial.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase ${dial.name}",
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Text(
            text = dial.description,
            color = TextMuted,
            fontSize = 9.sp,
            maxLines = 2,
            lineHeight = 11.sp
        )
    }
}

@Composable
fun PhaseCorrelationMeter(
    correlationValue: Float, // from -1.0 to +1.0
    targetValue: Float,
    modifier: Modifier = Modifier
) {
    val normVal = ((correlationValue + 1f) / 2f).coerceIn(0f, 1f)
    val normTarget = ((targetValue + 1f) / 2f).coerceIn(0f, 1f)
    val animatedNorm by animateFloatAsState(targetValue = normVal, label = "phaseAnim")

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PHASE CORRELATION",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = String.format("%+.2f", correlationValue),
                color = if (correlationValue >= 0.8f) NeonGreen else if (correlationValue >= 0.4f) NeonOrange else NeonRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFF0F1420))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size.width / 2

                // Center zero mark line
                drawLine(
                    color = Color.White.copy(alpha = 0.3f),
                    start = Offset(center, 0f),
                    end = Offset(center, size.height),
                    strokeWidth = 2.dp.toPx()
                )

                // Fill bar from center to current value
                val currentX = size.width * animatedNorm
                val barColor = if (correlationValue >= 0.8f) NeonGreen else if (correlationValue >= 0.4f) NeonOrange else NeonRed
                if (currentX >= center) {
                    drawRect(
                        color = barColor,
                        topLeft = Offset(center, 0f),
                        size = Size(currentX - center, size.height)
                    )
                } else {
                    drawRect(
                        color = NeonRed,
                        topLeft = Offset(currentX, 0f),
                        size = Size(center - currentX, size.height)
                    )
                }

                // Target indicator line
                val targetX = size.width * normTarget
                drawLine(
                    color = NeonCyan,
                    start = Offset(targetX, 0f),
                    end = Offset(targetX, size.height),
                    strokeWidth = 2.5.dp.toPx()
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("-1.0 (Out of Phase)", color = TextMuted, fontSize = 8.sp)
            Text("0 (Stereo Diff)", color = TextMuted, fontSize = 8.sp)
            Text("+1.0 (Pure Mono)", color = TextMuted, fontSize = 8.sp)
        }
    }
}

@Composable
fun LoudnessLufsMeter(
    currentLufs: Float,
    targetLufs: Float = -6.0f,
    truePeak: Float = -0.1f,
    modifier: Modifier = Modifier
) {
    // Range: -24 LUFS to 0 LUFS
    val minLufs = -24f
    val maxLufs = 0f
    val normLufs = ((currentLufs - minLufs) / (maxLufs - minLufs)).coerceIn(0f, 1f)
    val normTarget = ((targetLufs - minLufs) / (maxLufs - minLufs)).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FABFILTER PRO-L 2 LOUDNESS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "INTEGRATED: ${String.format("%.1f", currentLufs)} LUFS",
                    color = NeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "TRUE PEAK CEILING",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.1f", truePeak)} dBFS",
                    color = NeonOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Level bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF0F1420))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val fillWidth = size.width * normLufs

                // Gradient meter fill
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF10B981), // Green below -12
                            Color(0xFFF59E0B), // Yellow -12 to -8
                            Color(0xFF00FFA3), // Neon green at target -6
                            Color(0xFFEF4444)  // Red above -5
                        )
                    ),
                    size = Size(fillWidth, size.height)
                )

                // Target -6.0 LUFS marker
                val targetX = size.width * normTarget
                drawLine(
                    color = Color.White,
                    start = Offset(targetX, 0f),
                    end = Offset(targetX, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("-24 LUFS", color = TextMuted, fontSize = 9.sp)
            Text("-14 (Streaming)", color = TextMuted, fontSize = 9.sp)
            Text("-6.0 (Riddim Goal)", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("0 dBFS", color = TextMuted, fontSize = 9.sp)
        }
    }
}
