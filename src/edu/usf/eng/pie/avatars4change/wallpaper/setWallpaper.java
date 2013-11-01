package edu.usf.eng.pie.avatars4change.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class setWallpaper extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	// sets the wallpaper on the phone
/*
        try {		
            Intent intent = new Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            ComponentName component = new ComponentName(this,
                    avatarWallpaper.class);

            intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    component);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
        	Log.d(TAG,"Error setting wallpaper:"+e.getMessage());
            Toast.makeText(this, "Error setting wallpaper",
                    Toast.LENGTH_SHORT).show();
        }
        */
        	
    	Toast toast = Toast.makeText(this, "Choose 'Avatars4Change Avatar' from the list to set the Live Wallpaper.",Toast.LENGTH_LONG);
    	toast.show();

    	Intent intent = new Intent();
    	intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
    	startActivity(intent);
    	finish();
	}
}
