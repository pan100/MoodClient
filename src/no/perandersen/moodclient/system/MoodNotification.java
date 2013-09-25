package no.perandersen.moodclient.system;

import java.util.Date;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.activities.MainActivity;
import no.perandersen.moodclient.model.Day;
import no.perandersen.moodclient.model.Day.DayBuilder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class MoodNotification {

	NotificationCompat.Builder mBuilder;
	private Context context;
	private int id;

	/*
	 * takes the title and text of the notification to create and the activity
	 * to start. Also needs an id
	 */
	public MoodNotification(String title, String text, Class<?> activityClass,
			Context context, int id) {
		this.context = context;
		this.id = id;
		mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.smilewhite)
				.setContentTitle(title)
				.setContentText(text)
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_VIBRATE
								| Notification.DEFAULT_LIGHTS)
				.setAutoCancel(true);
		Intent resultIntent = new Intent(context, activityClass);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Day.DayBuilder daybuilder = new DayBuilder(new Date(), prefs.getString(
				"connection_username", ""));
		resultIntent.putExtra("DAY_BUILDER", daybuilder);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
	}

	public void show() {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, mBuilder.build());
	}

}
