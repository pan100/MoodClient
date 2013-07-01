package no.perandersen.moodclient.system;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.activities.MainActivity;
import no.perandersen.moodclient.activities.SleepLogActivity;
import no.perandersen.moodclient.application.MoodApplication;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class SleepNotificationReceiver extends BroadcastReceiver {
	private NotificationManager nm;
	private static final String TAG = "SleepNotificationReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.v(TAG, "Context: " + context.getClass().getName());
//		Log.v(TAG, "Intent action: " + intent.toString());
//		   NotificationCompat.Builder mBuilder =
//                   new NotificationCompat.Builder(context)
//                   .setSmallIcon(R.drawable.smilewhite)
//                   .setContentTitle("God morgen")
//                   .setContentText("Hvor lenge har du sovet?")
//                   .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
//                   .setAutoCancel(true);
//           Intent resultIntent = new Intent(context, SleepLogActivity.class);
//           //make the Day object and put it extra
//           
//           TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//       stackBuilder.addParentStack(MainActivity.class);
//       stackBuilder.addNextIntent(resultIntent);
//       PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//       mBuilder.setContentIntent(resultPendingIntent);
//       NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//       mNotificationManager.notify(notificationCount, mBuilder.build());
//       notificationCount++;
//		Log.v(TAG, "User should now have been notified. NotificationCount is " + notificationCount);
		MoodApplication app = (MoodApplication)context.getApplicationContext();
		new MoodNotification("God morgen", "Hvor lenge har du sovet?" , SleepLogActivity.class, context, app.newNotificationID()).show();
	}
}
