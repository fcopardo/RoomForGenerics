package com.pardo.roomwithaword.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.fcopardo.room.base.GenericDao
import com.pardo.roomwithaword.entities.Word

@Dao
interface WordDaoClassic : GenericDao<Word> {

    @Query("SELECT * from word ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("DELETE FROM word")
    fun deleteAll()

    @Query("SELECT * from word WHERE word like :id ")
    fun findWord(id: String) : LiveData<Word>
}