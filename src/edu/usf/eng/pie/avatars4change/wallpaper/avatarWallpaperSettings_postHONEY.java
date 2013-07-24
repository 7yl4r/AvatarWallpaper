package edu.usf.eng.pie.avatars4change.wallpaper;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.dataInterface.activityMonitor;
import edu.usf.eng.pie.avatars4change.dataInterface.userData;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class avatarWallpaperSettings_postHONEY extends PreferenceActivity {
	public static class SettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        @Override
	        public void onBuildHeaders(List<Header> target) {
	            loadHeadersFromResource(R.xml.preference_headers, target);
	        }
	        
	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.avatar_settings);
	    }
	}
}