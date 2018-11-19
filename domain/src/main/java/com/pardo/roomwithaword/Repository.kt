package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log
import com.pardo.roomwithaword.dao.BaseDao
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.entities.Word

class Repository {

    private var wordDao: WordDao
    private var allWords: LiveData<List<Word>>? = null

    constructor(application : Application){
        wordDao = MyDatabase.getDatabase(application).wordDao()
        allWords = wordDao.selectAll()
    }

    fun getAllWords(): LiveData<List<Word>>? {
        return allWords
    }

    fun persist(word : Word){

        class Task : Runnable{
            var word : Word

            constructor(word : Word){
                this.word = word
            }

            override fun run() {
                Log.e("room", "run persist")
                wordDao.persist(word)
            }
        }
        Thread(Task(word)).start()
    }
}