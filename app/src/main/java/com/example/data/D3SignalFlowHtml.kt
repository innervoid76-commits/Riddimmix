package com.example.data

object D3SignalFlowHtml {

    fun buildD3Html(): String {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
    <title>Interactive D3 Riddim Signal Flow Chart</title>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <style>
        :root {
            --bg-color: #0c0f17;
            --surface-color: #141926;
            --border-color: #2b344f;
            --neon-green: #00ffa3;
            --neon-cyan: #00e5ff;
            --neon-orange: #ff9100;
            --neon-pink: #ff4081;
            --neon-red: #ff1744;
            --text-primary: #f1f5f9;
            --text-secondary: #94a3b8;
        }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            background-color: var(--bg-color);
            color: var(--text-primary);
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            overflow: hidden;
            width: 100vw;
            height: 100vh;
        }
        #chart-container {
            width: 100%;
            height: 100%;
            position: relative;
        }
        #controls {
            position: absolute;
            top: 10px;
            left: 10px;
            right: 10px;
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
            z-index: 100;
            pointer-events: auto;
        }
        .btn-filter {
            background: #1c2233;
            border: 1px solid var(--border-color);
            color: var(--text-secondary);
            padding: 5px 10px;
            border-radius: 6px;
            font-size: 11px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s;
        }
        .btn-filter.active, .btn-filter:hover {
            background: var(--neon-green);
            color: #0c0f17;
            border-color: var(--neon-green);
        }
        #tooltip {
            position: absolute;
            display: none;
            background: #171d2b;
            border: 1px solid var(--neon-cyan);
            border-radius: 8px;
            padding: 10px 14px;
            font-size: 11px;
            color: var(--text-primary);
            pointer-events: none;
            box-shadow: 0 4px 16px rgba(0,0,0,0.6);
            z-index: 200;
            max-width: 280px;
        }
        .tooltip-title {
            font-weight: 800;
            color: var(--neon-green);
            font-size: 13px;
            margin-bottom: 4px;
        }
        .tooltip-badge {
            display: inline-block;
            background: #232b3d;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: monospace;
            font-size: 10px;
            margin-bottom: 6px;
        }
        .node {
            cursor: pointer;
            transition: transform 0.2s;
        }
        .node rect {
            stroke-width: 1.5px;
            rx: 6px;
            ry: 6px;
        }
        .node text {
            font-family: -apple-system, BlinkMacSystemFont, sans-serif;
            pointer-events: none;
        }
        .link {
            fill: none;
            stroke-opacity: 0.35;
            transition: stroke-opacity 0.3s, stroke-width 0.3s;
        }
        .link.active {
            stroke-opacity: 0.95 !important;
            stroke-width: 3px !important;
        }
        .link.dimmed {
            stroke-opacity: 0.08 !important;
        }
        .sidechain-link {
            stroke-dasharray: 4, 4;
            animation: dash 1s linear infinite;
        }
        @keyframes dash {
            to { stroke-dashoffset: -8; }
        }
        .zoom-controls {
            position: absolute;
            bottom: 15px;
            right: 15px;
            display: flex;
            flex-direction: column;
            gap: 6px;
            z-index: 100;
        }
        .zoom-btn {
            background: #1c2233;
            border: 1px solid var(--border-color);
            color: var(--text-primary);
            width: 32px;
            height: 32px;
            border-radius: 6px;
            font-size: 16px;
            font-weight: bold;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
        }
        .zoom-btn:hover {
            border-color: var(--neon-cyan);
            color: var(--neon-cyan);
        }
    </style>
</head>
<body>

<div id="chart-container">
    <div id="controls">
        <button class="btn-filter active" onclick="filterGraph('all')">All Paths</button>
        <button class="btn-filter" onclick="filterGraph('drums')">Drums & Sub</button>
        <button class="btn-filter" onclick="filterGraph('basses')">Mid & Growls</button>
        <button class="btn-filter" onclick="filterGraph('spatial')">Spatial M/S</button>
        <button class="btn-filter" onclick="filterGraph('sidechain')">Sidechains Only</button>
    </div>

    <div class="zoom-controls">
        <button class="zoom-btn" onclick="zoomIn()">+</button>
        <button class="zoom-btn" onclick="zoomOut()">-</button>
        <button class="zoom-btn" onclick="resetZoom()">⟲</button>
    </div>

    <div id="tooltip"></div>
</div>

<script>
const nodesData = [
    // Layer 0: 11 Input Channels
    { id: "kick", name: "Kick (Ch 1)", layer: 0, cat: "drums", peak: "-6.0 dBFS", phase: "+0.98", desc: "48Hz fundamental, 3.2kHz click. Neutron Clipper shaves 2dB.", color: "#ef4444" },
    { id: "snare", name: "Snare (Ch 2)", layer: 0, cat: "drums", peak: "-6.0 dBFS", phase: "+0.95", desc: "210Hz body thud, 4.5kHz crack. Saturn 2 warm tube drive.", color: "#f59e0b" },
    { id: "hihat", name: "Hi-Hat (Ch 3)", layer: 0, cat: "drums", peak: "-14.0 dBFS", phase: "+0.80", desc: "HP 650Hz. Leveling with Pro-C 2 opto.", color: "#10b981" },
    { id: "cymbal", name: "Cymbal (Ch 4)", layer: 0, cat: "drums", peak: "-12.0 dBFS", phase: "+0.70", desc: "HP 450Hz. Timeless 3 1/8D delay. Sidechained to kick.", color: "#10b981" },
    { id: "perc", name: "Perc (Ch 5)", layer: 0, cat: "drums", peak: "-10.0 dBFS", phase: "+0.85", desc: "Wood clicks. Clamped by Neutron Clipper.", color: "#10b981" },
    { id: "sub", name: "Sub Bass (Ch 6)", layer: 0, cat: "drums", peak: "-7.0 dBFS", phase: "+1.00", desc: "30-85Hz Pure Mono. Pro-MB dynamic sidechain ducked by Kick.", color: "#00e5ff" },
    { id: "midbass", name: "Mid Bass (Ch 7)", layer: 0, cat: "basses", peak: "-9.0 dBFS", phase: "+0.85", desc: "480Hz riddim honk. Saturn 2 heavy saturation. Sidechained.", color: "#ff4081" },
    { id: "growl", name: "Growl (Ch 8)", layer: 0, cat: "basses", peak: "-8.0 dBFS", phase: "+0.75", desc: "Formant scream (Volcano 3). Split 70% Mid / 30% Side.", color: "#ff4081" },
    { id: "lead", name: "Lead (Ch 9)", layer: 0, cat: "synths", peak: "-10.0 dBFS", phase: "+0.80", desc: "Laser stabs. HP 250Hz. Timeless 3 dub delay.", color: "#38bdf8" },
    { id: "pad", name: "Pad (Ch 10)", layer: 0, cat: "synths", peak: "-16.0 dBFS", phase: "+0.65", desc: "Atmosphere bed. Sidechain ducked -18dB on drop.", color: "#64748b" },
    { id: "vox", name: "Vocal (Ch 11)", layer: 0, cat: "vox", peak: "-9.0 dBFS", phase: "+0.90", desc: "Ragga chant. Pro-C 2 4:1 ratio. Ducks music beds.", color: "#ec4899" },

    // Layer 1: 8 Mixbuses
    { id: "bus_kick", name: "Kick Bus (20)", layer: 1, cat: "drums", peak: "-5.8 dBFS", desc: "Mono center kick transient channel with clipper.", color: "#ef4444" },
    { id: "bus_snare", name: "Snare Bus (21)", layer: 1, cat: "drums", peak: "-5.8 dBFS", desc: "Snare body sum and side clatter split.", color: "#f59e0b" },
    { id: "bus_perc", name: "Perc Bus (22)", layer: 1, cat: "drums", peak: "-9.0 dBFS", desc: "Tops groove glued with FabFilter Pro-C 2.", color: "#10b981" },
    { id: "bus_sub", name: "Sub Bus (23)", layer: 1, cat: "drums", peak: "-6.8 dBFS", desc: "100% Mono subwoofer rail. Never touches Side Bus.", color: "#00e5ff" },
    { id: "bus_growl", name: "Growl Bus (24)", layer: 1, cat: "basses", peak: "-7.5 dBFS", desc: "Riddim soundwall glued via Saturn 2 warm tube drive.", color: "#ff4081" },
    { id: "bus_lead", name: "Lead Bus (25)", layer: 1, cat: "synths", peak: "-9.5 dBFS", desc: "Lead presence and laser transient management.", color: "#38bdf8" },
    { id: "bus_pad", name: "Pad Bus (26)", layer: 1, cat: "synths", peak: "-15.0 dBFS", desc: "Wide side ambience bed.", color: "#64748b" },
    { id: "bus_vox", name: "Vocal Bus (27)", layer: 1, cat: "vox", peak: "-8.5 dBFS", desc: "Punchy dialogue and hype chant bus.", color: "#ec4899" },

    // Layer 2: 4 Spatial Stem Buses
    { id: "sp_mid", name: "MID BUS (Ch 30)", layer: 2, cat: "spatial", peak: "-4.2 dBFS", desc: "Sum L+R mono core. Pro-Q 3 LP cut 25Hz + Neutron Clipper.", color: "#00e5ff" },
    { id: "sp_side", name: "SIDE BUS (Ch 31)", layer: 2, cat: "spatial", peak: "-8.5 dBFS", desc: "Diff L-R. HP strictly at 135Hz! Zero stereo sub. Saturn 2.", color: "#00ffa3" },
    { id: "sp_left", name: "LEFT BUS (Ch 32)", layer: 2, cat: "spatial", peak: "-5.0 dBFS", desc: "Discrete Left monitoring and transient alignment.", color: "#f59e0b" },
    { id: "sp_right", name: "RIGHT BUS (Ch 33)", layer: 2, cat: "spatial", peak: "-5.0 dBFS", desc: "Discrete Right monitoring for stereo balance.", color: "#f59e0b" },

    // Layer 3: Pre-Master
    { id: "pm_pre", name: "PRE-MASTER (Ch 40)", layer: 3, cat: "master", peak: "-3.5 dBFS", desc: "Pro-MB multiband glue & crest factor containment.", color: "#a855f7" },

    // Layer 4: Master Chain Stages
    { id: "m_q3", name: "1. Pro-Q 3 (Lin Phase)", layer: 4, cat: "master", peak: "-3.2 dBFS", desc: "25Hz low-cut 48dB/oct + 125Hz side HP.", color: "#00e5ff" },
    { id: "m_sat", name: "2. Saturn 2 (Tape)", layer: 4, cat: "master", peak: "-2.8 dBFS", desc: "Warm Tape 1.2dB drive, 30% parallel mix.", color: "#ff9100" },
    { id: "m_clip", name: "3. Neutron Clipper", layer: 4, cat: "master", peak: "-0.8 dBFS", desc: "Shaves 2.4dB of transient peaks without distortion.", color: "#ff1744" },
    { id: "m_c2", name: "4. Pro-C 2 (Glue)", layer: 4, cat: "master", peak: "-0.7 dBFS", desc: "Opto 1.2:1 ratio, 0.5dB gain reduction.", color: "#00ffa3" },
    { id: "m_l2", name: "5. Pro-L 2 (Target)", layer: 4, cat: "master", peak: "-0.1 dBFS", desc: "Aggressive mode, 8x Oversampling, True Peak ON.", color: "#00ffa3" },

    // Layer 5: Output
    { id: "out_final", name: "STREAMING OUT", layer: 5, cat: "master", peak: "-6.0 LUFS", desc: "Hit -6.0 LUFS Integrated at -0.1 dBFS True Peak.", color: "#00ffa3" }
];

const linksData = [
    // Channels to Mixbuses
    { source: "kick", target: "bus_kick", color: "#ef4444", type: "audio" },
    { source: "snare", target: "bus_snare", color: "#f59e0b", type: "audio" },
    { source: "hihat", target: "bus_perc", color: "#10b981", type: "audio" },
    { source: "cymbal", target: "bus_perc", color: "#10b981", type: "audio" },
    { source: "perc", target: "bus_perc", color: "#10b981", type: "audio" },
    { source: "sub", target: "bus_sub", color: "#00e5ff", type: "audio" },
    { source: "midbass", target: "bus_growl", color: "#ff4081", type: "audio" },
    { source: "growl", target: "bus_growl", color: "#ff4081", type: "audio" },
    { source: "lead", target: "bus_lead", color: "#38bdf8", type: "audio" },
    { source: "pad", target: "bus_pad", color: "#64748b", type: "audio" },
    { source: "vox", target: "bus_vox", color: "#ec4899", type: "audio" },

    // Sidechain Connections (Dashed)
    { source: "kick", target: "sub", color: "#ff9100", type: "sidechain" },
    { source: "kick", target: "midbass", color: "#ff9100", type: "sidechain" },
    { source: "snare", target: "growl", color: "#ff9100", type: "sidechain" },
    { source: "vox", target: "pad", color: "#ff9100", type: "sidechain" },

    // Mixbuses to Spatial Stems
    { source: "bus_kick", target: "sp_mid", color: "#ef4444", type: "audio" },
    { source: "bus_sub", target: "sp_mid", color: "#00e5ff", type: "audio" },
    { source: "bus_snare", target: "sp_mid", color: "#f59e0b", type: "audio" },
    { source: "bus_snare", target: "sp_side", color: "#f59e0b", type: "audio" },
    { source: "bus_perc", target: "sp_side", color: "#10b981", type: "audio" },
    { source: "bus_perc", target: "sp_left", color: "#10b981", type: "audio" },
    { source: "bus_perc", target: "sp_right", color: "#10b981", type: "audio" },
    { source: "bus_growl", target: "sp_mid", color: "#ff4081", type: "audio" },
    { source: "bus_growl", target: "sp_side", color: "#ff4081", type: "audio" },
    { source: "bus_lead", target: "sp_mid", color: "#38bdf8", type: "audio" },
    { source: "bus_lead", target: "sp_side", color: "#38bdf8", type: "audio" },
    { source: "bus_pad", target: "sp_side", color: "#64748b", type: "audio" },
    { source: "bus_vox", target: "sp_mid", color: "#ec4899", type: "audio" },
    { source: "bus_vox", target: "sp_side", color: "#ec4899", type: "audio" },

    // Spatial Stems to Pre-Master
    { source: "sp_mid", target: "pm_pre", color: "#00e5ff", type: "audio" },
    { source: "sp_side", target: "pm_pre", color: "#00ffa3", type: "audio" },
    { source: "sp_left", target: "pm_pre", color: "#f59e0b", type: "audio" },
    { source: "sp_right", target: "pm_pre", color: "#f59e0b", type: "audio" },

    // Pre-Master to Master Chain Serial Pipeline
    { source: "pm_pre", target: "m_q3", color: "#a855f7", type: "audio" },
    { source: "m_q3", target: "m_sat", color: "#00e5ff", type: "audio" },
    { source: "m_sat", target: "m_clip", color: "#ff9100", type: "audio" },
    { source: "m_clip", target: "m_c2", color: "#ff1744", type: "audio" },
    { source: "m_c2", target: "m_l2", color: "#00ffa3", type: "audio" },
    { source: "m_l2", target: "out_final", color: "#00ffa3", type: "audio" }
];

const width = 1150;
const height = 650;
const layerX = [30, 220, 420, 620, 780, 1020];

// Compute node positions
const layerCounts = {};
nodesData.forEach(n => {
    layerCounts[n.layer] = (layerCounts[n.layer] || 0) + 1;
});
const layerIndexes = {};
nodesData.forEach(n => {
    const idx = (layerIndexes[n.layer] || 0);
    layerIndexes[n.layer] = idx + 1;
    const total = layerCounts[n.layer];
    n.x = layerX[n.layer];
    const spacing = Math.min(52, (height - 80) / (total + 1));
    n.y = 60 + (idx + 1) * spacing + ((height - 80) - (total * spacing)) / 2;
});

const nodeMap = new Map(nodesData.map(d => [d.id, d]));

const svg = d3.select("#chart-container")
    .append("svg")
    .attr("width", "100%")
    .attr("height", "100%")
    .attr("viewBox", "0 0 " + width + " " + height);

const g = svg.append("g");

const zoom = d3.zoom()
    .scaleExtent([0.5, 3])
    .on("zoom", (event) => g.attr("transform", event.transform));

svg.call(zoom);

// Draw Layer Headers
const headers = ["1. CHANNELS", "2. MIXBUSES", "3. SPATIAL M/S", "4. PRE-MASTER", "5. MASTER CHAIN", "6. OUTPUT"];
headers.forEach((h, i) => {
    g.append("text")
        .attr("x", layerX[i] + 45)
        .attr("y", 35)
        .attr("text-anchor", "middle")
        .attr("fill", "#64748b")
        .attr("font-size", "11px")
        .attr("font-weight", "800")
        .attr("letter-spacing", "0.5px")
        .text(h);
});

// Links generator
const linkGen = d3.linkHorizontal()
    .x(d => d.x)
    .y(d => d.y);

const linkElements = g.selectAll(".link")
    .data(linksData)
    .enter()
    .append("path")
    .attr("class", d => "link " + (d.type === "sidechain" ? "sidechain-link" : ""))
    .attr("d", d => {
        const s = nodeMap.get(d.source);
        const t = nodeMap.get(d.target);
        return linkGen({
            source: { x: s.x + 95, y: s.y + 14 },
            target: { x: t.x, y: t.y + 14 }
        });
    })
    .attr("stroke", d => d.color)
    .attr("stroke-width", d => d.type === "sidechain" ? 2 : 1.5);

// Nodes
const nodeElements = g.selectAll(".node")
    .data(nodesData)
    .enter()
    .append("g")
    .attr("class", "node")
    .attr("transform", function(d) { return "translate(" + d.x + ", " + d.y + ")"; })
    .on("click", (event, d) => onNodeClick(d))
    .on("mouseover", (event, d) => showTooltip(event, d))
    .on("mouseout", () => hideTooltip());

nodeElements.append("rect")
    .attr("width", 95)
    .attr("height", 28)
    .attr("fill", "#141926")
    .attr("stroke", d => d.color);

nodeElements.append("text")
    .attr("x", 8)
    .attr("y", 18)
    .attr("fill", "#f1f5f9")
    .attr("font-size", "10px")
    .attr("font-weight", "600")
    .text(d => d.name);

const tooltip = d3.select("#tooltip");

function showTooltip(event, d) {
    tooltip.style("display", "block")
        .style("left", (event.pageX + 15) + "px")
        .style("top", (event.pageY - 20) + "px")
        .html(
            '<div class="tooltip-title">' + d.name + '</div>' +
            '<div class="tooltip-badge">Peak: ' + d.peak + (d.phase ? ' | Phase: ' + d.phase : '') + '</div>' +
            '<div style="line-height: 1.4;">' + d.desc + '</div>'
        );
}

function hideTooltip() {
    tooltip.style("display", "none");
}

let activeNodeId = null;

function onNodeClick(d) {
    if (activeNodeId === d.id) {
        resetHighlights();
        activeNodeId = null;
        return;
    }
    activeNodeId = d.id;

    // Find upstream and downstream paths
    const connectedNodeIds = new Set([d.id]);
    const connectedLinks = new Set();

    // Traverse downstream
    let queue = [d.id];
    while (queue.length > 0) {
        const curr = queue.shift();
        linksData.forEach(l => {
            if (l.source === curr) {
                connectedLinks.add(l);
                connectedNodeIds.add(l.target);
                queue.push(l.target);
            }
        });
    }

    // Traverse upstream
    queue = [d.id];
    while (queue.length > 0) {
        const curr = queue.shift();
        linksData.forEach(l => {
            if (l.target === curr) {
                connectedLinks.add(l);
                connectedNodeIds.add(l.source);
                queue.push(l.source);
            }
        });
    }

    // Highlight
    linkElements
        .classed("active", l => connectedLinks.has(l))
        .classed("dimmed", l => !connectedLinks.has(l));

    nodeElements.select("rect")
        .attr("fill", n => connectedNodeIds.has(n.id) ? "#1f293d" : "#0e121a")
        .attr("stroke-width", n => connectedNodeIds.has(n.id) ? "2.5px" : "1px");

    // Call Android interface if available
    if (window.AndroidBridge && window.AndroidBridge.onNodeSelected) {
        window.AndroidBridge.onNodeSelected(d.id, d.name, d.desc);
    }
}

function resetHighlights() {
    linkElements.classed("active", false).classed("dimmed", false);
    nodeElements.select("rect")
        .attr("fill", "#141926")
        .attr("stroke-width", "1.5px");
}

function filterGraph(cat) {
    d3.selectAll(".btn-filter").classed("active", false);
    event.target.classList.add("active");

    if (cat === "all") {
        resetHighlights();
        nodeElements.style("opacity", 1);
        linkElements.style("opacity", 1);
        return;
    }

    if (cat === "sidechain") {
        linkElements.style("opacity", l => l.type === "sidechain" ? 1 : 0.05);
        nodeElements.style("opacity", n => ["kick", "sub", "midbass", "snare", "growl", "vox", "pad"].includes(n.id) ? 1 : 0.2);
        return;
    }

    nodeElements.style("opacity", n => (n.cat === cat || n.cat === "master") ? 1 : 0.2);
    linkElements.style("opacity", l => {
        const s = nodeMap.get(l.source);
        const t = nodeMap.get(l.target);
        return (s.cat === cat || t.cat === cat || s.cat === "master" || t.cat === "master") ? 0.8 : 0.05;
    });
}

function zoomIn() {
    svg.transition().duration(300).call(zoom.scaleBy, 1.3);
}
function zoomOut() {
    svg.transition().duration(300).call(zoom.scaleBy, 0.7);
}
function resetZoom() {
    svg.transition().duration(400).call(zoom.transform, d3.zoomIdentity);
}

// Initial center fitting
resetZoom();
</script>
</body>
</html>
        """.trimIndent()
    }
}
