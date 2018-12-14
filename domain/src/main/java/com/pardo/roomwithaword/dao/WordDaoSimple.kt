package com.pardo.roomwithaword.dao

import android.arch.persistence.room.Dao
import com.github.fcopardo.room.base.BaseDao
import com.pardo.roomwithaword.entities.Word


@Dao
abstract class WordDaoSimple : BaseDao<Word> {

    constructor(){
        this.myClass = Word::class.java
    }
}