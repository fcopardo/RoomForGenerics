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

    protected var myClass: Class<T>? = null

    @RawQuery
    abstract fun selectAll(query: SupportSQLiteQuery): MutableList<T>

    fun selectAll(): LiveData<MutableList<T>> {
        Log.e("RoomDao", "called select all")
        val data = MutableLiveData<MutableList<T>>()
        Task(data).executeMe()
        return data
    }

    @RawQuery
    abstract fun deleteAll(query: SupportSQLiteQuery): Boolean

    fun deleteAll(): Boolean {
        try {
            deleteAll(SimpleSQLiteQuery("DELETE FROM " + getTableName()))
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun triggerUpdate(data : MutableLiveData<MutableList<T>>) {
        Task(data).executeMe()
    }

    fun getTableName() : String{
        return myClass!!.simpleName
    }

}
