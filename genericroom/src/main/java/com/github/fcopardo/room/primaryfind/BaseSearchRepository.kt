package com.github.fcopardo.room.primaryfind

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.fcopardo.room.base.BaseRepository

class BaseSearchRepository<T, Y, X : SearchDao<T, Y>>(application : Application, aClass : Class<X>, provider: DatabaseProvider)
    : BaseRepository<T, X>(application, aClass, provider) {

    fun find(data : Y) : LiveData<T?>{
        return myDao.find(data)
    }
}