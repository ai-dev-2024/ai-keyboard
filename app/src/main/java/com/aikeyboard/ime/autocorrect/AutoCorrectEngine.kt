package com.aikeyboard.ime.autocorrect

import com.aikeyboard.dictionary.data.DictionaryDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoCorrectEngine @Inject constructor(
    private val dictionaryDao: DictionaryDao
) {
    suspend fun getSuggestions(prefix: String, limit: Int = 10): List<String> {
        return dictionaryDao.getSuggestions(prefix, limit).first().map { it.word }
    }

    suspend fun addWord(word: String) {
        val existing = dictionaryDao.getWord(word)
        if (existing != null) {
            dictionaryDao.update(existing.copy(frequency = existing.frequency + 1))
        } else {
            dictionaryDao.insert(com.aikeyboard.dictionary.data.DictionaryEntry(word))
        }
    }

    fun shouldAutoCorrect(word: String): Boolean {
        // Simple heuristic: auto-correct if word is not in dictionary
        // In production, use more sophisticated algorithms
        return word.length > 2 && !word.all { it.isUpperCase() }
    }
}












