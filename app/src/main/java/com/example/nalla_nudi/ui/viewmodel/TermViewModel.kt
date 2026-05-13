package com.example.nalla_nudi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nalla_nudi.data.repository.TermRepository
import com.example.nalla_nudi.model.Term
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermViewModel @Inject constructor(
    private val repository: TermRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Term>>(emptyList())
    val searchResults: StateFlow<List<Term>> = _searchResults

    private val _savedTerms = MutableStateFlow<List<Term>>(emptyList())
    val savedTerms: StateFlow<List<Term>> = _savedTerms

    private val _wordOfTheDay = MutableStateFlow<Term?>(null)
    val wordOfTheDay: StateFlow<Term?> = _wordOfTheDay

    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject: StateFlow<String> = _selectedSubject

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadWordOfTheDay()
        loadSavedTerms()
        searchTerms("")
    }

    fun searchTerms(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (_selectedSubject.value == "All") {
                repository.searchTerms(query).collectLatest {
                    _searchResults.value = it
                }
            } else {
                repository.getTermsBySubject(_selectedSubject.value).collectLatest { terms ->
                    _searchResults.value = if (query.isEmpty()) terms
                    else terms.filter {
                        it.englishTerm.contains(query, ignoreCase = true) ||
                                it.kannadaTerm.contains(query, ignoreCase = true)
                    }
                }
            }
        }
    }

    fun selectSubject(subject: String) {
        _selectedSubject.value = subject
        searchTerms(_searchQuery.value)
    }

    fun toggleSaved(term: Term) {
        viewModelScope.launch {
            repository.updateSavedStatus(term.id, !term.isSaved)
            searchTerms(_searchQuery.value)
            loadSavedTerms()
        }
    }

    private fun loadSavedTerms() {
        viewModelScope.launch {
            repository.getSavedTerms().collectLatest {
                _savedTerms.value = it
            }
        }
    }

    private fun loadWordOfTheDay() {
        viewModelScope.launch {
            _wordOfTheDay.value = repository.getWordOfTheDay()
        }
    }
}