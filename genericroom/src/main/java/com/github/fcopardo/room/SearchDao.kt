package com.github.fcopardo.room

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.RawQuery
import android.os.AsyncTask
import android.util.Log

abstract class SearchDao<T, X> : BaseDao<T>() {

    companion object {
        private var idFields = HashMap<String, String>()

        @JvmStatic
        fun addIdField(className : String, fieldName : String){
            idFields[className] = fieldName
        }

        @JvmStatic
        fun getIdField(className : String?) : String? {
            return idFields[className]
        }
    }

    inner class FindTask(var data: MutableLiveData<T?>, var id :  X) : AsyncTask<Void, Void, T?>() {

        override fun doInBackground(vararg voids: Void): T? {

            var fieldName: String? = getIdField(myClass?.canonicalName)

            if (fieldName == null) {
                for (field in myClass?.fields!!) {
                    field.declaredAnnotations.iterator().forEach {
                        if (it.javaClass.equals(PrimaryKey::class.java)){
                            addIdField(myClass!!.canonicalName, field.name)
                            fieldName = field.name
                        }
                    }
                }
            }

            var query : SimpleSQLiteQuery = SimpleSQLiteQuery("SELECT * FROM " + getTableName()+" where " + fieldName+" = "+id.toString())
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


}