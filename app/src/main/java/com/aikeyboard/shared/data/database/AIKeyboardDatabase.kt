package com.aikeyboard.shared.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aikeyboard.dictionary.data.DictionaryDao
import com.aikeyboard.dictionary.data.DictionaryEntry
import com.aikeyboard.clipboard.data.ClipboardDao
import com.aikeyboard.clipboard.data.ClipboardEntry

@Database(
    entities = [DictionaryEntry::class, ClipboardEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AIKeyboardDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
    abstract fun clipboardDao(): ClipboardDao
}












