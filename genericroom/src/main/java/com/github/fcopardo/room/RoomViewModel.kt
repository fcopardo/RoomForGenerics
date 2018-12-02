package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

abstract class RoomViewModel<T, X: BaseDao<T>> (application: Application, daoClass: Class<X>, provider : BaseRepository.DatabaseProvider)
    : AndroidViewModel(application) {

    private var repositoryWrapper: RepositoryWrapper<T, X> = RepositoryWrapper(application, daoClass, provider)

    fun getAllData() : LiveData<MutableList<T>>?{
        return repositoryWrapper.getAllData()
    }

    fun persist(data : T){
        repositoryWrapper.persist(data)
    }

    fun delete(data : T){
        repositoryWrapper.delete(data)
    }

    fun insert(data : T){
        repositoryWrapper.insert(data)
    }

    override fun onCleared(){
        super.onCleared()
        repositoryWrapper.onCleared()
    }
}