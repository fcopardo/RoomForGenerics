package com.pardo.roomwithaword

import android.app.Application
import com.github.fcopardo.room.base.BaseRepository
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.entities.Word

class WordRepository(application : Application, provider : DatabaseProvider) :
        BaseRepository<Word, WordDao>(application, WordDao::class.java, provider)