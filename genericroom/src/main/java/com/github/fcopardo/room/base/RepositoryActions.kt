package com.github.fcopardo.room.base

import android.arch.lifecycle.LiveData

interface RepositoryActions<T> {

    fun getAll(): LiveData<MutableList<T>>?

    fun insert(data : T)

    fun update(data : T)

    fun persist(data : T)

    fun delete(data : T)

    fun deleteAll() : LiveData<Boolean>

    fun tearDown()
}