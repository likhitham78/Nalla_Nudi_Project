package com.example.nalla_nudi.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nalla_nudi.ui.viewmodel.TermViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermDetailScreen(
    termId: Int,
    viewModel: TermViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context      = LocalContext.current
    val searchResults by viewModel.searchResults.collectAsState()
    val term          = searchResults.find { it.id == termId }

    // TTS setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
    }
    DisposableEffect(Unit) { onDispose { tts?.shutdown() } }

    // Subject color
    val subjectColor = when (term?.subject) {
        "Science"     -> Color(0xFF1565C0)
        "Mathematics" -> Color(0xFF6A1B9A)
        "Commerce"    -> Color(0xFF2E7D32)
        else          -> DarkBlue
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Term Details",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = Color.White
                    )
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
                actions = {
                    term?.let {
                        IconButton(onClick = { viewModel.toggleSaved(it) }) {
                            Icon(
                                if (it.isSaved) Icons.Default.Bookmark
                                else Icons.Default.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (it.isSaved) GoldColor else Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = subjectColor
                )
            )
        },
        containerColor = LightBg
    ) { padding ->

        if (term == null) {
            Box(
                modifier         = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Term not found", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Hero Banner ──────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(subjectColor, subjectColor.copy(alpha = 0.8f))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // English Term
                        Text(
                            term.englishTerm,
                            fontSize   = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White,
                            textAlign  = TextAlign.Center
                        )

                        // Kannada Term
                        Text(
                            term.kannadaTerm,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color      = Color(0xFFB3E5FC),
                            textAlign  = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Subject Tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    term.subject,
                                    modifier   = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontSize   = 12.sp,
                                    color      = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    term.subSubject,
                                    modifier   = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontSize   = 12.sp,
                                    color      = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Pronunciation Button
                        Button(
                            onClick = {
                                isSpeaking = true
                                tts?.speak(
                                    term.englishTerm,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                                isSpeaking = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Hear Pronunciation",
                                color      = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ── Content Cards ────────────────────────────
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Kannada Explanation
                    DetailCard(
                        title   = "ಕನ್ನಡ ವಿವರಣೆ (Explanation)",
                        content = term.explanation,
                        icon    = Icons.Default.Info,
                        color   = subjectColor
                    )

                    // Example Sentence
                    DetailCard(
                        title   = "Example Sentence",
                        content = term.exampleSentence,
                        icon    = Icons.Default.FormatQuote,
                        color   = Saffron,
                        isItalic = true
                    )

                    // How to Remember
                    DetailCard(
                        title   = "How to Remember",
                        content = "Associate '${term.englishTerm}' with '${term.kannadaTerm}'. " +
                                "Practice saying the English word out loud using the pronunciation guide above.",
                        icon    = Icons.Default.Lightbulb,
                        color   = GoldColor
                    )

                    // Save Button
                    Button(
                        onClick  = { viewModel.toggleSaved(term) },
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = if (term.isSaved) Color.Gray else subjectColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            if (term.isSaved) Icons.Default.BookmarkRemove
                            else Icons.Default.BookmarkAdd,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (term.isSaved) "Remove from My List"
                            else "Save to My List",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ── Detail Card ───────────────────────────────────────────────────
@Composable
fun DetailCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isItalic: Boolean = false
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint     = color,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    title,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = color
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                content,
                fontSize  = 15.sp,
                color     = Color.DarkGray,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                lineHeight = 22.sp
            )
        }
    }
}

