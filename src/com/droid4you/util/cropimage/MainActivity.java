package com.droid4you.util.cropimage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.storager.userData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
        //dialog for button click
        final String [] items			= new String [] { "Select a picture from gallery","Take a picture"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		builder.setTitle("Select image");	
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface selectImageDialog, int item ) { 
				if (item == 0) {	//pick from file 
					doTakePhotoAction1();
				} else {			//pick from camera
					doTakePhotoAction();
				}
			}
		} );
		final AlertDialog selectImageDialog = builder.create();
		
		drawSelectedImage();
		
		// select new image button
		Button selectNewBttn 	= (Button) findViewById(R.id.btn_crop);
		selectNewBttn.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				selectImageDialog.show();
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

	//select an image from the gallery
	private void doTakePhotoAction1() {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

//mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"tmp_contact_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
        mImageCaptureUri = Uri.fromFile(new File(userData.getFileDir(),"sprites/face/default/0.png"));		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

		try {
			intent.putExtra("return-data", false);
			startActivityForResult(intent, PICK_FROM_FILE);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	//take a photo using the camera
	private void doTakePhotoAction() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

  //mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				//"tmp_contact_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
mImageCaptureUri = Uri.fromFile(new File(userData.getFileDir(),"sprites/face/default/facedetect" + String.valueOf(System.currentTimeMillis()) + ".png"));		
		

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

		try {
			intent.putExtra("return-data", false);
			startActivityForResult(intent, PICK_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_CAMERA:
			Intent intent = new Intent(this, CropImage.class);
			intent.putExtra("image-path", mImageCaptureUri.getPath());
			intent.putExtra("scale", true);
			startActivity(intent);
			break;
			
		case PICK_FROM_FILE: 
			Intent intent1 = new Intent(this, CropImage.class);
			intent1.putExtra("image-path", mImageCaptureUri.getPath());
			intent1.putExtra("scale", true);
			startActivity(intent1);
	    	break;	    
		}
	}
	
	@Override
	public void onResume(){
		//redraw the selected image on returning to the activity
		drawSelectedImage();
		super.onResume();
	}

	private void drawSelectedImage(){
		//selected image display
		mImageView		= (ImageView) findViewById(R.id.image);
		String imagePath = userData.getFileDir()+"sprites/face/default/0.png";
		BitmapDrawable d = new BitmapDrawable(getResources(), imagePath);
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 70, getResources().getDisplayMetrics()); // 70 dip
		Bitmap scaledD = Bitmap.createScaledBitmap(d.getBitmap(), size, size, false);
		d = new BitmapDrawable(scaledD);
		mImageView.setImageDrawable(d);
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
		        
builder.setAdapter( adapter, new DialogInterface.OnClickListener() {public void onClick( DialogInterface dialog, int item ) {startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);}});
	        
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