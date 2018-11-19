package com.pardo.roomwithaword.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GenericDao<T> {

    @Insert
    void insert(T data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void persist(T data);

    @Delete
    void delete(T data);

    @Delete
    void delete(List<T> data);

    @Update
    void update(T data);

    @Update
    void update(List<T> data);

}
