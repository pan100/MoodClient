package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.application.MoodApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getString("connection_username", "").equals("")) {
			//show a dialog about username not set and open settings.
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.dialog_username_not_set)
			       .setTitle(R.string.username).setPositiveButton(R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//open settings
						startActivity(new Intent(MainActivity.this, SettingsActivity.class));
						
					}
				});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, SettingsActivity.class));
		return true;
		
	}
	
	public void openWebInterface(View view) {
		//send the user to the web interface in the browser
		String url = prefs.getString("connection_server_uri", "http://thresher.uib.no");
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}
	
	public void sleepLogButton(View view) {
		startActivity(new Intent(this, SleepLogActivity.class));
	}
	
	public void eveningLogButton(View view) {
		startActivity(new Intent(this, MoodActivity.class));
	}
	
	public void sendOffline(View v) {
		MoodApplication app = (MoodApplication)getApplicationContext();
		new SaveJsonTask().execute(app);
	}
	
	private class SaveJsonTask extends AsyncTask<Context, Void, Void> {
		@Override
		protected void onPostExecute(Void v) {

		}

		@Override
		protected Void doInBackground(Context... arg0) {
			MoodApplication app = (MoodApplication)getApplicationContext();
			app.persister.sendDaysInTempFile();
			return null;
		}
	}


}
