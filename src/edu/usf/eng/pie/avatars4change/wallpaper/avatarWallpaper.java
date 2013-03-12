package edu.usf.eng.pie.avatars4change.wallpaper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import ly.count.android.api.Countly;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Location;
import edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.Globals;
import edu.usf.eng.pie.avatars4change.userData.userData;
import edu.usf.eng.pie.avatars4change.wallpaper.Layer_Main;

// This animated wallpaper draws a virtual avatar animation from png images saved on the sd card
 
public class avatarWallpaper extends WallpaperService {
	private final String TAG = "avatarWallpaper";
	public static final String SHARED_PREFS_NAME="avatarsettings";
    private final Handler mHandler = new Handler();
    private Context mContext;
    private final String[] mLabels = {"still", "walking", "running"};
    public static float desiredFPS = 30;
    
    @Override
    public void onCreate() {
    	
    	mContext = getApplicationContext();
    	//set up countly:
    	String appKey        = "301238f5cbf557a6d4f80d4bb19b97b3da3a22ca";
    	String serverURL     = "http://testSubDomain.socialvinesolutions.com";
    	Countly.sharedInstance().init(mContext, serverURL, appKey);
    	
    	//setup the PA collector service:
    	Intent mServiceIntent = new Intent(mContext, edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.ServiceSensors.class);
 		int activityId = Globals.SERVICE_TASK_TYPE_CLASSIFY;	//TODO: ?
 		String label = mLabels[activityId];
 		Bundle extras = new Bundle();
 		extras.putString("label", label);
 		extras.putString("type", "collecting");
 		mServiceIntent.putExtras(extras);
 		Log.v(TAG, "starting SensorService");
 		startService(mServiceIntent); 
    	
 		//check for first time run (by looking for files)
 		boolean firstTime;
 		File file = new File(Environment.getExternalStorageDirectory()+"/MirrorMe/", "dataLog.txt" );
 		if (file.exists()) {
 			firstTime = false;
 		}else{
 			firstTime = true;
 		}
 		if(firstTime){
 			Log.v(TAG,"running 1st time setup");
	    	//run intial setup activity
 			Intent i = new Intent(getApplicationContext(), edu.usf.eng.pie.avatars4change.wallpaper.AvatarWallpaperSetup.class);
 			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 			startActivity(i);
 		} //else assume that everything is in working order
 		
    	super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new DrawEngine();
    }

    // All parts needed to draw the output go in this function
    class DrawEngine extends Engine 
    	implements SharedPreferences.OnSharedPreferenceChangeListener {

    	//vars for logging
    	long    visibilityStart;
    	boolean keepLogs        = true;
    	
    	//set up the file directory for saving data and retrieving sprites
    	String extStorageDirectory = Environment.getExternalStorageDirectory()+"/MirrorMe";
    	File   fileDirectory       = new File (extStorageDirectory);
    	
        //vars for the avatar
        Resources r                   = getResources();
        Avatar    theAvatar           = new Avatar(new Location(0,0,0,300,0), 3, "running");		//create new avatar

        
        //vars for canvas
        private float mCenterX;
        private float mCenterY;
        private float mHeight;
        private float mWidth;
        
//        //vars for touchPoint circle
//        private float mTouchX = -1;
//        private float mTouchY = -1;

    	//vars for offset based on home screen location
//        private float mOffset;
        
        //vars for frame rate
//        private long   mStartTime;	//time of app start
//        private int    lastActivityLevelChangeDay;
//        private String lastActivityLevel          = "active";
//        private long   lastTime                   = 0;	//time measurement for calculating deltaT and thus fps
        
        private final Runnable mDrawViz = new Runnable() {
        	private void updateSceneBehavior(){
        		sceneBehaviors.runBehavior(theAvatar);
        	}
        	
            public void run() {
                	
            		updateSceneBehavior();
                	drawFrame();
                	
            }
        };
            
        private boolean mVisible;
        private SharedPreferences mPrefs;
        
        DrawEngine() {
//            mStartTime = System.currentTimeMillis();	//set app start time
//            lastActivityLevelChangeDay = Time.getJulianDay(mStartTime, TimeZone.getDefault().getRawOffset()); 	//initialize to app start
            
            mPrefs = avatarWallpaper.this.getSharedPreferences(SHARED_PREFS_NAME, 0);	//load settings
            
            //register reciever for changed settings:
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPrefs, null);
        }
        
        private void loadPrefs(){
			Log.d("MirrorMe Avatar", "loading preferences");
				String key;
				key="RealismLevel";
					theAvatar.setRealismLevel(Integer.parseInt(mPrefs.getString(key, Integer.toString(theAvatar.getRealismLevel()))));
				key="CurrentActivity";
					theAvatar.setActivityName(mPrefs.getString(key, "inBed"));
					theAvatar.lastActivityChange = SystemClock.elapsedRealtime();
				key="ActivityLevelSelector";
					theAvatar.behaviorSelectorMethod = mPrefs.getString(key, theAvatar.behaviorSelectorMethod);
				key="ResetLogs";
					keepLogs = !mPrefs.getBoolean(key, keepLogs);
					//Log.d("MirrorMe Avatar", "keepLogs=" + String.valueOf(keepLogs));
				key="activeOnEvens";
					sceneBehaviors.activeOnEvens = mPrefs.getBoolean(key, sceneBehaviors.activeOnEvens);
				key="UID";
					userData.USERID = mPrefs.getString(key,"defaultUserID");
        }
        
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			Log.d("MirrorMe Avatar", "adjusting " + key + " preference");
			if(! (key == null)){	//skip if null
				if(key.equals("RealismLevel")){
					theAvatar.setRealismLevel(Integer.parseInt(prefs.getString(key, Integer.toString(theAvatar.getRealismLevel()))));
				} else if (key.equals("CurrentActivity")){
					theAvatar.setActivityName(prefs.getString(key, "inBed"));
					theAvatar.lastActivityChange = SystemClock.elapsedRealtime();
				} else if (key.equals("ActivityLevelSelector")){
					theAvatar.behaviorSelectorMethod = prefs.getString(key, theAvatar.behaviorSelectorMethod);
				} else if (key.equals("ResetLogs")){
					keepLogs = !prefs.getBoolean(key, keepLogs);
					//Log.d("MirrorMe Avatar", "keepLogs=" + String.valueOf(keepLogs));
				} else if (key.equals("activeOnEvens")){
					sceneBehaviors.activeOnEvens = prefs.getBoolean(key, sceneBehaviors.activeOnEvens);
				} else if (key.equals("behavior")){
					theAvatar.behaviorSelectorMethod = prefs.getString(key, theAvatar.behaviorSelectorMethod);
				} else { 
					Log.e(TAG,"unrecognized pref key: " + key);
				}
			}
		}

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //// By default we don't get touch events, so enable them.
            //setTouchEventsEnabled(true);

            //load the preferences
            loadPrefs();
            
            //set up the scene
            Layer_Main.setup(theAvatar);
        }

        @Override
        public void onDestroy() {
        	//TODO: save prefs here
            super.onDestroy();
            mHandler.removeCallbacks(mDrawViz);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
            	
            	Countly.sharedInstance().onStart();// in onStart.

     			drawFrame();
     			
                visibilityStart = System.currentTimeMillis();
            } else {
            	
            	Countly.sharedInstance().onStop(); // in onStop.
            	
                mHandler.removeCallbacks(mDrawViz);
                Long visibilityEnd = System.currentTimeMillis();
                long visibleTime = visibilityEnd - visibilityStart;
                File dataLogFile = new File(fileDirectory, "dataLog.txt");	//create file
                
                if(!fileDirectory.mkdirs()){	//create if directory not exist
                	//if creation of directory fails
                	Log.v("MirrorMe Avatar", "creation of directory fails, already exists?");
                }
                //create or open dataLog file:
                FileOutputStream dataFileOut = null;
                if(keepLogs){
					try {
						dataFileOut = new FileOutputStream(dataLogFile, true);	//append
					} catch (FileNotFoundException e) {
						// TODO
						e.printStackTrace();
					}
                } else {
                	try {
						dataFileOut = new FileOutputStream(dataLogFile, false);	//do not append
						DataOutputStream dataOut = new DataOutputStream(dataFileOut);
						//print header on data file
						try {
							dataOut.writeBytes("StartVisible,EndVisible,ViewTime,animationName\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.d("MirrorMe Avatar", "New dataLog file has been created");
						keepLogs = true;
					} catch (FileNotFoundException e) {
						// TODO
						e.printStackTrace();
					}
                }
				DataOutputStream dataOut = new DataOutputStream(dataFileOut);
				//write time viewed to file
                try {
					dataOut.writeBytes(String.valueOf(visibilityStart)+","+String.valueOf(visibilityEnd)+","+String.valueOf(visibilityEnd-visibilityStart)+
							"," + theAvatar.getActivityName() + "\n");
					Log.d("MirrorMe Avatar", visibleTime + " ms of time added to file");
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

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            mHeight = height;
            mWidth = width;
            //TODO: MOVE THIS:
            int s = Math.round(Math.min(mHeight,mWidth)*0.9f);
            theAvatar.setSize(s);
            

		   drawFrame();

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
                	desiredFPS = Math.round( (Math.exp(userData.currentActivityLevel))*4-3 );//update frameRate from PA level
                	
				   	Layer_Main.nextFrame();

                	c.save();
                	c.translate(mCenterX, mCenterY);
                	Layer_Main.draw(c);
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