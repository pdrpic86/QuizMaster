package com.example.quizmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quizmaster.ui.theme.QuizMasterTheme
import com.example.quizmaster.ui.util.AdaptiveDensity







































private const val DEBUG_UI_LAB = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
        if (DEBUG_UI_LAB) {
            MainActivityUiTestLab()
        } else {

            QuizMasterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizMasterApp()
                }
            }
        
        }
    }
    }
}



@Composable
private fun MainActivityUiTestLab() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF081412)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF07110F),
                                Color(0xFF101827),
                                Color(0xFF070B12)
                            )
                        )
                    )
                    .safeDrawingPadding()
                    .padding(14.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    DebugLabHeader()

                    DebugCleanThemeToggle()

                    DebugDashboardCardsVariantA()

                    DebugDashboardCardsVariantB()

                    Text(
                        text = "Ako ti se sviđa varijanta A ili B, prebaci se u stvarni HomeScreen.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DebugLabHeader() {
    val config = LocalConfiguration.current
    val profile = when {
        config.screenWidthDp <= 360 || config.screenHeightDp <= 720 -> "SMALL"
        config.screenWidthDp <= 411 || config.screenHeightDp <= 891 -> "NORMAL"
        else -> "LARGE"
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "QuizMaster UI Lab",
            fontSize = 26.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )

        Text(
            text = "$profile screen • ${config.screenWidthDp}dp x ${config.screenHeightDp}dp",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.62f)
        )
    }
}

@Composable
private fun DebugCleanThemeToggle() {
    var checked by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.07f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Theme toggle bez žute točkice",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = "Čist pill/switch, bez dekorativnog prišta gore desno.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.58f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (checked) "Dark" else "Light",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5CF6)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }
    }
}

@Composable
private fun DebugDashboardCardsVariantA() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Variant A: 2x2 grid, najčišće za mobitel",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.76f)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DebugDashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = "▦",
                    title = "Categories",
                    subtitle = "4 categories",
                    accent = Color(0xFF8B5CF6)
                )

                DebugDashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = "▥",
                    title = "Difficulty",
                    subtitle = "4 levels",
                    accent = Color(0xFF22C55E)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DebugDashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = "?",
                    title = "Questions",
                    subtitle = "880 loaded",
                    accent = Color(0xFF38BDF8)
                )

                DebugDashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = "★",
                    title = "Stats",
                    subtitle = "Coming soon",
                    accent = Color(0xFFF59E0B)
                )
            }
        }
    }
}

@Composable
private fun DebugDashboardCardsVariantB() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Variant B: horizontal mini cards, za dashboard feeling",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.76f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            DebugMiniDashboardCard(
                modifier = Modifier.weight(1f),
                icon = "▦",
                title = "Cat.",
                value = "4",
                accent = Color(0xFF8B5CF6)
            )

            DebugMiniDashboardCard(
                modifier = Modifier.weight(1f),
                icon = "?",
                title = "Questions",
                value = "880",
                accent = Color(0xFF38BDF8)
            )

            DebugMiniDashboardCard(
                modifier = Modifier.weight(1f),
                icon = "★",
                title = "Stats",
                value = "Soon",
                accent = Color(0xFFF59E0B)
            )
        }
    }
}

@Composable
private fun DebugDashboardCard(
    modifier: Modifier,
    icon: String,
    title: String,
    subtitle: String,
    accent: Color
) {
    Card(
        modifier = modifier.height(94.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.075f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 0.7.dp,
                    color = accent.copy(alpha = 0.34f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(11.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = accent
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        color = Color.White.copy(alpha = 0.58f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun DebugMiniDashboardCard(
    modifier: Modifier,
    icon: String,
    title: String,
    value: String,
    accent: Color
) {
    Card(
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.075f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 0.7.dp,
                    color = accent.copy(alpha = 0.34f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(9.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = accent
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = value,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = title,
                fontSize = 9.sp,
                lineHeight = 10.sp,
                color = Color.White.copy(alpha = 0.58f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



