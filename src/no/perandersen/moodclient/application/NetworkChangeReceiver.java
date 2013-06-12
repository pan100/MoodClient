package no.perandersen.moodclient.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
//http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app
public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable() || mobile.isAvailable()) {
			Log.d("Network Available ", "Flag No 1");
			// Persist not persisted days
			MoodApplication app = (MoodApplication) context.getApplicationContext();
			app.attemptSendNotSent();
		}
	}

}
