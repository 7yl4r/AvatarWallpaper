package edu.usf.eng.pie.avatars4change.wallpaper;

import edu.usf.eng.pie.avatars4change.dataInterface.userData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Layer_Background {
	static int color = Color.DKGRAY;
	public static void setup(){
		//TODO: get desired color(s) or whatever from settings
	}
	
	public static void nextFrame(){
		//nothing?
		//userData.FFT is updated by ServiceSensors class...
	}
	
    /*background */
    public static void drawPlainBG(Canvas c){
		c.save();
    	//CALCULATE BACKGROUND LOCATION BASED ON OFFSET:
    	//float yrot = (0.5f - mOffset) * 2.0f;
    	//TODO: replace solid color background with image
        //background
        c.drawColor(color); //(0xff000000);
        c.restore(); //return canvas to default location
    }

    public static void drawFFT_BG(Canvas c){
    	drawPlainBG(c);
    	c.save();
	    // Create a Paint to draw on
	    Paint paint = new Paint();
	    paint.setColor(Color.YELLOW);
	    paint.setStrokeWidth(2);
	    paint.setStyle(Paint.Style.STROKE);
	    //NOTE: only the 1st 1/2 of the array is drawn, since it mirrors over the y axis
	    for( int i = 0 ; i < 32 ; i++){
	    	c.drawLine(scaleX(i), scaleY((int)Math.round(userData.FFT[i])), scaleX(i+1) , scaleY((int)Math.round(userData.FFT[i+1])), paint);
	    }
    	c.restore();
    }
    
	private static int scaleX(int x) {
		
		return -160 + x * 10;
	}
	
	private static int scaleY(int y) {
		
		return -y - 250;
	}
}
