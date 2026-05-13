package com.example.nalla_nudi.data.repository

import com.example.nalla_nudi.data.local.TermDao
import com.example.nalla_nudi.model.Term
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermRepository @Inject constructor(
    private val termDao: TermDao
) {
    fun searchTerms(query: String): Flow<List<Term>> {
        return termDao.searchTerms(query)
    }

    fun getAllTerms(): Flow<List<Term>> {
        return termDao.getAllTerms()
    }

    fun getTermsBySubject(subject: String): Flow<List<Term>> {
        return termDao.getTermsBySubject(subject)
    }

    fun getSavedTerms(): Flow<List<Term>> {
        return termDao.getSavedTerms()
    }

    suspend fun updateSavedStatus(termId: Int, isSaved: Boolean) {
        termDao.updateSavedStatus(termId, isSaved)
    }

    suspend fun getWordOfTheDay(): Term? {
        return termDao.getWordOfTheDay()
    }
}