package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.pardo.roomwithaword.entities.Word

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private var repo : WordRepository = WordRepository(application)
    private var allWords: LiveData<MutableList<Word>>? = null

    init {
        allWords = repo.getAll()
    }

    fun getAllWords() : LiveData<MutableList<Word>>?{
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