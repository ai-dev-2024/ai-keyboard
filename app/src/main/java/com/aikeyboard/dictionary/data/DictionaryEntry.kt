package com.aikeyboard.dictionary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class DictionaryEntry(
    @PrimaryKey
    val word: String,
    val frequency: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)












