package com.example.model

data class DialSetting(
    val id: String,
    val name: String,
    val currentValue: Float,
    val defaultValue: Float,
    val unit: String,
    val min: Float,
    val max: Float,
    val description: String,
    val formatDecimals: Int = 1
)

data class FxPluginSetting(
    val id: String,
    val name: String,
    val category: String,
    val slotNumber: Int,
    val dials: List<DialSetting>,
    val flTips: String,
    val sidechainNotes: String = ""
)

data class ChannelConfig(
    val id: String,
    val name: String,
    val category: String, // Drums, Bass, Synth, Vox
    val targetPeakDb: Float,
    val phaseTarget: Float,
    val phaseDesc: String,
    val mixbusDestination: String,
    val description: String,
    val flMixerChannel: Int,
    val plugins: List<FxPluginSetting>,
    val sidechainConfig: String
)

data class MixbusConfig(
    val id: String,
    val name: String,
    val inputChannels: List<String>,
    val destinationBus: String,
    val targetPeakDb: Float,
    val routingColorHex: String,
    val purpose: String,
    val flMixerChannel: Int,
    val fxChain: List<FxPluginSetting>,
    val sidechainRouting: String
)

data class SpatialBusConfig(
    val id: String,
    val name: String, // Mid Bus, Side Bus, Left Bus, Right Bus
    val matrixType: String,
    val flStereoShaperSettings: String,
    val purpose: String,
    val targetPeakDb: Float,
    val fabFilterChain: List<FxPluginSetting>,
    val phaseRule: String
)

data class MasterStageConfig(
    val step: Int,
    val title: String,
    val pluginName: String,
    val targetGoal: String,
    val dialSettings: List<DialSetting>,
    val operationalGuide: String
)

data class LoudnessMetric(
    val name: String,
    val targetValue: String,
    val explanation: String,
    val streamingStatus: String
)
