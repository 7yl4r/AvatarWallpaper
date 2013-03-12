package edu.usf.eng.pie.avatars4change.wallpaper;

import edu.usf.eng.pie.avatars4change.userData.userData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	
	public static void draw(Canvas c){
		int xOffset = -200, yOffset = 200;
		mPaint.setColor(Color.BLACK); 
		mPaint.setTextSize(20); 
		//mPaint.setStrokeWidth(2);
		mPaint.setTypeface(Typeface.DEFAULT);
		c.drawText("You are currently", xOffset, yOffset, mPaint); 
		//set color based on activity level
		if(userData.currentActivityLevel == 0){
			mPaint.setColor(Color.RED);
		}else if(userData.currentActivityLevel ==1){
			mPaint.setColor(Color.YELLOW);
		}else if(userData.currentActivityLevel ==2){
			mPaint.setColor(Color.GREEN);
		}else{
			mPaint.setColor(Color.CYAN);
			Log.d(TAG,"unkown activityLevel encountered");
		}
		mPaint.setTextSize(50);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		yOffset += 40; xOffset += 50;
		c.drawText(userData.currentActivity,xOffset,yOffset,mPaint);
	}
}
