package com.pardo.roomwithaword.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.RawQuery;
import android.os.AsyncTask;

import java.util.List;

@Dao
public abstract class BaseDao<T> implements GenericDao<T> {

    protected Class<T> myClass;

    @RawQuery
    public abstract List<T> selectAll(SupportSQLiteQuery query);

    public LiveData<List<T>> selectAll(){
        final LiveData<List<T>> data = new MutableLiveData<>();
        class Task extends AsyncTask<Void, Void, List<T>>{
            @Override
            protected List<T> doInBackground(Void... voids) {
                return selectAll(new SimpleSQLiteQuery("SELECT * FROM "+myClass.getSimpleName()));
            }
            @Override
            protected void onPostExecute(List<T> result){
                ((MutableLiveData<List<T>>) data).setValue(result);
            }
        }
        Task task = new Task();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return data;
    }

    @RawQuery
    public abstract boolean deleteAll(SupportSQLiteQuery query);

    public boolean deleteAll(){
        try {
            deleteAll(new SimpleSQLiteQuery("DELETE FROM " + myClass.getSimpleName()));
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

}
