package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MasterStageConfig
import com.example.ui.components.InteractiveStudioDial
import com.example.ui.components.LoudnessLufsMeter
import com.example.ui.components.PhaseCorrelationMeter
import com.example.ui.theme.*

@Composable
fun MasteringLabScreen(
    masterStages: List<MasterStageConfig>,
    simulatedLufs: Float,
    simulatedTruePeak: Float,
    simulatedCorrelation: Float,
    getDialValue: (String, Float) -> Float,
    onUpdateDial: (String, Float) -> Unit,
    onResetDials: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Metering & Calibration HUD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mastering_hud"),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NeonGreen))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MASTER LAB METERS",
                                color = NeonGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = onResetDials,
                            modifier = Modifier.size(28.dp).testTag("btn_reset_master_dials")
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = "Reset Dials", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live LUFS Meter
                    LoudnessLufsMeter(
                        currentLufs = simulatedLufs,
                        targetLufs = -6.0f,
                        truePeak = simulatedTruePeak,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phase Correlation Meter
                    PhaseCorrelationMeter(
                        correlationValue = simulatedCorrelation,
                        targetValue = 0.88f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Status: ${if (simulatedLufs >= -6.3f && simulatedLufs <= -5.7f) "PERFECT -6.0 LUFS HIT!" else "ADJUST CLIPPER & PRO-L 2 GAIN"}",
                            color = if (simulatedLufs >= -6.3f && simulatedLufs <= -5.7f) NeonGreen else NeonOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "True Peak: ${if (simulatedTruePeak <= -0.1f) "Compliant" else "Exceeds Ceiling"}",
                            color = if (simulatedTruePeak <= -0.1f) NeonGreen else NeonRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Secret to -6 LUFS Callout
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NeonOrange))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚡ THE RIDDIM -6.0 LUFS FORMULA",
                            color = NeonOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Never rely on a limiter alone for -6 LUFS. Limiting drum transients squashes low-end and produces audible flutter. The secret is staging: Neutron Clipper shaves 2.2 dB of kick and snare peaks BEFORE hitting Pro-L 2. Pro-L 2 then only has to apply 2.5 to 3.0 dB of gain reduction with 8x oversampling.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // 5-Stage Serial Rack List
        item {
            Text(
                text = "5-STAGE SERIAL MASTER CHAIN",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        items(masterStages) { stage ->
            val isClipper = stage.pluginName.contains("Clipper")
            val isLimiter = stage.pluginName.contains("Pro-L 2")
            val cardAccent = if (isLimiter) NeonGreen else if (isClipper) NeonRed else DarkCardBorder

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("master_stage_${stage.step}"),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(cardAccent))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isLimiter) NeonGreen else if (isClipper) NeonRed else Color(0xFF26334A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${stage.step}",
                                    color = if (isLimiter || isClipper) DarkBg else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = stage.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stage.pluginName,
                                    color = if (isLimiter) NeonGreen else if (isClipper) NeonOrange else NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Goal: ${stage.targetGoal}",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dials
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        stage.dialSettings.forEach { dial ->
                            val curVal = getDialValue(dial.id, dial.currentValue)
                            val accent = if (isClipper) NeonOrange else if (isLimiter) NeonGreen else NeonCyan
                            InteractiveStudioDial(
                                dial = dial,
                                value = curVal,
                                onValueChange = { newVal -> onUpdateDial(dial.id, newVal) },
                                accentColor = accent,
                                modifier = Modifier.width(115.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Guide: ${stage.operationalGuide}",
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
