package com.t3h.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.t3h.item.EventCalendar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vaio on 11/13/2016.
 */

public class MyDatabase {
    public static final String DBNAME = "event.sqlite";
    public static final String TBNAME = "EventCalendar";
    public static final String DATE = "date";
    public static final String NOTE_NAME = "notename";
    public static final String NOTE_CONTENT = "notecontent";
    public static final String TIME = "time";
    public static final String MODE = "mode";
    public static final String WARNING_LEVELS = "warninglevels";
    public static final String LOOPING = "looping";
    public static final String PATH = Environment.getDataDirectory()+"/data/com.example.vaio.calendar/databases/"+DBNAME;
    private Context context;
    private SQLiteDatabase database;

    public MyDatabase(Context context) {
        this.context = context;
        copyDatabase(context);
    }
    public void copyDatabase(Context context){
        try {
            File file = new File(PATH);
            if(file.exists()){
                return;
            }
            File parent = file.getParentFile();
            parent.mkdirs();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte b[] = new byte[1024];
            InputStream inputStream = context.getAssets().open(DBNAME);
            int count = inputStream.read(b);
            while (count!=-1){
                outputStream.write(b,0,count);
                count = inputStream.read(b);
            }
            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void openDatabase(){
        database = context.openOrCreateDatabase(DBNAME,Context.MODE_PRIVATE,null);
    }
    public void closeDatabase(){
        database.close();
    }
    public ArrayList<EventCalendar> getEvent(){
        openDatabase();

        ArrayList<EventCalendar> arrEventCalendar = new ArrayList<>();
        Cursor cursor = database.query(TBNAME,null,null,null,null,null,null);
        int dateIndex = cursor.getColumnIndex(DATE);
        int noteNameIndex = cursor.getColumnIndex(NOTE_NAME);
        int noteContentIndex = cursor.getColumnIndex(NOTE_CONTENT);
        int timeIndex = cursor.getColumnIndex(TIME);
        int modeIndex = cursor.getColumnIndex(MODE);
        int warningLevelsIndex = cursor.getColumnIndex(WARNING_LEVELS);
        int loopingIndex = cursor.getColumnIndex(LOOPING);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String date  = cursor.getString(dateIndex);
            String noteName  = cursor.getString(noteNameIndex);
            String noteContent  = cursor.getString(noteContentIndex);
            String time  = cursor.getString(timeIndex);
            int mode = cursor.getInt(modeIndex);
            int warninglevels  = cursor.getInt(warningLevelsIndex);
            int looping  = cursor.getInt(loopingIndex);
            EventCalendar eventCalendar = new EventCalendar(date,noteName,noteContent,time,mode,warninglevels,looping);
            arrEventCalendar.add(eventCalendar);
            cursor.moveToNext();
        }

        closeDatabase();
        return arrEventCalendar;
    }
    public long insertEvent(EventCalendar eventCalendar){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,eventCalendar.getDate());
        contentValues.put(NOTE_NAME,eventCalendar.getNoteName());
        contentValues.put(NOTE_CONTENT,eventCalendar.getNoteContent());
        contentValues.put(TIME,eventCalendar.getTime());
        contentValues.put(MODE,eventCalendar.getMode());
        contentValues.put(WARNING_LEVELS,eventCalendar.getWarningLevels());
        contentValues.put(LOOPING,eventCalendar.getLooping());
        openDatabase();
        long id = database.insert(TBNAME,null,contentValues);
        closeDatabase();
        return id;
    }
    public long updateEvent(EventCalendar eventCalendar){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,eventCalendar.getDate());
        contentValues.put(NOTE_NAME,eventCalendar.getNoteName());
        contentValues.put(NOTE_CONTENT,eventCalendar.getNoteContent());
        contentValues.put(TIME,eventCalendar.getTime());
        contentValues.put(MODE,eventCalendar.getMode());
        contentValues.put(WARNING_LEVELS,eventCalendar.getWarningLevels());
        contentValues.put(LOOPING,eventCalendar.getLooping());
        openDatabase();
        String whereArgs[]={eventCalendar.getDate()+""};
        long row = database.update(TBNAME,contentValues,DATE+" = ?",whereArgs);
        closeDatabase();
        return row;
    }
}
