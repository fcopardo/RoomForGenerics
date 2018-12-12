package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.pardo.roomwithaword.entities.Word

@Dao
interface WordDaoClassic {

    @Query("SELECT * from word ORDER BY word ASC")
    fun getAllWords(): List<Word>

    @Insert
    fun insert(word: Word)

    @Query("DELETE FROM word")
    fun deleteAll()
}