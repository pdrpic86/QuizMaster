package com.example.quizmaster.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmaster.ui.theme.QuizMasterTheme

@Composable
fun GlassCardSample() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF050816)) // Background simulator
    ) {
        // Background Glow
        Box(
            modifier = Modifier
                .offset(x = 100.dp, y = (-20).dp)
                .size(150.dp)
                .blur(50.dp)
                .background(Color(0xFF2563EB).copy(alpha = 0.3f), CircleShape)
        )

        // Glass Surface
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 0.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(32.dp)
                ),
            color = Color.White.copy(alpha = 0.05f),
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("GLASSMORPHIC", color = Color(0xFF60A5FA), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Premium Look", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    Text("M3 Tonal Surface", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                }
                Icon(Icons.Rounded.EmojiEvents, null, modifier = Modifier.size(60.dp), tint = Color(0xFF2563EB))
            }
        }
    }
}

@Composable
fun InteractiveButtonSample(state: String) {
    val (containerColor, contentColor, icon) = when (state) {
        "Correct" -> Triple(Color(0xFF064E3B), Color(0xFF34D399), Icons.Rounded.CheckCircle)
        "Wrong" -> Triple(Color(0xFF7F1D1D), Color(0xFFF87171), Icons.Rounded.Close)
        else -> Triple(Color(0xFF1E293B), Color.White, null)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, contentColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sample Answer Option", modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Medium)
            if (icon != null) {
                Icon(icon, null, tint = contentColor, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Preview
@Composable
fun DesignGalleryPreview() {
    QuizMasterTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFF050816))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Visual Samples", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            
            Text("1. Glassmorphism", color = Color.White.copy(alpha = 0.5f))
            GlassCardSample()

            Text("2. Answer States", color = Color.White.copy(alpha = 0.5f))
            InteractiveButtonSample("Normal")
            InteractiveButtonSample("Correct")
            InteractiveButtonSample("Wrong")
            
            Text("3. Neon Timer Warning", color = Color.White.copy(alpha = 0.5f))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(1.1f)
                    .background(Color(0xFFEF4444).copy(alpha = 0.1f), CircleShape)
                    .border(2.dp, Color(0xFFEF4444), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("04", color = Color(0xFFEF4444), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            }
        }
    }
}
