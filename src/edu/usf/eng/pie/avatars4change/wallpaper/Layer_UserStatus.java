// This draws a layer showing text user status information such as physical activity level. 

package edu.usf.eng.pie.avatars4change.wallpaper;

import edu.usf.eng.pie.avatars4change.dataInterface.userData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class Layer_UserStatus {
	private static String TAG = "UserStatusView";
    private static final Paint mPaint = new Paint();
    
    public static void setup(){
	    // Create a Paint to draw on
	    mPaint.setAntiAlias(true);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStyle(Paint.Style.FILL);
	    
    }
	
	public static void draw(Canvas c, Rect frame){
		int xOffset = -frame.right/3 , yOffset = frame.bottom/4;
		mPaint.setColor(Color.BLACK); 
		mPaint.setTextSize(20); 
		//mPaint.setStrokeWidth(2);
		mPaint.setTypeface(Typeface.DEFAULT);
		c.drawText("You are currently", xOffset, yOffset, mPaint); 
		//set color from activity level
		int colorValue = Math.round(userData.recentAvgActivityLevel * 255.0f/10.0f);
		mPaint.setColor(Color.rgb(colorValue, 80, 255 - colorValue));
		mPaint.setTextSize(50);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		yOffset += 40; xOffset += 50;
		String statusText = "???";
		/*
		if(userData.currentActivity.equals("Standing")){
			statusText = "sedentary";
		} else if (userData.currentActivity.equals("Walking")){
			statusText = "active";
		} else if (userData.currentActivity.equals("Running")){
			statusText = "SUPER active!";
		} else {
			mPaint.setTextSize(30);
			statusText = "??? - please reset app.";
		}
		*/
		//set text based on activity level
		if( userData.recentAvgActivityLevel < 0){
			mPaint.setTextSize(30);
			statusText = "? - val out of bounds";
		}else if( userData.recentAvgActivityLevel < 3){ //implied && > 0
			statusText = "sedentary";
		}else if( userData.recentAvgActivityLevel < 6){ //implied && > 3
			statusText = "active";
		}else if(userData.recentAvgActivityLevel < 10){ //implied && > 6
			statusText = "SUPER active!";
		}else{ // > 10 || < 0
			mPaint.setTextSize(30);
			statusText = "? - val out of bounds";
		}
		c.drawText(statusText, xOffset, yOffset, mPaint);
		/*
		mPaint.setTextSize(50);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		yOffset += 40; xOffset += 50;
		c.drawText(userData.currentActivity,xOffset,yOffset,mPaint);
		*/
	}
}
