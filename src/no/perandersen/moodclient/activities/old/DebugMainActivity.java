package no.perandersen.moodclient.activities.old;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.activities.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DebugMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug_main);
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
	
	public void openSelectedActivity(View v) {
		
		
		
		Button b = (Button)v;
		
		Intent startActivityIntent = null;
		
		switch(b.getId()) {
		case R.id.MainActivityButton:
			startActivityIntent = new Intent(DebugMainActivity.this, MainActivity.class);
			break;
		case R.id.MedicineLoggerActivityButton:
			startActivityIntent = new Intent(DebugMainActivity.this, MedicineLogActivity.class);
			break;
		case R.id.SleepLogActivityButton:
			startActivityIntent = new Intent(DebugMainActivity.this, SleepLogActivity.class);
			break;
		case R.id.EveningButton:
			startActivityIntent = new Intent(DebugMainActivity.this, MoodActivity.class);
			break;		
		}
		if(startActivityIntent != null) {
			DebugMainActivity.this.startActivity(startActivityIntent);			
		}

	}

}
