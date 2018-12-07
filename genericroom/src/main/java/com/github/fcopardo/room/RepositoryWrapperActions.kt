package com.github.fcopardo.room

import android.arch.lifecycle.LiveData

interface RepositoryWrapperActions<T>{

    fun getAllData() : LiveData<MutableList<T>>?

    fun persist(data : T)

    fun delete(data : T)

    fun insert(data : T)

    fun onCleared()
}