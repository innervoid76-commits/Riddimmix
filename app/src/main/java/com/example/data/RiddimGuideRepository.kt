package com.example.data

import com.example.model.ChannelConfig
import com.example.model.DialSetting
import com.example.model.FxPluginSetting
import com.example.model.LoudnessMetric
import com.example.model.MasterStageConfig
import com.example.model.MixbusConfig
import com.example.model.SpatialBusConfig

object RiddimGuideRepository {

    val targetLoudnessMetrics = listOf(
        LoudnessMetric(
            name = "Integrated Loudness",
            targetValue = "-6.0 LUFS",
            explanation = "Main drop target loudness. Delivers maximum club impact and heavy trench riddim density while maintaining transient definition.",
            streamingStatus = "Streaming normalizes this by -8 dB (Spotify) or -10 dB (Apple Music), but with True Peak controlled at -0.1 dBFS, no inter-sample distortion occurs."
        ),
        LoudnessMetric(
            name = "Short-Term Loudness",
            targetValue = "-5.0 to -5.5 LUFS",
            explanation = "Drop peak loudness measured over 3-second rolling window. Ensures bass chops and kick/snare punches hit with maximum perceived loudness.",
            streamingStatus = "Optimal for club sound systems & CDJ playback without clipping converters."
        ),
        LoudnessMetric(
            name = "True Peak Ceiling",
            targetValue = "-0.1 dBFS (TP)",
            explanation = "Strict brickwall oversampled ceiling in FabFilter Pro-L 2. Prevents DAC inter-sample clipping on MP3, AAC, and OGG streaming transcodes.",
            streamingStatus = "100% compliant with high-fidelity streaming DSP algorithms."
        ),
        LoudnessMetric(
            name = "Dynamic Range / Crest Factor",
            targetValue = "5.5 to 6.5 dB",
            explanation = "The difference between peak transient and RMS energy. Controlled by Neutron Clipper shaving 2.5 dB before Pro-L 2.",
            streamingStatus = "Tight riddim punch without flatline fatigue or squash."
        ),
        LoudnessMetric(
            name = "Low-End Phase Correlation",
            targetValue = "+0.95 to +1.00",
            explanation = "Phase correlation below 120Hz must remain rock-solid mono. Eliminates bass phase cancellation in club mono subwoofers.",
            streamingStatus = "Zero energy loss when played through dual 18\" or 21\" sub arrays."
        ),
        LoudnessMetric(
            name = "Mid/High Stereo Correlation",
            targetValue = "+0.65 to +0.85",
            explanation = "Wide stereo imaging for hi-hats, vocal chops, and growl stereo detuning while maintaining clear mono fold-down.",
            streamingStatus = "Wide spatial soundstage on headphones and mobile phones."
        )
    )

    val channels: List<ChannelConfig> = listOf(
        ChannelConfig(
            id = "kick",
            name = "Kick",
            category = "Drums",
            targetPeakDb = -6.0f,
            phaseTarget = 0.98f,
            phaseDesc = "+0.98 to +1.00 (Strict Center Mono)",
            mixbusDestination = "Kick Mixbus",
            description = "Tight, punchy riddim kick fundamental centered around 48-55 Hz with transient knock at 100 Hz and click at 3.2 kHz.",
            flMixerChannel = 1,
            sidechainConfig = "Sends sidechain trigger to Sub Bass (Mixer Ch 6) and Mid Bass (Ch 7). Routed via FL Studio sidechain send with volume at 0%.",
            plugins = listOf(
                FxPluginSetting(
                    id = "kick_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Linear phase low-cut at 32Hz, 48dB/oct slope. Dynamic notch at 180Hz to clear mud.",
                    dials = listOf(
                        DialSetting("k_q_hp", "Low Cut Freq", 32.0f, 32.0f, "Hz", 20f, 60f, "Cuts subsonic rumble beneath kick fundamental"),
                        DialSetting("k_q_fund", "Fund Boost (50Hz)", 1.5f, 1.5f, "dB", -6f, 6f, "Trowel weight around 50Hz for sub resonance"),
                        DialSetting("k_q_notch", "Boxy Cut (250Hz)", -3.2f, -3.2f, "dB", -12f, 0f, "Removes cardboard resonance"),
                        DialSetting("k_q_click", "Transient Click (3.2kHz)", 2.0f, 2.0f, "dB", -6f, 6f, "Pierces through dense riddim bass chops")
                    )
                ),
                FxPluginSetting(
                    id = "kick_clipper",
                    name = "iZotope Neutron Clipper",
                    category = "Dynamics / Saturation",
                    slotNumber = 2,
                    flTips = "Shaves 2.0 dB of raw peak transient. Converts spike into harmonic presence.",
                    dials = listOf(
                        DialSetting("k_clip_ceil", "Ceiling", -0.5f, -0.5f, "dBFS", -6f, 0f, "Maximum kick peak level output"),
                        DialSetting("k_clip_drive", "Drive", 2.2f, 2.2f, "dB", 0f, 6f, "Pushes transient into clipper threshold"),
                        DialSetting("k_clip_knee", "Knee Softness", 25.0f, 25.0f, "%", 0f, 100f, "Soft clip curve for punchy warmth")
                    )
                ),
                FxPluginSetting(
                    id = "kick_c2",
                    name = "FabFilter Pro-C 2",
                    category = "Compression",
                    slotNumber = 3,
                    flTips = "Punch mode. Slow attack (25ms) allows initial transient click through, fast release (65ms).",
                    dials = listOf(
                        DialSetting("k_c_thresh", "Threshold", -14.0f, -14.0f, "dB", -30f, 0f, "Engages on kick body"),
                        DialSetting("k_c_ratio", "Ratio", 3.5f, 3.5f, ":1", 1f, 10f, "Moderate punch ratio"),
                        DialSetting("k_c_att", "Attack", 25.0f, 25.0f, "ms", 0.1f, 100f, "Preserves initial 100Hz transient click"),
                        DialSetting("k_c_rel", "Release", 65.0f, 65.0f, "ms", 10f, 300f, "Releases before next quarter note at 140 BPM")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "snare",
            name = "Snare (Clap/Snap)",
            category = "Drums",
            targetPeakDb = -6.0f,
            phaseTarget = 0.95f,
            phaseDesc = "+0.92 to +0.96 (Mono Core with Stereo Clatter)",
            mixbusDestination = "Snare Mixbus",
            description = "Heavy riddim 200 Hz body snap with metallic sizzle around 3-6 kHz. Must cut through walls of bass.",
            flMixerChannel = 2,
            sidechainConfig = "Sends sidechain trigger to Mid Bass, Growl, and Lead buses to carve space on beat 3.",
            plugins = listOf(
                FxPluginSetting(
                    id = "snare_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "High-pass at 120Hz. Sharp bell boost at 210Hz for chest thud, notch 400Hz mud.",
                    dials = listOf(
                        DialSetting("s_q_hp", "Low Cut", 120.0f, 120.0f, "Hz", 50f, 200f, "Leaves low end totally clear for sub"),
                        DialSetting("s_q_body", "Body Boost (210Hz)", 2.4f, 2.4f, "dB", -6f, 6f, "The heavy riddim wood thud"),
                        DialSetting("s_q_ring", "Ring Cut (750Hz)", -4.0f, -4.0f, "dB", -12f, 0f, "Dynamic notch for ping ringing"),
                        DialSetting("s_q_snap", "Crack (4.5kHz)", 3.0f, 3.0f, "dB", -6f, 6f, "Gives crack and presence in master")
                    )
                ),
                FxPluginSetting(
                    id = "snare_sat2",
                    name = "FabFilter Saturn 2",
                    category = "Saturation",
                    slotNumber = 2,
                    flTips = "Warm Tube style on upper band (above 1.5 kHz). Adds harmonic sizzle.",
                    dials = listOf(
                        DialSetting("s_sat_split", "Crossover Freq", 1500.0f, 1500.0f, "Hz", 500f, 5000f, "Band split for top end only"),
                        DialSetting("s_sat_drive", "Drive (High Band)", 1.8f, 1.8f, "dB", 0f, 10f, "Generates even order harmonics"),
                        DialSetting("s_sat_mix", "Mix", 45.0f, 45.0f, "%", 0f, 100f, "Blends dry body with saturated sizzle")
                    )
                ),
                FxPluginSetting(
                    id = "snare_clip",
                    name = "iZotope Neutron Clipper",
                    category = "Dynamics / Saturation",
                    slotNumber = 3,
                    flTips = "Hard clips top 2.5 dB of snare transient so limiter doesn't choke.",
                    dials = listOf(
                        DialSetting("s_clip_ceil", "Ceiling", -0.3f, -0.3f, "dBFS", -3f, 0f, "Transient peak wall"),
                        DialSetting("s_clip_drive", "Drive", 2.8f, 2.8f, "dB", 0f, 6f, "Square-clips the snare rim crack"),
                        DialSetting("s_clip_knee", "Knee Softness", 10.0f, 10.0f, "%", 0f, 50f, "Near hard-clip for maximum crack")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "hihat",
            name = "Hi-Hat (Closed / Open)",
            category = "Drums",
            targetPeakDb = -14.0f,
            phaseTarget = 0.80f,
            phaseDesc = "+0.75 to +0.85 (Stereo Spread)",
            mixbusDestination = "Percussion Mixbus",
            description = "Crisp rhythmic 16th or triplet groove. High-passed strictly above 500 Hz to prevent phase clash.",
            flMixerChannel = 3,
            sidechainConfig = "None. High gain headroom allows hats to breathe in high frequencies.",
            plugins = listOf(
                FxPluginSetting(
                    id = "hh_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Steep 24dB/oct low cut at 650Hz. Gentle high shelf at 11kHz for air.",
                    dials = listOf(
                        DialSetting("hh_q_hp", "Low Cut", 650.0f, 650.0f, "Hz", 300f, 1200f, "Deletes mid bleed"),
                        DialSetting("hh_q_harsh", "Harsh Dip (3.8kHz)", -2.0f, -2.0f, "dB", -8f, 0f, "Softens metallic ear fatigue"),
                        DialSetting("hh_q_air", "Air Shelf (11kHz)", 1.5f, 1.5f, "dB", -4f, 6f, "Silky top sparkle")
                    )
                ),
                FxPluginSetting(
                    id = "hh_c2",
                    name = "FabFilter Pro-C 2",
                    category = "Compression",
                    slotNumber = 2,
                    flTips = "Opto mode for smooth leveling without squashing natural swing velocities.",
                    dials = listOf(
                        DialSetting("hh_c_thresh", "Threshold", -18.0f, -18.0f, "dB", -30f, 0f, "Catches velocity peaks"),
                        DialSetting("hh_c_ratio", "Ratio", 2.2f, 2.2f, ":1", 1f, 6f, "Gentle leveling"),
                        DialSetting("hh_c_rel", "Release", 45.0f, 45.0f, "ms", 10f, 150f, "Instant reset for 16th hats")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "cymbal",
            name = "Cymbal (Crash / Ride)",
            category = "Drums",
            targetPeakDb = -12.0f,
            phaseTarget = 0.70f,
            phaseDesc = "+0.65 to +0.75 (Wide Ambience)",
            mixbusDestination = "Percussion Mixbus",
            description = "Impact crashes, downshifters, and ping rides. Carefully controlled dynamic resonance.",
            flMixerChannel = 4,
            sidechainConfig = "Sidechained to Kick (ducking 3 dB upon kick impact for clean downbeat).",
            plugins = listOf(
                FxPluginSetting(
                    id = "cym_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "High cut slope 18kHz to prevent digital aliasing harshness on oversampling.",
                    dials = listOf(
                        DialSetting("cy_q_hp", "Low Cut", 450.0f, 450.0f, "Hz", 200f, 800f, "Removes low crash resonance"),
                        DialSetting("cy_q_gong", "Gong Notch (850Hz)", -4.5f, -4.5f, "dB", -12f, 0f, "Cuts cheap tin-can resonance"),
                        DialSetting("cy_q_lp", "High Cut", 18500.0f, 18500.0f, "Hz", 15000f, 22000f, "Ceiling guard")
                    )
                ),
                FxPluginSetting(
                    id = "cym_time3",
                    name = "FabFilter Timeless 3",
                    category = "Spatial / Delay",
                    slotNumber = 2,
                    flTips = "Stereo ping-pong tape delay at 1/8D with low-cut filter enabled at 500Hz.",
                    dials = listOf(
                        DialSetting("cy_t_feed", "Feedback", 22.0f, 22.0f, "%", 0f, 80f, "Short atmospheric tail"),
                        DialSetting("cy_t_mix", "Dry/Wet Mix", 15.0f, 15.0f, "%", 0f, 100f, "Subtle spatial tail")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "percussion",
            name = "Percussion (Fills / Wood / Rim)",
            category = "Drums",
            targetPeakDb = -10.0f,
            phaseTarget = 0.85f,
            phaseDesc = "+0.80 to +0.90 (Semi-Stereo / Panned)",
            mixbusDestination = "Percussion Mixbus",
            description = "Ear candy polyrhythms, bongos, wood clicks, and metallic clanks that define riddim groove.",
            flMixerChannel = 5,
            sidechainConfig = "Grouped with hi-hats into Percussion Mixbus.",
            plugins = listOf(
                FxPluginSetting(
                    id = "perc_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Dynamic EQ enabled on 3kHz presence band to duck when lead synth plays.",
                    dials = listOf(
                        DialSetting("p_q_hp", "Low Cut", 350.0f, 350.0f, "Hz", 100f, 600f, "Clean floor"),
                        DialSetting("p_q_mid", "Wood Click (1.4kHz)", 2.0f, 2.0f, "dB", -6f, 6f, "Accentuates percussion transient"),
                        DialSetting("p_q_dyn", "Dynamic Range", -3.0f, -3.0f, "dB", -10f, 0f, "Tames sudden resonant peaks")
                    )
                ),
                FxPluginSetting(
                    id = "perc_clip",
                    name = "iZotope Neutron Clipper",
                    category = "Dynamics / Saturation",
                    slotNumber = 2,
                    flTips = "Controls sharp woodblock spikes to maintain consistent percussion mix level.",
                    dials = listOf(
                        DialSetting("p_clip_ceil", "Ceiling", -1.0f, -1.0f, "dBFS", -6f, 0f, "Tames sudden velocity spikes"),
                        DialSetting("p_clip_drive", "Drive", 1.5f, 1.5f, "dB", 0f, 5f, "Gives punchy density")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "sub_bass",
            name = "Sub Bass (Pure 30-80 Hz)",
            category = "Bass",
            targetPeakDb = -7.0f,
            phaseTarget = 1.00f,
            phaseDesc = "+1.00 (Mandatory 100% Mono Sum)",
            mixbusDestination = "Sub Mixbus",
            description = "The foundation of riddim. Pure mono sine or slightly filtered triangle wave between 32 Hz and 80 Hz.",
            flMixerChannel = 6,
            sidechainConfig = "CRITICAL SIDECHAIN: Triggered by Kick (Ch 1) via FabFilter Pro-MB or Pro-C 2 sidechain input. Cuts 100% volume for 0.08s.",
            plugins = listOf(
                FxPluginSetting(
                    id = "sub_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Brickwall Low Cut 28Hz (prevents DC offset). Brickwall High Cut at 88Hz (zero bleed into mid-bass). Set to MONO processing.",
                    dials = listOf(
                        DialSetting("sb_q_hp", "Sub Cut Freq", 28.0f, 28.0f, "Hz", 20f, 40f, "Prevents cone over-excursion"),
                        DialSetting("sb_q_hpslope", "Low Cut Slope", 48.0f, 48.0f, "dB/oct", 24f, 96f, "Ultra steep brickwall filter"),
                        DialSetting("sb_q_lp", "High Cut Freq", 85.0f, 85.0f, "Hz", 65f, 110f, "Hard low-pass to isolate pure sub"),
                        DialSetting("sb_q_fund", "Sub Fund (44Hz)", 1.0f, 1.0f, "dB", -4f, 4f, "Key of F / F# / G riddim tuning")
                    )
                ),
                FxPluginSetting(
                    id = "sub_mb_sc",
                    name = "FabFilter Pro-MB (Sidechain)",
                    category = "Dynamic Multiband / Sidechain",
                    slotNumber = 2,
                    flTips = "Sidechain input routed from Kick Ch 1. Single low band 30-90Hz ducking by -18 dB with 0.1ms attack, 110ms release.",
                    sidechainNotes = "In FL Studio VST wrapper: Settings -> Processing -> Connections -> Map Sidechain Input 1 to Kick Mixer Channel.",
                    dials = listOf(
                        DialSetting("sb_sc_range", "Duck Range", -18.0f, -18.0f, "dB", -30f, 0f, "Complete ducking during kick attack"),
                        DialSetting("sb_sc_att", "Attack", 0.1f, 0.1f, "ms", 0.05f, 5f, "Instantaneous kick duck"),
                        DialSetting("sb_sc_rel", "Release", 115.0f, 115.0f, "ms", 40f, 300f, "Timed to 1/8 note pump at 140 BPM"),
                        DialSetting("sb_sc_knee", "Knee", 4.0f, 4.0f, "dB", 0f, 15f, "Firm clamping curve")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "mid_bass",
            name = "Mid Bass (Square / Saw Chop)",
            category = "Bass",
            targetPeakDb = -9.0f,
            phaseTarget = 0.85f,
            phaseDesc = "+0.80 to +0.90 (Mono Center with Stereo Flange)",
            mixbusDestination = "Growl/Bass Mixbus",
            description = "The riddim 'chop' riff. Square or saw wave modulated with Serum/Vital wavetables between 100 Hz and 1.2 kHz.",
            flMixerChannel = 7,
            sidechainConfig = "Sidechained to Kick (-12 dB duck) and Snare (-15 dB duck) to preserve transient punch.",
            plugins = listOf(
                FxPluginSetting(
                    id = "mb_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Low cut at 90Hz (48dB/oct) to prevent sub clash. Bell boost at 450Hz for riddim 'honk' tone.",
                    dials = listOf(
                        DialSetting("mb_q_hp", "Low Cut", 92.0f, 92.0f, "Hz", 70f, 150f, "Mandatory clearance for sub bass"),
                        DialSetting("mb_q_honk", "Honk Peak (480Hz)", 2.5f, 2.5f, "dB", -6f, 6f, "Iconic riddim vowel grit"),
                        DialSetting("mb_q_notch", "Nasal Notch (1.1kHz)", -3.0f, -3.0f, "dB", -8f, 0f, "Removes harsh piercing resonance")
                    )
                ),
                FxPluginSetting(
                    id = "mb_sat2",
                    name = "FabFilter Saturn 2",
                    category = "Saturation / Drive",
                    slotNumber = 2,
                    flTips = "Heavy Saturation style across 150Hz - 2.5kHz. Adds analog warmth and grit.",
                    dials = listOf(
                        DialSetting("mb_sat_drive", "Drive", 3.5f, 3.5f, "dB", 0f, 12f, "Aggressive saturation crunch"),
                        DialSetting("mb_sat_tone", "Tone Control", 1.2f, 1.2f, "dB", -6f, 6f, "Brightens upper harmonics"),
                        DialSetting("mb_sat_mix", "Dry/Wet Mix", 70.0f, 70.0f, "%", 0f, 100f, "Parallel saturation blend")
                    )
                ),
                FxPluginSetting(
                    id = "mb_clip",
                    name = "iZotope Neutron Clipper",
                    category = "Dynamics / Saturation",
                    slotNumber = 3,
                    flTips = "Hard clips mid-bass envelope peaks before reaching the mixbus. Prevents unpredictable bass surges.",
                    dials = listOf(
                        DialSetting("mb_clip_ceil", "Ceiling", -0.8f, -0.8f, "dBFS", -6f, 0f, "Bass headroom boundary"),
                        DialSetting("mb_clip_drive", "Drive", 2.0f, 2.0f, "dB", 0f, 6f, "Flattening dynamic envelope")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "growl",
            name = "Growl / Screech Bass",
            category = "Bass",
            targetPeakDb = -8.0f,
            phaseTarget = 0.75f,
            phaseDesc = "+0.70 to +0.82 (Stereo Chorus & Haas Chops)",
            mixbusDestination = "Growl/Bass Mixbus",
            description = "Comb filtered, formanted metallic riddim screeches and guttural monster growls.",
            flMixerChannel = 8,
            sidechainConfig = "Sidechained to Kick and Snare. Routed to Mid/Side processing matrix for extreme width.",
            plugins = listOf(
                FxPluginSetting(
                    id = "gr_volc3",
                    name = "FabFilter Volcano 3",
                    category = "Filtering / Resonance",
                    slotNumber = 1,
                    flTips = "Dual bandpass formant filters modulating in opposite stereo directions.",
                    dials = listOf(
                        DialSetting("gr_v_f1", "Formant Peak 1", 720.0f, 720.0f, "Hz", 300f, 2000f, "Vowel mouth formant"),
                        DialSetting("gr_v_f2", "Formant Peak 2", 2400.0f, 2400.0f, "Hz", 1000f, 5000f, "Screech metallic ear"),
                        DialSetting("gr_v_peak", "Filter Peak Q", 4.2f, 4.2f, "Q", 1f, 10f, "High resonance scream")
                    )
                ),
                FxPluginSetting(
                    id = "gr_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 2,
                    flTips = "Mid/Side mode: Mid cuts 100Hz, Side band high-passed at 200Hz to keep sub mono.",
                    dials = listOf(
                        DialSetting("gr_q_hp", "Low Cut (Mid/Side)", 110.0f, 110.0f, "Hz", 80f, 160f, "No sub clash"),
                        DialSetting("gr_q_ear", "Dynamic Cut (3.2kHz)", -3.8f, -3.8f, "dB", -10f, 0f, "Removes ice-pick resonance"),
                        DialSetting("gr_q_air", "Side High Shelf", 2.2f, 2.2f, "dB", -4f, 6f, "Widens screech in side bus")
                    )
                ),
                FxPluginSetting(
                    id = "gr_clip",
                    name = "iZotope Neutron Clipper",
                    category = "Dynamics / Saturation",
                    slotNumber = 3,
                    flTips = "Clipper prevents filter resonance spikes from stealing master limiter headroom.",
                    dials = listOf(
                        DialSetting("gr_clip_ceil", "Ceiling", -0.5f, -0.5f, "dBFS", -4f, 0f, "Hard brickwall ceiling"),
                        DialSetting("gr_clip_drive", "Drive", 2.5f, 2.5f, "dB", 0f, 6f, "Saturates resonant spikes into harmonics")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "lead",
            name = "Lead / Laser Synth",
            category = "Synths",
            targetPeakDb = -10.0f,
            phaseTarget = 0.80f,
            phaseDesc = "+0.75 to +0.85 (Stereo Spread)",
            mixbusDestination = "Lead Mixbus",
            description = "High pitched FM chirps, laser blips, and call-and-response melodies alternating with the bass chop.",
            flMixerChannel = 9,
            sidechainConfig = "Sidechained to Kick and Snare.",
            plugins = listOf(
                FxPluginSetting(
                    id = "ld_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "High-pass at 250Hz. Bell dip at 1.8kHz to sit behind snare crack.",
                    dials = listOf(
                        DialSetting("ld_q_hp", "Low Cut", 250.0f, 250.0f, "Hz", 150f, 400f, "Removes low mud"),
                        DialSetting("ld_q_pierce", "Laser Focus (4.2kHz)", 2.0f, 2.0f, "dB", -6f, 6f, "Cuts through thick drop walls"),
                        DialSetting("ld_q_lp", "High Cut", 17500.0f, 17500.0f, "Hz", 14000f, 20000f, "Protects master limiter")
                    )
                ),
                FxPluginSetting(
                    id = "ld_time3",
                    name = "FabFilter Timeless 3",
                    category = "Spatial / Delay",
                    slotNumber = 2,
                    flTips = "Dotted 1/8 note delay with pitch flutter for authentic dub atmosphere.",
                    dials = listOf(
                        DialSetting("ld_t_time", "Delay Time", 185.0f, 185.0f, "ms", 50f, 500f, "Tempo synced delay"),
                        DialSetting("ld_t_feed", "Feedback", 30.0f, 30.0f, "%", 0f, 80f, "Echo repeats"),
                        DialSetting("ld_t_mix", "Dry/Wet Mix", 20.0f, 20.0f, "%", 0f, 100f, "Subtle space")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "pad",
            name = "Atmospheric Pad / Drones",
            category = "Synths",
            targetPeakDb = -16.0f,
            phaseTarget = 0.65f,
            phaseDesc = "+0.55 to +0.72 (Ultra Wide Stereo Bed)",
            mixbusDestination = "Pad Mixbus",
            description = "Eerie minor chords, vinyl static, tension drones in intro and breakdown.",
            flMixerChannel = 10,
            sidechainConfig = "Heavy sidechain pump during drops (-18 dB) to keep drops focused.",
            plugins = listOf(
                FxPluginSetting(
                    id = "pad_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "Cut sub 200Hz. Wide mid scoop between 400Hz and 1.5kHz to keep center stage open.",
                    dials = listOf(
                        DialSetting("pd_q_hp", "Low Cut", 220.0f, 220.0f, "Hz", 150f, 350f, "Cleans room for bass"),
                        DialSetting("pd_q_scoop", "Center Scoop (800Hz)", -4.5f, -4.5f, "dB", -12f, 0f, "Leaves pocket for vocals/leads"),
                        DialSetting("pd_q_shelf", "Air Shimmer (10kHz)", 2.0f, 2.0f, "dB", -4f, 6f, "Silky tension")
                    )
                ),
                FxPluginSetting(
                    id = "pad_c2",
                    name = "FabFilter Pro-C 2",
                    category = "Compression",
                    slotNumber = 2,
                    flTips = "Sidechained to Kick and Snare for dramatic atmospheric ducking.",
                    dials = listOf(
                        DialSetting("pd_c_thresh", "Threshold", -20.0f, -20.0f, "dB", -35f, 0f, "Engages on every drum hit"),
                        DialSetting("pd_c_ratio", "Ratio", 5.0f, 5.0f, ":1", 1f, 10f, "Deep ducking pump"),
                        DialSetting("pd_c_rel", "Release", 220.0f, 220.0f, "ms", 50f, 500f, "Breathes back in slowly")
                    )
                )
            )
        ),
        ChannelConfig(
            id = "vocal",
            name = "Vocal Chants / Pre-Drop Vox",
            category = "Vocals",
            targetPeakDb = -9.0f,
            phaseTarget = 0.90f,
            phaseDesc = "+0.85 to +0.95 (Front-and-Center Mono)",
            mixbusDestination = "Vocal Mixbus",
            description = "Pitched Jamaican ragga toasting, hip-hop vocal stabs, and iconic pre-drop phrases.",
            flMixerChannel = 11,
            sidechainConfig = "Sends sidechain trigger to Pad and Lead buses to duck background music when vox speaks.",
            plugins = listOf(
                FxPluginSetting(
                    id = "vox_q3",
                    name = "FabFilter Pro-Q 3",
                    category = "Equalization",
                    slotNumber = 1,
                    flTips = "High-pass 130Hz. Narrow dynamic notches at 3.1kHz and 6.8kHz for sibilance/de-essing.",
                    dials = listOf(
                        DialSetting("vx_q_hp", "Low Cut", 135.0f, 135.0f, "Hz", 80f, 200f, "Removes mic rumble"),
                        DialSetting("vx_q_box", "Chest Mud (320Hz)", -3.0f, -3.0f, "dB", -8f, 0f, "Clears vocal clutter"),
                        DialSetting("vx_q_pres", "Presence Boost (2.8kHz)", 2.5f, 2.5f, "dB", -4f, 6f, "In-your-face vocal punch"),
                        DialSetting("vx_q_deess", "Dynamic De-ess (6.5kHz)", -4.0f, -4.0f, "dB", -10f, 0f, "Automatic harshness control")
                    )
                ),
                FxPluginSetting(
                    id = "vox_c2",
                    name = "FabFilter Pro-C 2",
                    category = "Compression",
                    slotNumber = 2,
                    flTips = "Vocal style compressor. Fast 1ms attack, 4:1 ratio, 4 dB gain reduction.",
                    dials = listOf(
                        DialSetting("vx_c_thresh", "Threshold", -16.0f, -16.0f, "dB", -30f, 0f, "Locks vocal dynamics"),
                        DialSetting("vx_c_ratio", "Ratio", 4.0f, 4.0f, ":1", 1f, 8f, "Tight vocal containment"),
                        DialSetting("vx_c_att", "Attack", 1.5f, 1.5f, "ms", 0.5f, 20f, "Catches consonant spikes"),
                        DialSetting("vx_c_rel", "Release", 85.0f, 85.0f, "ms", 20f, 250f, "Smooth speech cadence")
                    )
                ),
                FxPluginSetting(
                    id = "vox_sat2",
                    name = "FabFilter Saturn 2",
                    category = "Saturation",
                    slotNumber = 3,
                    flTips = "Warm Tube drive adds odd and even harmonics, cutting through dense 200Hz-2kHz bass chops.",
                    dials = listOf(
                        DialSetting("vx_sat_drive", "Drive", 2.2f, 2.2f, "dB", 0f, 8f, "Adds tube crunch and grit"),
                        DialSetting("vx_sat_mix", "Dry/Wet Mix", 50.0f, 50.0f, "%", 0f, 100f, "Natural harmonic blend")
                    )
                )
            )
        )
    )

    val mixbuses: List<MixbusConfig> = listOf(
        MixbusConfig(
            id = "bus_kick",
            name = "Kick Mixbus",
            inputChannels = listOf("Kick"),
            destinationBus = "Mid Bus",
            targetPeakDb = -5.8f,
            routingColorHex = "#EF4444",
            purpose = "Isolates kick transient and anchors the mono center channel.",
            flMixerChannel = 20,
            sidechainRouting = "Sends unmuted sidechain cables to Sub Bus (Ch 23) and Growl Bus (Ch 24) with volume knob at 0.",
            fxChain = listOf(
                FxPluginSetting("bk_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bk_hp", "Low Cut", 30f, 30f, "Hz", 20f, 50f, "Linear phase clean"),
                    DialSetting("bk_mono", "Stereo Placement", 100f, 100f, "% Mono", 0f, 100f, "Forced pure mono")
                ), "Ensures kick is 100% dead center mono"),
                FxPluginSetting("bk_clip", "iZotope Neutron Clipper", "Dynamics", 2, listOf(
                    DialSetting("bk_ceil", "Ceiling", -0.5f, -0.5f, "dBFS", -3f, 0f, "Prevents clipping in bus sum"),
                    DialSetting("bk_drive", "Drive", 1.2f, 1.2f, "dB", 0f, 4f, "Final transient tame")
                ), "Clamps bus peaks")
            )
        ),
        MixbusConfig(
            id = "bus_snare",
            name = "Snare Mixbus",
            inputChannels = listOf("Snare"),
            destinationBus = "Mid Bus & Left/Right Buses",
            targetPeakDb = -5.8f,
            routingColorHex = "#F59E0B",
            purpose = "Punches the backbeat and distributes snare body to Mid and clatter to Sides.",
            flMixerChannel = 21,
            sidechainRouting = "Sidechains to Mid Bass, Growls, and Pad buses for beat 3 ducking.",
            fxChain = listOf(
                FxPluginSetting("bs_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bs_snap", "Crack Boost (3.5kHz)", 1.2f, 1.2f, "dB", -3f, 4f, "Top crack"),
                    DialSetting("bs_body", "Body 200Hz", 1.0f, 1.0f, "dB", -3f, 3f, "Solid core")
                ), "Mid/Side split in Pro-Q 3 keeps body mono and sizzle wide"),
                FxPluginSetting("bs_clip", "iZotope Neutron Clipper", "Dynamics", 2, listOf(
                    DialSetting("bs_ceil", "Ceiling", -0.3f, -0.3f, "dBFS", -2f, 0f, "Limits peak"),
                    DialSetting("bs_drive", "Drive", 1.8f, 1.8f, "dB", 0f, 5f, "Crisp rim transient")
                ), "Protects pre-master from snare overshoots")
            )
        ),
        MixbusConfig(
            id = "bus_perc",
            name = "Percussion Mixbus",
            inputChannels = listOf("HiHat", "Cymbal", "Percussion"),
            destinationBus = "Side Bus & Left/Right Buses",
            targetPeakDb = -9.0f,
            routingColorHex = "#10B981",
            purpose = "Glues tops, pans, and atmospheric percussion into a coherent groove.",
            flMixerChannel = 22,
            sidechainRouting = "Ducked by Kick Bus via Pro-MB on downbeats by -2.5 dB.",
            fxChain = listOf(
                FxPluginSetting("bp_c2", "FabFilter Pro-C 2", "Compression", 1, listOf(
                    DialSetting("bp_c_ratio", "Ratio", 2.0f, 2.0f, ":1", 1f, 4f, "Gentle bus glue"),
                    DialSetting("bp_c_att", "Attack", 30f, 30f, "ms", 10f, 60f, "Lets hat ticks pass"),
                    DialSetting("bp_c_rel", "Release", 80f, 80f, "ms", 30f, 200f, "Groove tempo sync")
                ), "Glues percussion groove together"),
                FxPluginSetting("bp_q3", "FabFilter Pro-Q 3", "EQ", 2, listOf(
                    DialSetting("bp_hp", "Low Cut", 350f, 350f, "Hz", 200f, 500f, "Keeps bottoms sterile")
                ), "Guarantees zero low-frequency clutter enters spatial buses")
            )
        ),
        MixbusConfig(
            id = "bus_sub",
            name = "Sub Mixbus",
            inputChannels = listOf("Sub Bass"),
            destinationBus = "Mid Bus",
            targetPeakDb = -6.8f,
            routingColorHex = "#3B82F6",
            purpose = "Mono subwoofer power bus. Strictly isolated from all stereo wideners.",
            flMixerChannel = 23,
            sidechainRouting = "Driven by Kick sidechain. Output routes ONLY to Mid Bus (never Side Bus).",
            fxChain = listOf(
                FxPluginSetting("bsub_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bsub_hp", "Low Cut", 28f, 28f, "Hz", 20f, 40f, "48dB/oct steep"),
                    DialSetting("bsub_lp", "High Cut", 90f, 90f, "Hz", 75f, 110f, "Strict cutoff")
                ), "Ensures pure sub energy without upper distortion"),
                FxPluginSetting("bsub_c2", "FabFilter Pro-C 2", "Dynamics", 2, listOf(
                    DialSetting("bsub_mode", "Style", 1.0f, 1.0f, "Clean", 1f, 1f, "Transparent mode"),
                    DialSetting("bsub_thresh", "Threshold", -8f, -8f, "dB", -15f, 0f, "Leveler")
                ), "Maintains unwavering RMS level on all bass notes")
            )
        ),
        MixbusConfig(
            id = "bus_growl",
            name = "Growl / Mid-Bass Bus",
            inputChannels = listOf("Mid Bass", "Growl"),
            destinationBus = "Mid Bus & Side Bus",
            targetPeakDb = -7.5f,
            routingColorHex = "#8B5CF6",
            purpose = "The main drop attraction. Blends mid tear bite in Mid Bus and stereo width in Side Bus.",
            flMixerChannel = 24,
            sidechainRouting = "Receives sidechain from Kick and Snare. Routed 70% to Mid Bus, 30% to Side Bus.",
            fxChain = listOf(
                FxPluginSetting("bg_sat2", "FabFilter Saturn 2", "Saturation", 1, listOf(
                    DialSetting("bg_sat_tube", "Warm Tube Drive", 1.8f, 1.8f, "dB", 0f, 6f, "Harmonic binder"),
                    DialSetting("bg_sat_dynamics", "Dynamics", 1.0f, 1.0f, "dB", -2f, 4f, "Enhances transient bite")
                ), "Binds chopped bass patches together into a uniform soundwall"),
                FxPluginSetting("bg_clip", "iZotope Neutron Clipper", "Dynamics", 2, listOf(
                    DialSetting("bg_ceil", "Ceiling", -0.6f, -0.6f, "dBFS", -3f, 0f, "Bus peak limit"),
                    DialSetting("bg_drive", "Drive", 2.2f, 2.2f, "dB", 0f, 5f, "Riddim crunch")
                ), "Controls dynamic peaks so drop volume stays consistent")
            )
        ),
        MixbusConfig(
            id = "bus_lead",
            name = "Lead Mixbus",
            inputChannels = listOf("Lead"),
            destinationBus = "Mid Bus & Side Bus",
            targetPeakDb = -9.5f,
            routingColorHex = "#06B6D4",
            purpose = "Melodic hook and laser stab bus. Dynamic space carving.",
            flMixerChannel = 25,
            sidechainRouting = "Ducked by Kick, Snare, and Vocal channels.",
            fxChain = listOf(
                FxPluginSetting("bl_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bl_hp", "Low Cut", 300f, 300f, "Hz", 200f, 500f, "Clear space"),
                    DialSetting("bl_mid", "Presence (3kHz)", 1.5f, 1.5f, "dB", -3f, 5f, "Lead clarity")
                ), "Removes conflicting mid frequencies"),
                FxPluginSetting("bl_c2", "FabFilter Pro-C 2", "Compression", 2, listOf(
                    DialSetting("bl_ratio", "Ratio", 3.0f, 3.0f, ":1", 1f, 6f, "Smooth control")
                ), "Leveling spikes")
            )
        ),
        MixbusConfig(
            id = "bus_pad",
            name = "Pad Mixbus",
            inputChannels = listOf("Pad"),
            destinationBus = "Side Bus",
            targetPeakDb = -15.0f,
            routingColorHex = "#64748B",
            purpose = "Surrounds the listener on the sides without interfering with the mono center.",
            flMixerChannel = 26,
            sidechainRouting = "Heavily ducked (-18 dB) during drop sections.",
            fxChain = listOf(
                FxPluginSetting("bpad_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bpad_hp", "Low Cut", 250f, 250f, "Hz", 150f, 400f, "Total low cut"),
                    DialSetting("bpad_width", "Side Boost", 1.8f, 1.8f, "dB", 0f, 4f, "Pushes ambience to sides")
                ), "Mid/Side processing widens pad stereo image")
            )
        ),
        MixbusConfig(
            id = "bus_vox",
            name = "Vocal Mixbus",
            inputChannels = listOf("Vocal"),
            destinationBus = "Mid Bus & Side Bus",
            targetPeakDb = -8.5f,
            routingColorHex = "#EC4899",
            purpose = "Center mono vocal energy with stereo delay/reverb throws.",
            flMixerChannel = 27,
            sidechainRouting = "Carves space across all synths and pads whenever active.",
            fxChain = listOf(
                FxPluginSetting("bvx_q3", "FabFilter Pro-Q 3", "EQ", 1, listOf(
                    DialSetting("bvx_fund", "Clarity 2.5kHz", 2.0f, 2.0f, "dB", -3f, 5f, "Cuts through drop"),
                    DialSetting("bvx_air", "High Air 12kHz", 1.5f, 1.5f, "dB", -2f, 4f, "Polished shimmer")
                ), "Polishes dialogue / hype vocal chops"),
                FxPluginSetting("bvx_c2", "FabFilter Pro-C 2", "Compression", 2, listOf(
                    DialSetting("bvx_att", "Attack", 2.0f, 2.0f, "ms", 0.5f, 10f, "Fast clamp"),
                    DialSetting("bvx_ratio", "Ratio", 4.0f, 4.0f, ":1", 1f, 8f, "In-your-face leveling")
                ), "Keeps chants locked at consistent level")
            )
        )
    )

    val spatialBuses: List<SpatialBusConfig> = listOf(
        SpatialBusConfig(
            id = "mid_bus",
            name = "Mid Bus (Sum L+R)",
            matrixType = "Center / Mono Channel (M = L + R - 3dB)",
            flStereoShaperSettings = "FL Studio Fruity Stereo Shaper preset 'Mid Only' (Left to L: +1.0, Right to L: +1.0, Left to R: +1.0, Right to R: +1.0, Inverted: None). Or FabFilter Pro-Q 3 set to Mid Solo.",
            purpose = "Carries 85% of riddim drop energy: Kick fundamental, Snare body, Sub Bass, Mono Growl center core, and Lead presence.",
            targetPeakDb = -4.2f,
            phaseRule = "Correlation MUST be +1.00 down low and minimum +0.85 across full spectrum.",
            fabFilterChain = listOf(
                FxPluginSetting("mbus_q3", "FabFilter Pro-Q 3 (Mid Channel)", "Linear Phase EQ", 1, listOf(
                    DialSetting("m_q_hp", "Linear Phase Low Cut", 25.0f, 25.0f, "Hz", 20f, 35f, "Clears DC offset from mono sum"),
                    DialSetting("m_q_mud", "Low Mid Clean (280Hz)", -1.8f, -1.8f, "dB", -6f, 0f, "Removes mud buildup from bus summing"),
                    DialSetting("m_q_bite", "Bite Presence (2.2kHz)", 1.2f, 1.2f, "dB", -3f, 3f, "Brings center riddim chops to front")
                ), "Linear Phase mode prevents transient phase smearing across center mono elements."),
                FxPluginSetting("mbus_clip", "iZotope Neutron Clipper", "Bus Clipper", 2, listOf(
                    DialSetting("m_clip_ceil", "Ceiling", -0.8f, -0.8f, "dBFS", -3f, 0f, "Pre-master headroom guardian"),
                    DialSetting("m_clip_drive", "Drive", 1.8f, 1.8f, "dB", 0f, 4f, "Riddim center transient density"),
                    DialSetting("m_clip_knee", "Knee", 20.0f, 20.0f, "%", 0f, 50f, "Soft-to-hard knee transition")
                ), "Shaves 1.5 dB off summing peaks before Pre-Master.")
            )
        ),
        SpatialBusConfig(
            id = "side_bus",
            name = "Side Bus (Diff L-R)",
            matrixType = "Stereo Difference Channel (S = L - R)",
            flStereoShaperSettings = "FL Studio Fruity Stereo Shaper preset 'Side Only' (Left to L: +1.0, Right to L: -1.0, Left to R: -1.0, Right to R: +1.0).",
            purpose = "Contains only stereo information: Cymbal wash, reverb decays, haas effect chops, detuned screech harmonics, delay ping-pongs.",
            targetPeakDb = -8.5f,
            phaseRule = "High-pass strictly above 120 Hz. ZERO sub frequency energy is permitted in this channel.",
            fabFilterChain = listOf(
                FxPluginSetting("sbus_q3", "FabFilter Pro-Q 3 (Side Channel)", "Stereo Cleaning EQ", 1, listOf(
                    DialSetting("s_q_hp", "Side Low Cut (CRITICAL)", 135.0f, 135.0f, "Hz", 100f, 200f, "48dB/oct steep low cut eliminates all stereo sub"),
                    DialSetting("s_q_box", "Hollow Scoop (500Hz)", -2.0f, -2.0f, "dB", -6f, 0f, "Prevents hollow side clutter"),
                    DialSetting("s_q_air", "Stereo Air (12kHz)", 2.2f, 2.2f, "dB", -2f, 6f, "Creates wide three-dimensional headroom")
                ), "The most critical step in riddim mastering: completely sterilizes stereo sub."),
                FxPluginSetting("sbus_sat2", "FabFilter Saturn 2", "Stereo Saturation", 2, listOf(
                    DialSetting("s_sat_style", "Warm Tube Style", 1.0f, 1.0f, "Tube", 1f, 1f, "Pleasing acoustic bloom"),
                    DialSetting("s_sat_drive", "Drive", 1.4f, 1.4f, "dB", 0f, 4f, "Wide saturation harmonics"),
                    DialSetting("s_sat_mix", "Mix", 35.0f, 35.0f, "%", 0f, 100f, "Parallel stereo polish")
                ), "Gives wide spatial stereo elements analog weight without widening phase drift.")
            )
        ),
        SpatialBusConfig(
            id = "left_bus",
            name = "Left Bus (Discrete L Channel)",
            matrixType = "Discrete Left Processing (L Only)",
            flStereoShaperSettings = "Fruity Stereo Shaper: Left to L: +1.0, Left to R: 0.0, Right to L: 0.0, Right to R: 0.0. Routes to Pre-Master Left input.",
            purpose = "Ensures left channel transient integrity and allows isolated metering of left stereo field balance.",
            targetPeakDb = -5.0f,
            phaseRule = "Monitors left vs right channel transient parity (within 0.3 dB dynamic balance).",
            fabFilterChain = listOf(
                FxPluginSetting("lbus_q3", "FabFilter Pro-Q 3 (Left Stem)", "Corrective EQ", 1, listOf(
                    DialSetting("l_trim", "Channel Gain Trim", 0.0f, 0.0f, "dB", -3f, 3f, "Calibrates left stereo balance")
                ), "Verifies left speaker punch matches right speaker punch exactly.")
            )
        ),
        SpatialBusConfig(
            id = "right_bus",
            name = "Right Bus (Discrete R Channel)",
            matrixType = "Discrete Right Processing (R Only)",
            flStereoShaperSettings = "Fruity Stereo Shaper: Right to R: +1.0, Right to L: 0.0, Left to R: 0.0, Left to L: 0.0. Routes to Pre-Master Right input.",
            purpose = "Ensures right channel transient integrity and validates stereo balance with Left Bus.",
            targetPeakDb = -5.0f,
            phaseRule = "Maintains stereo symmetry. Prevents drop chops from leaning heavily to one ear.",
            fabFilterChain = listOf(
                FxPluginSetting("rbus_q3", "FabFilter Pro-Q 3 (Right Stem)", "Corrective EQ", 1, listOf(
                    DialSetting("r_trim", "Channel Gain Trim", 0.0f, 0.0f, "dB", -3f, 3f, "Calibrates right stereo balance")
                ), "Guarantees stereo drops maintain centered perceptual weight.")
            )
        )
    )

    val masterChainStages: List<MasterStageConfig> = listOf(
        MasterStageConfig(
            step = 1,
            title = "Linear Phase Surgical Sculpting",
            pluginName = "FabFilter Pro-Q 3 (Master)",
            targetGoal = "Eliminate inaudible DC offset, sterilize sub side mud, and polish high-frequency air.",
            dialSettings = listOf(
                DialSetting("mq_hp", "Low Cut (Linear Phase High)", 25.0f, 25.0f, "Hz", 20f, 32f, "48dB/oct steep cut protects subwoofers"),
                DialSetting("mq_side_hp", "Side Band Low Cut", 125.0f, 125.0f, "Hz", 90f, 160f, "Sterilizes all stereo information below 125Hz"),
                DialSetting("mq_res_cut", "Resonance Notch (3.4kHz)", -1.2f, -1.2f, "dB", -4f, 0f, "Dynamic notch tames digital ear-piercing scream"),
                DialSetting("mq_air", "Air Shelf (16kHz)", 0.8f, 0.8f, "dB", -2f, 3f, "Subtle high-end polish and expensive sheen")
            ),
            operationalGuide = "Mode: Linear Phase (Medium or High resolution). This preserves kick and sub bass phase alignment without phase pre-ringing smearing."
        ),
        MasterStageConfig(
            step = 2,
            title = "Analog Harmonic Saturation & Glue",
            pluginName = "FabFilter Saturn 2 (Master)",
            targetGoal = "Generate subtle tape/tube harmonics to bind drums and bass together, creating analog density.",
            dialSettings = listOf(
                DialSetting("msat_drive", "Drive", 1.2f, 1.2f, "dB", 0.5f, 3f, "Subtle harmonic excitement"),
                DialSetting("msat_mix", "Dry/Wet Mix", 30.0f, 30.0f, "%", 10f, 60f, "Parallel warmth injection"),
                DialSetting("msat_dyn", "Dynamics Control", 0.5f, 0.5f, "dB", -2f, 2f, "Gently expands transient punch"),
                DialSetting("msat_hq", "Oversampling", 4.0f, 4.0f, "x", 1f, 8f, "Linear phase oversampling to prevent aliasing")
            ),
            operationalGuide = "Select 'Warm Tape' or 'Clean Tube'. We want less than 1 dB of harmonic coloration to subtly thicken the RMS without muddying transients."
        ),
        MasterStageConfig(
            step = 3,
            title = "Pre-Limiter Hard / Soft Transient Clipping",
            pluginName = "iZotope Neutron Clipper / Standard Clipper",
            targetGoal = "Shave rogue 2.0 to 3.0 dB transient spikes from Kick & Snare so Pro-L 2 limiter does not choke or pump.",
            dialSettings = listOf(
                DialSetting("mclip_ceil", "Clip Ceiling", -0.8f, -0.8f, "dBFS", -2f, 0f, "Sets hard boundary before final limiter"),
                DialSetting("mclip_drive", "Drive / Input Gain", 2.4f, 2.4f, "dB", 0f, 5f, "Pushes signal into clipping ceiling"),
                DialSetting("mclip_knee", "Knee Softness", 20.0f, 20.0f, "%", 0f, 60f, "Soft-clip knee prevents harsh square wave distortion"),
                DialSetting("mclip_gr", "Peak Shave (Target GR)", 2.2f, 2.2f, "dB", 1f, 4f, "Ideal transient peak reduction")
            ),
            operationalGuide = "CRITICAL SECRET TO -6 LUFS: Limiting drum transients creates mud and pumping; clipping drum transients creates punch and perceived loudness. The clipper converts inaudible spikes into harmonic density."
        ),
        MasterStageConfig(
            step = 4,
            title = "Subtle Bus Dynamics Glue",
            pluginName = "FabFilter Pro-C 2 (Master Glue)",
            targetGoal = "Subtle 0.5 to 1.0 dB dynamic glue to anchor the crest factor before the final limiter.",
            dialSettings = listOf(
                DialSetting("mc_style", "Style: Bus / Opto", 1.0f, 1.0f, "Bus", 1f, 1f, "Classic SSL style bus compression"),
                DialSetting("mc_thresh", "Threshold", -10.0f, -10.0f, "dB", -20f, 0f, "Only catches macro dynamic swells"),
                DialSetting("mc_ratio", "Ratio", 1.3f, 1.3f, ":1", 1.1f, 2f, "Gentle mastering glue ratio"),
                DialSetting("mc_att", "Attack", 30.0f, 30.0f, "ms", 15f, 50f, "Ultra slow attack lets drum transients pass unharmed"),
                DialSetting("mc_rel", "Release", 100.0f, 100.0f, "ms", 50f, 250f, "Auto or 100ms tempo synced")
            ),
            operationalGuide = "Ensure gain reduction needle moves no more than 0.5 to 1.0 dB during the heaviest drop chops. This holds the mix firmly together."
        ),
        MasterStageConfig(
            step = 5,
            title = "Final Brickwall True Peak Limiting to -6.0 LUFS",
            pluginName = "FabFilter Pro-L 2 (Final Brickwall Limiter)",
            targetGoal = "Achieve target -6.0 LUFS Integrated (-5.0 LUFS Short Term Drop) at True Peak -0.1 dBFS with zero audible distortion.",
            dialSettings = listOf(
                DialSetting("ml_style", "Limiter Style: Aggressive", 1.0f, 1.0f, "Aggressive", 1f, 1f, "Maintains punch on heavy dubstep/riddim transients"),
                DialSetting("ml_gain", "Limiter Gain Drive", 7.8f, 7.8f, "dB", 2f, 12f, "Drives signal to target -6.0 LUFS"),
                DialSetting("ml_ceil", "Output Ceiling", -0.1f, -0.1f, "dBFS", -0.5f, 0f, "Strict -0.1 dB True Peak protection"),
                DialSetting("ml_tp", "True Peak Limiting", 1.0f, 1.0f, "ON", 0f, 1f, "Inter-sample peak prevention"),
                DialSetting("ml_oversample", "Oversampling", 8.0f, 8.0f, "x", 2f, 32f, "8x linear-phase oversampling eliminates aliasing"),
                DialSetting("ml_lookahead", "Lookahead", 0.15f, 0.15f, "ms", 0.05f, 0.5f, "Ultra fast lookahead preserves transient edge"),
                DialSetting("ml_attack", "Attack Time", 4.0f, 4.0f, "ms", 1f, 15f, "Fast transient clamp"),
                DialSetting("ml_rel", "Release Time", 140.0f, 140.0f, "ms", 50f, 300f, "Timed to 1/8 note groove at 140 BPM"),
                DialSetting("ml_chanlink", "Channel Linking (Trans/Rel)", 85.0f, 85.0f, "%", 50f, 100f, "100% Transient / 50% Release linking")
            ),
            operationalGuide = "Because the Neutron Clipper already shaved 2.2 dB of spikes, Pro-L 2 only has to work 2.5 - 3.5 dB of gain reduction. This keeps the drop sounding enormous, deep, and crystal-clear."
        )
    )

    val stepByStepWorkflow = listOf(
        "Step 1: Session Setup & Calibration" to "Set FL Studio tempo to 140 or 142 BPM. Set Audio Engine buffer to 512 or 1024 samples for mastering latency. Turn off default Limiter on FL Master track.",
        "Step 2: Gain Staging All 11 Channels" to "Solo Kick and adjust fader to peak at -6.0 dBFS. Level Sub Bass at -7.0 dBFS, Snare at -6.0 dBFS, Mid Bass at -9.0 dBFS. Leave 6 dB headroom on every channel.",
        "Step 3: Sidechain Routing & Ducking Setup" to "Route Kick (Mixer Ch 1) to Sub Bass (Ch 6) using 'Route to this track only (sidechain)'. Open FabFilter Pro-MB on Sub Bass, enable Ext Sidechain, duck 30-85Hz by -18 dB with 0.1ms attack.",
        "Step 4: Grouping to the 8 Mixbuses" to "Route drums to Kick, Snare, and Perc buses. Route bass channels to Sub and Growl buses. Route leads, pads, and vocals to their respective mixbuses. Disconnect direct channel routing to Master!",
        "Step 5: Mid / Side & Left / Right Bus Matrix" to "Route Mixbuses into Mid Bus (mono sum) and Side Bus (stereo difference). High-pass Side Bus at 120Hz with Pro-Q 3. Use Left & Right discrete buses to calibrate balance.",
        "Step 6: Pre-Master Summing & Glue" to "Sum Mid, Side, Left, Right buses into Pre-Master (Ch 40). Ensure peak level is hitting -3.5 to -4.0 dBFS with crest factor around 7 dB.",
        "Step 7: Master Chain Execution to -6.0 LUFS" to "Load Master Chain: Pro-Q 3 Linear Phase -> Saturn 2 (1.2 dB warm tape) -> Neutron Clipper (shaving 2.2 dB transient peaks) -> Pro-C 2 (0.5 dB glue) -> Pro-L 2 (8x oversampled, -0.1 dBFS ceiling, +7.8 dB gain hitting -6.0 LUFS Integrated)."
    )
}
