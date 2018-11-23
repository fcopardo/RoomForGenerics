package com.pardo.roomwithaword.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.RawQuery
import android.os.AsyncTask
import android.util.Log

@Dao
abstract class BaseDao<T> : GenericDao<T> {

    inner class Task(var data: MutableLiveData<List<T>>) : AsyncTask<Void, Void, List<T>>() {

        override fun doInBackground(vararg voids: Void): List<T> {
            return selectAll(SimpleSQLiteQuery("SELECT * FROM " + myClass!!.simpleName))
        }

        override fun onPostExecute(result: List<T>) {
            Log.e("RoomDao", "values reset")
            data.value = result
        }

        fun executeMe() : Task {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            return this
        }
    }

    protected var myClass: Class<T>? = null
    protected val data = MutableLiveData<List<T>>()

    @RawQuery
    abstract fun selectAll(query: SupportSQLiteQuery): List<T>

    fun selectAll(): LiveData<List<T>> {
        Log.e("RoomDao", "called select all")
        Task(data).executeMe()
        return data
    }

    @RawQuery
    abstract fun deleteAll(query: SupportSQLiteQuery): Boolean

    fun deleteAll(): Boolean {
        try {
            deleteAll(SimpleSQLiteQuery("DELETE FROM " + myClass!!.simpleName))
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun triggerUpdate() {
        Task(data).executeMe()
    }

}
