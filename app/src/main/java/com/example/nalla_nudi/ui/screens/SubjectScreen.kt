package com.example.nalla_nudi.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nalla_nudi.ui.viewmodel.TermViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    subject: String,
    viewModel: TermViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val context       = LocalContext.current
    val searchResults by viewModel.searchResults.collectAsState()

    // TTS setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        viewModel.selectSubject(subject)
    }
    DisposableEffect(Unit) { onDispose { tts?.shutdown() } }

    // Sub-subjects based on subject
    val subSubjects = remember(subject) {
        when (subject) {
            "Science"     -> listOf("All", "Physics", "Chemistry", "Biology")
            "Mathematics" -> listOf("All", "Algebra", "Geometry", "Statistics")
            "Commerce"    -> listOf("All", "Economics", "Accounting")
            else          -> listOf("All")
        }
    }

    // Subject color
    val subjectColor = when (subject) {
        "Science"     -> Color(0xFF1565C0)
        "Mathematics" -> Color(0xFF6A1B9A)
        "Commerce"    -> Color(0xFF2E7D32)
        else          -> DarkBlue
    }

    var selectedSubSub by remember { mutableStateOf("All") }

    val filteredTerms = remember(searchResults, selectedSubSub) {
        if (selectedSubSub == "All") searchResults
        else searchResults.filter { it.subSubject == selectedSubSub }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            subject,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp,
                            color      = Color.White
                        )
                        Text(
                            "${filteredTerms.size} terms",
                            fontSize = 12.sp,
                            color    = Color.White.copy(alpha = 0.8f)
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
                    containerColor = subjectColor
                )
            )
        },
        containerColor = LightBg
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── Subject Banner ───────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(subjectColor, subjectColor.copy(alpha = 0.7f))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            subject,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                        Text(
                            "Tap any term to see full details",
                            fontSize = 13.sp,
                            color    = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // ── Sub-Subject Filter ───────────────────────────
            if (subSubjects.size > 1) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subSubjects) { sub ->
                            FilterChip(
                                selected = selectedSubSub == sub,
                                onClick  = { selectedSubSub = sub },
                                label    = { Text(sub, fontWeight = FontWeight.Medium) },
                                colors   = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = subjectColor,
                                    selectedLabelColor     = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // ── Terms List ───────────────────────────────────
            items(filteredTerms) { term ->
                TermCard(
                    term    = term,
                    onSpeak = { tts?.speak(term.englishTerm, TextToSpeech.QUEUE_FLUSH, null, null) },
                    onSave  = { viewModel.toggleSaved(term) },
                    onClick = { onNavigateToDetail(term.id) }
                )
            }
        }
    }
}

