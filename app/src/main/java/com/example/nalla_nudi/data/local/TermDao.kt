package com.example.nalla_nudi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nalla_nudi.model.Term
import kotlinx.coroutines.flow.Flow

@Dao
interface TermDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(terms: List<Term>)

    @Query("SELECT * FROM terms WHERE englishTerm LIKE '%' || :query || '%' OR kannadaTerm LIKE '%' || :query || '%'")
    fun searchTerms(query: String): Flow<List<Term>>

    @Query("SELECT * FROM terms WHERE subject = :subject")
    fun getTermsBySubject(subject: String): Flow<List<Term>>

    @Query("SELECT * FROM terms")
    fun getAllTerms(): Flow<List<Term>>

    @Query("SELECT * FROM terms WHERE isSaved = 1")
    fun getSavedTerms(): Flow<List<Term>>

    @Query("UPDATE terms SET isSaved = :isSaved WHERE id = :termId")
    suspend fun updateSavedStatus(termId: Int, isSaved: Boolean)

    @Query("SELECT * FROM terms ORDER BY RANDOM() LIMIT 1")
    suspend fun getWordOfTheDay(): Term?

    @Query("SELECT COUNT(*) FROM terms")
    suspend fun getTermCount(): Int
}