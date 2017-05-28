package com.azuyo.happybeing.adapter;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by nSmiles on 5/26/2017.
 */

public class DBAdapter {
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "Mydatabase";
    public static final String KEY_ROWID = "_id";

    private static final String DATABASE_TABLE = "RelexAndRelieve";

    public static final String RELEX_TITLE = "TEXT_TITLE";
    public static final String RELEX_TITLE_AUDIO = "TEXT_TITLE_IMAGE";

    private static final String DATABASE_CREATE_RelexAndRelieve =
            "create table RelexAndRelieve (_id integer primary key autoincrement, "
                    + "TEXT_TITLE text not null,TEXT_TITLE_IMAGE text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
         exportDB();

        }

        private void exportDB() {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + "com.azuyo.happybeing" + "/databases/" + DATABASE_NAME;
            String backupDBPath = "MyDataBase3.sqlite";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE_RelexAndRelieve);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS RelexAndRelieve");
            onCreate(db);
        }
    }

    //==============================================opens the database  ==================================================
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        System.out.println("Data base created successfully");
        return this;
    }


    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }



}
