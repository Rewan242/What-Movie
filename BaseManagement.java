package com.napps.napps.newtrailers;

/**
 * Created by Rewan on 2017-06-13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseManagement extends SQLiteOpenHelper{

    public BaseManagement(Context context) {
        super(context, "saveddata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table data(" +
                        "title text," +
                        "ID text," +
                        "description text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addSlot(Data data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("ID",data.getID());
        values.put("description", data.getDescription());
        db.insertOrThrow("data",null, values);
    }

    public void removeSlot(String title){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={title};
        db.delete("data", "title=?", arguments);
    }
    public List<Data> loadData(){
        List<Data> mList = new ArrayList<>();
        String[] columns={"title","ID","description"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("data",columns,null,null,null,null,null);
        for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            Data data = new Data();
            data.setTitle(cursor.getString(0));
            data.setID(cursor.getString(1));
            data.setDescription(cursor.getString(2));
            mList.add(data);
        }
        return mList;
    }


}
