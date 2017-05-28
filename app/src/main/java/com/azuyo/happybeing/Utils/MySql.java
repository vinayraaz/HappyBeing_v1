package com.azuyo.happybeing.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MySql extends SQLiteOpenHelper {

	public MySql(Context context, String name, CursorFactory factory, int version) {

		super(context, name, factory, version);
		System.out.println("db created successfully");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE Gratitude_Table (_id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, TIME INTEGER, PLAY_URL TEXT, TYPE TEXT, CONTENT TEXT)");
		db.execSQL("CREATE TABLE My_Happy_Moments (_id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, IMAGE_URI TEXT, PLAY_URL TEXT, TYPE TEXT, CONTENT TEXT, TIME INTEGER)");
		db.execSQL("CREATE TABLE Goals_Table (_id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, IMAGE_URI TEXT, PLAY_URL TEXT, STEPS_TO_ACHIEVE TEXT, TIME INTEGER, TYPE TEXT, ACHIEVE_BY INTEGER,DEFINE TEXT, BENEFITS TEXT, COMPROMISE_MADE TEXT, COMPROMISE_NOT_MADE TEXT, TEAM_MEMBERS TEXT)");
		db.execSQL("CREATE TABLE Feelings_Table (_id INTEGER PRIMARY KEY AUTOINCREMENT, BASIC_FEELING TEXT, FEELING TEXT, TIME INTEGER)");
		db.execSQL("CREATE TABLE StressReliefOptions(_id INTEGER PRIMARY KEY AUTOINCREMENT, OPTION_NAME TEXT, IMAGE_ID INTEGER, ONCLICK_OPTION TEXT)");
		db.execSQL("CREATE TABLE MindGymAudioCount(_id INTEGER PRIMARY KEY AUTOINCREMENT, MIND_GYM_AUDIO_COUNT INTEGER)");
		db.execSQL("CREATE TABLE NotificationsTimings(_id INTEGER PRIMARY KEY AUTOINCREMENT, MIND_GYM_START_TIME INTEGER,  MIND_GYM_END_TIME INTEGER,  RELAX_START_TIME INTEGER,  RELAX_END_TIME INTEGER)");
		db.execSQL("CREATE TABLE RelexAndRelieve(_id INTEGER PRIMARY KEY AUTOINCREMENT, TEXT_TITLE TEXT,TEXT_TITLE_IMAGE TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Current version is gretter than oldest version");
			if (oldVersion < newVersion) {
				db.execSQL("DROP TABLE IF EXISTS MindGymAudioCount");
				db.execSQL("DROP TABLE IF EXISTS NotificationsTimings");
				db.execSQL("CREATE TABLE MindGymAudioCount(_id INTEGER PRIMARY KEY AUTOINCREMENT, MIND_GYM_AUDIO_COUNT INTEGER)");
				db.execSQL("CREATE TABLE NotificationsTimings(_id INTEGER PRIMARY KEY AUTOINCREMENT, MIND_GYM_START_TIME INTEGER,  MIND_GYM_END_TIME INTEGER,  RELAX_START_TIME INTEGER,  RELAX_END_TIME INTEGER)");
			}
	}
	/*private void exportDB() {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + "com.azuyo.happybeing" + "/databases/" + "mydb";
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

	}*/

}
