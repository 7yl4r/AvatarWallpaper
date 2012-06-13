package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import edu.usf.PIE.tylar.MirrorMe.R;

/*
 * This animated wallpaper draws a rotating wireframe cube.
 */
public class avatarWallpaper extends WallpaperService {

    private final Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    	 //theAvatar = new avatarObject(this.getResources(), level_of_realism, level_of_activity); 
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }

    class CubeEngine extends Engine {

        //vars for the avatar
    	long lastFrameChange = 0;		//last frame update [ms]
        int level_of_activity = 3;		//TODO: set this variable somewhere/someway else
        int level_of_realism = 3;		//0=least realistic
        Resources r = getResources();
        
        Bitmap test = BitmapFactory.decodeResource(r,R.drawable.r0_a3_body_f0);
        avatarObject theAvatar = new avatarObject(r, level_of_realism, level_of_activity);
        
        //vars for canvas
        private final Paint mPaint = new Paint();
        private float mCenterX;
        private float mCenterY;
        
        //vars for touchPoint circle
        private float mTouchX = -1;
        private float mTouchY = -1;

    	//vars for the cube???
        private float mOffset;
        
        //vars for frame rate
        private long mStartTime;	//time of app start
        private long lastTime = 0;	//time measurement for calculating deltaT and thus fps
        private float desiredFPS = 15;
        private float[] lastFPS = {0,0,0,0,0,0,0,0,0,0};	//saved past 10 fps measurements
        
        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        CubeEngine() {
            // Create a Paint to draw the lines for our cube
            final Paint paint = mPaint;
            paint.setColor(Color.GRAY); //0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mStartTime = SystemClock.elapsedRealtime();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
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
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
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
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                	drawBG(c);
                    drawCube(c);
                    drawTouchPoint(c);
                    drawAvatar(c);
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
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 1000 / 25);
            }
        }

        /*
         * Draw a wireframe cube by drawing 12 3 dimensional lines between
         * adjacent corners of the cube
         */
        void drawCube(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            // draw the cube
            drawLine(c, -400, -400, -400,  400, -400, -400);
            drawLine(c,  400, -400, -400,  400,  400, -400);
            drawLine(c,  400,  400, -400, -400,  400, -400);
            drawLine(c, -400,  400, -400, -400, -400, -400);

            drawLine(c, -400, -400,  400,  400, -400,  400);
            drawLine(c,  400, -400,  400,  400,  400,  400);
            drawLine(c,  400,  400,  400, -400,  400,  400);
            drawLine(c, -400,  400,  400, -400, -400,  400);

            drawLine(c, -400, -400,  400, -400, -400, -400);
            drawLine(c,  400, -400,  400,  400, -400, -400);
            drawLine(c,  400,  400,  400,  400,  400, -400);
            drawLine(c, -400,  400,  400, -400,  400, -400);
            
            c.restore();
        }

        /*
         * Draw a 3 dimensional line on to the screen
         */
        void drawLine(Canvas c, int x1, int y1, int z1, int x2, int y2, int z2) {
            long now = SystemClock.elapsedRealtime();
            float xrot = ((float)(now - mStartTime)) / 1000;
            float yrot = (0.5f - mOffset) * 2.0f;
            float zrot = 0;

            // 3D transformations

            // rotation around X-axis
            float newy1 = (float)(Math.sin(xrot) * z1 + Math.cos(xrot) * y1);
            float newy2 = (float)(Math.sin(xrot) * z2 + Math.cos(xrot) * y2);
            float newz1 = (float)(Math.cos(xrot) * z1 - Math.sin(xrot) * y1);
            float newz2 = (float)(Math.cos(xrot) * z2 - Math.sin(xrot) * y2);

            // rotation around Y-axis
            float newx1 = (float)(Math.sin(yrot) * newz1 + Math.cos(yrot) * x1);
            float newx2 = (float)(Math.sin(yrot) * newz2 + Math.cos(yrot) * x2);
            newz1 = (float)(Math.cos(yrot) * newz1 - Math.sin(yrot) * x1);
            newz2 = (float)(Math.cos(yrot) * newz2 - Math.sin(yrot) * x2);

            // 3D-to-2D projection
            float startX = newx1 / (4 - newz1 / 400);
            float startY = newy1 / (4 - newz1 / 400);
            float stopX =  newx2 / (4 - newz2 / 400);
            float stopY =  newy2 / (4 - newz2 / 400);

            c.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) {
            if (mTouchX >=0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }
        
        /*draw avatar*/
        void drawAvatar(Canvas c) {
        	
        	//determine if enough time has passed to move to next frame
        	long now = SystemClock.elapsedRealtime();
             if(((float)(now - lastFrameChange)) > (((float)1000)/desiredFPS)){		//if total ms elapsed > desired ms elapsed
            	 theAvatar.nextFrame();
            	 lastFrameChange = now;
             } //else display same as last loop
             
        	c.translate(mCenterX, mCenterY);
        	theAvatar.drawAvatar(c,mCenterX*2,mCenterY*2);
            c.restore();
        }
        
        
        /*background */
        void drawBG(Canvas c){
            //background
            c.drawColor(Color.DKGRAY); //(0xff000000);
        }
    }
}