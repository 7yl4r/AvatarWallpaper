package edu.usf.eng.pie.avatars4change.storager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;
import edu.usf.eng.pie.avatars4change.wallpaper.Layer_Background;
import edu.usf.eng.pie.avatars4change.wallpaper.avatarWallpaper;

public class Sdcard {
	private static final String TAG = "storager.Sdcard";
	public static void onStart(){
		//Sdcard.setupCardReceiver();
	}
	
    public static boolean isPresent(){
    	return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    //delays the thread & shows toast until sdcard is connected
	public static void waitForCard(Context context){
		//delay if no sdCard
		while(!Sdcard.isPresent()){
			missingCardError(context);
			try {
				Thread.sleep(750);		//TODO: wow, this is ugly...
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    //delays the thread & shows toast until sdcard is connected.
	//additional canvas argument allows for printing a warning to the canvas.
	public static void waitForCard(Context context, Canvas c){
		//delay if no sdCard
		while(!Sdcard.isPresent()){
			missingCardError(context,c);
			try {
				Thread.sleep(750);		//TODO: wow, this is ugly...
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static DataOutputStream getVisibilityLog(Context contx, boolean append){
    	//set up the file directory for saving data and retrieving sprites
    	String extStorageDirectory = userData.getFileDir(contx);
    	File   fileDirectory       = new File (extStorageDirectory);
    	
        File dataLogFile = new File(fileDirectory, "dataLog.txt");	//create file
        FileOutputStream outStream = null;
		DataOutputStream dataOut = null;

        if(!fileDirectory.mkdirs()){	//create if directory not exist
        	//if creation of directory fails
        	Log.v(TAG, "creation of directory '"+ fileDirectory +"' fails, already exists?");
        }

		if(append){
			try {
				outStream = new FileOutputStream(dataLogFile, true);	//append
			} catch (FileNotFoundException e) {
				Sdcard.waitForCard(contx);
				try {
					outStream = new FileOutputStream(dataLogFile, true);
				} catch (FileNotFoundException E){
					E.printStackTrace();
				}
			}
			dataOut = new DataOutputStream(outStream);
        } else { //do not append
        	try {
        		outStream = new FileOutputStream(dataLogFile, false);	
				dataOut = new DataOutputStream(outStream);
				//print header on data file
				try {
					dataOut.writeBytes("StartVisible,EndVisible,ViewTime,animationName\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d(TAG, "New dataLog file has been created");
				avatarWallpaper.keepLogs = true;	//change the flag so that logs won't be reset every time
			} catch (FileNotFoundException e) {
				// TODO
				e.printStackTrace();
			}
        }
		return dataOut;
	}
	
	// tells the user that the sdcard is not reachable.
	private static void missingCardError(Context context){
		Log.d(TAG,"waiting for sdCard...");
		Toast.makeText(context, "avatarWallpaper searching for sdCard", Toast.LENGTH_SHORT).show();
	}
	
	// tells the user that the sdcard is not reachable.
	// extra canvas argument used to print error to canvas.
    private static void missingCardError(Context context, Canvas c){
    	missingCardError(context);
		if (c != null){	// show error on the canvas if given
			c.save();
			Layer_Background.draw(c);
		    Paint mPaint = new Paint();
		    mPaint.setColor(Color.BLACK); 
			mPaint.setTextSize(30); 
			//mPaint.setStrokeWidth(2);
			mPaint.setTypeface(Typeface.DEFAULT);
			c.drawText("cannot detect SD card", -90, 90, mPaint); 
			c.restore();
		}
    }
  
/* This is the alternate method for defining Sdcard.isPresent which uses a broadcast receiver.
 * Testing showed that this wasn't reliable, so the more clumsy way is used, but this is retained 
 * in case we want to switch back.

    public static boolean sdPresent = false;

	public static void setupCardReceiver(){
		//register sdCard connect receiver
 		IntentFilter conFilter = new IntentFilter (Intent.ACTION_MEDIA_MOUNTED); 
 		conFilter.addDataScheme("file"); 
 		registerReceiver(this.SDconnReceiver, new IntentFilter(conFilter));
 		//register sdCard remove receiver
 		IntentFilter remFilter = new IntentFilter (Intent.ACTION_MEDIA_MOUNTED); 
 		remFilter.addDataScheme("file"); 
 		registerReceiver(this.SDremovReceiver, new IntentFilter(remFilter));
    }
    
    //SD card connected receiver
    private BroadcastReceiver SDconnReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
        sdPresent = true;
        }
    }; 

    //SD card removed receiver
    private BroadcastReceiver SDremovReceiver = new BroadcastReceiver(){
         @Override
         public void onReceive(Context arg0, Intent intent) {
         sdPresent = false;
         }
     }; 
     */
}
