package com.pardo.roomwithaword.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity
class Word {

    @PrimaryKey
    @NonNull
    private var word: String

    constructor(@NonNull word: String){
        this.word = word
    }

    fun getWord(): String {
        return this.word
    }
}