package com.aikeyboard.dictionary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary WHERE word LIKE :prefix || '%' ORDER BY frequency DESC LIMIT :limit")
    fun getSuggestions(prefix: String, limit: Int = 10): Flow<List<DictionaryEntry>>

    @Query("SELECT * FROM dictionary WHERE word = :word")
    suspend fun getWord(word: String): DictionaryEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DictionaryEntry)

    @Update
    suspend fun update(entry: DictionaryEntry)

    @Delete
    suspend fun delete(entry: DictionaryEntry)

    @Query("DELETE FROM dictionary")
    suspend fun clearAll()
}












