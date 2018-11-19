package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import com.pardo.roomwithaword.entities.Word

@Dao
abstract class WordDao : BaseDao<Word>{
    constructor(){
        this.myClass = Word::class.java
    }
}