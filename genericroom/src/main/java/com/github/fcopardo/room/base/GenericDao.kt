package com.github.fcopardo.room.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

@Dao
interface GenericDao<T> {

    @Insert
    fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persist(data: T)

    @Delete
    fun delete(data: T)

    @Delete
    fun delete(data: List<T>)

    @Update
    fun update(data: T)

    @Update
    fun update(data: List<T>)
}
