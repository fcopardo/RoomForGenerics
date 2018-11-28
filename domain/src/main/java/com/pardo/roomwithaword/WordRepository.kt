package com.pardo.roomwithaword

import android.app.Application
import com.pardo.roomwithaword.dao.WordDao
import com.pardo.roomwithaword.entities.Word

class WordRepository(application : Application) : BaseRepository<Word, WordDao>(application, WordDao::class.java) {
}