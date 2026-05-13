package com.example.nalla_nudi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms")
data class Term(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val englishTerm: String,
    val kannadaTerm: String,
    val explanation: String,
    val subject: String,
    val subSubject: String,
    val exampleSentence: String,
    val isSaved: Boolean = false
)