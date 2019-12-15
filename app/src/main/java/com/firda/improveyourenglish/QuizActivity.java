package com.firda.improveyourenglish;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private List<Button> listButtons;
    private static final String TAG = "MainActivity";
    Map<String, String> words;
    List<String> russian;
    private String currentEngish;
    private TextView resultTextView;
    String chosenWords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        listButtons = new ArrayList<>();
        listButtons.add((Button)findViewById(R.id.button4));
        listButtons.add((Button)findViewById(R.id.button5));
        listButtons.add((Button)findViewById(R.id.button6));
        listButtons.add((Button)findViewById(R.id.button7));
        listButtons.add((Button)findViewById(R.id.button8));
        listButtons.add((Button)findViewById(R.id.button9));
        listButtons.add((Button)findViewById(R.id.button10));
        listButtons.add((Button)findViewById(R.id.button11));
        listButtons.add((Button)findViewById(R.id.button12));
        listButtons.add((Button)findViewById(R.id.button13));
        resultTextView = findViewById(R.id.textView3);
        resultTextView.setText("0");
        Intent intent = getIntent();
        if (intent.getStringExtra("KEY") != null)
            chosenWords = intent.getStringExtra("KEY");
        if (chosenWords.equals("")) {
            rWords();
        } else {
            cWords();
        }

    }
    public void cWords() {
        ArrayList<Integer> randomWords = new ArrayList<>();
        int size = 0;
        SQLiteOpenHelper englishDatabaseHeleper = new EnglishDatabaseHelper(this);
        try {
            SQLiteDatabase db = englishDatabaseHeleper.getReadableDatabase();
            size = ((EnglishDatabaseHelper) englishDatabaseHeleper).getSize(db, "CHOSEN");
            Log.d(TAG, Integer.toString(size));
            for (int i = 0; i < 5; i++) {
                while (true) {
                    int randomNum = (int)(Math.random()*size+1);
                    if (!randomWords.contains(randomNum)) {
                        randomWords.add(randomNum);
                        break;
                    }
                }
            }
            Cursor cursor = db.query("CHOSEN",
                    new String[]{"ENGLISH", "RUSSIAN"},
                    "_id = ? OR _id = ? OR _id = ? OR _id = ? OR _id = ?",
                    new String[] {Integer.toString(randomWords.get(0)), Integer.toString(randomWords.get(1)),
                            Integer.toString(randomWords.get(2)), Integer.toString(randomWords.get(3)),
                            Integer.toString(randomWords.get(4))},
                    null, null, null);
            cursor.moveToFirst();
            int i = 0;
            words = new HashMap<>();
            russian = new ArrayList<>();
            while (cursor.isAfterLast() == false) {
                words.put(cursor.getString(0), cursor.getString(1));
                listButtons.get(i).setText(cursor.getString(0));
                russian.add(cursor.getString(1));
                i++;
                cursor.moveToNext();
            }
            Collections.shuffle(russian);
            for (int j = 0; j < 5; j++) {
                listButtons.get(j+5).setText(russian.get(j));
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void rWords() {
        ArrayList<Integer> randomWords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            while (true) {
                int randomNum = (int)(Math.random()*7000+1);
                if (!randomWords.contains(randomNum)) {
                    randomWords.add(randomNum);
                    break;
                }
            }
        }
        SQLiteOpenHelper englishDatabaseHeleper = new EnglishDatabaseHelper(this);
        try {
            SQLiteDatabase db = englishDatabaseHeleper.getReadableDatabase();
            Cursor cursor = db.query("WORD",
                    new String[]{"ENGLISH", "RUSSIAN"},
                    "_id = ? OR _id = ? OR _id = ? OR _id = ? OR _id = ?",
                    new String[] {Integer.toString(randomWords.get(0)), Integer.toString(randomWords.get(1)),
                            Integer.toString(randomWords.get(2)), Integer.toString(randomWords.get(3)),
                            Integer.toString(randomWords.get(4))},
                    null, null, null);
            cursor.moveToFirst();
            int i = 0;
            words = new HashMap<>();
            russian = new ArrayList<>();
            while (cursor.isAfterLast() == false) {
                words.put(cursor.getString(0), cursor.getString(1));
                listButtons.get(i).setText(cursor.getString(0));
                russian.add(cursor.getString(1));
                i++;
                cursor.moveToNext();
            }
            Collections.shuffle(russian);
            for (int j = 0; j < 5; j++) {
                listButtons.get(j+5).setText(russian.get(j));
            }
            cursor.close();
            db.close();

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void russianClicked(View v) {
        Button btn = (Button)v;
        //if (currentEngish.equals("")) {
          //  currentEngish = btn.getText().toString();
        //} else {
            if (currentEngish != null && words.get(currentEngish) != null && words.get(currentEngish).equals(btn.getText())) {
                btn.setVisibility(View.GONE);
                for (int i = 0; i < 5; i++) {
                    if ((listButtons.get(i).getText()).equals(currentEngish)) {
                        listButtons.get(i).setVisibility(View.GONE);
                    }
                }
                for (int i = 0; i < 5; i++) {
                    if (listButtons.get(i).getVisibility() != View.GONE) {
                        break;
                    } else if (i == 4) {
                        round();
                    }
                }
                currentEngish = "";
            } else {
                resultTextView.setText(Integer.toString(Integer.parseInt(resultTextView.getText().toString())+1));
            }
        //}

    }

    public void round() {
        for (int i = 0; i < 10; i++) {
            listButtons.get(i).setVisibility(View.VISIBLE);
            listButtons.get(i).setEnabled(true);
        }
        if (chosenWords.equals("")) {
            rWords();
        } else {
            cWords();
        }
    }

    public void englishClicked(View v) {
        for (int i = 0; i < 5; i++) {
            listButtons.get(i).setEnabled(true);
        }
        Button btn = (Button)v;
        currentEngish = (String)btn.getText();
        btn.setEnabled(false);
    }
}
