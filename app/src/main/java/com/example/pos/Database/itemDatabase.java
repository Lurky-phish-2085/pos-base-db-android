package com.example.pos.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class itemDatabase extends RoomDatabase {
    private static itemDatabase instance;
    public abstract Dao dao();

    public static synchronized itemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, itemDatabase.class, "itemDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AsyncItemTask(instance).execute();
        }
    };

    private static class AsyncItemTask extends AsyncTask<Void, Void, Void>{
        private static Dao dao;

        private AsyncItemTask(itemDatabase db) {
            this.dao = db.dao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insert(new Item("Toy", "A thing to play with"));
            return null;
        }
    }
}
