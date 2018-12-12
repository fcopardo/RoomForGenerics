package com.pardo.roomwithaword

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.dao.WordDaoClassic
import com.pardo.roomwithaword.entities.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    companion object {

        @Volatile
        var INSTANCE : MyDatabase? = null

        fun getDatabase(context : Context) : MyDatabase{
            var temp = INSTANCE
            if(temp!=null){
                return temp
            }else{
                temp = Room.databaseBuilder(context.applicationContext,
                        MyDatabase::class.java, getName())
                        .build();
                INSTANCE = temp
                return temp
            }
        }

        fun getName(): String {
            return "word_database"
        }
    }

    abstract fun wordDao(): WordDao
    abstract fun wordDaoClassic(): WordDaoClassic

    private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            PopulateDbAsync(INSTANCE!!).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private class PopulateDbAsync internal constructor(db: MyDatabase) : AsyncTask<Void, Void, Void>() {

        private val mDao: WordDao = db.wordDao()

        override fun doInBackground(vararg params: Void): Void? {
            mDao.deleteAll()
            var word = Word("Hello")
            mDao.insert(word)
            word = Word("World")
            mDao.insert(word)
            return null
        }
    }

}