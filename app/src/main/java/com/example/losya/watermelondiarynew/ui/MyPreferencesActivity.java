package com.example.losya.watermelondiarynew.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.example.losya.watermelondiarynew.R;


public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Preference myPref = findPreference("btheme");
//            if (myPref) {
//                getListView().setBackgroundColor(Color.parseColor("#786478"));
//            }
//        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                return true;
//            }
//        });
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean lightPref = prefs.getBoolean("btheme", true);
        if (lightPref) {
            getListView().setBackgroundColor(Color.parseColor("#786478"));
        }
    }
}