package com.example.nalla_nudi.di

import android.content.Context
import androidx.room.Room
import com.example.nalla_nudi.data.local.NallaNudiDatabase
import com.example.nalla_nudi.data.local.TermDao
import com.example.nalla_nudi.data.repository.TermRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NallaNudiDatabase {
        return NallaNudiDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTermDao(
        database: NallaNudiDatabase
    ): TermDao {
        return database.termDao()
    }

    @Provides
    @Singleton
    fun provideTermRepository(
        termDao: TermDao
    ): TermRepository {
        return TermRepository(termDao)
    }
}