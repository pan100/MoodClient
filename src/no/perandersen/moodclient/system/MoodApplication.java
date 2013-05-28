package no.perandersen.moodclient.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

public class MoodApplication extends Application {
	private static final String TAG = "MoodActivity";
	private static MoodApplication singleton;
	
	private SharedPreferences sharedPref;
	private HttpClient httpclient;
	
	private ArrayList<String> notSentJson;
	
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
		
		httpclient = new DefaultHttpClient();
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		notSentJson = new ArrayList<String>();
		
		registerAlarms();
	}
 
	private void registerAlarms() {
		// TODO Set up the alarms
		
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public void submitJson(String json) {
		//attempt to send json to server, if exception save it for later
		HttpPost httppost = new HttpPost("http://" + sharedPref.getString("connection_server_uri", "thresher.uib.no"));
		try {
			StringEntity postContent = new StringEntity(json);
			httppost.addHeader("content-type", "contcation/json");
			httppost.setEntity(postContent);
			try {
				HttpResponse response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				notSentJson.add(json);
				Log.v(TAG, e.getMessage());
			} catch (IOException e) {
				notSentJson.add(json);
				Log.v(TAG, e.getMessage());
			}
			finally {
				httpclient.getConnectionManager().shutdown();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.v(TAG, e.getMessage());
		}
	}
}
