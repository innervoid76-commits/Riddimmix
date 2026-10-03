package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.D3SignalFlowHtml
import com.example.ui.theme.*

class D3Bridge(private val onNodeSelectedCallback: (String, String, String) -> Unit) {
    @JavascriptInterface
    fun onNodeSelected(id: String, name: String, desc: String) {
        onNodeSelectedCallback(id, name, desc)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun D3SignalFlowWebView(
    onNodeSelected: (id: String, name: String, desc: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNodeName by remember { mutableStateOf<String?>(null) }
    var selectedNodeDesc by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
    ) {
        // D3 Chart Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF161C2C))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "D3.JS INTERACTIVE SIGNAL FLOW ENGINE",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "Pinch/Pan • Tap Node",
                color = NeonCyan,
                fontSize = 10.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
        }

        // Embedded Android WebView running D3
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .testTag("d3_webview_container")
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = false
                        setBackgroundColor(android.graphics.Color.parseColor("#0C0F17"))
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()

                        addJavascriptInterface(
                            D3Bridge { id, name, desc ->
                                selectedNodeName = name
                                selectedNodeDesc = desc
                                onNodeSelected(id, name, desc)
                            },
                            "AndroidBridge"
                        )

                        val d3Html = D3SignalFlowHtml.buildD3Html()
                        loadDataWithBaseURL("https://d3js.org", d3Html, "text/html", "UTF-8", null)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Selected Node Callout if tapped
        if (selectedNodeName != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant)
                    .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(0.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = selectedNodeName ?: "",
                        color = NeonGreen,
                        fontSize = 12.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        text = selectedNodeDesc ?: "",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
