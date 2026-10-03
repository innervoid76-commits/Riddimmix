package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Layers
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
import com.example.model.MixbusConfig
import com.example.model.SpatialBusConfig
import com.example.ui.components.InteractiveStudioDial
import com.example.ui.components.VisualStereoPhaseMeter
import com.example.ui.theme.*

@Composable
fun MixbusesScreen(
    mixbuses: List<MixbusConfig>,
    spatialBuses: List<SpatialBusConfig>,
    selectedMixbusId: String,
    selectedSpatialBusId: String,
    selectedPhaseBusId: String = "master",
    onSelectMixbus: (String) -> Unit,
    onSelectSpatialBus: (String) -> Unit,
    onSelectPhaseBus: (String) -> Unit = {},
    getDialValue: (String, Float) -> Float,
    onUpdateDial: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var subTab by remember { mutableStateOf(0) } // 0: 8 Mixbuses, 1: 4 Spatial Stem Buses

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Visual Stereo Phase Correlation Meter
        item {
            VisualStereoPhaseMeter(
                selectedBusId = selectedPhaseBusId,
                onSelectBus = onSelectPhaseBus,
                modifier = Modifier.testTag("mixbus_visual_phase_meter")
            )
        }

        // Toggle SubTab: 8 Mixbuses vs 4 Spatial Buses
        item {
            TabRow(
                selectedTabIndex = subTab,
                containerColor = DarkSurface,
                contentColor = NeonGreen,
                divider = { HorizontalDivider(color = DarkCardBorder) }
            ) {
                Tab(
                    selected = subTab == 0,
                    onClick = { subTab = 0 },
                    text = {
                        Text(
                            "8 Submix Buses",
                            fontWeight = if (subTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (subTab == 0) NeonGreen else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_submix")
                )
                Tab(
                    selected = subTab == 1,
                    onClick = { subTab = 1 },
                    text = {
                        Text(
                            "Spatial M/S & L/R Matrix",
                            fontWeight = if (subTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (subTab == 1) NeonCyan else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_spatial")
                )
            }
        }

        if (subTab == 0) {
            // 8 Mixbuses Content
            item {
                Text(
                    text = "SELECT SUBMIX BUS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mixbuses.forEach { bus ->
                        val isSelected = bus.id == selectedMixbusId
                        val busColor = Color(android.graphics.Color.parseColor(bus.routingColorHex))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) DarkCardBorder else DarkSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) busColor else Color(0xFF222B3D),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { 
                                    onSelectMixbus(bus.id)
                                    onSelectPhaseBus(bus.id)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("mixbus_tab_${bus.id}")
                        ) {
                            Column {
                                Text(
                                    text = bus.name,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                )
                                Text(
                                    text = "Ch ${bus.flMixerChannel} | ${bus.targetPeakDb} dB",
                                    color = busColor,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            val activeMixbus = mixbuses.find { it.id == selectedMixbusId } ?: mixbuses.first()

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            Color(android.graphics.Color.parseColor(activeMixbus.routingColorHex))
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "MIXBUS CH ${activeMixbus.flMixerChannel}",
                                    color = Color(android.graphics.Color.parseColor(activeMixbus.routingColorHex)),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = activeMixbus.name,
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Peak: ${activeMixbus.targetPeakDb} dBFS",
                                color = NeonRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeMixbus.purpose,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Inputs: ${activeMixbus.inputChannels.joinToString(", ")}",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "➔ To: ${activeMixbus.destinationBus}",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (activeMixbus.sidechainRouting.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Cable, contentDescription = null, tint = NeonOrange, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activeMixbus.sidechainRouting,
                                    color = NeonOrange,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "MIXBUS GLUE & CLIPPER FX CHAIN",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            items(activeMixbus.fxChain) { plugin ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${plugin.slotNumber}. ${plugin.name}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = plugin.flTips,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            plugin.dials.forEach { dial ->
                                val curVal = getDialValue(dial.id, dial.currentValue)
                                InteractiveStudioDial(
                                    dial = dial,
                                    value = curVal,
                                    onValueChange = { newVal -> onUpdateDial(dial.id, newVal) },
                                    accentColor = NeonGreen,
                                    modifier = Modifier.width(115.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Spatial M/S & L/R Stem Processing Content
            item {
                Text(
                    text = "THE 4 SPATIAL STEM PROCESSING CHANNELS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    spatialBuses.forEach { sbus ->
                        val isSelected = sbus.id == selectedSpatialBusId
                        val accent = if (sbus.id == "mid_bus") NeonCyan else if (sbus.id == "side_bus") NeonGreen else NeonOrange

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) DarkCardBorder else DarkSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) accent else Color(0xFF222B3D),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { 
                                    onSelectSpatialBus(sbus.id)
                                    onSelectPhaseBus(sbus.id)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("spatial_tab_${sbus.id}")
                        ) {
                            Column {
                                Text(
                                    text = sbus.name,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                )
                                Text(
                                    text = "Peak ${sbus.targetPeakDb} dBFS",
                                    color = accent,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            val activeSpatial = spatialBuses.find { it.id == selectedSpatialBusId } ?: spatialBuses.first()

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (activeSpatial.id == "mid_bus") NeonCyan else if (activeSpatial.id == "side_bus") NeonGreen else NeonOrange
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = activeSpatial.name,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeSpatial.matrixType,
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeSpatial.purpose,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // FL Stereo Shaper Settings
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AltRoute, contentDescription = null, tint = NeonOrange, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("FL Studio Fruity Stereo Shaper Setup:", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(activeSpatial.flStereoShaperSettings, color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Phase Rule: ${activeSpatial.phaseRule}",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(activeSpatial.fabFilterChain) { plugin ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${plugin.slotNumber}. ${plugin.name}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = plugin.flTips,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            plugin.dials.forEach { dial ->
                                val curVal = getDialValue(dial.id, dial.currentValue)
                                InteractiveStudioDial(
                                    dial = dial,
                                    value = curVal,
                                    onValueChange = { newVal -> onUpdateDial(dial.id, newVal) },
                                    accentColor = if (activeSpatial.id == "mid_bus") NeonCyan else NeonGreen,
                                    modifier = Modifier.width(115.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
