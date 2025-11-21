package com.aikeyboard.clipboard.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface ClipboardDao {
    @Query("SELECT * FROM clipboard ORDER BY isPinned DESC, timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 50): Flow<List<ClipboardEntry>>

    @Query("SELECT * FROM clipboard WHERE isPinned = 1 ORDER BY timestamp DESC")
    fun getPinned(): Flow<List<ClipboardEntry>>

    @Insert
    suspend fun insert(entry: ClipboardEntry): Long

    @Update
    suspend fun update(entry: ClipboardEntry)

    @Delete
    suspend fun delete(entry: ClipboardEntry)

    @Query("DELETE FROM clipboard WHERE isPinned = 0 AND timestamp < :before")
    suspend fun deleteOld(before: Instant)

    @Query("DELETE FROM clipboard")
    suspend fun clearAll()
}












