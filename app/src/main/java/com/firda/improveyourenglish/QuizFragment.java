package com.firda.improveyourenglish;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {


    public QuizFragment() {
        // Required empty public constructor
    }
    private static final String TAG = "MainActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_quiz, container, false);
        Button randomWords = layout.findViewById(R.id.button);
        randomWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });
        Button chosenWords = layout.findViewById(R.id.button2);
        chosenWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteOpenHelper englishDatabaseHeleper = new EnglishDatabaseHelper(getActivity());
                try {
                    SQLiteDatabase db = englishDatabaseHeleper.getReadableDatabase();
                    int size = ((EnglishDatabaseHelper) englishDatabaseHeleper).getSize(db, "CHOSEN");
                    if (size >= 5) {
                        Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                        intent.putExtra("KEY", "1");
                        getActivity().startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getActivity(),
                                "Not enough chosen words. Should be more than 5",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (SQLiteException e) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Database unavailable",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        Log.d(TAG, "DD");
        return layout;
    }


}
