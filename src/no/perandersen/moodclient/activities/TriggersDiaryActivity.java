package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;

public class TriggersDiaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_triggers_diary);
		// Show the Up button in the action bar.
		setupActionBar();
		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.triggersTextView);
		textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_debug_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
	}
	
	public void nextButtonClick(View w) {
		//IMPLEMENT AND SEND TO THE SUMMARY
	}
	
	public void previousButtonClick(View w) {
		Intent startActivityIntent = new Intent(TriggersDiaryActivity.this, MoodActivity.class);
		TriggersDiaryActivity.this.startActivity(startActivityIntent);
		
	}

}
