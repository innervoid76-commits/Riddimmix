package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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
import com.example.ui.theme.*

data class RoutingNode(
    val id: String,
    val name: String,
    val stage: String,
    val flChannel: String,
    val color: Color,
    val peakDb: String,
    val destNode: String,
    val sidechainTo: String = "",
    val details: String
)

@Composable
fun InteractiveSignalFlowView(
    onSelectNode: (RoutingNode) -> Unit,
    modifier: Modifier = Modifier
) {
    val nodes = remember {
        listOf(
            // Stage 1: Channels
            RoutingNode("ch_kick", "Kick", "Channels", "Ch 1", NeonRed, "-6.0 dBFS", "Kick Bus", "Sub & Mid Bass", "Punchy 50Hz fundamental. Linear phase low cut 32Hz."),
            RoutingNode("ch_snare", "Snare", "Channels", "Ch 2", NeonOrange, "-6.0 dBFS", "Snare Bus", "Mid Bass & Growls", "210Hz body thud with 4.5kHz crack. High passed at 120Hz."),
            RoutingNode("ch_sub", "Sub Bass", "Channels", "Ch 6", NeonCyan, "-7.0 dBFS", "Sub Bus", "", "Pure 30-80Hz mono sine. Pro-MB dynamic sidechain ducked by Kick."),
            RoutingNode("ch_midbass", "Mid Bass", "Channels", "Ch 7", NeonPink, "-9.0 dBFS", "Growl Bus", "", "480Hz riddim honk. Shaved by Neutron Clipper."),
            RoutingNode("ch_growl", "Growl", "Channels", "Ch 8", NeonPink, "-8.0 dBFS", "Growl Bus", "", "Comb filtered screech. Routed 70% Mid, 30% Side."),
            RoutingNode("ch_perc", "Tops/Perc", "Channels", "Ch 3-5", NeonGreen, "-10.0 dBFS", "Perc Bus", "", "Hi-Hats, Cymbals & Woodblocks. High passed above 350Hz."),
            RoutingNode("ch_synths", "Leads/Pads", "Channels", "Ch 9-10", Color(0xFF38BDF8), "-12.0 dBFS", "Lead/Pad Bus", "", "Laser stabs & atmospheric background beds."),
            RoutingNode("ch_vox", "Vocal Chants", "Channels", "Ch 11", Color(0xFFF43F5E), "-9.0 dBFS", "Vocal Bus", "Ducks Synths", "Ragga toasting & pre-drop vocal chants."),

            // Stage 2: Mixbuses
            RoutingNode("bus_kick", "Kick Bus", "Mixbuses", "Ch 20", NeonRed, "-5.8 dBFS", "Mid Bus", "", "Mono center kick transient channel with clipper."),
            RoutingNode("bus_snare", "Snare Bus", "Mixbuses", "Ch 21", NeonOrange, "-5.8 dBFS", "Mid & Side", "", "Snare body sum with side clatter split."),
            RoutingNode("bus_perc", "Perc Bus", "Mixbuses", "Ch 22", NeonGreen, "-9.0 dBFS", "Side & L/R", "", "Top end groove glued with FabFilter Pro-C 2."),
            RoutingNode("bus_sub", "Sub Bus", "Mixbuses", "Ch 23", NeonCyan, "-6.8 dBFS", "Mid Bus Only", "", "100% Mono subwoofer power rail. Never touches Side Bus."),
            RoutingNode("bus_growl", "Growl Bus", "Mixbuses", "Ch 24", NeonPink, "-7.5 dBFS", "Mid & Side", "", "Riddim soundwall glued via Saturn 2 warm tube saturation."),
            RoutingNode("bus_music", "Synths & Vox", "Mixbuses", "Ch 25-27", Color(0xFF38BDF8), "-8.5 dBFS", "Mid & Side", "", "Leads, pads, and vocal chants."),

            // Stage 3: Spatial Stem Processing
            RoutingNode("sp_mid", "MID BUS", "Spatial", "Ch 30", NeonCyan, "-4.2 dBFS", "Pre-Master", "", "Sum L+R mono core. Kick, Sub, Snare body, and lead presence."),
            RoutingNode("sp_side", "SIDE BUS", "Spatial", "Ch 31", NeonGreen, "-8.5 dBFS", "Pre-Master", "", "Diff L-R. High-passed at 135Hz with 48dB/oct steep slope."),
            RoutingNode("sp_lr", "LEFT & RIGHT BUS", "Spatial", "Ch 32-33", NeonOrange, "-5.0 dBFS", "Pre-Master", "", "Discrete L/R channel balance & transient alignment."),

            // Stage 4: Pre-Master & Master
            RoutingNode("pm_pre", "PRE-MASTER", "PreMaster", "Ch 40", Color(0xFFA855F7), "-3.5 dBFS", "Master Track", "", "Multiband glue & crest factor control before master."),
            RoutingNode("m_final", "MAIN MASTER", "Master", "Master", NeonGreen, "-6.0 LUFS", "Audio Out", "", "Pro-Q 3 -> Saturn 2 -> Neutron Clipper -> Pro-C 2 -> Pro-L 2 (-0.1 TP).")
        )
    }

    var selectedNode by remember { mutableStateOf(nodes.first()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Cable,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INTERACTIVE ROUTING MATRIX",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "Tap node to inspect",
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal scrolling columns representing the 4 stages
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Column 1: Channels
            StageColumn(
                title = "1. CHANNELS",
                nodes = nodes.filter { it.stage == "Channels" },
                selectedNode = selectedNode,
                onSelect = {
                    selectedNode = it
                    onSelectNode(it)
                }
            )

            // Column 2: Mixbuses
            StageColumn(
                title = "2. 8 MIXBUSES",
                nodes = nodes.filter { it.stage == "Mixbuses" },
                selectedNode = selectedNode,
                onSelect = {
                    selectedNode = it
                    onSelectNode(it)
                }
            )

            // Column 3: Spatial M/S & L/R
            StageColumn(
                title = "3. SPATIAL STEMS",
                nodes = nodes.filter { it.stage == "Spatial" },
                selectedNode = selectedNode,
                onSelect = {
                    selectedNode = it
                    onSelectNode(it)
                }
            )

            // Column 4: Pre-Master & Master
            StageColumn(
                title = "4. PRE-MASTER / MASTER",
                nodes = nodes.filter { it.stage in listOf("PreMaster", "Master") },
                selectedNode = selectedNode,
                onSelect = {
                    selectedNode = it
                    onSelectNode(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Inspector Card for Selected Node
        AnimatedVisibility(visible = true) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("node_inspector_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(selectedNode.color))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
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
                                    .background(selectedNode.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedNode.name,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "[${selectedNode.flChannel}]",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = "Peak: ${selectedNode.peakDb}",
                            color = selectedNode.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = selectedNode.details,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "➔ Routes To: ${selectedNode.destNode}",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (selectedNode.sidechainTo.isNotEmpty()) {
                            Text(
                                text = "⚡ Sidechains: ${selectedNode.sidechainTo}",
                                color = NeonOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StageColumn(
    title: String,
    nodes: List<RoutingNode>,
    selectedNode: RoutingNode,
    onSelect: (RoutingNode) -> Unit
) {
    Column(
        modifier = Modifier.width(170.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        nodes.forEach { node ->
            val isSelected = node.id == selectedNode.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) DarkCardBorder else Color(0xFF161C2C))
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) node.color else Color(0xFF232B3E),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelect(node) }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .testTag("node_${node.id}"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = node.name,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = node.peakDb,
                        color = node.color,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = node.color,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
