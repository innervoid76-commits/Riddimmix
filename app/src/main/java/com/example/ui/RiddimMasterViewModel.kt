package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HtmlExportGenerator
import com.example.data.RiddimGuideRepository
import com.example.model.ChannelConfig
import com.example.model.MixbusConfig
import com.example.model.SpatialBusConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

enum class StudioTab(val title: String) {
    WALKTHROUGH("Walkthrough"),
    SIGNAL_FLOW("Signal Flow"),
    CHANNELS("11 Channels"),
    MIXBUSES("Mixbuses & M/S"),
    MASTERING("Master Lab (-6 LUFS)"),
    EXPORT("Export & APK")
}

data class UiState(
    val currentTab: StudioTab = StudioTab.WALKTHROUGH,
    val selectedChannelId: String = "kick",
    val selectedCategory: String = "All",
    val selectedMixbusId: String = "bus_kick",
    val selectedSpatialBusId: String = "mid_bus",
    val dialOverrides: Map<String, Float> = emptyMap(),
    val isExporting: Boolean = false,
    val exportSuccessMessage: String? = null,
    val exportedHtmlFilePath: String? = null,
    val simulatedLufs: Float = -6.0f,
    val simulatedTruePeak: Float = -0.1f,
    val simulatedCorrelation: Float = 0.88f,
    val selectedPhaseBusId: String = "master"
)

class RiddimMasterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val channels: List<ChannelConfig> = RiddimGuideRepository.channels
    val mixbuses: List<MixbusConfig> = RiddimGuideRepository.mixbuses
    val spatialBuses: List<SpatialBusConfig> = RiddimGuideRepository.spatialBuses
    val masterStages = RiddimGuideRepository.masterChainStages
    val metrics = RiddimGuideRepository.targetLoudnessMetrics

    fun setTab(tab: StudioTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun selectChannel(channelId: String) {
        _uiState.value = _uiState.value.copy(selectedChannelId = channelId)
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectMixbus(busId: String) {
        _uiState.value = _uiState.value.copy(selectedMixbusId = busId)
    }

    fun selectSpatialBus(busId: String) {
        _uiState.value = _uiState.value.copy(selectedSpatialBusId = busId)
    }

    fun selectPhaseBus(busId: String) {
        _uiState.value = _uiState.value.copy(selectedPhaseBusId = busId)
    }

    fun updateDial(dialId: String, newValue: Float) {
        val currentOverrides = _uiState.value.dialOverrides.toMutableMap()
        currentOverrides[dialId] = newValue

        // Update simulated dynamic impact
        val gainDrive = currentOverrides["ml_gain"] ?: 7.8f
        val clipDrive = currentOverrides["mclip_drive"] ?: 2.4f
        val calculatedLufs = (-14.0f + (gainDrive * 0.75f) + (clipDrive * 0.35f)).coerceIn(-12.0f, -4.5f)
        val ceiling = currentOverrides["ml_ceil"] ?: -0.1f

        _uiState.value = _uiState.value.copy(
            dialOverrides = currentOverrides,
            simulatedLufs = calculatedLufs,
            simulatedTruePeak = ceiling
        )
    }

    fun getDialValue(dialId: String, defaultValue: Float): Float {
        return _uiState.value.dialOverrides[dialId] ?: defaultValue
    }

    fun exportHtmlGuide(context: Context, openShareSheet: Boolean = true) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true, exportSuccessMessage = null)
            try {
                val htmlContent = HtmlExportGenerator.generateHtml()
                val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
                val exportFile = File(exportDir, "Riddim_Mixing_Mastering_FLStudio20_FabFilter2025.html")
                exportFile.writeText(htmlContent)

                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportSuccessMessage = "Successfully generated ${exportFile.name} (${htmlContent.length / 1024} KB)",
                    exportedHtmlFilePath = exportFile.absolutePath
                )

                if (openShareSheet) {
                    shareHtmlFile(context, exportFile)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportSuccessMessage = "Export failed: ${e.localizedMessage}"
                )
            }
        }
    }

    private fun shareHtmlFile(context: Context, file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/html"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Riddim Mixing & Mastering Guide (FL Studio 20)")
                putExtra(Intent.EXTRA_TEXT, "Comprehensive Riddim Mixing & Mastering Guide for FL Studio 20 with FabFilter 2025 VST perimeters and -6.0 LUFS target.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share or Save HTML Guide")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (_: Exception) {
            // Intent fallback handled gracefully
        }
    }

    fun copyHtmlToClipboard(context: Context) {
        val htmlContent = HtmlExportGenerator.generateHtml()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Riddim Guide HTML", htmlContent)
        clipboard.setPrimaryClip(clip)
        _uiState.value = _uiState.value.copy(
            exportSuccessMessage = "HTML markup copied to clipboard!"
        )
    }

    fun resetDials() {
        _uiState.value = _uiState.value.copy(
            dialOverrides = emptyMap(),
            simulatedLufs = -6.0f,
            simulatedTruePeak = -0.1f
        )
    }
}
