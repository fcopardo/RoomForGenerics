package com.pardo.roomwithaword

import android.app.Application
import android.arch.persistence.room.RoomDatabase
import com.github.fcopardo.room.BaseRepository
import com.github.fcopardo.room.RoomViewModel
import com.github.fcopardo.room.SearchRoomViewModel
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.entities.Word

class WordViewModel(application: Application)
    : SearchRoomViewModel<Word, String, WordDao>(application, WordDao::class.java,
        object : BaseRepository.DatabaseProvider {
            override fun getDatabase(application: Application): RoomDatabase {
                return MyDatabase.getDatabase(application)
            }
        }
)