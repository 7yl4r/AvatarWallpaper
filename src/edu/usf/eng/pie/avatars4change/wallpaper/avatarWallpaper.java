package edu.usf.eng.pie.avatars4change.wallpaper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import ly.count.android.api.Countly;

import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Location;
import edu.usf.eng.pie.avatars4change.dataInterface.countlyInterface;
import edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.Globals;
import edu.usf.eng.pie.avatars4change.wallpaper.Layer_Main;
import edu.usf.eng.pie.avatars4change.storager.Sdcard;

// This animated wallpaper draws a virtual avatar animation from png images saved on the sd card
 
public class avatarWallpaper extends WallpaperService {
	private static final String TAG                    = "avatarWallpaper";	//for logs
    private final Handler mHandler              = new Handler();
    private final String[] mLabels              = {"still", "walking", "running"};
    public static float desiredFPS              = 10;
    public static boolean wifiOnly              = false;	//enable if program should only use wifi
    
    public static Avatar    theAvatar;

	//vars for background visibility logging
	long    visibilityStart;
	public static boolean keepLogs              = true;
	public static long lastLogTime = 0;
    
    @Override
    public void onCreate() {
    	super.onCreate();
        Log.d(TAG,"application started with context =" + getApplicationContext().toString());

    	Sdcard.waitForReady(getApplicationContext());
    	
        avatarSetup();
        
        //TODO: if setting countlyLogging == true
        countlySetup();
        
        //TODO: setting to choose built-in classifier (myRunsDataCollector) or mMonitor here
        // then do setup for chosen, then register receiver
    	PAcollectorSetup();

    	checkForFirstTime();
 		
    	Sdcard.onStart();
    }


	//sets up the avatar (called in onCreate)
	private void avatarSetup(){
        theAvatar = new Avatar(new Location(0,0,0,300,0), 3,"sleeping", getApplicationContext());		//create new avatar
        avatarWallpaperSettings.loadPrefs(avatarWallpaper.this.getSharedPreferences(getString(R.string.shared_prefs_name), 0));
	}
	
	//sets up countly server (called in onCreate)
	private void countlySetup(){
    	//set up countly:
    	String appKey        = "301238f5cbf557a6d4f80d4bb19b97b3da3a22ca";
    	String serverURL     = "http://testSubDomain.socialvinesolutions.com";
    	Countly.sharedInstance().init(getApplicationContext(), serverURL, appKey);
    	
        //start up countly
    	Countly.sharedInstance().onStart();// in onStart.
    	
    	countlyInterface.startSendingData();
	}
	
	//sets up the physical activity collector activity (called in onCreate)
	private void PAcollectorSetup(){
    	//setup the PA collector service:
    	Intent mServiceIntent = new Intent(getApplicationContext(), edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.ServiceSensors.class);
 		int activityId = Globals.SERVICE_TASK_TYPE_CLASSIFY;	//TODO: ?
 		String label = mLabels[activityId];
 		Bundle extras = new Bundle();
 		extras.putString("label", label);
 		extras.putString("type", "collecting");
 		mServiceIntent.putExtras(extras);
 		Log.v(TAG, "starting SensorService");
 		startService(mServiceIntent); 
	}
	
	//checks for first time run (by looking for files) and runs appropriate setup if needed
	private void checkForFirstTime(){
 		boolean firstTime;
 		File file = new File(Sdcard.getFileDir(getApplicationContext()), "dataLog.txt" );
 		if (file.exists()) {
 			firstTime = false;
 		}else{
 			firstTime = true;
 		}
 		if(firstTime){
 			Log.v(TAG,"running 1st time setup");
	    	//run initial setup activity
 			Intent i = new Intent(getApplicationContext(), edu.usf.eng.pie.avatars4change.wallpaper.AvatarWallpaperSetup.class);
 			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 			startActivity(i);
 		} //else assume that everything is in working order
	}
	


    @Override
    public void onDestroy() {
        super.onDestroy();
        //shut down countly
       	Countly.sharedInstance().onStop(); // in onStop.
    }

    @Override
    public Engine onCreateEngine() {
		return new DrawEngine();
    }
    
    // All parts needed to draw the output go in this function
    class DrawEngine extends Engine {
        	
        //vars for the avatar
        Resources r                = getResources();
        
        //vars for canvas
        private float mCenterX;
        private float mCenterY;
// height&width are const now...
//        private float mHeight;
//        private float mWidth;
        
//        //vars for touchPoint circle
//        private float mTouchX = -1;
//        private float mTouchY = -1;

    	//vars for offset based on home screen location

//        private float mOffset;
        
        private final Runnable mDrawViz = new Runnable() {
        	private void updateSceneBehavior(){
        		sceneBehaviors.runBehavior(getApplicationContext(),theAvatar);
        	}
        	
            public void run() {
            		updateSceneBehavior();
                	drawFrame();                	
            }
        };
            
        private boolean mVisible;
        
        DrawEngine() {

//            mStartTime = System.currentTimeMillis();	//set app start time
//            lastActivityLevelChangeDay = Time.getJulianDay(mStartTime, TimeZone.getDefault().getRawOffset()); 	//initialize to app start
                        
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //// By default we don't get touch events, so enable them.
            //setTouchEventsEnabled(true);

            //load the preferences
            avatarWallpaperSettings.loadPrefs(avatarWallpaper.this.getSharedPreferences(getString(R.string.shared_prefs_name), 0));
            
            //set up the scene
            Layer_Main.setup(theAvatar);
        }

        @Override
        public void onDestroy() {
        	//TODO: save prefs here?
            super.onDestroy();
            mHandler.removeCallbacks(mDrawViz);
        }

        private void logVisibilityData(final boolean visible){
        	mVisible = visible;
            if (visible) {
            	drawFrame();
                visibilityStart = System.currentTimeMillis();
            } else {
                mHandler.removeCallbacks(mDrawViz);
                Long visibilityEnd = System.currentTimeMillis();
                long visibleTime = visibilityEnd - visibilityStart;
                
            	if(Sdcard.storageReady()){
	                //create or open dataLog file:
	                DataOutputStream dataOut = null;
	                if(keepLogs){
						dataOut = Sdcard.getVisibilityLog(getApplicationContext(),true);
	                } else {
	                	dataOut = Sdcard.getVisibilityLog(getApplicationContext(),false);
	                }

	                //write time viewed to file
	                try {
						dataOut.writeBytes(String.valueOf(visibilityStart)+","+String.valueOf(visibilityEnd)+","+String.valueOf(visibilityEnd-visibilityStart)+
								"," + theAvatar.getActivityName() + "\n");
						Log.d(TAG, visibleTime + " ms of time added to file");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                try {
						dataOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            logVisibilityData(visible);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            getSurfaceInfo(height,width);
            drawFrame();
        }

        private void getSurfaceInfo(int height, int width){
            // store the center of the surface, so we can draw in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            /*
             * size is now constant
            mHeight = height;
            mWidth = width; 
            int s = Math.round(Math.min(mHeight,mWidth)*0.8f);
        	//Log.d(TAG,"onSurfaceChanged makes avatar size " + theAvatar.);
            theAvatar.setSize(s);
            */
        }
        
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawViz);
        }

        /*
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
        }
        */

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
/*        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }
        */
        
        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	c.save();
                	c.translate(mCenterX, mCenterY);
                	boolean sdPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                	if(sdPresent){
                		Layer_Main.nextFrame();
	                	Layer_Main.draw(c, holder.getSurfaceFrame(),theAvatar);
                	}else{//SDcard not present
                		Sdcard.waitForReady(getApplicationContext(),c);
                	}
                	c.restore();
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawViz);
            if (mVisible) {
                mHandler.postDelayed(mDrawViz, Math.round( 1000 / desiredFPS ));
            }
        }

    }
}
