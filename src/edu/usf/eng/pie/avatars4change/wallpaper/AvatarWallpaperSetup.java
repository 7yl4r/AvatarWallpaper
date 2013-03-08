package edu.usf.eng.pie.avatars4change.wallpaper;

import edu.usf.eng.pie.avatars4change.R;
import edu.usf.eng.pie.avatars4change.userData.userData;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AvatarWallpaperSetup extends Activity{
	private static TextView uidBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// start up the face selector
		startActivity(new Intent(getApplicationContext(), com.droid4you.util.cropimage.MainActivity.class));
		// then start up the id name selector
		idChooser();
	}
	private void idChooser(){
		setContentView(R.layout.uid_chooser);
		//show default UID
		uidBox = new TextView(getApplicationContext());
		uidBox = (TextView) findViewById(R.id.text_uid);
		uidBox.setText(userData.USERID);
		// done button
		Button doneBttn = (Button) findViewById(R.id.btn_done);
		doneBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userData.USERID = uidBox.getText().toString();
				finish();
			}
		});
		
	}
	
}
