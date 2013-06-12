package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.application.MoodApplication;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    WebView webview = (WebView)findViewById(R.id.mainWebView);
	    webview.getSettings().setJavaScriptEnabled(true);
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    webview.loadUrl(MoodApplication.TRANSFERPROTOCOL + prefs.getString("connection_server_uri", "thresher.uib.no"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_debug_main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, SettingsActivity.class));
		return true;
		
	}

}
