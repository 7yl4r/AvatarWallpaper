package edu.usf.eng.pie.avatars4change.photoSelector;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import com.droid4you.util.cropimage.Util;

import edu.usf.eng.pie.avatars4change.R;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;

import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import edu.usf.eng.pie.avatars4change.storager.*;

public class GetPhotoActivity extends Activity {
	private static final String TAG = "photoSelector";
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.photo_selector_main);
        
        final String [] items			= new String [] {"Take from camera", "Select from gallery"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("Avatar Intro");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { //pick from file
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
		// face image
		mImageView		= (ImageView) findViewById(R.id.iv_photo);
		
		// crop button
		Button button 	= (Button) findViewById(R.id.btn_crop);		
		button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		
		// done button
		Button doneBttn = (Button) findViewById(R.id.btn_done);
		doneBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
	    switch (requestCode) {
		    case PICK_FROM_CAMERA:
		    	doCrop();
		    	
		    	break;
		    	
		    case PICK_FROM_FILE: 
		    	mImageCaptureUri = data.getData();
		    	
		    	doCrop();
	    
		    	break;	    	
	    
		    case CROP_FROM_CAMERA:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {
		        	// get the photo
		            Bitmap photo = extras.getParcelable("data");
		            photo.setHasAlpha(true); //encourage alpha if possible
		            /*
		            if(!photo.hasAlpha()){ //if still no alpha
		            	// create new config
		            	Bitmap buffer = photo.copy(Bitmap.Config.ARGB_8888, true);
		            	photo = buffer;
		            	Log.d(TAG,"bitmap converted to alpha-friendly config");
		            	if(!photo.hasAlpha()){ //if STILL no alpha
		            		throw new IllegalStateException("bitmap must have alpha");
		            	}
		            }
		            */
		            if(!photo.isMutable()){
		            	throw new IllegalStateException("bitmap must be mutable");
		            }           
		            
		    		// If we are circle cropping, we want alpha channel, which is the
		    		// third param here.
		    		Bitmap croppedImage = Bitmap.createBitmap(photo.getWidth(),
		    				photo.getHeight(),Bitmap.Config.ARGB_8888);

	    			Canvas canvas = new Canvas(croppedImage);
	    			Rect dstRect = new Rect(0, 0, photo.getWidth(), photo.getHeight());
	    			canvas.drawBitmap(photo, dstRect, dstRect, null);
		            
		            // Bitmaps are inherently rectangular but we want to return
					// something that is a circle.  So we fill in the
					// area around the circle with alpha.  Note the all important
					// PortDuff.Mode.CLEAR.  try TRANSPARENT BACKGROUND
					Path p = new Path();
					p.addCircle(photo.getWidth() / 2F, photo.getHeight() / 2F,
							photo.getWidth() / 2F, Path.Direction.CW);
					canvas.clipPath(p, Region.Op.DIFFERENCE);
					canvas.drawColor(android.R.color.transparent, PorterDuff.Mode.CLEAR);
					
					//save the file
			        Uri saveUri = Uri.fromFile(new File(Sdcard.getFileDir(getApplicationContext()),"sprites/face/default/0.png"));		
			        saveOutput(croppedImage,saveUri);
			        
					//show the photo
		            mImageView.setImageBitmap(croppedImage);
			        
			        //cleanup
			        File f = new File(mImageCaptureUri.getPath()); 
			        if (f.exists()) f.delete();
		        }
		        break;
	    }
	}
    
	private void saveOutput(Bitmap croppedImage, Uri saveUri) {
		if (saveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = getContentResolver().openOutputStream(saveUri);
				if (outputStream != null) {
					croppedImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
				}
			} catch (IOException ex) {
				// TODO: report error to caller
				Log.e(TAG, "Cannot open file: " + saveUri, ex);
			} finally {
				Util.closeSilently(outputStream);
			}
			Bundle extras = new Bundle();
			setResult(RESULT_OK, new Intent(saveUri.toString())
			.putExtras(extras));
		} else {
			Log.e(TAG, "not defined image url");
		}
	}
    
    private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(mImageCaptureUri);
            
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i 		= new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	        	startActivityForResult(i, CROP_FROM_CAMERA);
        	} else {
		        for (ResolveInfo res : list) {
		        	final CropOption co = new CropOption();
		        	
		        	co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		        	co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		        	co.appIntent= new Intent(intent);
		        	
		        	co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		            cropOptions.add(co);
		        }
	        
		        CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Choose Crop App");
		        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		            public void onClick( DialogInterface dialog, int item ) {
		                startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		            }
		        });
	        
		        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		            @Override
		            public void onCancel( DialogInterface dialog ) {
		               
		                if (mImageCaptureUri != null ) {
		                    getContentResolver().delete(mImageCaptureUri, null, null );
		                    mImageCaptureUri = null;
		                }
		            }
		        } );
		        
		        AlertDialog alert = builder.create();
		        
		        alert.show();
        	}
        }
	}
}