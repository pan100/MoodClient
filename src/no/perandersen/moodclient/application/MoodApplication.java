package no.perandersen.moodclient.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import no.perandersen.moodclient.model.Day;
import no.perandersen.moodclient.system.EveningNotificationReceiver;
import no.perandersen.moodclient.system.SleepNotificationReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class MoodApplication extends Application implements OnSharedPreferenceChangeListener{
	private static final String TAG = "MoodApplication";
	private static MoodApplication singleton;
	private static final String FILENAME_TEMP = "moodtempfile.json";

	// should be https:// in prod.
	public static final String TRANSFERPROTOCOL = "http://";

	public Persister persister;
	private SharedPreferences sharedPref;
	private AlarmManager am;
	private int notificationCount;

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
		notificationCount = 0;
		persister = new Persister();
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPref.registerOnSharedPreferenceChangeListener(this);
		registerAlarms();
	}

	public void registerAlarms() {

		String sleepString = sharedPref.getString("time_sleepLog", "08:00");
		String[] pieces = sleepString.split(":");
		int sleepHour = Integer.parseInt(pieces[0]);
		int sleepMinute = Integer.parseInt(pieces[1]);

		String eveningString = sharedPref.getString("time_eveningLog", "21:00");
		pieces = null;
		pieces = eveningString.split(":");
		int eveningHour = Integer.parseInt(pieces[0]);
		Log.v(TAG, "eveningHour in registerAlarms: " + eveningHour);
		int eveningMinute = Integer.parseInt(pieces[1]);

		Calendar now = Calendar.getInstance();
		// create calendar objects pointing to the next time this clock will
		// occur
		Calendar sleepCal = Calendar.getInstance();
		sleepCal.set(Calendar.HOUR_OF_DAY, sleepHour);
		sleepCal.set(Calendar.MINUTE, sleepMinute);
		sleepCal.set(Calendar.SECOND, 0);
		//if the time has passed we need to add a day
		if(now.after(sleepCal)) {
			sleepCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		Calendar eveningCal = Calendar.getInstance();
		eveningCal.set(Calendar.HOUR_OF_DAY, eveningHour);
		eveningCal.set(Calendar.MINUTE, eveningMinute);
		eveningCal.set(Calendar.SECOND, 0);
		if(now.after(eveningCal)) {
			eveningCal.add(Calendar.DAY_OF_MONTH, 1);
		}	
		// TODO refactor SleepNotificationService and EveningNotificationService
		// into one class and use flags? Or maybe send something with putExtra to identify which
		// screen should be shown?
		Intent morningIntent = new Intent(this, SleepNotificationReceiver.class);
		morningIntent.putExtra("MoodSleepLogAlarm", 0);
		Intent eveningIntent = new Intent(this,
				EveningNotificationReceiver.class);
		eveningIntent.putExtra("MoodEveningLogAlarm", 0);
		PendingIntent sleepPending = PendingIntent.getBroadcast(this, 0,
				morningIntent, 0);
		PendingIntent eveningPending = PendingIntent.getBroadcast(this, 1,
				eveningIntent, 0);

		// then set the alarms
		am.setRepeating(AlarmManager.RTC_WAKEUP, sleepCal.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, sleepPending);
		am.setRepeating(AlarmManager.RTC_WAKEUP, eveningCal.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, eveningPending);
		Log.v(TAG, "Alarm for sleep registered at " + sleepCal.getTime());
		Log.v(TAG, "Alarm for evening registered at " + eveningCal.getTime());
	}

	/**
	 * Used for cancelling alarms on termination
	 */
	public void cancelAlarms() {
		// TODO refactor SleepNotificationService and EveningNotificationService
		// into one class and use flags?
		Intent syncIntent = new Intent(this, SleepNotificationReceiver.class);
		//		Intent eveningNotifyIntent = new Intent(this,
		//				EveningNotificationService.class);
		PendingIntent sleepPending = PendingIntent.getService(this, 0,
				syncIntent, 0);
		//		PendingIntent eveningPending = PendingIntent.getService(this, 1,
		//				eveningNotifyIntent, 0);
		// first, remember to clear existing alarms (this method can be called
		// when settings are changed)
		am.cancel(sleepPending);
		//am.cancel(eveningPending);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("time_sleepLog") || key.equals("time_eveningLog")) {
			// call MoodApplication.registerAlarms();
			cancelAlarms();
			registerAlarms();
		}
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

	public int newNotificationID() {
		notificationCount++;
		return notificationCount;
	}

	/*
	 * Call the persist method on the Persister with a Day object and a password
	 * and forget about it. The Persister either sends it to the server
	 * immediately or saves it so that it can be sent later. To send not sent
	 * days call the persistNotSent() method. This is normally done by
	 * NetworkChangeReceiver, a BroadcastReceiver set to trigger when wifi or
	 * mobile network is available.
	 */
	public class Persister {
		private HttpParams httpParams;
		private HttpClient httpclient;
		private String serverUri;

		public Persister() {
			httpParams = new BasicHttpParams();
			int timeout = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);
			httpclient = new DefaultHttpClient(httpParams);
			httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
			serverUri = PreferenceManager.getDefaultSharedPreferences(
					MoodApplication.this)
					.getString("connection_server_uri", "")
					+ "/json/day";
		}

		public void holdForLater(Day day) {
			//serialize notSent and save it to a file;
			MoodApplication app = (MoodApplication)getApplicationContext();
			File tempFile = new File(FILENAME_TEMP);
			if(!tempFile.exists()) {
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			FileOutputStream fos;
			try {
				fos = app.getApplicationContext().openFileOutput(FILENAME_TEMP, Context.MODE_APPEND);
				try {
					fos.write((day.toJSONObject().toString() + " \n").getBytes());
					Log.v(TAG, "Written to file: " + day.toJSONObject().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "File not found error: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

		public void sendDaysInTempFile() {
			MoodApplication app = (MoodApplication)getApplicationContext();
			FileInputStream fos;

			try {
				fos=app.getApplicationContext().openFileInput(FILENAME_TEMP);
				InputStreamReader inputStreamReader = new InputStreamReader(fos);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String line = "";
				boolean deleteFile = false;
				
				try {
					while ((line = bufferedReader.readLine()) != null) {
						sb.append(line);
						attemptToSendJson(line);
						deleteFile = true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v(TAG, "content sent from file: " + sb.toString());
				File dir = getFilesDir();
				File file = new File(dir, FILENAME_TEMP);
				if(deleteFile) {
					file.delete();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void attemptToSendJson(String line) {
			class SendDay extends AsyncTask<Void, Void, Void> {
				private String line;
				
				public SendDay(String line) {
						this.line = line;
					}
				@Override
				protected Void doInBackground(Void... params) {
					boolean success = false;
					// attempt to send this to the server
					HttpPost request = new HttpPost(serverUri);
					try {
						StringEntity se = new StringEntity(line);
						se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
								"application/json"));
						request.setEntity(se);
						Log.v(TAG, line);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, "Encoding error: " + e.getMessage());
					}
					try {
						HttpResponse response = httpclient.execute(request);
						Log.v(TAG, "HTTP response: "
								+ response.getStatusLine().getStatusCode());
						// TODO switch case
						if (response.getStatusLine().getStatusCode() == 200) {
							success = true;
						} else if (response.getStatusLine().getStatusCode() == 403) {
							//TODO FIX THIS ETERNAL LOOP - data temporarily saved with incorrect credentials will never disappear or be fixed
							success = false;
						}

					} catch (ClientProtocolException e) {
						Log.e(TAG, "ClientProtocol error: " + e.getMessage());
					} catch (IOException e) {
						Log.e(TAG, "IO error: " + e.getMessage());
					}
					return null;
				}
			}
			new SendDay(line).execute();

		}

		public String persist(Day day) {
			String message = "Ukjent feil";
			// attempt to send this to the server
			HttpPost request = new HttpPost(serverUri);
			try {
				StringEntity se = new StringEntity(day.toJSONObject()
						.toString(), "UTF-8");
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				request.setEntity(se);
				Log.v(TAG, day.toJSONObject()
						.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "JSON error: " + e.getMessage());
				message = e.getMessage();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Encoding error: " + e.getMessage());
				message = e.getMessage();
			}
			try {
				HttpResponse response = httpclient.execute(request);
				Log.v(TAG, "HTTP response: "
						+ response.getStatusLine().getStatusCode());
				// TODO switch case
				if (response.getStatusLine().getStatusCode() == 200) {
					message = "Dataene ble lagret på serveren.";
				} else if (response.getStatusLine().getStatusCode() == 403) {
					message = "Feil. Sjekk brukernavn eller passord i instillinger.";
				}

			} catch (ClientProtocolException e) {
				Log.e(TAG, "ClientProtocol error: " + e.getMessage());
				message = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, "IO error: " + e.getMessage());
				message = "Nettverksfeil. Lagrer midlertidig på disk og sender senere.";
				// if it fails, put it in the arraylist notSent
				// serialize all objects in the notSent arraylist and save them
				// to a file
				holdForLater(day);
			}
			return message;
		}
	}
}
