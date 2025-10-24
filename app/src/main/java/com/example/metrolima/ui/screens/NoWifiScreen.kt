package com.example.metrolima.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metrolima.ui.Language

@Composable
fun NoWifiScreen(
    language: Language,
    onRetry: (() -> Unit)? = null
) {
    val strings = when (language) {
        Language.SPANISH -> NoWifiStrings(
            title = "Sin conexión a internet",
            subtitle = "Verifica tu conexión y vuelve a intentarlo.",
            retry = "Reintentar"
        )
        Language.ENGLISH -> NoWifiStrings(
            title = "No internet connection",
            subtitle = "Check your connection and try again.",
            retry = "Retry"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = strings.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = strings.subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { onRetry?.invoke() },
            enabled = onRetry != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = strings.retry)
        }
    }
}

data class NoWifiStrings(
    val title: String,
    val subtitle: String,
    val retry: String
)
