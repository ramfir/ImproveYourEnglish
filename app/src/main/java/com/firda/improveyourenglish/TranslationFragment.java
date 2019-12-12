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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranslationFragment extends Fragment {

    private static final String TAG = "MainActivity";

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
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                SQLiteOpenHelper englishDatabaseHeleper = new EnglishDatabaseHelper(getContext());
                try {
                    SQLiteDatabase db = englishDatabaseHeleper.getReadableDatabase();
                    Cursor cursor = db.query("WORD",
                            new String[]{"ENGLISH", "RUSSIAN"},
                            "ENGLISH = ?",
                            new String[] {word.getText().toString().toLowerCase()},
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        Log.d(TAG, cursor.getString(1));
                        translate.setText("Translation\n\n" + cursor.getString(1));
                    } else {
                        translate.setText("Translation\n\nNo result");
                        Log.d(TAG, "fffffffffffffff");
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
                SQLiteOpenHelper englishDatabaseHeleper = new EnglishDatabaseHelper(getActivity());
                SQLiteDatabase db = englishDatabaseHeleper.getWritableDatabase();
                SQLiteDatabase dbb = englishDatabaseHeleper.getReadableDatabase();
                Cursor cursor = dbb.query("WORD",
                        new String[]{"ENGLISH", "RUSSIAN"},
                        "ENGLISH = ?",
                        new String[] {word.getText().toString().toLowerCase()},
                        null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d(TAG, cursor.getString(1));
                    ContentValues wordValues = new ContentValues();
                    wordValues.put("ENGLISH", word.getText().toString().toLowerCase());
                    wordValues.put("RUSSIAN", cursor.getString(1));
                    db.insert("CHOSEN", null, wordValues);
                    cursor.close();
                    db.close();
                    dbb.close();
                    Toast toast = Toast.makeText(getContext(),
                            "Was added",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //translate.setText("Translation\n\nNo result");
                    Toast toast = Toast.makeText(getContext(),
                            "Was not added",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "fffffffffffffff");
                }

                //word.setText("");
            }
        });
        return layout;
    }


}
