package com.firda.improveyourenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); // made up toolbar
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // keyboard doesn't appear over

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        receiveDataFromFragment();
        receiveDataFromTopButton();

        // тест
        //new UpdateDrinkTask().execute();
    }
    /*private class UpdateDrinkTask extends AsyncTask<Void, Void, URLConnection> {
        protected URLConnection doInBackground(Void... params) {
            try {
                URLConnection connection = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20191222T210710Z.4eb9d958c35aef14.8f1b0ca08c0e5b7eac1b7ae19b6ed0ea57002387&text=sadfsadf&lang=en-ru&format=plain&options=1").openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                char[] buffer = new char[256];
                int rc;

                StringBuilder sb = new StringBuilder();

                while ((rc = reader.read(buffer)) != -1)
                    sb.append(buffer, 0, rc);

                reader.close();

                Log.d(TAG, sb.toString());
                Object obj = new JSONParser().parse(sb.toString());
                JSONObject jo = (JSONObject) obj;
                JSONArray  firstName = (JSONArray) jo.get("text");
                Iterator phonesItr = firstName.iterator();
                while (phonesItr.hasNext()) {
                    Log.d(TAG, (String) phonesItr.next());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            hideKeyboard();
            switch (position) {
                case 0:
                    return new TranslationFragment();
                case 1:
                    return new QuizFragment();
                case 2:
                    return new AboutAppFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.TranslationFragment);
                case 1:
                    return getResources().getText(R.string.QuizFragment);
                case 2:
                    return "About";
            }
            return null;
        }
    }
    private void receiveDataFromFragment()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();

        String chosen = i.getStringExtra("KEY");
        if (chosen != null) {
            pager.setCurrentItem(1);
            int ch = Integer.parseInt(chosen);
            ch = ch*2;
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("KEY", chosen);
            Log.d(TAG, chosen);
            Log.d(TAG, Integer.toString(ch));
            startActivity(intent);
        }

    }
    private void receiveDataFromTopButton() {
        Intent i = getIntent();
        String chosen = i.getStringExtra("STRING_TO_PASS");
        if (chosen != null) {
            pager.setCurrentItem(1);
        }
    }
}
