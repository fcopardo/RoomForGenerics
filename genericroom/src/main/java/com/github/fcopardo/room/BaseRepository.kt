package com.github.fcopardo.room

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.RoomDatabase
import android.util.Log
import java.lang.reflect.Method

open class BaseRepository<T, X : BaseDao<T>> {

    interface DatabaseProvider{
        fun getDatabase(application: Application) : RoomDatabase
    }

    private var myDao: X
    private var allResults: MutableLiveData<MutableList<T>>? = null
    private var observers : HashMap<String, InvalidationTracker.Observer>? = null

    constructor(application : Application, aClass : Class<X>, provider: DatabaseProvider){
        //var database : MyDatabase = MyDatabase.getDatabase(application)
        var database = provider.getDatabase(application)

        var method : Method? = if(database::class.java.getMethod(aClass.simpleName.decapitalize()) != null)
            database::class.java.getMethod(aClass.simpleName.decapitalize())
        else
            database::class.java.getMethod(aClass.simpleName)

        method?.isAccessible = true
        myDao = method?.invoke(database) as X
        myDao.setDB<X>(database)

        observers = HashMap()

        var observer : InvalidationTracker.Observer = object : InvalidationTracker.Observer(myDao.getTableName()) {
            override fun onInvalidated(tables: Set<String>) {
                Log.e("RoomDB", "trigged repo observer")
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

    fun insert(data : T){
        cud(data, 1)
    }

    fun persist(data : T){

        /*class Task : Runnable{
            var data : T

            constructor(data : T){
                this.data = data
            }

            override fun run() {
                myDao.persist(data)
            }
        }
        Thread(Task(data)).start()*/
        cud(data, 3)
    }

    fun delete(data : T){
        cud(data, 4)
    }

    private fun cud(data : T, operation : Int){
        class Task : Runnable{
            var data : T
            var operation : Int

            constructor(data : T, operation : Int){
                this.data = data
                this.operation = operation
            }

            override fun run() {
                when(operation){
                    1 -> myDao.insert(data)
                    2 -> myDao.persist(data)
                    3 -> myDao.persist(data)
                    4 -> myDao.delete(data)
                    else -> Log.e("RoomDB", "not implemented")
                }
            }
        }
        Thread(Task(data, operation)).start()
    }

    fun tearDown(){
        observers?.forEach{
            myDao.getDatabase()?.invalidationTracker?.removeObserver(it.value)
            observers?.remove(it.key)
        }
    }
}
