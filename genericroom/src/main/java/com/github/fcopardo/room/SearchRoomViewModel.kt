package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.LiveData

open class SearchRoomViewModel<T : Any, Y, X: SearchDao<T, Y>> (application: Application, daoClass: Class<X>, provider : BaseRepository.DatabaseProvider) :
        RoomViewModel<T, X>(application, daoClass, provider) {

    override var repositoryWrapper : RepositoryWrapperActions<T> = SearchRepositoryWrapper<T, Y, X>(application, daoClass, provider)

    fun find(data : Y) : LiveData<T?>{
        return getRepoWrapper().find(data)
    }

    private fun getRepoWrapper() : SearchRepositoryWrapper<T, Y, X>{
        return repositoryWrapper as SearchRepositoryWrapper<T, Y, X>
    }

}