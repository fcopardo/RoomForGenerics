package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.RoomDatabase
import com.github.fcopardo.room.BaseRepository
import com.pardo.roomwithaword.entities.Word

class WordViewModel2(application: Application) : AndroidViewModel(application) {

    private var repo : WordRepository = WordRepository(application, object : BaseRepository.DatabaseProvider {
        override fun getDatabase(application: Application): RoomDatabase {
            return MyDatabase.getDatabase(application)
        }
    })
    private var allWords: LiveData<MutableList<Word>>? = null

    init {
        allWords = repo.getAll()
    }

    fun getAllData() : LiveData<MutableList<Word>>?{
        return allWords
    }

    fun persist(data : Word){
        repo.persist(data)
    }

    override fun onCleared(){
        super.onCleared()
        repo.tearDown()
    }
}