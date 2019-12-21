package com.firda.improveyourenglish;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranslationFragment extends Fragment {

    private static final String TAG = "MainActivity";
    List<String> chosenWords;
    ListView listWords;
    SQLiteOpenHelper englishDatabaseHeleper;
    SQLiteDatabase db;

    public TranslationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_translation, container, false);
        final TextView translate = layout.findViewById(R.id.textView2);
        final EditText word = layout.findViewById(R.id.editText);
        ImageView search = layout.findViewById(R.id.imageView);
        listWords = layout.findViewById(R.id.list_words);
        englishDatabaseHeleper = new EnglishDatabaseHelper(getActivity());
        db = englishDatabaseHeleper.getReadableDatabase();
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Cursor cursor = db.query("WORD",
                            new String[]{"ENGLISH", "RUSSIAN"},
                            "ENGLISH = ?",
                            new String[] {word.getText().toString().toLowerCase()},
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        translate.setText("Translation\n\n" + cursor.getString(1));
                    } else {
                        translate.setText("Translation\n\nNo result");
                    }
                } catch (SQLiteException e) {
                    Toast toast = Toast.makeText(getContext(),
                            "Database unavailable",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        ImageView clear = layout.findViewById(R.id.imageView2);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word.setText("");
            }
        });
        ImageView add = layout.findViewById(R.id.imageView3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = englishDatabaseHeleper.getWritableDatabase();
                SQLiteDatabase dbb = englishDatabaseHeleper.getReadableDatabase();
                Cursor cursor = dbb.query("WORD",
                        new String[]{"ENGLISH", "RUSSIAN"},
                        "ENGLISH = ?",
                        new String[] {word.getText().toString().toLowerCase()},
                        null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    //Log.d(TAG, cursor.getString(1));
                    Cursor cursor1 = dbb.query("CHOSEN",
                            new String[]{"ENGLISH", "RUSSIAN"},
                            "ENGLISH = ?",
                            new String[] {word.getText().toString().toLowerCase()},
                            null, null, null);
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        //Log.d(TAG, cursor1.getString(1));
                        Toast toast = Toast.makeText(getContext(),
                                "Already exist",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        ContentValues wordValues = new ContentValues();
                        wordValues.put("ENGLISH", word.getText().toString().toLowerCase());
                        wordValues.put("RUSSIAN", cursor.getString(1));
                        db.insert("CHOSEN", null, wordValues);
                        Toast toast = Toast.makeText(getContext(),
                                "Was added",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        updateListView();
                    }

                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Was not added",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        updateListView();
        return layout;
    }

    public void updateListView() {
        chosenWords = new ArrayList<>();

        try {
            Cursor cursor = db.query("CHOSEN",
                    new String[]{"ENGLISH", "RUSSIAN"},
                    null,
                    null,
                    null, null, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                chosenWords.add(cursor.getString(0) + " - " + cursor.getString(1));
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                chosenWords);
        listWords.setAdapter(listAdapter);
    }


}
