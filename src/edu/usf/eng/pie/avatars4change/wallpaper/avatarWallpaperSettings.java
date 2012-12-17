package edu.usf.eng.pie.avatars4change.wallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import edu.usf.eng.pie.avatars4change.R;

public class avatarWallpaperSettings extends PreferenceActivity 
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        getPreferenceManager().setSharedPreferencesName(avatarWallpaper.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.avatar_settings);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	Log.d("MirroMe Avatar", key + " preferenence changed");
    }
}
