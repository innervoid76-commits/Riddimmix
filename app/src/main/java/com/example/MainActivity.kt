package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.RiddimMasterViewModel
import com.example.ui.StudioTab
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: RiddimMasterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current

                // BackHandler support
                BackHandler(enabled = uiState.currentTab != StudioTab.WALKTHROUGH) {
                    viewModel.setTab(StudioTab.WALKTHROUGH)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBg,
                    topBar = {
                        StudioTopAppBar(
                            simulatedLufs = uiState.simulatedLufs,
                            simulatedTruePeak = uiState.simulatedTruePeak,
                            onExportClick = { viewModel.setTab(StudioTab.EXPORT) }
                        )
                    },
                    bottomBar = {
                        StudioBottomNavigation(
                            currentTab = uiState.currentTab,
                            onSelectTab = { viewModel.setTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentTab) {
                            StudioTab.WALKTHROUGH -> WalkthroughScreen(
                                onNavigateTab = { viewModel.setTab(it) },
                                onExportClicked = {
                                    viewModel.exportHtmlGuide(context, openShareSheet = true)
                                    viewModel.setTab(StudioTab.EXPORT)
                                }
                            )
                            StudioTab.SIGNAL_FLOW -> SignalFlowScreen()
                            StudioTab.CHANNELS -> ChannelsScreen(
                                channels = viewModel.channels,
                                selectedChannelId = uiState.selectedChannelId,
                                selectedCategory = uiState.selectedCategory,
                                onSelectChannel = { viewModel.selectChannel(it) },
                                onSelectCategory = { viewModel.selectCategory(it) },
                                getDialValue = { id, defaultVal -> viewModel.getDialValue(id, defaultVal) },
                                onUpdateDial = { id, newVal -> viewModel.updateDial(id, newVal) }
                            )
                            StudioTab.MIXBUSES -> MixbusesScreen(
                                mixbuses = viewModel.mixbuses,
                                spatialBuses = viewModel.spatialBuses,
                                selectedMixbusId = uiState.selectedMixbusId,
                                selectedSpatialBusId = uiState.selectedSpatialBusId,
                                onSelectMixbus = { viewModel.selectMixbus(it) },
                                onSelectSpatialBus = { viewModel.selectSpatialBus(it) },
                                getDialValue = { id, defaultVal -> viewModel.getDialValue(id, defaultVal) },
                                onUpdateDial = { id, newVal -> viewModel.updateDial(id, newVal) }
                            )
                            StudioTab.MASTERING -> MasteringLabScreen(
                                masterStages = viewModel.masterStages,
                                simulatedLufs = uiState.simulatedLufs,
                                simulatedTruePeak = uiState.simulatedTruePeak,
                                simulatedCorrelation = uiState.simulatedCorrelation,
                                getDialValue = { id, defaultVal -> viewModel.getDialValue(id, defaultVal) },
                                onUpdateDial = { id, newVal -> viewModel.updateDial(id, newVal) },
                                onResetDials = { viewModel.resetDials() }
                            )
                            StudioTab.EXPORT -> ExportScreen(
                                isExporting = uiState.isExporting,
                                exportMessage = uiState.exportSuccessMessage,
                                onExportHtml = { ctx -> viewModel.exportHtmlGuide(ctx, openShareSheet = true) },
                                onCopyClipboard = { ctx -> viewModel.copyHtmlToClipboard(ctx) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioTopAppBar(
    simulatedLufs: Float,
    simulatedTruePeak: Float,
    onExportClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "RIDDIM",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MASTER",
                    color = NeonGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1E2638))
                        .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FL20 / 2025",
                        color = NeonGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        },
        actions = {
            // Live LUFS status badge
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "${String.format("%.1f", simulatedLufs)} LUFS",
                    color = if (simulatedLufs >= -6.3f && simulatedLufs <= -5.7f) NeonGreen else NeonOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${String.format("%.1f", simulatedTruePeak)} TP",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            IconButton(
                onClick = onExportClick,
                modifier = Modifier.testTag("top_bar_export_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Export Guide",
                    tint = NeonCyan
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBg,
            titleContentColor = TextPrimary
        ),
        modifier = Modifier.testTag("studio_top_app_bar")
    )
}

data class NavItem(
    val tab: StudioTab,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun StudioBottomNavigation(
    currentTab: StudioTab,
    onSelectTab: (StudioTab) -> Unit
) {
    val items = listOf(
        NavItem(StudioTab.WALKTHROUGH, "Guide", Icons.Default.MenuBook),
        NavItem(StudioTab.SIGNAL_FLOW, "Routing", Icons.Default.AccountTree),
        NavItem(StudioTab.CHANNELS, "11 Ch", Icons.Default.GraphicEq),
        NavItem(StudioTab.MIXBUSES, "Buses", Icons.Default.Layers),
        NavItem(StudioTab.MASTERING, "Master", Icons.Default.Speed),
        NavItem(StudioTab.EXPORT, "Export", Icons.Default.Download)
    )

    NavigationBar(
        containerColor = DarkSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars).testTag("bottom_nav_bar")
    ) {
        items.forEach { item ->
            val isSelected = currentTab == item.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(item.tab) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBg,
                    selectedTextColor = NeonGreen,
                    indicatorColor = NeonGreen,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_tab_${item.tab.name}")
            )
        }
    }
}
