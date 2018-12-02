package com.github.fcopardo.room

import android.app.Application

class BaseSearchRepository<T, Y, X : SearchDao<T, Y>>(application : Application, aClass : Class<X>, provider: DatabaseProvider)
    : BaseRepository<T, X>(application, aClass, provider) {




}