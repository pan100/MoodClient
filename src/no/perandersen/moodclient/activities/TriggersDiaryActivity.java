package no.perandersen.moodclient.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.application.MoodApplication;
import no.perandersen.moodclient.fragments.DatePickerFragment;
import no.perandersen.moodclient.fragments.PasswordDialogBuilder;
import no.perandersen.moodclient.model.Day;
import no.perandersen.moodclient.model.Day.DayBuilder;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class TriggersDiaryActivity extends Activity implements MoodClientActivityInterface{
	private Day.DayBuilder dayBuilder;
	private SharedPreferences prefs;
	private Button dateButton;
	MultiAutoCompleteTextView triggerTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_triggers_diary);
		// Show the Up button in the action bar.
		setupActionBar();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(getIntent().hasExtra("DAY_BUILDER")) {
			dayBuilder = (DayBuilder) getIntent().getSerializableExtra("DAY_BUILDER");
		}
		else {
			dayBuilder = new Day.DayBuilder(new Date(), prefs.getString("connection_username", ""));
		}
		dateButton = (Button) findViewById(R.id.dateButton);
		dateButton.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(dayBuilder.getDate()));
		
		triggerTextView = (MultiAutoCompleteTextView) findViewById(R.id.triggersTextView);
		triggerTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}
	
	public void setDateClick(View view) {
	    DialogFragment newFragment = new DatePickerFragment();
	    ((DatePickerFragment) newFragment).setDate(dayBuilder.getDate());
	    newFragment.show(this.getFragmentManager(), "Velg dato for logging");
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
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

	public void saveClick(View w) {
		EditText diaryEditText = (EditText) findViewById(R.id.diaryEditText);
		dayBuilder.diaryText(diaryEditText.getText().toString());
		
		ArrayList<String> triggers = new ArrayList<String>(Arrays.asList(triggerTextView.getText().toString().split("\\s+")));
		dayBuilder.setTriggers(triggers);
		//ask the user for his password
//		PasswordDialogBuilder alert = new PasswordDialogBuilder(this);
//
//		alert.show();
		//in the pilot, we don't want to do this as it hinders usability
		save(prefs.getString("connection_password", ""));
	}
	
	public void previousButtonClick(View w) {
		EditText diaryEditText = (EditText) findViewById(R.id.diaryEditText);
		dayBuilder.diaryText(diaryEditText.getText().toString());
		
		ArrayList<String> triggers = new ArrayList<String>(Arrays.asList(triggerTextView.getText().toString().split("\\s+")));
		dayBuilder.setTriggers(triggers);
		Intent startActivityIntent = new Intent(TriggersDiaryActivity.this, MoodActivity.class);
		startActivityIntent.putExtra("DAY_BUILDER", dayBuilder);
		TriggersDiaryActivity.this.startActivity(startActivityIntent);
		finish();
		
	}

	@Override
	public void dateSetCallback(Date date) {
		// TODO Auto-generated method stub
		
	}
	
	public void save(String password) {
		// disable save button
		Button saveButton = (Button) findViewById(R.id.button2);
		saveButton.setEnabled(false);
		dayBuilder.password(password);
		new SaveTask().execute(dayBuilder.build());
	}
	
	private class SaveTask extends AsyncTask<Day, Void, String> {

		@Override
		protected String doInBackground(Day... params) {
			MoodApplication app = (MoodApplication)getApplicationContext();
			return app.persister.persist(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// show a small toast
			Context context = getApplicationContext();
			//TODO strings.xml
			CharSequence text = result;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			// enable save button
			Button saveButton = (Button) findViewById(R.id.button2);
			saveButton.setEnabled(true);

			// SLEEP 2 SECONDS HERE ...
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					startActivity(new Intent(TriggersDiaryActivity.this,MainActivity.class));
				}
			}, 2000);
			finish();
		}
	}

}
