package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import com.pardo.roomwithaword.entities.Word
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query


@Dao
abstract class WordDao : BaseDao<Word>{
    constructor(){
        this.myClass = Word::class.java
    }

    @Query("SELECT * from word ORDER BY word ASC")
    abstract fun getAllWords(): LiveData<List<Word>>
}