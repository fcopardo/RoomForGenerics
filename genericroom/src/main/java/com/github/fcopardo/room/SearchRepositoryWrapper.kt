package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.LiveData

class SearchRepositoryWrapper<T : Any, Y, X: SearchDao<T, Y>> (application: Application, daoClass: Class<X>, provider
: BaseRepository.DatabaseProvider) : RepositoryWrapper<T, X>(application, daoClass, provider){

    override var repo : RepositoryActions<T> = BaseSearchRepository(application, daoClass, provider)

    init {
        repo = BaseSearchRepository(application, daoClass, provider)
    }

    fun find(data : Y): LiveData<T?> {
        return getRepo().find(data)
    }

    private fun getRepo() : BaseSearchRepository<T, Y, X>{
        return repo as BaseSearchRepository<T, Y, X>
    }
}