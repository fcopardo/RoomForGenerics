package com.github.fcopardo.room.primaryfind

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.fcopardo.room.base.BaseRepository
import com.github.fcopardo.room.base.RepositoryActions
import com.github.fcopardo.room.base.RoomViewModel

open class SearchRoomViewModel<T : Any, Y, X: SearchDao<T, Y>> (application: Application, daoClass: Class<X>, provider : BaseRepository.DatabaseProvider) :
        RoomViewModel<T, X>(application, daoClass, provider) {

    override var repository : RepositoryActions<T> = BaseSearchRepository(application, daoClass, provider)

    fun find(data : Y) : LiveData<T?>{
        return getRepoWrapper().find(data)
    }

    private fun getRepoWrapper() : BaseSearchRepository<T, Y, X> {
        return repository as BaseSearchRepository<T, Y, X>
    }

}