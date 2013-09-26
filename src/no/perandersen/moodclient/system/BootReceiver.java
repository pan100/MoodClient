package no.perandersen.moodclient.system;

import no.perandersen.moodclient.application.MoodApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * use this method to register alarms in case there is a bug on certain phones 
		 * where alarms don't trigger after reboot. At the time of writing this is not
		 * a problem.
		 */
		MoodApplication app = (MoodApplication)context.getApplicationContext();
		app.registerAlarms();
	}

}
