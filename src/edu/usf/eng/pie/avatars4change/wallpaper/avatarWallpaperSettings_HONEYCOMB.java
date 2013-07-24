package edu.usf.eng.pie.avatars4change.wallpaper;

import java.util.List;

import android.support.v4.app.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.dataInterface.activityMonitor;
import edu.usf.eng.pie.avatars4change.dataInterface.userData;

public class avatarWallpaperSettings extends PreferenceActivity 
    implements SharedPreferences.OnSharedPreferenceChangeListener {
	private static final String TAG = "avatarWallpaperSettings";
	private static final String[] PREFERENCE_KEYS = {
		"killMe",				//1
		"RealismLevel",			//2
		"CurrentActivity",		//3
		"ActivityLevelSelector",//4 is duplicate of 8!!!
		"ResetLogs",			//5
		"activeOnEvens",		//6
		"UID",					//7
		"behavior",				//8
		"wifiOnly",				//9
		"scale",                //10
		"activityMonitor",       //11
		"proMode"
	};	// this is mostly for reference; numbers are for ensuring that all are accounted for in handleKey()
	//DEPRECIATED KEYS:
	//		"ActivityLevelSelector" //4
	
    public static String currentActivityMonitor = "none"; //name of current activity monitor method used
    public static boolean debugMode = true;	//TODO: this is not yet a setting, but should be
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);		
        
        // Add a button to the header list.
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Some action");
            setListFooter(button);
        }
        
//        getPreferenceManager().setSharedPreferencesName(getString(R.string.shared_prefs_name));
//        addPreferencesFromResource(R.xml.avatar_settings);
//        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences);
        }
    }
    
    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first preferences fragment.
     */
    public static class Prefs1FragmentInner extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Can retrieve arguments from preference XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences_inner);
        }
    }

    /**
     * This fragment shows the preferences for the second header.
     */
    public static class Prefs2Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Can retrieve arguments from headers XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference_dependencies);
        }
    }
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	//       groupID  ,itemID,order, title
    	menu.add(Menu.NONE,0     ,0    , "Support");
    	menu.add(Menu.NONE,1     ,1    , "initialSetup");
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
*/
    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    // preference listener is triggered when a preference changes and responds accordingly
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	avatarWallpaperSettings.handleKey(getApplicationContext(),key,sharedPreferences); 
    }
    
    // load in all preferences
    public static void loadPrefs(Context ctx, SharedPreferences sharedPreferences){
		Log.d(TAG, "loading preferences...");
		for ( int i = 0; i<avatarWallpaperSettings.PREFERENCE_KEYS.length; i++ ){
			if(avatarWallpaperSettings.PREFERENCE_KEYS[i].equals("killMe")){	//skip over killMe pref
				continue;
			}else{	// load preference
				avatarWallpaperSettings.handleKey(ctx, avatarWallpaperSettings.PREFERENCE_KEYS[i],sharedPreferences);
			}
		}
    }

    //  responds to the preference key given accordingly
    private static void handleKey(Context ctx, String key, SharedPreferences mPrefs){
    	if(key.equals("killMe")){	//1
    		android.os.Process.killProcess(android.os.Process.myPid());
    		
    	}else if(key.equals("RealismLevel")){	//2
			avatarWallpaper.theAvatar.setRealismLevel((int) mPrefs.getLong(key, avatarWallpaper.theAvatar.getRealismLevel()));
			Log.d(TAG, "RealismLevel:"+avatarWallpaper.theAvatar.getRealismLevel());

    	}else if(key.equals("CurrentActivity")){	//3
			avatarWallpaper.theAvatar.setActivityName(mPrefs.getString(key, "running"));
			avatarWallpaper.theAvatar.lastActivityChange = SystemClock.elapsedRealtime();
			Log.d(TAG, "CurrentActivity:"+avatarWallpaper.theAvatar.getActivityName());
			
			// AcivitiyLevelSelector is the same as behaviorSelector???
    	}else if (key.equals("ActivityLevelSelector")){
    		Log.e(TAG,"use of depreciated settings key ActivityLevelSelector; should use behaviorSelector instead");
    		handleKey(ctx,"behavior",mPrefs);
    		
    	}else if (key.equals("ResetLogs")){	//5
			avatarWallpaper.keepLogs = !mPrefs.getBoolean(key, avatarWallpaper.keepLogs);
			Log.d(TAG, "keepLogs?:"+avatarWallpaper.keepLogs);
			
    	}else if (key.equals("activeOnEvens")){	//6
			sceneBehaviors.setActiveOnEvens(mPrefs.getBoolean(key, sceneBehaviors.getActiveOnEvens()));			
			Log.d(TAG,"activeOnEvens:"+sceneBehaviors.getActiveOnEvens());
			
    	}else if (key.equals("UID")){	//7
			userData.USERID = mPrefs.getString(key,userData.USERID);
			Log.d(TAG,"UID:"+userData.USERID);
			
    	}else if (key.equals("behavior")){	//8
			avatarWallpaper.theAvatar.setBehaviorSelectorMethod(mPrefs.getString(key, avatarWallpaper.theAvatar.behaviorSelectorMethod));
			Log.d(TAG, "behaviorSelector:"+avatarWallpaper.theAvatar.behaviorSelectorMethod);
			
    	}else if (key.equals("wifiOnly")){	//9
			avatarWallpaper.wifiOnly = mPrefs.getBoolean(key,avatarWallpaper.wifiOnly);
			Log.d(TAG,"wifiOnly:"+avatarWallpaper.wifiOnly);
			
    	}else if (key.equals("scale")){		//10
			avatarWallpaper.theAvatar.scaler = Float.parseFloat(mPrefs.getString(key, "1.0f"));
			Log.d(TAG, "RealismLevel:"+avatarWallpaper.theAvatar.getRealismLevel());
			
    	}else if (key.equals(PREFERENCE_KEYS[10])){ //11 = activity monitor selector
    		activityMonitor.setActivityMonitor(ctx,mPrefs.getString(key, avatarWallpaper.theAvatar.behaviorSelectorMethod));
    		userData.resetPAmeasures();	
			Log.d(TAG, "activityMonitor:"+activityMonitor.getActivityMonitor());
    	}else if (key.equals("proMode")){	//12
    		//TODO: start activity or something...
    		Log.d(TAG,"entering proMode");
    	}else{	//unknown preference key
    		Log.e(TAG,"unrecognized preference key '"+key+"'... trying not to panic.");
    		return;
    	} 
    	Log.d(TAG, key + " preference changed..."); //only prints if last 'else' case not triggered
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case 0:
        		displayContactInfo();
        		return true;
            case 1:
                startActivity(new Intent(getApplicationContext(), edu.usf.eng.pie.avatars4change.wallpaper.AvatarWallpaperSetup.class));
                return true;
        }
        return false;
    }
    
    private void displayContactInfo(){
    	AlertDialog.Builder dlg = new AlertDialog.Builder(this);
    	dlg.setMessage("For support please contact " + getString(R.string.contactemail) + "\n"+
    			       "\n" +
    			       "To report issues or for more info please visit our repo " +
    			       "on github.com/7yl4r/AvatarWallpaper");
    	dlg.setTitle("AvatarWallpaper Support");
    	dlg.setPositiveButton("OK", null);
    	dlg.setCancelable(true);
    	dlg.create().show();
    }
}
