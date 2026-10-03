package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class MixbusPhaseProfile(
    val id: String,
    val name: String,
    val flChannel: String,
    val correlation: Float, // -1.0 to +1.0
    val stereoWidthPercent: Int, // 0 to 150%
    val monoLossDb: Float, // dB lost upon mono folddown
    val lowEndCorrelation: Float, // 30-120 Hz
    val verdict: String,
    val clubStatus: String,
    val recommendations: String,
    val color: Color
)

@Composable
fun VisualStereoPhaseMeter(
    selectedBusId: String,
    onSelectBus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profiles = remember {
        listOf(
            MixbusPhaseProfile(
                id = "master",
                name = "Main Master Sum",
                flChannel = "Master Track",
                correlation = 0.88f,
                stereoWidthPercent = 65,
                monoLossDb = -0.3f,
                lowEndCorrelation = 1.00f,
                verdict = "EXCELLENT MONO COMPATIBILITY",
                clubStatus = "100% Club Safe & Punchy",
                recommendations = "Mid/Side processing keeps punchy elements mono and atmospheric pads wide.",
                color = NeonGreen
            ),
            MixbusPhaseProfile(
                id = "bus_sub",
                name = "Sub Mixbus",
                flChannel = "Ch 23",
                correlation = 1.00f,
                stereoWidthPercent = 0,
                monoLossDb = 0.0f,
                lowEndCorrelation = 1.00f,
                verdict = "ROCK-SOLID MONO SUM",
                clubStatus = "Festival Subwoofer Safe",
                recommendations = "Zero stereo width below 90 Hz prevents devastating phase cancellation in dual 18\" subs.",
                color = NeonCyan
            ),
            MixbusPhaseProfile(
                id = "bus_kick",
                name = "Kick Mixbus",
                flChannel = "Ch 20",
                correlation = 0.98f,
                stereoWidthPercent = 5,
                monoLossDb = -0.1f,
                lowEndCorrelation = 1.00f,
                verdict = "STRICT CENTER PUNCH",
                clubStatus = "Maximum Transient Impact",
                recommendations = "Transient click is dead center; no phase smearing across left and right monitors.",
                color = NeonRed
            ),
            MixbusPhaseProfile(
                id = "bus_snare",
                name = "Snare Mixbus",
                flChannel = "Ch 21",
                correlation = 0.94f,
                stereoWidthPercent = 25,
                monoLossDb = -0.2f,
                lowEndCorrelation = 1.00f,
                verdict = "MONO BODY / STEREO SNAP",
                clubStatus = "Club Safe",
                recommendations = "200 Hz body thud is mono while 4-8 kHz rim clatter blooms into stereo sides.",
                color = NeonOrange
            ),
            MixbusPhaseProfile(
                id = "bus_growl",
                name = "Growl / Bass Bus",
                flChannel = "Ch 24",
                correlation = 0.76f,
                stereoWidthPercent = 75,
                monoLossDb = -0.8f,
                lowEndCorrelation = 0.98f,
                verdict = "WIDE HAAS CHOP (CONTROLLED)",
                clubStatus = "Safe: Sub is Protected",
                recommendations = "High-pass the side channel of growls at 135 Hz so wide chorusing doesn't rob sub power.",
                color = NeonPink
            ),
            MixbusPhaseProfile(
                id = "bus_perc",
                name = "Percussion Bus",
                flChannel = "Ch 22",
                correlation = 0.82f,
                stereoWidthPercent = 80,
                monoLossDb = -0.6f,
                lowEndCorrelation = 1.00f,
                verdict = "NATURAL STEREO SPREAD",
                clubStatus = "Clean Fold-Down",
                recommendations = "Hi-hats and cymbals pan &plusmn;35% for an expansive soundstage without center clutter.",
                color = NeonGreen
            ),
            MixbusPhaseProfile(
                id = "bus_pad",
                name = "Pad Mixbus",
                flChannel = "Ch 26",
                correlation = 0.62f,
                stereoWidthPercent = 110,
                monoLossDb = -1.9f,
                lowEndCorrelation = 1.00f,
                verdict = "EXPANSIVE STEREO BED",
                clubStatus = "Acceptable Ambience Loss",
                recommendations = "Subtle volume dip in mono is intentional; keeps drop center stage wide open.",
                color = Color(0xFF64748B)
            ),
            MixbusPhaseProfile(
                id = "sp_side",
                name = "Side Bus (Diff L-R)",
                flChannel = "Ch 31",
                correlation = 0.15f,
                stereoWidthPercent = 150,
                monoLossDb = -12.0f,
                lowEndCorrelation = 1.00f,
                verdict = "PURE STEREO DIFFERENCE",
                clubStatus = "High-passed @ 135 Hz",
                recommendations = "Folds completely to zero in mono by mathematical definition. Crucial that no sub enters here!",
                color = NeonCyan
            )
        )
    }

    val currentProfile = profiles.find { it.id == selectedBusId } ?: profiles.first()
    var isSimulatingMono by remember { mutableStateOf(false) }

    // Pulsing animation for the Lissajous scope
    val infiniteTransition = rememberInfiniteTransition(label = "scopeAnim")
    val scopePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phaseSweep"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(currentProfile.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VISUAL STEREO & PHASE CORRELATION ANALYZER",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }

            // Mono Fold-Down Listen Toggle
            Button(
                onClick = { isSimulatingMono = !isSimulatingMono },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSimulatingMono) NeonOrange else Color(0xFF1E2538)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp).testTag("btn_mono_folddown_toggle")
            ) {
                Icon(
                    imageVector = if (isSimulatingMono) Icons.Default.Hearing else Icons.Default.Headphones,
                    contentDescription = null,
                    tint = if (isSimulatingMono) DarkBg else TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSimulatingMono) "MONO CHECK: ON" else "MONO FOLDDOWN",
                    color = if (isSimulatingMono) DarkBg else TextPrimary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Mixbus Selector Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            profiles.forEach { prof ->
                val isSelected = prof.id == currentProfile.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) DarkCardBorder else Color(0xFF131722))
                        .border(
                            1.dp,
                            if (isSelected) prof.color else Color(0xFF202638),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onSelectBus(prof.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("phase_tab_${prof.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prof.name,
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${(prof.correlation * 100).toInt()}%",
                            color = prof.color,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Goniometer & Vectorscope Canvas + Dual Meters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Goniometer / Lissajous Vectorscope
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF090C12))
                    .border(1.dp, Color(0xFF1E283D), CircleShape)
                    .testTag("goniometer_canvas"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2 - 8.dp.toPx()

                    // Crosshair Axes (M = Vertical, S = Horizontal)
                    drawLine(
                        color = Color(0xFF1E2638),
                        start = Offset(center.x, center.y - radius),
                        end = Offset(center.x, center.y + radius),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF1E2638),
                        start = Offset(center.x - radius, center.y),
                        end = Offset(center.x + radius, center.y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // 45-degree L and R diagonal guides
                    val diag = radius * 0.707f
                    drawLine(
                        color = Color(0xFF171F2E),
                        start = Offset(center.x - diag, center.y - diag),
                        end = Offset(center.x + diag, center.y + diag),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF171F2E),
                        start = Offset(center.x + diag, center.y - diag),
                        end = Offset(center.x - diag, center.y + diag),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Draw concentric reference circles (dB steps)
                    drawCircle(color = Color(0xFF131926), radius = radius * 0.5f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = Color(0xFF182030), radius = radius, style = Stroke(1.dp.toPx()))

                    // Draw dynamic Lissajous Phase Ellipse / Soundfield Cloud
                    val widthNorm = if (isSimulatingMono) 0.05f else (currentProfile.stereoWidthPercent / 100f).coerceIn(0.05f, 1.4f)
                    val corrNorm = if (isSimulatingMono) 1.0f else currentProfile.correlation

                    val path = Path()
                    val points = 32
                    val majorR = radius * 0.85f
                    val minorR = radius * 0.85f * (widthNorm * 0.65f)

                    for (i in 0..points) {
                        val angle = (i.toFloat() / points) * 2 * PI.toFloat()
                        val wobble = sin(angle * 3 + scopePhase) * 3.dp.toPx()

                        // Ellipse coordinates oriented vertically (Mid axis)
                        val xRaw = minorR * sin(angle) + wobble
                        val yRaw = -majorR * cos(angle)

                        // Skew slightly by correlation
                        val ptX = center.x + xRaw
                        val ptY = center.y + yRaw

                        if (i == 0) path.moveTo(ptX, ptY) else path.lineTo(ptX, ptY)
                    }

                    // Draw glow fill
                    val scopeColor = if (isSimulatingMono) NeonOrange else currentProfile.color
                    drawPath(
                        path = path,
                        brush = Brush.radialGradient(
                            colors = listOf(scopeColor.copy(alpha = 0.25f), Color.Transparent),
                            center = center,
                            radius = radius
                        )
                    )

                    // Draw trace line
                    drawPath(
                        path = path,
                        color = scopeColor,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Axis Labels
                Text("M", color = TextMuted, fontSize = 8.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp))
                Text("S", color = TextMuted, fontSize = 8.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 4.dp))
                Text("L", color = TextMuted, fontSize = 7.sp, modifier = Modifier.align(Alignment.TopStart).padding(start = 12.dp, top = 12.dp))
                Text("R", color = TextMuted, fontSize = 7.sp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 12.dp, top = 12.dp))
            }

            // Right side stats & readouts
            Column(modifier = Modifier.weight(1f)) {
                // Correlation Value Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("CORRELATION", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (isSimulatingMono) "+1.00 MONO" else String.format("%+.2f", currentProfile.correlation),
                            color = if (currentProfile.correlation >= 0.8f) NeonGreen else if (currentProfile.correlation >= 0.5f) NeonCyan else NeonOrange,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("STEREO WIDTH", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (isSimulatingMono) "0% (FOLDED)" else "${currentProfile.stereoWidthPercent}%",
                            color = if (currentProfile.stereoWidthPercent == 0) NeonCyan else currentProfile.color,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Phase Correlation Bar Gauge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F1420))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = size.width / 2
                        val currentCorr = if (isSimulatingMono) 1.0f else currentProfile.correlation
                        val norm = ((currentCorr + 1f) / 2f).coerceIn(0f, 1f)
                        val barColor = if (currentCorr >= 0.8f) NeonGreen else if (currentCorr >= 0.5f) NeonCyan else NeonOrange

                        // Center line (0.0)
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = Offset(center, 0f),
                            end = Offset(center, size.height),
                            strokeWidth = 2.dp.toPx()
                        )

                        // Fill bar
                        val targetX = size.width * norm
                        if (targetX >= center) {
                            drawRect(
                                color = barColor,
                                topLeft = Offset(center, 0f),
                                size = Size(targetX - center, size.height)
                            )
                        } else {
                            drawRect(
                                color = NeonRed,
                                topLeft = Offset(targetX, 0f),
                                size = Size(center - targetX, size.height)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("-1.0", color = TextMuted, fontSize = 8.sp)
                    Text("0.0 (Wide)", color = TextMuted, fontSize = 8.sp)
                    Text("+1.0 (Mono)", color = TextMuted, fontSize = 8.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Mono Folddown Loss
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mono Sum Loss:", color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = if (isSimulatingMono) "Active in preview" else "${currentProfile.monoLossDb} dB",
                        color = if (currentProfile.monoLossDb >= -0.5f) NeonGreen else if (currentProfile.monoLossDb >= -1.5f) NeonYellow else NeonOrange,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mono Compatibility Verdict Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(currentProfile.color)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (currentProfile.correlation >= 0.7f) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = currentProfile.color,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentProfile.verdict,
                            color = currentProfile.color,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Text(
                        text = currentProfile.clubStatus,
                        color = NeonGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentProfile.recommendations,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Sub Range (30-120Hz) Correlation: ${String.format("%+.2f", currentProfile.lowEndCorrelation)} ${if (currentProfile.lowEndCorrelation >= 0.98f) "✓ 100% Monophonic Sub" else "⚠ Stereo Sub Warning"}",
                    color = if (currentProfile.lowEndCorrelation >= 0.98f) NeonCyan else NeonRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
