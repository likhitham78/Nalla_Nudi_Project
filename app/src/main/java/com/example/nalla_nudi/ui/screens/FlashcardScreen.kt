package com.example.nalla_nudi.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nalla_nudi.model.Term
import com.example.nalla_nudi.ui.viewmodel.TermViewModel
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    viewModel: TermViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val savedTerms by viewModel.savedTerms.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }


    var learntCount by rememberSaveable { mutableStateOf(0) }
    var reviseCount by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Flashcard Revision",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            "${currentIndex + 1} of ${savedTerms.size}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue
                )
            )
        },
        containerColor = LightBg
    ) { padding ->

        if (savedTerms.isEmpty()) {
            // ── Empty State ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Style,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Text(
                        "No words to revise!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        "Save some words to My List\nto start flashcard revision.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Go Back")
                    }
                }
            }
        } else {
            val currentTerm = savedTerms[currentIndex.coerceIn(0, savedTerms.size - 1)]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── Progress Bar ─────────────────────────────────
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / savedTerms.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Saffron,
                    trackColor = Color.LightGray
                )

                // ── Stats Row ────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatChip(
                        label = "Learnt",
                        count = learntCount,
                        color = GreenColor
                    )
                    StatChip(
                        label = "Revise Again",
                        count = reviseCount,
                        color = Saffron
                    )
                }

                // ── Flashcard ────────────────────────────────────
                FlashCard(
                    term = currentTerm,
                    isFlipped = isFlipped,
                    onFlip = { isFlipped = !isFlipped }
                )

                Text(
                    "Tap card to flip",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                // ── Action Buttons ───────────────────────────────
                if (isFlipped) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Revise Again
                        Button(
                            onClick = {
                                reviseCount++
                                isFlipped = false
                                if (currentIndex < savedTerms.size - 1) {
                                    currentIndex++
                                } else {
                                    currentIndex = 0
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC62828)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Revise Again", fontWeight = FontWeight.Bold)
                        }

                        // Learnt
                        Button(
                            onClick = {
                                learntCount++
                                isFlipped = false
                                if (currentIndex < savedTerms.size - 1) {
                                    currentIndex++
                                } else {
                                    currentIndex = 0
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Learnt!", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // ── Navigation ───────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) {
                                currentIndex--
                                isFlipped = false
                            }
                        },
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Previous",
                            tint = if (currentIndex > 0) DarkBlue else Color.LightGray
                        )
                    }
                    IconButton(
                        onClick = {
                            if (currentIndex < savedTerms.size - 1) {
                                currentIndex++
                                isFlipped = false
                            }
                        },
                        enabled = currentIndex < savedTerms.size - 1
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = if (currentIndex < savedTerms.size - 1) DarkBlue else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

// ── Flashcard Composable ──────────────────────────────────────────
@Composable
fun FlashCard(
    term: Term,
    isFlipped: Boolean,
    onFlip: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flip"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .graphicsLayer { rotationY = rotation }
            .clickable { onFlip() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rotation <= 90f) DarkBlue else CardWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // ── Front: English Term ──────────────────────────
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "English",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        term.englishTerm,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Saffron.copy(alpha = 0.8f)
                    ) {
                        Text(
                            term.subject,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // ── Back: Kannada Meaning ────────────────────────
                Column(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "ಕನ್ನಡ",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        term.kannadaTerm,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenColor,
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(color = LightBg)
                    Text(
                        term.explanation,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "e.g. ${term.exampleSentence}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

// ── Stat Chip ─────────────────────────────────────────────────────
@Composable
fun StatChip(label: String, count: Int, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                fontSize = 13.sp,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
