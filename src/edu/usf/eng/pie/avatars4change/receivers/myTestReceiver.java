package edu.usf.eng.pie.avatars4change.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myTestReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("MirrorMe testReceiver","broadcast received. avatar action changed");
	}
}
