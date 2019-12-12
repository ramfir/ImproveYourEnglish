package com.firda.improveyourenglish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class EnglishDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "english";
    private static final int DB_VERSION = 1;
    private static final String TAG = "MainActivity";
    private static Context mcontext;

    EnglishDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ENGLISH TEXT, "
                + "RUSSIAN TEXT); ");
        db.execSQL("CREATE TABLE CHOSEN (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ENGLISH TEXT, "
                + "RUSSIAN TEXT); ");
        insertWords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private static void insertWords(SQLiteDatabase db) {
        byte[] buffer;
        InputStream is;
        try {
            is = mcontext.getAssets().open("text.txt");
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            String dataToSave = new String(buffer);
            String line = "";
            for (int i = 0; i < dataToSave.length(); i++) {
                if (dataToSave.charAt(i) == '\n') {
                    ContentValues wordValues = new ContentValues();
                    wordValues.put("ENGLISH", line.substring(0, line.indexOf("--")-1));
                    wordValues.put("RUSSIAN", line.substring(line.indexOf("--")+3));
                    db.insert("WORD", null, wordValues);
                    line = "";
                    continue;
                }
                line += dataToSave.charAt(i);
            }
            Log.d(TAG, "Database was created!!!");
        } catch (IOException e) {
            Log.d(TAG, "Database was not created!!!");
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
    public int getSize(SQLiteDatabase db, String table) {
        Cursor cursor = db.query(table,
                new String[]{"ENGLISH", "RUSSIAN"},
                null,
                null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor.getCount();
        return 0;
    }
}
