package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.domain.models.Collector
import com.tsdc.vinilos.ui.shared.constants.UiTestTags

@Preview(showBackground = true)
@Composable
fun CollectorItemPreview() {
    CollectorItem(
        collector = Collector(
            id = 1,
            name = "Mateo Rivera",
            telephone = "3001112233",
            email = "mateo@mail.com"
        ),
        albumCount = 142
    )
}

@Composable
fun CollectorItem(collector: Collector, albumCount: Int = 0) {
    Card(
        modifier = Modifier
            .testTag(UiTestTags.COLLECTOR_LIST_ITEM)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EAFF)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "",
                        contentDescription = collector.name,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = collector.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Text(
                        text = "Coleccionista",
                        fontSize = 12.sp,
                        color = Color(0xFF888888),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = albumCount.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B35BD)
            )
        }
    }
}
