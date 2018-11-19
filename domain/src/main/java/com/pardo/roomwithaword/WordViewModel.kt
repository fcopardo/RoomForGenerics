package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.pardo.roomwithaword.entities.Word

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private var repo : Repository = Repository(application)
    private var allWords: LiveData<List<Word>>? = null

    init {
        allWords = repo.getAllWords()
    }

    fun getAllWords() : LiveData<List<Word>>?{
        return allWords
    }

    fun persist(data : Word){
        repo.persist(data)
    }
}