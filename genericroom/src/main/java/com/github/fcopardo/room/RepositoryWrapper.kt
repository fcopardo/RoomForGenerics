package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.LiveData

open class RepositoryWrapper<T, X: BaseDao<T>> (application: Application, daoClass: Class<X>, provider
: BaseRepository.DatabaseProvider) : RepositoryWrapperActions<T> {

    protected open var repo : RepositoryActions<T> = BaseRepository(application, daoClass, provider)
    private var allData: LiveData<MutableList<T>>? = null

    init {
        repo = BaseRepository(application, daoClass, provider)
        allData = repo.getAll()
    }

    override fun getAllData() : LiveData<MutableList<T>>?{
        return allData
    }

    override fun persist(data : T){
        repo.persist(data)
    }

    override fun delete(data : T){
        repo.delete(data)
    }

    override fun insert(data : T){
        repo.insert(data)
    }

    override fun onCleared(){
        repo.tearDown()
    }
}