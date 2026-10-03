package com.example.data

object HtmlExportGenerator {

    fun generateHtml(): String {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Professional Riddim Mixing & Mastering Guide (FL Studio 20 & FabFilter 2025)</title>
    <style>
        :root {
            --bg-dark: #0b0e14;
            --surface-dark: #131722;
            --surface-card: #1c2233;
            --border-color: #2a334a;
            --neon-green: #00ffa3;
            --neon-cyan: #00e5ff;
            --neon-orange: #ff9100;
            --neon-pink: #ff4081;
            --neon-red: #ff1744;
            --text-main: #f1f5f9;
            --text-dim: #94a3b8;
            --text-muted: #64748b;
        }
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            background-color: var(--bg-dark);
            color: var(--text-main);
            line-height: 1.6;
            padding: 24px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        header {
            border-bottom: 2px solid var(--border-color);
            padding-bottom: 24px;
            margin-bottom: 32px;
            text-align: center;
        }
        .badge {
            display: inline-block;
            background: rgba(0, 255, 163, 0.15);
            color: var(--neon-green);
            border: 1px solid var(--neon-green);
            padding: 4px 14px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 700;
            letter-spacing: 1px;
            text-transform: uppercase;
            margin-bottom: 12px;
        }
        h1 {
            font-size: 2.4rem;
            color: #ffffff;
            margin-bottom: 8px;
            font-weight: 800;
            letter-spacing: -0.5px;
        }
        .subtitle {
            color: var(--text-dim);
            font-size: 1.1rem;
            max-width: 800px;
            margin: 0 auto;
        }
        .metric-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 16px;
            margin: 24px 0 36px 0;
        }
        .metric-card {
            background: var(--surface-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 18px;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0,0,0,0.4);
        }
        .metric-val {
            font-size: 1.8rem;
            font-weight: 800;
            color: var(--neon-green);
            margin: 4px 0;
            font-family: monospace;
        }
        .metric-label {
            font-size: 0.85rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            color: var(--text-dim);
        }
        h2 {
            font-size: 1.6rem;
            color: var(--neon-cyan);
            border-left: 4px solid var(--neon-cyan);
            padding-left: 12px;
            margin: 36px 0 18px 0;
        }
        h3 {
            font-size: 1.25rem;
            color: #ffffff;
            margin: 16px 0 8px 0;
        }
        p, li {
            color: var(--text-dim);
            margin-bottom: 12px;
        }
        strong {
            color: var(--text-main);
        }
        .diagram-box {
            background: var(--surface-dark);
            border: 1px solid var(--border-color);
            border-radius: 14px;
            padding: 20px;
            margin: 20px 0 32px 0;
            overflow-x: auto;
        }
        svg {
            display: block;
            margin: 0 auto;
            max-width: 100%;
            height: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--surface-card);
            border-radius: 10px;
            overflow: hidden;
            margin: 16px 0 28px 0;
            font-size: 0.95rem;
        }
        th, td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }
        th {
            background: #151a28;
            color: var(--neon-green);
            font-weight: 700;
            text-transform: uppercase;
            font-size: 0.8rem;
            letter-spacing: 0.5px;
        }
        tr:hover {
            background: rgba(255,255,255,0.02);
        }
        .tag-pill {
            display: inline-block;
            padding: 2px 8px;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 600;
            font-family: monospace;
        }
        .tag-peak { background: rgba(255, 64, 129, 0.2); color: var(--neon-pink); }
        .tag-phase { background: rgba(0, 229, 255, 0.2); color: var(--neon-cyan); }
        .tag-mono { background: rgba(0, 255, 163, 0.2); color: var(--neon-green); }
        .code-callout {
            background: #0f131c;
            border-left: 3px solid var(--neon-orange);
            padding: 14px 18px;
            border-radius: 0 8px 8px 0;
            font-family: monospace;
            font-size: 0.9rem;
            color: #e2e8f0;
            margin: 14px 0;
        }
        .grid-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        @media (max-width: 768px) {
            .grid-2 { grid-template-columns: 1fr; }
            h1 { font-size: 1.8rem; }
            body { padding: 14px; }
        }
        footer {
            margin-top: 50px;
            border-top: 1px solid var(--border-color);
            padding-top: 24px;
            text-align: center;
            color: var(--text-muted);
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
<div class="container">
    <header>
        <span class="badge">Mastering Specification 2025</span>
        <h1>FL Studio 20 Riddim Mixing & Mastering Architecture</h1>
        <p class="subtitle">Complete technical blueprint for achieving -6.0 LUFS Integrated at -0.1 dBFS True Peak with FabFilter 2025 VSTs, iZotope Neutron Clipper, and Mid/Side/Left/Right Stem Routing.</p>
    </header>

    <div class="metric-grid">
        <div class="metric-card">
            <div class="metric-label">Target Integrated Loudness</div>
            <div class="metric-val">-6.0 LUFS</div>
            <div class="metric-label">High-Impact Drop Power</div>
        </div>
        <div class="metric-card">
            <div class="metric-label">True Peak Ceiling</div>
            <div class="metric-val">-0.1 dBFS</div>
            <div class="metric-label">8x Oversampled Brickwall</div>
        </div>
        <div class="metric-card">
            <div class="metric-label">Pre-Limiter Clip Shave</div>
            <div class="metric-val">2.2 to 2.8 dB</div>
            <div class="metric-label">Neutron Clipper Shave</div>
        </div>
        <div class="metric-card">
            <div class="metric-label">Sub Phase Correlation</div>
            <div class="metric-val">+1.00 Mono</div>
            <div class="metric-label">30-90 Hz Pure Monophonic</div>
        </div>
    </div>

    <h2>1. Full DAW Signal Routing Architecture Diagram</h2>
    <p>All individual channels are disconnected from the Master track and routed strictly into 8 designated Mixbuses, which feed the 4 Spatial Processing Channels (Mid, Side, Left, Right), summing into the Pre-Master, and terminating at the Final Master Chain.</p>

    <div class="diagram-box">
        <svg viewBox="0 0 1000 480" width="1000" height="480">
            <defs>
                <linearGradient id="gradDrums" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#ef4444" />
                    <stop offset="100%" stop-color="#b91c1c" />
                </linearGradient>
                <linearGradient id="gradBass" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#8b5cf6" />
                    <stop offset="100%" stop-color="#6d28d9" />
                </linearGradient>
                <linearGradient id="gradBuses" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#00ffa3" />
                    <stop offset="100%" stop-color="#059669" />
                </linearGradient>
                <linearGradient id="gradSpatial" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#00e5ff" />
                    <stop offset="100%" stop-color="#0284c7" />
                </linearGradient>
                <linearGradient id="gradMaster" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#ff9100" />
                    <stop offset="100%" stop-color="#ea580c" />
                </linearGradient>
            </defs>

            <!-- Column 1: Channels -->
            <rect x="20" y="20" width="180" height="440" rx="8" fill="#131722" stroke="#2a334a" />
            <text x="110" y="45" fill="#94a3b8" font-size="12" font-weight="700" text-anchor="middle">11 SOURCE CHANNELS</text>

            <rect x="35" y="60" width="150" height="24" rx="4" fill="#1e2538" stroke="#ef4444" />
            <text x="45" y="76" fill="#f1f5f9" font-size="11" font-weight="600">Kick (-6 dBFS)</text>

            <rect x="35" y="90" width="150" height="24" rx="4" fill="#1e2538" stroke="#f59e0b" />
            <text x="45" y="106" fill="#f1f5f9" font-size="11" font-weight="600">Snare (-6 dBFS)</text>

            <rect x="35" y="120" width="150" height="24" rx="4" fill="#1e2538" stroke="#10b981" />
            <text x="45" y="136" fill="#f1f5f9" font-size="11" font-weight="600">Hi-Hats (-14 dBFS)</text>

            <rect x="35" y="150" width="150" height="24" rx="4" fill="#1e2538" stroke="#10b981" />
            <text x="45" y="166" fill="#f1f5f9" font-size="11" font-weight="600">Cymbals (-12 dBFS)</text>

            <rect x="35" y="180" width="150" height="24" rx="4" fill="#1e2538" stroke="#10b981" />
            <text x="45" y="196" fill="#f1f5f9" font-size="11" font-weight="600">Percussion (-10 dBFS)</text>

            <rect x="35" y="215" width="150" height="24" rx="4" fill="#1e2538" stroke="#3b82f6" />
            <text x="45" y="231" fill="#f1f5f9" font-size="11" font-weight="600">Sub Bass (-7 dBFS Mono)</text>

            <rect x="35" y="245" width="150" height="24" rx="4" fill="#1e2538" stroke="#8b5cf6" />
            <text x="45" y="261" fill="#f1f5f9" font-size="11" font-weight="600">Mid Bass (-9 dBFS)</text>

            <rect x="35" y="275" width="150" height="24" rx="4" fill="#1e2538" stroke="#8b5cf6" />
            <text x="45" y="291" fill="#f1f5f9" font-size="11" font-weight="600">Growl (-8 dBFS)</text>

            <rect x="35" y="310" width="150" height="24" rx="4" fill="#1e2538" stroke="#06b6d4" />
            <text x="45" y="326" fill="#f1f5f9" font-size="11" font-weight="600">Lead Synth (-10 dBFS)</text>

            <rect x="35" y="340" width="150" height="24" rx="4" fill="#1e2538" stroke="#64748b" />
            <text x="45" y="356" fill="#f1f5f9" font-size="11" font-weight="600">Pad / Drones (-16 dBFS)</text>

            <rect x="35" y="375" width="150" height="24" rx="4" fill="#1e2538" stroke="#ec4899" />
            <text x="45" y="391" fill="#f1f5f9" font-size="11" font-weight="600">Vocal Chants (-9 dBFS)</text>

            <!-- Column 2: 8 Mixbuses -->
            <rect x="250" y="20" width="190" height="440" rx="8" fill="#131722" stroke="#2a334a" />
            <text x="345" y="45" fill="#94a3b8" font-size="12" font-weight="700" text-anchor="middle">8 MIXBUSES (GLUE & CLIP)</text>

            <rect x="265" y="65" width="160" height="34" rx="4" fill="#1c2233" stroke="#ef4444" />
            <text x="275" y="86" fill="#00ffa3" font-size="11" font-weight="700">Kick Bus (Ch 20)</text>

            <rect x="265" y="110" width="160" height="34" rx="4" fill="#1c2233" stroke="#f59e0b" />
            <text x="275" y="131" fill="#00ffa3" font-size="11" font-weight="700">Snare Bus (Ch 21)</text>

            <rect x="265" y="155" width="160" height="34" rx="4" fill="#1c2233" stroke="#10b981" />
            <text x="275" y="176" fill="#00ffa3" font-size="11" font-weight="700">Percussion Bus (Ch 22)</text>

            <rect x="265" y="200" width="160" height="34" rx="4" fill="#1c2233" stroke="#3b82f6" />
            <text x="275" y="221" fill="#00ffa3" font-size="11" font-weight="700">Sub Bus (Ch 23)</text>

            <rect x="265" y="245" width="160" height="34" rx="4" fill="#1c2233" stroke="#8b5cf6" />
            <text x="275" y="266" fill="#00ffa3" font-size="11" font-weight="700">Growl/Bass Bus (Ch 24)</text>

            <rect x="265" y="290" width="160" height="34" rx="4" fill="#1c2233" stroke="#06b6d4" />
            <text x="275" y="311" fill="#00ffa3" font-size="11" font-weight="700">Lead Bus (Ch 25)</text>

            <rect x="265" y="335" width="160" height="34" rx="4" fill="#1c2233" stroke="#64748b" />
            <text x="275" y="356" fill="#00ffa3" font-size="11" font-weight="700">Pad Bus (Ch 26)</text>

            <rect x="265" y="380" width="160" height="34" rx="4" fill="#1c2233" stroke="#ec4899" />
            <text x="275" y="401" fill="#00ffa3" font-size="11" font-weight="700">Vocal Bus (Ch 27)</text>

            <!-- Column 3: Spatial M/S & L/R Buses -->
            <rect x="490" y="20" width="190" height="440" rx="8" fill="#131722" stroke="#2a334a" />
            <text x="585" y="45" fill="#94a3b8" font-size="12" font-weight="700" text-anchor="middle">SPATIAL PROCESSING</text>

            <rect x="505" y="75" width="160" height="60" rx="6" fill="#1c2233" stroke="#00e5ff" stroke-width="2" />
            <text x="515" y="98" fill="#00e5ff" font-size="12" font-weight="800">MID BUS (L+R Sum)</text>
            <text x="515" y="118" fill="#94a3b8" font-size="10">Pro-Q 3 + Clipper (Punch)</text>

            <rect x="505" y="155" width="160" height="60" rx="6" fill="#1c2233" stroke="#00ffa3" stroke-width="2" />
            <text x="515" y="178" fill="#00ffa3" font-size="12" font-weight="800">SIDE BUS (L-R Diff)</text>
            <text x="515" y="198" fill="#94a3b8" font-size="10">HP @ 135Hz (Sterile Sub)</text>

            <rect x="505" y="240" width="160" height="50" rx="6" fill="#1c2233" stroke="#f59e0b" />
            <text x="515" y="262" fill="#f59e0b" font-size="11" font-weight="700">LEFT BUS (L Monitor)</text>
            <text x="515" y="278" fill="#94a3b8" font-size="10">Transient Alignment</text>

            <rect x="505" y="310" width="160" height="50" rx="6" fill="#1c2233" stroke="#f59e0b" />
            <text x="515" y="332" fill="#f59e0b" font-size="11" font-weight="700">RIGHT BUS (R Monitor)</text>
            <text x="515" y="348" fill="#94a3b8" font-size="10">Stereo Symmetry</text>

            <!-- Column 4: Pre-Master & Master -->
            <rect x="730" y="20" width="240" height="440" rx="8" fill="#131722" stroke="#2a334a" />
            <text x="850" y="45" fill="#94a3b8" font-size="12" font-weight="700" text-anchor="middle">PRE-MASTER & FINAL MASTER</text>

            <rect x="745" y="70" width="210" height="55" rx="6" fill="#1c2233" stroke="#8b5cf6" />
            <text x="755" y="94" fill="#a78bfa" font-size="12" font-weight="800">PRE-MASTER (Ch 40)</text>
            <text x="755" y="112" fill="#94a3b8" font-size="10">Glue Multiband | Peak -3.5 dBFS</text>

            <!-- Master Chain Box -->
            <rect x="745" y="145" width="210" height="295" rx="6" fill="#161b29" stroke="#ff9100" stroke-width="2" />
            <text x="850" y="170" fill="#ff9100" font-size="13" font-weight="800" text-anchor="middle">MASTER FX CHAIN</text>

            <rect x="755" y="185" width="190" height="35" rx="4" fill="#1f273d" />
            <text x="765" y="207" fill="#f1f5f9" font-size="10" font-weight="700">1. Pro-Q 3 (Linear Phase 25Hz HP)</text>

            <rect x="755" y="228" width="190" height="35" rx="4" fill="#1f273d" />
            <text x="765" y="250" fill="#f1f5f9" font-size="10" font-weight="700">2. Saturn 2 (1.2dB Warm Tape)</text>

            <rect x="755" y="271" width="190" height="35" rx="4" fill="#1f273d" stroke="#ff1744" />
            <text x="765" y="293" fill="#ff4081" font-size="10" font-weight="700">3. Neutron Clipper (-0.8dB / 2.2dB GR)</text>

            <rect x="755" y="314" width="190" height="35" rx="4" fill="#1f273d" />
            <text x="765" y="336" fill="#f1f5f9" font-size="10" font-weight="700">4. Pro-C 2 (1.2:1 Opto Glue, 0.5dB GR)</text>

            <rect x="755" y="357" width="190" height="42" rx="4" fill="#1f273d" stroke="#00ffa3" stroke-width="1.5" />
            <text x="765" y="377" fill="#00ffa3" font-size="10" font-weight="800">5. Pro-L 2 (Target -6.0 LUFS)</text>
            <text x="765" y="392" fill="#94a3b8" font-size="9">Aggressive | 8x OS | -0.1 dB True Peak</text>

            <text x="850" y="425" fill="#00ffa3" font-size="12" font-weight="800" text-anchor="middle">TARGET: -6.0 LUFS @ -0.1 TP</text>

            <!-- Signal routing connector lines -->
            <path d="M 185 72 L 265 82" stroke="#ef4444" stroke-width="1.5" fill="none" />
            <path d="M 185 102 L 265 127" stroke="#f59e0b" stroke-width="1.5" fill="none" />
            <path d="M 185 132 L 265 172" stroke="#10b981" stroke-width="1.5" fill="none" />
            <path d="M 185 162 L 265 172" stroke="#10b981" stroke-width="1.5" fill="none" />
            <path d="M 185 192 L 265 172" stroke="#10b981" stroke-width="1.5" fill="none" />
            <path d="M 185 227 L 265 217" stroke="#3b82f6" stroke-width="2" fill="none" />
            <path d="M 185 257 L 265 262" stroke="#8b5cf6" stroke-width="1.5" fill="none" />
            <path d="M 185 287 L 265 262" stroke="#8b5cf6" stroke-width="1.5" fill="none" />
            <path d="M 185 322 L 265 307" stroke="#06b6d4" stroke-width="1.5" fill="none" />
            <path d="M 185 352 L 265 352" stroke="#64748b" stroke-width="1.5" fill="none" />
            <path d="M 185 387 L 265 397" stroke="#ec4899" stroke-width="1.5" fill="none" />

            <!-- Buses to Spatial -->
            <path d="M 425 82 L 505 95" stroke="#00e5ff" stroke-width="2" fill="none" />
            <path d="M 425 217 L 505 105" stroke="#00e5ff" stroke-width="2" fill="none" />
            <path d="M 425 172 L 505 175" stroke="#00ffa3" stroke-width="1.5" fill="none" />
            <path d="M 425 262 L 505 115" stroke="#00e5ff" stroke-width="1" fill="none" />
            <path d="M 425 262 L 505 185" stroke="#00ffa3" stroke-width="1" fill="none" />
            <path d="M 425 352 L 505 195" stroke="#00ffa3" stroke-width="1" fill="none" />

            <!-- Spatial to PreMaster -->
            <path d="M 665 105 L 745 95" stroke="#00e5ff" stroke-width="2" fill="none" />
            <path d="M 665 185 L 745 98" stroke="#00ffa3" stroke-width="1.5" fill="none" />
            <path d="M 665 265 L 745 105" stroke="#f59e0b" stroke-width="1.5" fill="none" />
            <path d="M 665 335 L 745 110" stroke="#f59e0b" stroke-width="1.5" fill="none" />

            <!-- PreMaster to Master -->
            <path d="M 850 125 L 850 145" stroke="#ff9100" stroke-width="3" fill="none" marker-end="url(#arrow)" />
        </svg>
    </div>

    <h2>2. Channel-by-Channel Dial & Parameter Settings</h2>
    <p>Calibrated gain staging peak levels, phase correlation targets, and exact FabFilter 2025 and Neutron Clipper parameter values for all 11 core riddim channels.</p>

    <table>
        <thead>
            <tr>
                <th>Channel</th>
                <th>Target Peak</th>
                <th>Phase Target</th>
                <th>Mixbus</th>
                <th>Core Plugin Settings (Dials & Perimeters)</th>
                <th>Sidechain Matrix</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Kick</strong></td>
                <td><span class="tag-pill tag-peak">-6.0 dBFS</span></td>
                <td><span class="tag-pill tag-mono">+0.98 (Mono)</span></td>
                <td>Kick Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 32Hz 48dB/oct, 50Hz Boost +1.5dB, Notch 250Hz -3.2dB, Click 3.2kHz +2dB.<br>
                    <strong>Neutron Clipper:</strong> Ceiling -0.5 dBFS, Drive +2.2dB, Knee 25% (Soft clip shaves 2dB).<br>
                    <strong>Pro-C 2:</strong> Punch mode, Attack 25ms, Release 65ms, Ratio 3.5:1.
                </td>
                <td>Sends trigger to Sub Bass (Ch 6) & Mid Bass (Ch 7).</td>
            </tr>
            <tr>
                <td><strong>Snare</strong></td>
                <td><span class="tag-pill tag-peak">-6.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.95</span></td>
                <td>Snare Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 120Hz, Body 210Hz +2.4dB, Ring notch 750Hz -4dB, Crack 4.5kHz +3dB.<br>
                    <strong>Saturn 2:</strong> High band crossover 1.5kHz, Warm Tube Drive +1.8dB, Mix 45%.<br>
                    <strong>Neutron Clipper:</strong> Ceiling -0.3 dBFS, Drive +2.8dB, Knee 10% (Hard clip).
                </td>
                <td>Sidechains to Mid Bass, Growl, and Pads on beat 3.</td>
            </tr>
            <tr>
                <td><strong>Hi-Hat</strong></td>
                <td><span class="tag-pill tag-peak">-14.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.80</span></td>
                <td>Perc Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> Low Cut 650Hz 24dB/oct, Dip 3.8kHz -2dB, Air Shelf 11kHz +1.5dB.<br>
                    <strong>Pro-C 2:</strong> Opto mode, Attack 10ms, Release 45ms, Ratio 2.2:1.
                </td>
                <td>Panned gently &plusmn;25% for wide stereo hats.</td>
            </tr>
            <tr>
                <td><strong>Cymbal</strong></td>
                <td><span class="tag-pill tag-peak">-12.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.70</span></td>
                <td>Perc Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 450Hz, Gong Notch 850Hz -4.5dB, LP 18.5kHz.<br>
                    <strong>Timeless 3:</strong> Stereo tape delay 1/8D, HP filter 500Hz, Mix 15%.
                </td>
                <td>Sidechained to Kick (ducks 3dB on kick impact).</td>
            </tr>
            <tr>
                <td><strong>Percussion</strong></td>
                <td><span class="tag-pill tag-peak">-10.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.85</span></td>
                <td>Perc Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 350Hz, Click 1.4kHz +2dB, Dynamic EQ -3dB range.<br>
                    <strong>Neutron Clipper:</strong> Ceiling -1.0 dBFS, Drive +1.5dB (clamps wood clicks).
                </td>
                <td>Routes to Percussion Mixbus.</td>
            </tr>
            <tr>
                <td><strong>Sub Bass</strong></td>
                <td><span class="tag-pill tag-peak">-7.0 dBFS</span></td>
                <td><span class="tag-pill tag-mono">+1.00 (Pure Mono)</span></td>
                <td>Sub Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> Low Cut 28Hz 48dB/oct, Brickwall High Cut 85Hz, Boost 44Hz +1dB. Mono mode.<br>
                    <strong>Pro-MB:</strong> Dynamic sidechain low band 30-90Hz, Duck Range -18dB, Attack 0.1ms, Release 115ms.
                </td>
                <td><strong>CRITICAL:</strong> Instant kick ducking. Feeds ONLY Mid Bus.</td>
            </tr>
            <tr>
                <td><strong>Mid Bass</strong></td>
                <td><span class="tag-pill tag-peak">-9.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.85</span></td>
                <td>Growl Bus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 92Hz 48dB/oct, Honk Peak 480Hz +2.5dB, Notch 1.1kHz -3dB.<br>
                    <strong>Saturn 2:</strong> Heavy Saturation Drive +3.5dB, Mix 70%.<br>
                    <strong>Neutron Clipper:</strong> Ceiling -0.8 dBFS, Drive +2.0dB.
                </td>
                <td>Ducked by Kick (-12dB) & Snare (-15dB).</td>
            </tr>
            <tr>
                <td><strong>Growl</strong></td>
                <td><span class="tag-pill tag-peak">-8.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.75</span></td>
                <td>Growl Bus</td>
                <td>
                    <strong>Volcano 3:</strong> Dual bandpass 720Hz & 2.4kHz, Peak Q 4.2.<br>
                    <strong>Pro-Q 3 (M/S):</strong> Mid HP 110Hz, Side HP 200Hz, Dynamic cut 3.2kHz -3.8dB, Side Shelf +2.2dB.<br>
                    <strong>Neutron Clipper:</strong> Ceiling -0.5 dBFS, Drive +2.5dB.
                </td>
                <td>Split 70% to Mid Bus, 30% to Side Bus.</td>
            </tr>
            <tr>
                <td><strong>Lead</strong></td>
                <td><span class="tag-pill tag-peak">-10.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.80</span></td>
                <td>Lead Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 250Hz, Laser Focus 4.2kHz +2dB, LP 17.5kHz.<br>
                    <strong>Timeless 3:</strong> 185ms delay, Feedback 30%, Mix 20%.
                </td>
                <td>Ducked by Kick, Snare, and Vocal channels.</td>
            </tr>
            <tr>
                <td><strong>Pad</strong></td>
                <td><span class="tag-pill tag-peak">-16.0 dBFS</span></td>
                <td><span class="tag-pill tag-phase">+0.65</span></td>
                <td>Pad Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 220Hz, Center Scoop 800Hz -4.5dB, Air Shimmer 10kHz +2dB.<br>
                    <strong>Pro-C 2:</strong> Sidechain ducking pump, Ratio 5:1, Release 220ms.
                </td>
                <td>Ducked -18dB during drop; routes to Side Bus.</td>
            </tr>
            <tr>
                <td><strong>Vocal</strong></td>
                <td><span class="tag-pill tag-peak">-9.0 dBFS</span></td>
                <td><span class="tag-pill tag-mono">+0.90</span></td>
                <td>Vocal Mixbus</td>
                <td>
                    <strong>Pro-Q 3:</strong> HP 135Hz, Chest mud 320Hz -3dB, Presence 2.8kHz +2.5dB, Dynamic De-ess 6.5kHz -4dB.<br>
                    <strong>Pro-C 2:</strong> Vocal style, Attack 1.5ms, Release 85ms, Ratio 4:1.<br>
                    <strong>Saturn 2:</strong> Warm Tube Drive +2.2dB, Mix 50%.
                </td>
                <td>Sends ducking trigger to Pads and Leads.</td>
            </tr>
        </tbody>
    </table>

    <h2>3. The Sidechain Matrix: Perimeters & Wiring</h2>
    <div class="code-callout">
        <strong>FL Studio 20 Sidechain Protocol:</strong><br>
        1. Select Kick track (Ch 1) in FL Mixer.<br>
        2. Right-click the send enable arrow at the bottom of Sub Bass (Ch 6) -> Select <em>"Sidechain to this track"</em>. (FL Studio automatically sets the audio volume to 0%, providing silent sidechain audio signal).<br>
        3. Open FabFilter Pro-MB or Pro-C 2 on Sub Bass -> Click the gear icon (Wrapper Settings) -> Click 'Processing' tab.<br>
        4. Under 'Connections', right-click 'Sidechain 1' and select 'Kick'.<br>
        5. Inside Pro-MB: Click 'Expert', set Sidechain mode to 'Ext', target band 30-90Hz, set Attack to 0.1ms and Release to 115ms.
    </div>

    <h2>4. Spatial Processing: Mid, Side, Left, and Right Stem Routing</h2>
    <div class="grid-2">
        <div class="metric-card" style="text-align: left;">
            <h3 style="color: var(--neon-cyan);">Mid Bus (Sum L+R)</h3>
            <p>Captures all monophonic core punch. Sums Kick, Sub, Snare core, and Lead center.</p>
            <ul>
                <li><strong>Matrix:</strong> Fruity Stereo Shaper preset "Mid Only" (or Pro-Q 3 Mid Solo).</li>
                <li><strong>Pro-Q 3:</strong> Linear Phase HP @ 25Hz, notch 280Hz mud buildup (-1.8dB).</li>
                <li><strong>Neutron Clipper:</strong> Soft knee 20%, Drive +1.8dB, Ceiling -0.8 dBFS.</li>
                <li><strong>Phase Target:</strong> Strictly +1.00 on sub, +0.90 overall.</li>
            </ul>
        </div>
        <div class="metric-card" style="text-align: left;">
            <h3 style="color: var(--neon-green);">Side Bus (Diff L-R)</h3>
            <p>Captures stereo width, reverb tails, cymbal wash, and detuned synth screams.</p>
            <ul>
                <li><strong>Matrix:</strong> Fruity Stereo Shaper preset "Side Only" (Left to L +1.0, Right to L -1.0).</li>
                <li><strong>Pro-Q 3:</strong> Low Cut @ 135Hz (48dB/oct steep). Absolute zero sub allowed!</li>
                <li><strong>Saturn 2:</strong> Warm Tube Drive +1.4dB, Mix 35% for rich side bloom.</li>
                <li><strong>Peak Target:</strong> -8.5 dBFS.</li>
            </ul>
        </div>
    </div>

    <h2>5. Mastering Chain Execution: Achieving -6.0 LUFS @ -0.1 dBFS True Peak</h2>
    <p>The final Master track uses a high-precision 5-stage serial rack. The secret to achieving competitive dubstep/riddim loudness without distortion is clipping transient crest factor <em>before</em> hitting the limiter.</p>

    <div class="diagram-box">
        <h3>Master Rack Serial Pipeline:</h3>
        <ol style="margin-left: 24px; padding-top: 8px;">
            <li><strong>Stage 1: FabFilter Pro-Q 3 (Linear Phase High Resolution)</strong><br>
                Sub cut 25Hz (48dB/oct) to prevent DC bias. Side band high-pass at 125Hz. Dynamic resonance notch at 3.4kHz (-1.2dB) to eliminate digital harshness. High air shelf at 16kHz (+0.8dB).</li>
            <li><strong>Stage 2: FabFilter Saturn 2 (Subtle Analog Tape Glue)</strong><br>
                Linear phase 4x oversampling. Warm Tape mode. Drive +1.2dB, Dynamics +0.5dB, Dry/Wet Mix 30%. Adds second and third-order harmonics that bind drum transients and bass chops.</li>
            <li><strong>Stage 3: iZotope Neutron Clipper / Standard Clipper (The -6 LUFS Secret)</strong><br>
                Ceiling set to -0.8 dBFS. Soft knee at 20%. Input Drive pushed +2.4dB to shave off 2.2 to 2.5 dB of rogue snare and kick transient spikes. This flattens the crest factor from 9dB down to 6.5dB without any limiter pumping!</li>
            <li><strong>Stage 4: FabFilter Pro-C 2 (Master Bus Opto Glue)</strong><br>
                Bus/Opto style. Ratio 1.2:1 to 1.3:1. Slow attack (30ms) lets clipped transients pass untouched. Release auto/100ms. Shaves no more than 0.5 to 0.8 dB of macro dynamics.</li>
            <li><strong>Stage 5: FabFilter Pro-L 2 (Final True Peak Brickwall Limiter)</strong><br>
                Limiter Style: <em>Aggressive</em> (preserves aggressive riddim transients).<br>
                Oversampling: <em>8x</em> linear-phase (100% inter-sample peak prevention).<br>
                True Peak Limiting: <em>ON</em>.<br>
                Output Ceiling: <em>-0.1 dBFS</em>.<br>
                Lookahead: <em>0.15ms</em>, Attack: <em>4.0ms</em>, Release: <em>140ms</em> (tempo matched to 140 BPM).<br>
                Channel Linking: <em>100% Transient / 50% Release</em>.<br>
                Gain: <em>+7.8 dB</em> to push drop master to <strong>-6.0 LUFS Integrated (-5.2 LUFS Short Term)</strong>.
            </li>
        </ol>
    </div>

    <h2>6. Streaming Platform Compatibility Analysis</h2>
    <table>
        <thead>
            <tr>
                <th>Platform</th>
                <th>Target Reference</th>
                <th>Penalty / Normalization Applied</th>
                <th>Playback Result & Sound Quality</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Master File (Direct)</strong></td>
                <td>-6.0 LUFS | -0.1 dB True Peak</td>
                <td>0 dB (Raw Club Playback)</td>
                <td>Max punch, explosive sub bass on festival sound systems.</td>
            </tr>
            <tr>
                <td><strong>Spotify</strong></td>
                <td>-14.0 LUFS</td>
                <td>Turned down by -8.0 dB</td>
                <td>Zero distortion, tight crest factor, sounds louder than dynamic tracks.</td>
            </tr>
            <tr>
                <td><strong>Apple Music</strong></td>
                <td>-16.0 LUFS</td>
                <td>Turned down by -10.0 dB</td>
                <td>Sound Check preserves full punch and clarity. -0.1 TP prevents AAC clipping.</td>
            </tr>
            <tr>
                <td><strong>YouTube Music</strong></td>
                <td>-14.0 LUFS</td>
                <td>Turned down by -8.0 dB</td>
                <td>Crisp transient definition; zero harshness.</td>
            </tr>
            <tr>
                <td><strong>SoundCloud</strong></td>
                <td>No normalization (Raw)</td>
                <td>0 dB (Transcoded to 128k MP3)</td>
                <td>-0.1 dBFS True Peak with 8x OS prevents MP3 encoder splatter.</td>
            </tr>
            <tr>
                <td><strong>Beatport / DJ Pool</strong></td>
                <td>No normalization</td>
                <td>0 dB (WAV / 320k MP3)</td>
                <td>Matches standard heavy dubstep and riddim club release standards.</td>
            </tr>
        </tbody>
    </table>

    <footer>
        <p>Riddim Master Engineering Suite &bull; Exported from FL Studio 20 &amp; FabFilter 2025 Architecture</p>
    </footer>
</div>
</body>
</html>
        """.trimIndent()
    }
}
