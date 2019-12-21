package com.firda.improveyourenglish;

import android.content.Intent;
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
import android.view.WindowManager;

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
