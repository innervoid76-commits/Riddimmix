package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.D3SignalFlowWebView
import com.example.ui.components.InteractiveSignalFlowView
import com.example.ui.components.RoutingNode
import com.example.ui.theme.*

@Composable
fun SignalFlowScreen(
    modifier: Modifier = Modifier
) {
    var viewMode by remember { mutableStateOf(0) } // 0: D3 Interactive Graph, 1: Studio Bus Grid
    var selectedNodeState by remember { mutableStateOf<RoutingNode?>(null) }
    var d3SelectedInfo by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "DAW ROUTING TOPOLOGY",
                color = NeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Interactive D3 Signal Flow & Stem Processing",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Visualize the complete path from the 11 audio channels into the 8 mixbuses, the 4 spatial stem channels (Mid/Side/L/R), through the Pre-Master, and terminating into the 5-stage Master Chain.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        // View Mode Selector (D3 vs Bus Grid)
        item {
            TabRow(
                selectedTabIndex = viewMode,
                containerColor = DarkSurface,
                contentColor = NeonGreen,
                divider = { HorizontalDivider(color = DarkCardBorder) }
            ) {
                Tab(
                    selected = viewMode == 0,
                    onClick = { viewMode = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BubbleChart, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "D3.js Signal Graph",
                                fontWeight = if (viewMode == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (viewMode == 0) NeonGreen else TextSecondary
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_d3_view")
                )
                Tab(
                    selected = viewMode == 1,
                    onClick = { viewMode = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Studio Bus Columns",
                                fontWeight = if (viewMode == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (viewMode == 1) NeonCyan else TextSecondary
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_grid_view")
                )
            }
        }

        // Active Visualization Engine
        if (viewMode == 0) {
            item {
                D3SignalFlowWebView(
                    onNodeSelected = { id, name, desc ->
                        d3SelectedInfo = "$name: $desc"
                    }
                )
            }
        } else {
            item {
                InteractiveSignalFlowView(
                    onSelectNode = { selectedNodeState = it }
                )
            }
        }

        // Sidechain Routing Protocol
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("sidechain_protocol_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SettingsInputComponent, contentDescription = null, tint = NeonOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FL STUDIO 20 SIDECHAIN WIRING PROTOCOL",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1. Unlink Master Send: Highlight Channels 1-11 and ensure direct send cables to the Master Track are completely detached (click send arrow to disable).",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2. Sidechain Cables: Select Kick (Ch 1) -> Right click Sub Bass (Ch 6) send arrow -> Select 'Sidechain to this track'. Volume will sit at 0 dB (silent trigger).",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3. VST Wrapper Connections: In FabFilter Pro-MB/Pro-C 2 -> Wrapper Settings (Gear) -> Processing Tab -> Map Sidechain Input 1 to Kick Channel.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Spatial Matrix Rules
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AltRoute, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MID / SIDE & LEFT / RIGHT BUS SPECIFICATIONS",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Text("MID BUS (L+R)", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mono core sum. Carries Kick knock, Sub, Snare body. Processed with Pro-Q 3 linear phase & Neutron Clipper.", color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Text("SIDE BUS (L-R)", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Stereo difference. High-cut sub strictly at 135Hz! Zero bass allowed. Warm Tube saturation via Saturn 2.", color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Text("LEFT BUS (L)", color = NeonOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Discrete Left monitoring & alignment. Ensures Left speaker punch matches Right speaker punch within 0.3 dB.", color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Text("RIGHT BUS (R)", color = NeonOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Discrete Right monitoring. Prevents asymmetrical drop phase drift across festival arrays.", color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
