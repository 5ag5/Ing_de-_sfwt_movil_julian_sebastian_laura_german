package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.CollectorDetailViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectorDetailScreenPreview() {
    VinilosTheme {
        CollectorDetailContent(
            collector = Collector(
                id = 1,
                name = "Mateo Rivera",
                telephone = "3001112233",
                email = "mateo@mail.com"
            ),
            isLoading = false,
            error = null,
            onRetry = {},
            onBack = {}
        )
    }
}

@Composable
fun CollectorDetailScreen(
    viewModel: CollectorDetailViewModel,
    collectorId: Int,
    onBack: () -> Unit
) {
    val collector by viewModel.collector.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(collectorId) {
        viewModel.loadCollector(collectorId)
    }

    CollectorDetailContent(
        collector = collector,
        isLoading = isLoading,
        error = error,
        onRetry = { viewModel.loadCollector(collectorId) },
        onBack = onBack
    )
}

@Composable
private fun CollectorDetailContent(
    collector: Collector?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(UiTestTags.COLLECTOR_DETAIL_ROOT)
            .fillMaxSize()
            .background(Color(0xFFF2F2F6))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF2B35BD)
                )
            }
            Text(
                text = "Collectors",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191B24)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF2B35BD))
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (error != null) {
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = Color(0xFFB00020),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reintentar", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            val currentCollector = collector ?: Collector(
                id = -1,
                name = "Cargando...",
                telephone = "-",
                email = "-"
            )

            Text(
                text = currentCollector.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A2E),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            Text(
                text = "Coleccionista",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E4F2)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "INFORMACIÓN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF2B35BD),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    CollectorDetailRow(label = "ID", value = currentCollector.id.toString())
                    CollectorDetailRow(label = "Nombre", value = currentCollector.name)
                    CollectorDetailRow(label = "Teléfono", value = currentCollector.telephone)
                    CollectorDetailRow(label = "Correo electrónico", value = currentCollector.email)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Volver", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CollectorDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF4D4D4D)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E)
        )
    }
}
