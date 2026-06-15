package com.example.quizmaster.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmaster.ui.components.GlowCircle
import com.example.quizmaster.ui.theme.QuizMasterTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF070B1A),
                        Color(0xFF0B1024),
                        Color(0xFF050816)
                    )
                )
            )
    ) {
        // Decorative background elements
        GlowCircle(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-60).dp, y = (-20).dp)
                .scale(1.2f)
        )

        GlowCircle(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo area
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Login,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quiz Master",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Sign in to continue",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Glassmorphic Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
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
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email Address", color = Color.White.copy(alpha = 0.3f)) },
                        leadingIcon = { Icon(Icons.Rounded.Email, null, tint = Color.White.copy(alpha = 0.5f)) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF60A5FA),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Password", color = Color.White.copy(alpha = 0.3f)) },
                        leadingIcon = { Icon(Icons.Rounded.Lock, null, tint = Color.White.copy(alpha = 0.5f)) },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF60A5FA),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFF60A5FA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = Color(0xFF60A5FA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    QuizMasterTheme {
        LoginScreen(onLoginClick = {})
    }
}
