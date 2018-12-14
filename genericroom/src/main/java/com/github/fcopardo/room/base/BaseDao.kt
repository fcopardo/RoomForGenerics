package com.github.fcopardo.room.base

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.RawQuery
import android.arch.persistence.room.RoomDatabase
import android.os.AsyncTask
import android.util.Log


@Dao
abstract class BaseDao<T> : GenericDao<T> {

    inner class Task(var data: MutableLiveData<MutableList<T>>) : AsyncTask<Void, Void, MutableList<T>>() {

        override fun doInBackground(vararg voids: Void): MutableList<T> {
            return selectAll(SimpleSQLiteQuery("SELECT * FROM " + getTableName()))
        }

        override fun onPostExecute(result: MutableList<T>) {
            Log.e("RoomDao", "values reset")
            data.value = result
        }

        fun executeMe() : Task {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            return this
        }
    }

    inner class DeleteTask(var data : MutableLiveData<Boolean>) : AsyncTask<Void, Void, MutableLiveData<Boolean>>() {
        override fun doInBackground(vararg params: Void?): MutableLiveData<Boolean> {
            return return try{
                deleteAll(SimpleSQLiteQuery("DELETE FROM '" + getTableName()+"'"))
                data.value = true
                data
            } catch(e : java.lang.Exception){
                e.printStackTrace()
                data.value = false
                data
            }
        }

        fun executeMe() : DeleteTask {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            return this
        }
    }

    protected var myClass: Class<T>? = null
    private var database : RoomDatabase? = null
    protected var observers : HashMap<String, InvalidationTracker.Observer>? = null
    protected var allResults = MutableLiveData<MutableList<T>>()
    protected var deleteDone = MutableLiveData<Boolean>()

    @RawQuery
    protected abstract fun selectAll(query: SupportSQLiteQuery): MutableList<T>

    fun selectAll(): MutableLiveData<MutableList<T>> {
        Log.e("RoomDao", "called select all")
        Task(allResults).executeMe()
        return allResults
    }

    @RawQuery
    protected abstract fun deleteAll(query: SupportSQLiteQuery): Boolean

    fun deleteAll(): MutableLiveData<Boolean> {
        try {
            DeleteTask(deleteDone).executeMe()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return deleteDone
    }

    fun triggerUpdate(data : MutableLiveData<MutableList<T>>) {
        Task(data).executeMe()
    }

    fun getTableName() : String{
        return myClass!!.simpleName
    }

    fun <X: BaseDao<T>> setDB(db: RoomDatabase) : X {
        database = db
        setObservers()
        return this as X
    }

    fun getDatabase() : RoomDatabase?{
        return database
    }

    protected open fun setObservers(){
        observers = HashMap()

        var observer : InvalidationTracker.Observer = object : InvalidationTracker.Observer(getTableName()) {
            override fun onInvalidated(tables: Set<String>) {
                Log.e("RoomDB", "trigged repo observer")
                if(tables.contains(getTableName())) {
                    triggerUpdate(allResults!!)
                }
            }
        }
        observers?.put(getTableName(), observer)
        getDatabase()?.invalidationTracker?.addObserver(observer)
    }

    fun tearDown(){
        observers?.forEach{
            getDatabase()?.invalidationTracker?.removeObserver(it.value)
            observers?.remove(it.key)
        }
    }

}
