package com.pardo.roomwithaword;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.pardo.roomwithaword.dao.WordDao;
import com.pardo.roomwithaword.entities.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class MyDatabaseJ extends RoomDatabase {
    private static volatile MyDatabaseJ INSTANCE;

    static MyDatabaseJ getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabaseJ.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabaseJ.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract WordDao wordDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;

        PopulateDbAsync(MyDatabaseJ db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Word word = new Word("Hello");
            mDao.insert(word);
            word = new Word("World");
            mDao.insert(word);
            return null;
        }
    }

}