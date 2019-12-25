package com.firda.improveyourenglish;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranslationFragment extends Fragment {

    private static final String TAG = "MainActivity";
    List<String> chosenWords;
    ListView listWords;
    SQLiteOpenHelper englishDatabaseHeleper;
    SQLiteDatabase db;
    TextView translate;
    EditText word;

    public TranslationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_translation, container, false);
        translate = layout.findViewById(R.id.textView2);
        word = layout.findViewById(R.id.editText);
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
                        translate.setText("Translation\n\nNo result. Turn on wifi and repeat");
                        yandexAPI();
                    }
                } catch (SQLiteException e) {
                    Toast toast = Toast.makeText(getContext(),
                            "Database unavailable",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                hideKeyboard(getActivity());

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
                hideKeyboard(getActivity());
            }
        });
        updateListView();
        //new UpdateDrinkTask().execute();
        return layout;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void yandexAPI() {
        new UpdateDrinkTask().execute(String.valueOf(word.getText()));
    }
    private class UpdateDrinkTask extends AsyncTask<String, Void, String> {
        String edText = "";
        protected String doInBackground(String... params) {
            try {
                String text = params[0];
                edText = params[0];
                URLConnection connection = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20191222T210710Z.4eb9d958c35aef14.8f1b0ca08c0e5b7eac1b7ae19b6ed0ea57002387&text="+text+"&lang=en-ru&format=plain&options=1").openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                char[] buffer = new char[256];
                int rc;
                StringBuilder sb = new StringBuilder();
                while ((rc = reader.read(buffer)) != -1)
                    sb.append(buffer, 0, rc);
                reader.close();
                Object obj = new JSONParser().parse(sb.toString());
                JSONObject jo = (JSONObject) obj;
                JSONArray firstName = (JSONArray) jo.get("text");
                Iterator phonesItr = firstName.iterator();
                while (phonesItr.hasNext()) {
                    return (String) phonesItr.next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String s) {
            if (s != null && !s.equals(edText)) {
                translate.setText("Translation\n\n" + s);
                SQLiteDatabase db = englishDatabaseHeleper.getWritableDatabase();
                ContentValues wordValues = new ContentValues();
                wordValues.put("ENGLISH", edText.toLowerCase());
                wordValues.put("RUSSIAN", s);
                db.insert("WORD", null, wordValues);
            }

        }
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
