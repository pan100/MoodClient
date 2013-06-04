package no.perandersen.moodclient.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MoodApplication app = (MoodApplication)context.getApplicationContext();
		app.registerAlarms();
	}

}
