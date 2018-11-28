package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.InvalidationTracker
import android.util.Log
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.entities.Word

class Repository {

    private var wordDao: WordDao
    private var allWords: MutableLiveData<MutableList<Word>>? = null
    private var observers : HashMap<String, InvalidationTracker.Observer>? = null

    constructor(application : Application){
        wordDao = MyDatabase.getDatabase(application).wordDao().setDB(MyDatabase.getDatabase(application))

        observers = HashMap()

        var observer : InvalidationTracker.Observer = object : InvalidationTracker.Observer(wordDao.getTableName()) {
            override fun onInvalidated(tables: Set<String>) {
                wordDao.triggerUpdate(allWords!!)
            }
        }

        observers?.put(wordDao.getTableName(), observer)

        wordDao.getDatabase()?.invalidationTracker?.addObserver(observer)
        allWords = wordDao.selectAll()
    }

    fun getAllWords(): LiveData<MutableList<Word>>? {
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
                allWords?.value?.add(word)
            }
        }
        Thread(Task(word)).start()
    }

    fun tearDown(){
        observers?.forEach{
            wordDao.getDatabase()?.invalidationTracker?.removeObserver(it.value)
            observers?.remove(it.key)
        }
    }
}