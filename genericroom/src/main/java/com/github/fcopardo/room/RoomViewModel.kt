package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

abstract class RoomViewModel<T, X: BaseDao<T>> (application: Application, daoClass: Class<X>, provider : BaseRepository.DatabaseProvider)
    : AndroidViewModel(application) {

    private var repo : BaseRepository<T, X> = BaseRepository(application, daoClass, provider)
    private var allData: LiveData<MutableList<T>>? = null

    init {
        allData = repo.getAll()
    }

    fun getAllData() : LiveData<MutableList<T>>?{
        return allData
    }

    fun persist(data : T){
        repo.persist(data)
    }

    fun delete(data : T){
        repo.delete(data)
    }

    fun insert(data : T){
        repo.insert(data)
    }

    override fun onCleared(){
        super.onCleared()
        repo.tearDown()
    }
}