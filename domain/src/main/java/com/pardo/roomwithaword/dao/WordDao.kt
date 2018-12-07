package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import com.pardo.roomwithaword.entities.Word
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query
import com.github.fcopardo.room.BaseDao
import com.github.fcopardo.room.SearchDao


@Dao
abstract class WordDao : SearchDao<Word, String> {

    constructor(){
        this.myClass = Word::class.java
    }

    @Query("SELECT * from word ORDER BY word ASC")
    abstract fun getAllWords(): LiveData<List<Word>>

    override fun getIdField(): String {
        return "word"
    }
}