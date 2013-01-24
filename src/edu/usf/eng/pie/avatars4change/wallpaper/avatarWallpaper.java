package edu.usf.eng.pie.avatars4change.wallpaper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.TimeZone;

import edu.usf.eng.pie.avatars4change.avatar.Animation;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Location;
import edu.usf.eng.pie.avatars4change.avatar.Scene;
import edu.usf.eng.pie.avatars4change.avatar.Sprite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws a virtual avatar animation from png images saved on the sd card
 */
public class avatarWallpaper extends WallpaperService {

	public static final String SHARED_PREFS_NAME="avatarsettings";
    private final Handler mHandler = new Handler();
    
    @Override
    public void onCreate() {
    	SetDirectory();
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

    /* All parts needed to draw the output go in this function
     */
    class DrawEngine extends Engine 
    	implements SharedPreferences.OnSharedPreferenceChangeListener {

    	//vars for logging
    	long visibilityStart;
    	boolean keepLogs = true;
    	
    	//set up the file directory for saving data and retrieving sprites
    	String extStorageDirectory = Environment.getExternalStorageDirectory()+"/MirrorMe";
    	File fileDirectory = new File (extStorageDirectory);
    	
        //vars for the avatar
    	long lastFrameChange = 0;		//last frame update [ms]
        Resources r = getResources();
        Avatar theAvatar = new Avatar(new Location(0,0,0,300,0), 3, "sleeping");		//create new avatar
        String selectorMethod = "Constant";
        long lastActivityChange = 0;	//last time activity level was changed [ms]
        long deltaActivityChange = 5*1000;	//60*60*1000;	//desired time between activity level updates [ms]
        int bedTime = 23;
        int wakeTime = 5;
        boolean activeOnEvens = true;	//active on even days?
        
        //vars for canvas
        private final Paint mPaint = new Paint();
        private float mCenterX;
        private float mCenterY;
        private float mHeight;
        private float mWidth;
        
        //vars for touchPoint circle
        private float mTouchX = -1;
        private float mTouchY = -1;

    	//vars for offset based on home screen location
        private float mOffset;
        
        //vars for frame rate
        private long mStartTime;	//time of app start
        int lastActivityLevelChangeDay;
        String lastActivityLevel = "active";
        private long lastTime = 0;	//time measurement for calculating deltaT and thus fps
        private float desiredFPS =30;
        private float[] lastFPS = {0,0,0,0,0,0,0,0,0,0};	//saved past 10 fps measurements

        //TODO set up the scene
        Scene mainScene = new Scene("mainScene");
        //Scene testScene = new Scene("testScene");
        
        // === BEGIN TEST CODE SECTION === 
        /*
        //test animation:
        String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
    	String spriteDir = baseFileDirectory + "/sprites";
    	String testFile = spriteDir + "/body/active/running/.";
    	int testSize =  100;
    	int testAngle= -10;
    	int testX    =  0;
    	int testY    = -50;
    	location testLoc = new location(testX, testY, testSize, testAngle);
    	animation testAnimation = new animation("test", testFile, testLoc);
    	
    	//test entity
     	int En_testSize =  200;
     	int En_testAngle= -150;
     	int En_testX    =  33;
     	int En_testY    =  11;
     	location testEntLoc = new location(En_testX, En_testY, En_testSize, En_testAngle);
     	entity testEntity   = new entity("name",testEntLoc);  
     	*/   	
    	
    	// === END TEST CODE SECTION === 
        
        private final Runnable mDrawViz = new Runnable() {
            public void run() {
            	if(isFrameChangeTime()){
	            	//check for enough time to change animation
	            	//TODO: change this next if issue#5 persists
	        		long now = SystemClock.elapsedRealtime();		//TODO: ensure that this works even if phone switched off. 
	                if((now - lastActivityChange) > deltaActivityChange){		//if time elapsed > desired time
	                	//if past bedTime and before wakeTime, sleep
	                    int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	                    Log.v("Avatars4Change Avatar sleep clock", "current hour:" + currentHour);
	                    if(currentHour >= bedTime || currentHour < wakeTime){
	                    	//draw sleeping
	                    	theAvatar.setActivityLevel("sleeping");
	                    } else {	//awake
	                    	int today = Time.getJulianDay(System.currentTimeMillis(), (long) (TimeZone.getDefault().getRawOffset()/1000.0) ); 	//(new Time()).toMillis(false)
	                    	Log.v("Avatars4Change day calculator","time:"+System.currentTimeMillis()+"\ttimezone:"+TimeZone.getDefault().getRawOffset()+"\ttoday:"+today);
	    	            	//set active or passive, depending on even or odd julian day
	    	            	if(today%2 == 0){	//if today is even
	    	            		if(activeOnEvens){
	    	            			theAvatar.setActivityLevel("active");
	    	            		}else{
	    	            			theAvatar.setActivityLevel("passive");
	    	            		}
	    	            	}else{	//today is odd
	    	            		if(!activeOnEvens){	//if active on odd days
	    	            			theAvatar.setActivityLevel("active");
	    	            		}else{
	    	            			theAvatar.setActivityLevel("passive");
	    	            		}
	    	            	}
	                    }
	                	//avatar changes activity 
	                	theAvatar.randomActivity(theAvatar.getActivityLevel());
	               	 	lastActivityChange = now;
	               	 	Log.v("mirrorMe Broadcaster","activity changed; broadcast sent");
	               	 	//test broadcast:
	               	 	Intent intent = new Intent();
	               	 	intent.setAction("com.tylar.research.avatars");
	               	 	sendBroadcast(intent); 
	                }
     			   drawFrame();
     		   } //else display same as last loop
            }
        };
        
        private boolean mVisible;
        private SharedPreferences mPrefs;
        
        DrawEngine() {
            // Create a Paint to draw on
            final Paint paint = mPaint;
            paint.setColor(Color.GRAY); //0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mStartTime = System.currentTimeMillis();	//set app start time
            lastActivityLevelChangeDay = Time.getJulianDay(mStartTime, TimeZone.getDefault().getRawOffset()); 	//initialize to app start
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
					lastActivityChange = SystemClock.elapsedRealtime();
				key="ActivityLevelSelector";
					selectorMethod = mPrefs.getString(key, selectorMethod);
				key="ResetLogs";
					keepLogs = !mPrefs.getBoolean(key, keepLogs);
					//Log.d("MirrorMe Avatar", "keepLogs=" + String.valueOf(keepLogs));
				key="activeOnEvens";
					activeOnEvens = mPrefs.getBoolean(key, activeOnEvens);
        }
        
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			Log.d("MirrorMe Avatar", "adjusting " + key + " preference");
			if(! (key == null)){	//skip if null
				if(key.equals("RealismLevel")){
					theAvatar.setRealismLevel(Integer.parseInt(prefs.getString(key, Integer.toString(theAvatar.getRealismLevel()))));
				}
				if (key.equals("CurrentActivity")){
					theAvatar.setActivityName(prefs.getString(key, "inBed"));
					lastActivityChange = SystemClock.elapsedRealtime();
				}
				if (key.equals("ActivityLevelSelector")){
					selectorMethod = prefs.getString(key, selectorMethod);
				}
				if (key.equals("ResetLogs")){
					keepLogs = !prefs.getBoolean(key, keepLogs);
					//Log.d("MirrorMe Avatar", "keepLogs=" + String.valueOf(keepLogs));
				}
				if(key.equals("activeOnEvens")){
					activeOnEvens = prefs.getBoolean(key, activeOnEvens);
				}
			}
		}

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //// By default we don't get touch events, so enable them.
            //setTouchEventsEnabled(true);

            loadPrefs();	//load the preferences
          //TODO set up the scene
            //setupTestScene();
            setupMainScene();

        }
        public void setupMainScene(){
         	mainScene.addEntity(theAvatar);
        }
        /*
        public void setupTestScene(){
        	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
        	String spriteDir = baseFileDirectory + "/sprites";
        	String testFile = spriteDir + "/body/active/basketball/.";
        	int testSize =  150;
        	int testAngle= -45;
        	int testX    = -150;
        	int testY    =  300;
        	Location testLoc = new Location(testX, testY, testSize, testAngle);
        	Animation tAnimation = new Animation("tAnim", testFile, testLoc);
         	testScene.addAnimation(tAnimation);
         	Sprite tsprite = new Sprite("tSprite", testFile+"3.png", new Location(0,0,1000,0));
         	testScene.addSprite(tsprite);
        }
        */

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawViz);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
            	if(isFrameChangeTime()){
     			   drawFrame();
     		    } //else display same as last loop
                visibilityStart = System.currentTimeMillis();
            } else {
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
            
		   if(isFrameChangeTime()){
			   drawFrame();
		   } //else display same as last loop
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

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            if(isFrameChangeTime()){
 			   drawFrame();
 		   } //else display same as last loop
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
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
				   	mainScene.nextFrame();
				   	//testScene.nextFrame();
				   	
                	drawBG(c);
                    //drawTouchPoint(c);
                    drawFPS(c);
                    
                    //drawTestScene(c);
                    drawMainScene(c);
                    /*
                    drawTestSprite(c);
                    drawTestAnimation(c);
                    drawTestEntity(c);
                    */
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawViz);
            if (mVisible) {
                mHandler.postDelayed(mDrawViz, 1000 / 25);
            }
        }

        // Draw a circle around the current touch point, if any.
        void drawTouchPoint(Canvas c) {
            if (mTouchX >=0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }
        
        void drawMainScene(Canvas c){
        	c.save();
        	c.translate(mCenterX, mCenterY);
        	mainScene.draw(c);
        	c.restore();
        }
        
        /*
        void drawTestScene(Canvas c){
        	c.save();
        	c.translate(mCenterX, mCenterY);
        	testScene.draw(c);
        	c.restore();
        }
        */
        /*
        void drawTestEntity(Canvas c){
        	c.save();
            if(isFrameChangeTime()){
            	testEntity.nextFrame();
            } //else display same as last loop
        	c.translate(mCenterX, mCenterY);
        	testEntity.draw(c);
        	c.restore();
        }
        void drawTestSprite(Canvas c){
        	c.save();
        	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
        	String spriteDir = baseFileDirectory + "/sprites";
        	String spriteFile = spriteDir + "/face/default/.0.png";
        	int testSize = 50;
        	int testAngle= 45;
        	int testX = 100;
        	int testY = 200;
        	location testLoc = new location(testX, testY, testSize, testAngle);
        	sprite testSprite = new sprite("test",spriteFile, testLoc);
        	c.translate(mCenterX, mCenterY);
        	testSprite.draw(c);
        	c.restore();
        }
        void drawTestAnimation(Canvas c){
        	c.save();
            if(isFrameChangeTime()){
            	testAnimation.nextFrame();
            } //else display same as last loop
        	c.translate(mCenterX, mCenterY);
        	testAnimation.draw(c);
        	c.restore();
        }
        */
        
        boolean isFrameChangeTime(){
        	//determine if enough time has passed to move to next frame
        	long now = SystemClock.elapsedRealtime();
             if(((float)(now - lastFrameChange)) > (((float)1000)/desiredFPS)){		//if total ms elapsed > desired ms elapsed
            	 lastFrameChange = now;
            	 return true;
             }
             else return false;
        }
        
        /*background */
        void drawBG(Canvas c){
        	//CALCULATE BACKGROUND LOCATION BASED ON OFFSET:
        	//float yrot = (0.5f - mOffset) * 2.0f;
        	//TODO: replace solid color background with image
            //background
            c.drawColor(Color.DKGRAY); //(0xff000000);
            //return canvas to default location
            //c.restore();
        }
        
        //draw fps text for debugging
        void drawFPS(Canvas c){
        	//calculate current frame rate
            long thisTime = System.currentTimeMillis();
            long elapsedTime = thisTime-lastTime;
            lastTime = thisTime;
            float FPSsum = 0;
            for(int i = 9; i > 0; i--){
            	lastFPS[i] = lastFPS[i-1];
            	FPSsum += lastFPS[i];
            }
        	lastFPS[0] = (float)1000 / ((float)elapsedTime); // 1 frame / <ms passed> * 1000ms/s = frame/s
        	FPSsum += lastFPS[0];
        	float fps = FPSsum/(float)10;	// 10 is # of saved previous FPS measures
        	//draw the frame rate to the screen
        	mPaint.setColor(Color.BLACK); 
        	mPaint.setTextSize(20); 
        	c.drawText("virtual FPS: " + desiredFPS + "    actual FPS: " + fps, 10, 100, mPaint); 
        }
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
            	//Log.v("MirrorMe Avatar", files[i] + " is directory");
            	copier(prefix + fileName,extStorageDir);	//add dir name to prefix
            	continue;
            }
            //implied else
            //Log.v("MirrorMe Avatar", files[i] + " is file");
            
            File fDir = new File (extStorageDir + "/" + prefix);	//file object for mkdirs
            fDir.mkdirs();	//create directory

            try{	//copy the file
                out = new FileOutputStream(extStorageDir + "/" + prefix + '.' + files[i]);
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