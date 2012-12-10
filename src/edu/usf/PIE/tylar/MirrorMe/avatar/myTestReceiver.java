package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class myTestReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("MirrorMe testReceiver","broadcast received. avatar action changed");
	}
}
