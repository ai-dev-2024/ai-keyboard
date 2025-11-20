package com.aikeyboard.clipboard.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "clipboard")
data class ClipboardEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val timestamp: Instant = Instant.now(),
    val isPinned: Boolean = false
)

