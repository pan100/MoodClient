package no.perandersen.moodclient.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import no.perandersen.moodclient.system.EveningNotificationService;
import no.perandersen.moodclient.system.SleepNotificationService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import com.perandersen.moodclient.model.Day;

public class MoodApplication extends Application {
	private static final String TAG = "MoodActivity";
	private static MoodApplication singleton;
	
	//should be https:// in prod.
	public static final String TRANSFERPROTOCOL = "http://";
	
	Persister persister;
	private SharedPreferences sharedPref;
	private AlarmManager am;
	
	public MoodApplication getInstance() {
		return singleton;
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		
		super.onCreate();
		singleton = this;
		persister = new Persister();
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		registerAlarms();
	}
 
	public void registerAlarms() {
		// TODO Set up the alarms that triggers the notifications.
		
		
		//get the set times for the alarms in the settings
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		String sleepString = sharedPref.getString("time_sleepLog", "08:00");
		String[] pieces = sleepString.split(":");
		int sleepHour = Integer.parseInt(pieces[0]);
		int sleepMinute = Integer.parseInt(pieces[1]);
		
		String eveningString = sharedPref.getString("time_eveningLog", "20:00");
		pieces = null;
		pieces = eveningString.split(":");
		int eveningHour = Integer.parseInt(pieces[0]);
		int eveningMinute = Integer.parseInt(pieces[1]);	
		//create calendar objects pointing to the next time this clock will occur
		Calendar sleepCal = Calendar.getInstance();
		sleepCal.set(Calendar.HOUR, sleepHour);
		sleepCal.set(Calendar.MINUTE, sleepMinute);
		
		Calendar eveningCal = Calendar.getInstance();
		eveningCal.set(Calendar.HOUR, eveningHour);
		eveningCal.set(Calendar.MINUTE, eveningMinute);
		//TODO refactor SleepNotificationService and EveningNotificationService into one class and use flags?
		Intent sleepNotifyIntent = new Intent(this, SleepNotificationService.class);
		Intent eveningNotifyIntent = new Intent(this, EveningNotificationService.class);
		PendingIntent sleepPending = PendingIntent.getService(this, 0, sleepNotifyIntent, 0);
		PendingIntent eveningPending = PendingIntent.getService(this, 0, eveningNotifyIntent, 0);
		//first, remember to clear existing alarms (this method can be called when settings are changed)
		am.cancel(sleepPending);
		am.cancel(eveningPending);
		//then set the alarms
		am.setRepeating(AlarmManager.RTC_WAKEUP, sleepCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY , sleepPending);
		am.setRepeating(AlarmManager.RTC_WAKEUP, eveningCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY , eveningPending);
	}
	/**
	 * Used for cancelling alarms on termination
	 */
	public void cancelAlarms() {
		//TODO refactor SleepNotificationService and EveningNotificationService into one class and use flags?
		Intent sleepNotifyIntent = new Intent(this, SleepNotificationService.class);
		Intent eveningNotifyIntent = new Intent(this, EveningNotificationService.class);		
		PendingIntent sleepPending = PendingIntent.getService(this, 0, sleepNotifyIntent, 0);
		PendingIntent eveningPending = PendingIntent.getService(this, 0, eveningNotifyIntent, 0);
		//first, remember to clear existing alarms (this method can be called when settings are changed)
		am.cancel(sleepPending);
		am.cancel(eveningPending);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
		cancelAlarms();
	}
	
	/*
	 * Call the persist method on the Persister with a Day object and a password and forget about it. The 
	 * Persister either sends it to the server immediately or saves it so that it can be sent later. To send
	 * not sent days call the persistNotSent() method. This is normally done by NetworkChangeReceiver, a
	 * BroadcastReceiver set to trigger when wifi or mobile network is available.
	 */
	private class Persister {
		private ArrayList<Day> notSent;
		private HttpClient httpclient;
		public Persister() {
			notSent = new ArrayList<Day>();
			httpclient = new DefaultHttpClient();
		}
		
		public void persist(Day day) {
			//attempt to send this to the server
			
		}
	}
}
