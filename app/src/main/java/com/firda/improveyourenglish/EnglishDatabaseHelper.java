package com.firda.improveyourenglish;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private static void insertWords(SQLiteDatabase db/*, Context context*/) {
        /*try (InputStream is = new FileInputStream("text.txt")) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead;
            while ( (bytesRead=is.read(b)) != -1 ) {
                bos.write(b, 0, bytesRead);
            }
            byte[] bytes = bos.toByteArray();
            String dataToSave = new String(bytes);
            String line = "";
            for (int i = 0; i < dataToSave.length(); i++) {
                if (dataToSave.charAt(i) == '\n') {
                    //System.out.println(line);
                    //System.out.println("ura");
                    ContentValues wordValues = new ContentValues();
                    wordValues.put("ENGLISH", line.substring(0, line.indexOf("--")));
                    wordValues.put("RUSSIAN", line.substring(line.indexOf("--")+2));
                    db.insert("WORD", null, wordValues);
                    line = "";
                    continue;
                }
                line += dataToSave.charAt(i);

            }
            Log.d(TAG, "Database was created!!!");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Database was not created!!!");
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } catch(IOException ex){
            Log.d(TAG, "Database was not created!!!");
            Log.d(TAG, ex.getMessage());
        }*/
        ; // where file is the .txt you want to convert

        byte[] buffer = null;
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
                    //System.out.println(line);
                    //System.out.println("ura");
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


        /*try(BufferedReader br = new BufferedReader(is)/*new BufferedReader(new FileReader("C:\\Users\\firda\\AndroidStudioProjects\\ImproveYourEnglish\\app\\src\\main\\java\\com\\firda\\improveyourenglish\\text.txt")))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                ContentValues wordValues = new ContentValues();
                wordValues.put("ENGLISH", s.substring(0, s.indexOf("--")));
                wordValues.put("RUSSIAN", s.substring(s.indexOf("--")+2));
                db.insert("WORD", null, wordValues);
                /*System.out.print(s.substring(0, s.indexOf("--")));
                System.out.print("|");
                System.out.println(s.substring(s.indexOf("--")+2));
            }
            Log.d(TAG, "Database was created!!!");
        }
                catch(IOException ex){
                    Log.d(TAG, "Database was not created!!!");
                    Log.d(TAG, ex.getMessage());
                }*/


    }
}
