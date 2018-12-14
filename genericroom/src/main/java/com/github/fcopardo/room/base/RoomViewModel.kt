package com.github.fcopardo.room.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

abstract class RoomViewModel<T, X: BaseDao<T>> (application: Application, daoClass: Class<X>, provider : BaseRepository.DatabaseProvider)
    : AndroidViewModel(application) {

    protected open var repository : RepositoryActions<T> = BaseRepository(application, daoClass, provider)

    fun getAllData() : LiveData<MutableList<T>>?{
        return repository.getAll()
    }

    fun persist(data : T){
        repository.persist(data)
    }

    fun delete(data : T){
        repository.delete(data)
    }

    fun insert(data : T){
        repository.insert(data)
    }

    fun deleteAll() : LiveData<Boolean> {
        return repository.deleteAll()
    }

    override fun onCleared(){
        super.onCleared()
        repository.tearDown()
    }
}