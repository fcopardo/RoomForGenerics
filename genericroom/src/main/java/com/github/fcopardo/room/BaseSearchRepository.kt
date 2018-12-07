package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.LiveData

class BaseSearchRepository<T: Any, Y, X : SearchDao<T, Y>>(application : Application, aClass : Class<X>, provider: DatabaseProvider)
    : BaseRepository<T, X>(application, aClass, provider) {

    fun find(data : Y) : LiveData<T?>{
        return myDao.find(data)
    }
}