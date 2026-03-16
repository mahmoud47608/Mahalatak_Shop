package com.aait.base.common.screens.general

import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.aait.base.R
import com.aait.base.ui.theme.PaddingDimensions


@Composable
fun GeneralScreenContent(uiState: GeneralState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingDimensions.high),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = uiState.image,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "alkarashi Logo",
                contentScale = ContentScale.Fit
            )
        }

        // Content in WebView
        uiState.content?.let {
            val rtlHtml = """
                <!DOCTYPE html>
                <html dir="rtl">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            direction: rtl;
                            text-align: right;
                            font-size: 16px;
                            line-height: 1.6;
                            margin: 16px;
                            padding: 0;
                            font-family: 'IBM Plex Sans Arabic', Arial, sans-serif;
                        }
                        p, div, span, h1, h2, h3, h4, h5, h6 {
                            direction: rtl !important;
                            text-align: right !important;
                        }
                        img {
                            max-width: 100%;
                            height: auto;
                        }
                    </style>
                </head>
                <body>
                    $it
                </body>
                </html>
            """.trimIndent()

            HtmlWebView(
                html = it,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = PaddingDimensions.medium)
            )
        }
    }
}

@Composable
fun HtmlWebView(
    html: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                // WebView settings
                settings.apply {
                    javaScriptEnabled = false
                    domStorageEnabled = true
                    defaultTextEncodingName = "utf-8"
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    setSupportZoom(false)
                    builtInZoomControls = false
                    displayZoomControls = false
                }

                // Set WebViewClient to handle page loading
                webViewClient = WebViewClient()

                // Set background to transparent
                setBackgroundColor(Color.TRANSPARENT)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                null,
                html,
                "text/html; charset=utf-8",
                "utf-8",
                null
            )
        }
    )
}

