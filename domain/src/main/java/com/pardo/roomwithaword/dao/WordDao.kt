package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import com.github.fcopardo.room.primaryfind.SearchDao
import com.pardo.roomwithaword.entities.Word


@Dao
abstract class WordDao : SearchDao<Word, String> {

    constructor(){
        this.myClass = Word::class.java
    }

    override fun getIdField(): String {
        return "word"
    }
}