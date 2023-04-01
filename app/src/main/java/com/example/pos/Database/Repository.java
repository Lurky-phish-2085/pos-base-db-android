package com.example.pos.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class Repository {
    private Dao dao;
    private LiveData<List<Item>> itemList;

    public Repository(Application application) {
        itemDatabase db = itemDatabase.getInstance(application);
        dao = db.dao();

        itemList = dao.getAllItems();
    }

    public void insert(Item item){
        new AsyncItemTask(dao, 0).execute(item);
    }

    public void update(Item item){
        new AsyncItemTask(dao, 1).execute(item);
    }

    public void delete(Item item){
        new AsyncItemTask(dao, 2).execute(item);
    }

    public void deleteAll(){
        new AsyncItemTask(dao, 3).execute();
    }

    public LiveData<List<Item>> getItemList(){
        return dao.getAllItems();
    }

    public LiveData<List<Item>> getSpecificItemList(String name){
        return dao.getSpecificItem(name);
    }

        private static class AsyncItemTask extends AsyncTask<Item, Void, Void>{
        private static Dao dao;
        private static int mode;

        private AsyncItemTask(Dao dao, int mode) {
            this.dao = dao;
            this.mode = mode;
        }

        @Override
        protected Void doInBackground(Item... items) {
            switch (mode){
                case 0:
                    dao.insert(items[0]);
                case 1:
                    dao.update(items[0]);
                case 2:
                    dao.delete(items[0]);
                case 3:
                    dao.deleteAll();
            }
            return null;
        }
    }
}
