package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.InvalidationTracker
import android.util.Log
import com.pardo.roomwithaword.dao.BaseDao
import java.lang.reflect.Method

open class BaseRepository<T, X : BaseDao<T>> {

    private var myDao: X
    private var allResults: MutableLiveData<MutableList<T>>? = null
    private var observers : HashMap<String, InvalidationTracker.Observer>? = null

    constructor(application : Application, aClass : Class<X>){
        var database : MyDatabase = MyDatabase.getDatabase(application)

        var method : Method? = database::class.java.getMethod(aClass.simpleName.decapitalize())
        method?.isAccessible = true
        myDao = method?.invoke(database) as X

        observers = HashMap()

        var observer : InvalidationTracker.Observer = object : InvalidationTracker.Observer(myDao.getTableName()) {
            override fun onInvalidated(tables: Set<String>) {
                myDao.triggerUpdate(allResults!!)
            }
        }

        observers?.put(myDao.getTableName(), observer)

        myDao.getDatabase()?.invalidationTracker?.addObserver(observer)
        allResults = myDao.selectAll()
    }

    fun getAll(): LiveData<MutableList<T>>? {
        return allResults
    }

    fun persist(data : T){

        class Task : Runnable{
            var data : T

            constructor(data : T){
                this.data = data
            }

            override fun run() {
                Log.e("room", "run persist")
                myDao.persist(data)
                //allResults?.value?.add(data)
            }
        }
        Thread(Task(data)).start()
    }

    fun tearDown(){
        observers?.forEach{
            myDao.getDatabase()?.invalidationTracker?.removeObserver(it.value)
            observers?.remove(it.key)
        }
    }
}