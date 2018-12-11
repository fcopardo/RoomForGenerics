package com.github.fcopardo.room.primaryfind

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.RawQuery
import android.os.AsyncTask
import android.util.Log
import com.github.fcopardo.room.base.BaseDao


abstract class SearchDao<T, X> : BaseDao<T>() {

    inner class FindTask(var data: MutableLiveData<T?>, var id :  X) : AsyncTask<Void, Void, T?>() {

        override fun doInBackground(vararg voids: Void): T? {
            var query = SimpleSQLiteQuery("SELECT * FROM " + getTableName()+" where " + getIdField()+" = "+"'"+id.toString()+"'")
            return find(query)
        }

        override fun onPostExecute(result: T?) {
            data.value = result
        }

        fun executeMe() : FindTask {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            return this
        }
    }

    @RawQuery
    abstract fun find(query: SupportSQLiteQuery): T?

    fun find(id : X): MutableLiveData<T?> {
        Log.e("RoomDao", "called find")
        val data = MutableLiveData<T?>()
        FindTask(data, id).executeMe()
        return data
    }

    abstract fun getIdField() : String
}