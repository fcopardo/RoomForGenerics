package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.pardo.roomwithaword.dao.BaseDao

abstract class BaseViewModel<T, X: BaseDao<T>> (application: Application, daoClass: Class<X>)
    : AndroidViewModel(application) {

    private var repo : BaseRepository<T, X> = BaseRepository(application, daoClass)
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