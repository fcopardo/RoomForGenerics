package com.github.fcopardo.room.base

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.RoomDatabase
import android.util.Log
import java.lang.reflect.Method

open class BaseRepository<T, X : BaseDao<T>> : RepositoryActions<T> {

    interface DatabaseProvider{
        fun getDatabase(application: Application) : RoomDatabase
    }

    companion object {
        private var daoFactoryMethod : HashMap<String, Method> = HashMap()

        @JvmStatic fun getMethod(daoClass : String) : Method?{
            return daoFactoryMethod[daoClass]
        }

        @JvmStatic fun <T> getMethod(aClass : Class<T>) : Method?{
            return if (daoFactoryMethod[aClass.simpleName]!=null) daoFactoryMethod[aClass.simpleName]
            else daoFactoryMethod[aClass.simpleName.decapitalize()]
        }

        @JvmStatic fun cacheMethod(daoClass : String, method : Method) {
            daoFactoryMethod[daoClass] = method
        }

    }

    protected var myDao: X
    protected var allResults: MutableLiveData<MutableList<T>>? = null
    protected var observers : HashMap<String, InvalidationTracker.Observer>? = null

    constructor(application : Application, aClass : Class<X>, provider: DatabaseProvider){
        var database = provider.getDatabase(application)

        var method : Method? = getMethod(database::class.java)

        if(method==null){
            method = try {
                database::class.java.getMethod(aClass.simpleName.decapitalize())
            }catch (e : NoSuchMethodException){
                database::class.java.getMethod(aClass.simpleName)
            }
        }


        method?.isAccessible = true
        myDao = method?.invoke(database) as X
        myDao.setDB<X>(database)

        setObservers()
        allResults = myDao.selectAll()
    }

    protected fun setObservers(){
        observers = HashMap()

        var observer : InvalidationTracker.Observer = object : InvalidationTracker.Observer(myDao.getTableName()) {
            override fun onInvalidated(tables: Set<String>) {
                Log.e("RoomDB", "trigged repo observer")
                if(tables.contains(myDao.getTableName())) {
                    myDao.triggerUpdate(allResults!!)
                }
            }
        }
        observers?.put(myDao.getTableName(), observer)
        myDao.getDatabase()?.invalidationTracker?.addObserver(observer)
    }

    override fun getAll(): LiveData<MutableList<T>>? {
        return allResults
    }

    override fun insert(data : T){
        cud(data, 1)
    }

    override fun update(data : T){
        cud(data, 2)
    }

    override fun persist(data : T){
        cud(data, 3)
    }

    override fun delete(data : T){
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
                    2 -> myDao.update(data)
                    3 -> myDao.persist(data)
                    4 -> myDao.delete(data)
                    else -> Log.e("RoomDB", "not implemented")
                }
            }
        }
        Thread(Task(data, operation)).start()
    }

    override fun tearDown(){
        observers?.forEach{
            myDao.getDatabase()?.invalidationTracker?.removeObserver(it.value)
            observers?.remove(it.key)
        }
    }
}
