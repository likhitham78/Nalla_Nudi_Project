package com.example.nalla_nudi.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nalla_nudi.ui.viewmodel.TermViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: TermViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val context       = LocalContext.current
    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery   by viewModel.searchQuery.collectAsState()
    val focusRequester = remember { FocusRequester() }

    // TTS setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        focusRequester.requestFocus()
    }
    DisposableEffect(Unit) { onDispose { tts?.shutdown() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = { viewModel.searchTerms(it) },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder   = { Text("Search English or ಕನ್ನಡ...", fontSize = 14.sp) },
                        trailingIcon  = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.searchTerms("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape  = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = Color.White,
                            unfocusedBorderColor    = Color.White.copy(alpha = 0.5f),
                            focusedContainerColor   = Color.White.copy(alpha = 0.15f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.10f),
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White,
                            cursorColor             = Color.White
                        ),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue)
            )
        },
        containerColor = LightBg
    ) { padding ->
        LazyColumn(
            modifier        = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding  = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── Results Count ────────────────────────────────
            item {
                Text(
                    if (searchQuery.isEmpty()) "Type to search terms..."
                    else "${searchResults.size} results for \"$searchQuery\"",
                    fontSize   = 12.sp,
                    color      = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            // ── Empty State ──────────────────────────────────
            if (searchQuery.isNotEmpty() && searchResults.isEmpty()) {
                item {
                    Box(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment  = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint     = Color.LightGray
                            )
                            Text(
                                "No results found for\n\"$searchQuery\"",
                                fontSize   = 16.sp,
                                color      = Color.Gray,
                                textAlign  = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Try searching in English or ಕನ್ನಡ",
                                fontSize  = 13.sp,
                                color     = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // ── Term Cards ───────────────────────────────────
            items(searchResults) { term ->
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

