package edu.usf.PIE.tylar.MirrorMe.cube2;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import edu.usf.PIE.tylar.MirrorMe.R;

public class CubeWallpaper2Settings extends PreferenceActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                CubeWallpaper2.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.cube2_settings);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onDestroy();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
    }
}
