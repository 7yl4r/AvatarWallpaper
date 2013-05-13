package edu.usf.eng.pie.avatars4change.wallpaper;

import edu.usf.eng.pie.avatars4change.userData.userData;
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

	public static void draw(Canvas c){
		plainBG(c);

	//	fftBG(c);
	}
	
    /*background */
    static void plainBG(Canvas c){
		c.save();
    	//CALCULATE BACKGROUND LOCATION BASED ON OFFSET:
    	//float yrot = (0.5f - mOffset) * 2.0f;
    	//TODO: replace solid color background with image
        //background
        c.drawColor(color); //(0xff000000);
        c.restore(); //return canvas to default location
    }

    static void fftBG(Canvas c){
    	c.save();
	    // Create a Paint to draw on
	    Paint paint = new Paint();
	    paint.setColor(Color.YELLOW);
	    paint.setStrokeWidth(2);
	    paint.setStyle(Paint.Style.STROKE);
	    
	    for( int i = 0 ; i < 64 ; i++){
	    	c.drawLine(scaleX(i), scaleY((int)Math.round(userData.FFT[i])), scaleX(i+1) , scaleY((int)Math.round(userData.FFT[i+1])), paint);
	    }
    	c.restore();
    	
    }
    
	private static int scaleX(int x) {
		
		return -160 + x * 5;
	}
	
	private static int scaleY(int y) {
		
		return -y - 250;
	}
}
