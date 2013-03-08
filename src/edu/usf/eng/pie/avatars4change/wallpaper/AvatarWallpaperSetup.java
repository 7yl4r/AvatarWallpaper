package edu.usf.eng.pie.avatars4change.wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.userData.userData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AvatarWallpaperSetup extends Activity{
	private static TextView uidBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
 		//setup the file directory:
    	SetDirectory();
		// start up the face selector
		startActivity(new Intent(getApplicationContext(), com.droid4you.util.cropimage.MainActivity.class));
		// then start up the id name selector
		idChooser();
		// lastly, show privacy disclaimer
		//privacyDisclaimer(); this is called at the end of idChooser
	}

	private void privacyDisclaimer(){
		setContentView(R.layout.privacy_disclaimer);
		// done button
				Button doneBttn = (Button) findViewById(R.id.btn_done);
				doneBttn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}
	private void idChooser(){
		//get default UID
    	TelephonyManager tManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    	userData.USERID = tManager.getDeviceId();
		
		setContentView(R.layout.uid_chooser);
		//show default UID
		uidBox = new TextView(getApplicationContext());
		uidBox = (TextView) findViewById(R.id.text_uid);
		uidBox.setText(userData.USERID);
		// done button
		Button doneBttn = (Button) findViewById(R.id.btn_done);
		doneBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userData.USERID = uidBox.getText().toString();
		    	//set new UID
		    	SharedPreferences settings = getSharedPreferences(avatarWallpaper.SHARED_PREFS_NAME, MODE_PRIVATE);
		    	SharedPreferences.Editor editor = settings.edit();
		    	editor.putString("UID", userData.USERID);
		    	editor.commit();
				
		    	privacyDisclaimer();
			}
		});
		
	}

    /**
     * -- Check to see if the sdCard is mounted and create a directory w/in it
     * ========================================================================
     **/
    private void SetDirectory() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

            File txtDirectory = new File(extStorageDirectory);
            // Create
            // a
            // File
            // object
            // for
            // the
            // parent
            // directory
            txtDirectory.mkdirs();// Have the object build the directory
            // structure, if needed.
            CopyAssets(extStorageDirectory); // Then run the method to copy the file.
            
            //lastly, add .nomedia file
            File nomediaFile = new File(extStorageDirectory + "/MirrorMe/", ".nomedia");
            try {
				nomediaFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

        } else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {
        	Log.e("MirrorMe Asset Copier", "SD card is missing");
            //AlertsAndDialogs.sdCardMissing(this);//Or use your own method ie: Toast
        }
    }

    /**
     * -- Copy the files from the assets folder to the sdCard
     * ===========================================================
     **/
    private void CopyAssets(String extStorageDir) {
        copier("MirrorMe",extStorageDir);
    }
    
    //copy file or directory
    private void copier(String inDir, String extStorageDir){
    	AssetManager assetManager = getAssets();
        String[] files = null;
        Log.v("MirrorMe Avatar", "copying files in " + inDir);
        try {
            files = assetManager.list(inDir);
        } catch (IOException e) {
            Log.e("MirrorMe asset listing", e.getMessage());
        }
        String prefix = inDir;
    	if(!inDir.equals("")){
    		prefix += "/";
    	}
        for (int i = 0; i < files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            String fileName = files[i];
            try {
                in = assetManager.open(prefix + fileName);
            } catch(Exception e){	//failed file open means listing is a directory
            	Log.v("MirrorMe Avatar", files[i] + " is directory");
            	copier(prefix + fileName,extStorageDir);	//add dir name to prefix
            	continue;
            }
            //implied else
            Log.v("MirrorMe Avatar", files[i] + " is file");
            
            File fDir = new File (extStorageDir + "/" + prefix);	//file object for mkdirs
            fDir.mkdirs();	//create directory

            try{	//copy the file
                out = new FileOutputStream(extStorageDir + "/" + prefix + files[i]);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("MirrorMe copyfile", e.getMessage());
            }
        }
    }

    //copy file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
