package edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.meapsoft.FFT;

import edu.usf.eng.pie.avatars4change.dataInterface.userData;

public class ServiceSensors extends Service implements SensorEventListener {
	private String TAG = "ServiceSensors";
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private int mServiceTaskType;
	private String mLabel;
	
	//private Instances mDataset;
	//private Attribute mClassAttribute;
	
	private OnSensorChangedTask mAsyncTask;
	
	//BROADCAST MEMBER VARIABLE
	private Intent mBroadcastIntent;
	// float alpha =0.8; // t/(t + dT) with t, the low - pass filter 
	//////////////////
	
	float[] gravity = new float[3];
	float [] linear_acceleration = new float[3];
	int [] buscar =new int[500];
    float lastX, lastY, lastZ;
    long interval;
  //  int classifiedValue;
    Context mContext = this;
    
	// private NotificationManager mNM;

	private static ArrayBlockingQueue<Double> mAccBuffer;
	public static final DecimalFormat mdf = new DecimalFormat("#.##");
	
	@Override
	public void onCreate() {
		
		mAccBuffer = new ArrayBlockingQueue<Double>(Globals.ACCELEROMETER_BLOCK_CAPACITY);
		 
		mAsyncTask = new OnSensorChangedTask();
	    mAsyncTask.execute();
		 
		//Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Service Created");	   
		super.onCreate();		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(TAG,"onAccuracyChanged");
		return;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		 Log.i(TAG, "Received start id " + startId + ": " + intent);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);		
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);	
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		
		Bundle extras = intent.getExtras();
		mLabel = extras.getString("label");
		
		Log.d(TAG,mLabel);
		
		if("collecting".compareTo(extras.getString("type"))==0){
			mServiceTaskType = Globals.SERVICE_TASK_TYPE_COLLECT;
			
		} else {
			mServiceTaskType = Globals.SERVICE_TASK_TYPE_CLASSIFY;
				
			//////BROADCAST THE BROADCAST//////////////////////////////
				
			mBroadcastIntent = new Intent();
			mBroadcastIntent.setAction(Globals.ACTION_MOTTION_UPDATED);
				
			///////////////////////////////////////////////////////////
			}
		   return START_STICKY;
	}
	
	 @Override
	  public void onDestroy() {
		 //Toast.makeText(ServiceSensors.this, "Estoy en destroy", Toast.LENGTH_SHORT).show();
	     Log.d(TAG, "sensorService onDestroy");
	    
	   if(mAsyncTask != null)
	    {
	      if(!mAsyncTask.isCancelled())
	    	  mAsyncTask.cancel(true);
	    }
	  }

	public void onSensorChanged(SensorEvent event) {
		//Toast.makeText(this, "Estoy on Sensor Change", Toast.LENGTH_LONG).show();
		//Log.d(TAG, "OnSensorChanged");
		
		//Converting row acceleration to Linear acceleration according to the example in android developers web page
		final float alpha = 0.8f;
         
        // End of the data transformation
	
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			
			 //lastX= event.values[SensorManager.DATA_X];
			 //lastY= event.values[SensorManager.DATA_Y];
			 //lastZ= event.values[SensorManager.DATA_Z];
			 		
			//I am not sure if its work, What I am doing here, is to compute the linear acceleration
			
			 gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
	         gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
	         gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

	         linear_acceleration[0] = event.values[0] - gravity[0];
	         linear_acceleration[1] = event.values[1] - gravity[1];
	         linear_acceleration[2] = event.values[2] - gravity[2];
	         
	  //Toast.makeText(this, Float.toString(lastX) + "-"+ Double.toString(linear_acceleration[0]) +":"+  Float.toString(lastY) + "-" +Double.toString(linear_acceleration[2])+":"+ Float.toString(lastZ) + "-"+Double.toString(linear_acceleration[1])  , Toast.LENGTH_LONG).show();
	
	      // Toast.makeText(this, Float.toString(lastX) + "-"+ Float.toString(event.values[0])  , Toast.LENGTH_LONG).show();	         
	         
			//double m= Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] + event.values[2]*event.values[2]);
double m= Math.sqrt(linear_acceleration[0]*linear_acceleration[0] + linear_acceleration[1]*linear_acceleration[1] + linear_acceleration[2]*linear_acceleration[2]);
			
			//float m= (float) Math.sqrt(lastX*lastX + lastY*lastY + lastZ*lastZ);
			
			try{
				mAccBuffer.add(Double.valueOf(m));
			} catch (IllegalStateException e) {
				ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(mAccBuffer.size()*2);
				//Log.d(TAG, "Size of accel buffer increased to: " + newBuf.size());
				mAccBuffer.drainTo(newBuf);
				mAccBuffer =  newBuf;
				mAccBuffer.add(Double.valueOf(m));
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG,"onBind");
		return null;
	}
	
//---
private class OnSensorChangedTask extends AsyncTask<Void, Void, Void>{
	//Intent intent1 = new Intent();

	@Override
	protected Void doInBackground(Void... arg0) {
		Log.d(TAG, "doInBackground");
		
		int blockSize = 0;
		FFT fft = new FFT(Globals.ACCELEROMETER_BLOCK_CAPACITY);
		double[] accBlock = new double [Globals.ACCELEROMETER_BLOCK_CAPACITY];
		double[] re= accBlock;
		double[] im =  new double[Globals.ACCELEROMETER_BLOCK_CAPACITY];
		
		ArrayList<Double> featVect = new ArrayList<Double>(Globals.ACCELEROMETER_BLOCK_CAPACITY);
		
		double max = Double.MIN_VALUE;
				
		while(true){
			try{  if(isCancelled()){break;}
				 
				accBlock[blockSize++]= mAccBuffer.take().doubleValue();
				
				if(blockSize==Globals.ACCELEROMETER_BLOCK_CAPACITY){
					blockSize = 0;
				
					//time = System.currentTimeMillis();
					max = .0;
					for(double val : accBlock){
						if(max<val){
							max=val;
						}
					}
					fft.fft(re, im);
					
					//Log.v(TAG,"FFT len = " + Integer.toString(re.length) );
					boolean oopsFlag = false; //this flag is to catch the error saving to userData.FFT
					double[] FFTarray = new double[65];
					for(int i=0; i< re.length; i++){
						double mag = Math.sqrt(re[i]*re[i]+ im[i]*im[i]);
						if(i < 64){
							FFTarray[i] = mag;	//add to userData (just for fun)
						} else {
							oopsFlag = true;
						}
						//inst.setValue(i, mag);  //Remove as soon as possible
						featVect.add(Double.valueOf(mag)); //New for classification
						im[i] = .0; //Clear the field
					}
					userData.updateFFT(FFTarray);
					if(oopsFlag){	//print error to debugger
						Log.e(TAG,"oops! FFT is > 64. FFT.length = " + Integer.toString(re.length) );
					}
					
					featVect.add(Double.valueOf(max));
					
					int classifiedValue = (int) Classifier.classify(featVect.toArray());
					
					//Log.d(TAG, Integer.toHexString(classifiedValue));
			
					/////////////////BROADCAST/////////////////////////////////////////////////
					
					  Intent intent= new Intent("SOME_ACTION");
				      //intent.setAction("com.example.HOLA");
					  
					  intent.putExtra("HOLA", Integer.toString(classifiedValue));
					  sendBroadcast(intent);
					  
					  //////////////BROADCAST/////////////////////////////////////////////////
					
					//Log.v(TAG, "Motion update broadcast sent");
					featVect.clear();
					}
				} catch(Exception e){
					e.printStackTrace();
				}
     		}
			return null;
         }
	
	protected void onCancelled(){
		//Toast.makeText(getApplicationContext(), "Ultimo onCancell", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onCancelled");
		
		if(mServiceTaskType == Globals.SERVICE_TASK_TYPE_CLASSIFY){
			super.onCancelled();
			return;
		}
		//Toast.makeText(getApplicationContext(), "Que pasa aqui", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onCancelled");
		super.onCancelled();		
	}
	  }
}
