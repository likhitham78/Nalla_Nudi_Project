package com.example.nalla_nudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nalla_nudi.model.Term
import com.example.nalla_nudi.ui.viewmodel.TermViewModel

// ── Brand Colours ─────────────────────────────────────────────────
val DarkBlue   = Color(0xFF1A237E)
val MidBlue    = Color(0xFF3949AB)
val Saffron    = Color(0xFFFF6B00)
val LightBg    = Color(0xFFF3F4FF)
val GreenColor = Color(0xFF2E7D32)
val CardWhite  = Color(0xFFFFFFFF)
val GoldColor  = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TermViewModel = hiltViewModel(),
    onNavigateToMyList: () -> Unit,
    onNavigateToFlashcard: () -> Unit,
    onNavigateToSubject: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val wordOfTheDay by viewModel.wordOfTheDay.collectAsState()
    val savedTerms   by viewModel.savedTerms.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "ನಲ್ಲ-ನುಡಿ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            "Bridge Dictionary",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue
                ),
                actions = {
                    IconButton(onClick = onNavigateToMyList) {
                        BadgedBox(
                            badge = {
                                if (savedTerms.isNotEmpty()) {
                                    Badge { Text(savedTerms.size.toString()) }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Bookmarks,
                                contentDescription = "My List",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        containerColor = LightBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Hero Search Banner ────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkBlue, MidBlue)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Search Technical Terms",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    // Fake Search Bar (navigates to Search Screen)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToSearch() },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = DarkBlue)
                            Text(
                                "Search English or ಕನ್ನಡ term...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Word of the Day ───────────────────────────────
            wordOfTheDay?.let { word ->
                WordOfTheDayCard(
                    term   = word,
                    onSave = { viewModel.toggleSaved(word) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Browse by Subject ─────────────────────────────
            Text(
                "Browse by Subject",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Subject Cards Grid
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SubjectCard(
                        title    = "Science",
                        subtitle = "Physics · Chemistry · Biology",
                        icon     = Icons.Default.Science,
                        color    = Color(0xFF1565C0),
                        modifier = Modifier.weight(1f),
                        onClick  = { onNavigateToSubject("Science") }
                    )
                    SubjectCard(
                        title    = "Mathematics",
                        subtitle = "Algebra · Geometry · Stats",
                        icon     = Icons.Default.Calculate,
                        color    = Color(0xFF6A1B9A),
                        modifier = Modifier.weight(1f),
                        onClick  = { onNavigateToSubject("Mathematics") }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SubjectCard(
                        title    = "Commerce",
                        subtitle = "Economics · Accounting",
                        icon     = Icons.Default.TrendingUp,
                        color    = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f),
                        onClick  = { onNavigateToSubject("Commerce") }
                    )
                    SubjectCard(
                        title    = "All Terms",
                        subtitle = "Browse everything",
                        icon     = Icons.Default.MenuBook,
                        color    = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick  = { onNavigateToSubject("All") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Quick Actions ─────────────────────────────────
            Text(
                "Quick Actions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title    = "My List",
                    subtitle = "${savedTerms.size} saved",
                    icon     = Icons.Default.Bookmark,
                    color    = Saffron,
                    modifier = Modifier.weight(1f),
                    onClick  = onNavigateToMyList
                )
                QuickActionCard(
                    title    = "Flashcards",
                    subtitle = "Revise now",
                    icon     = Icons.Default.Style,
                    color    = GreenColor,
                    modifier = Modifier.weight(1f),
                    onClick  = onNavigateToFlashcard
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Subject Card ──────────────────────────────────────────────────
@Composable
fun SubjectCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title,    fontSize = 14.sp, fontWeight = FontWeight.Bold,   color = Color.White)
                Text(subtitle, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

// ── Quick Action Card ─────────────────────────────────────────────
@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(title,    fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

// ── Word of the Day Card ──────────────────────────────────────────
@Composable
fun WordOfTheDayCard(
    term: Term,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = DarkBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Word of the Day", fontSize = 12.sp, color = GoldColor, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                    Icon(
                        if (term.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (term.isSaved) GoldColor else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(term.englishTerm, fontSize = 22.sp, fontWeight = FontWeight.Bold,   color = Color.White)
            Text(term.kannadaTerm, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFFB3E5FC))
            Spacer(modifier = Modifier.height(6.dp))
            Text(term.explanation, fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "e.g. ${term.exampleSentence}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.65f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

// ── Term Card (reused across screens) ────────────────────────────
@Composable
fun TermCard(
    term: Term,
    onSpeak: () -> Unit,
    onSave: () -> Unit,
    onClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape  = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(term.englishTerm, fontSize = 17.sp, fontWeight = FontWeight.Bold,   color = DarkBlue)
                    Text(term.kannadaTerm, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = GreenColor)
                }
                Row {
                    IconButton(onClick = onSpeak, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Speak", tint = DarkBlue, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onSave, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (term.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (term.isSaved) Saffron else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = LightBg)
                Spacer(modifier = Modifier.height(8.dp))
                Text(term.explanation, fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("e.g. ${term.exampleSentence}", fontSize = 12.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SubjectChip(term.subject)
                    SubjectChip(term.subSubject)
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tap to expand", fontSize = 11.sp, color = Color.LightGray)
            }
        }
    }
}

// ── Subject Chip ──────────────────────────────────────────────────
@Composable
fun SubjectChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = LightBg
    ) {
        Text(
            label,
            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            fontSize   = 11.sp,
            color      = DarkBlue,
            fontWeight = FontWeight.Medium
        )
    }
}