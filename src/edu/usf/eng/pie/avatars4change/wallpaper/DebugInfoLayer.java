package edu.usf.eng.pie.avatars4change.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DebugInfoLayer {
	private static long lastTime = 0;
	private static float lastFPS[] = new float[5];	
    private static final Paint mPaint = new Paint();
	
    public static void setup(){
	    // Create a Paint to draw on
	    Paint paint = mPaint;
	    paint.setColor(Color.GRAY); //0xffffffff);
	    paint.setAntiAlias(true);
	    paint.setStrokeWidth(2);
	    paint.setStrokeCap(Paint.Cap.ROUND);
	    paint.setStyle(Paint.Style.STROKE);
    }
    
    public static void nextFrame(){
    	//nothing... for now...
    }
	    
    //draw fps text for debugging
    public static void drawFPS(Canvas c, float desiredFPS){
    	//calculate current frame rate
        long thisTime = System.currentTimeMillis();
        long elapsedTime = thisTime-lastTime;
        lastTime = thisTime;
        float FPSsum = 0;
        for(int i = lastFPS.length-1; i > 0; i--){
        	lastFPS[i] = lastFPS[i-1];
        	FPSsum += lastFPS[i];
        }
    	lastFPS[0] = (float)1000 / ((float)elapsedTime); // 1 frame / <ms passed> * 1000ms/s = frame/s
    	FPSsum += lastFPS[0];
    	float fps = FPSsum/(float)10;	// 10 is # of saved previous FPS measures
    	//draw the frame rate to the screen
    	mPaint.setColor(Color.BLACK); 
    	mPaint.setTextSize(20); 
    	int xOffset = -200, yOffset = -200;
    	c.drawText("virtual FPS: " + desiredFPS + "    actual FPS: " + fps, xOffset, yOffset, mPaint); 
    }
    
    // Draw a circle around the current touch point, if any.
    void drawTouchPoint(Canvas c) {
        //if (mTouchX >=0 && mTouchY >= 0) {
        //   c.drawCircle(mTouchX, mTouchY, 80, mPaint);
        //}
    }
}
