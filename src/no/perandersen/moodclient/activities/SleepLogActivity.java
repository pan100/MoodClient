package no.perandersen.moodclient.activities;

import java.util.Date;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.application.MoodApplication;
import no.perandersen.moodclient.fragments.DatePickerFragment;
import no.perandersen.moodclient.fragments.PasswordDialogBuilder;
import no.perandersen.moodclient.model.Day;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;

public class SleepLogActivity extends Activity implements MoodClientActivity{
	private NumberPicker np;
	private Button dateButton;
	private Day.DayBuilder dayBuilder;
	private SharedPreferences prefs;
	private Date date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_log);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		dateButton = (Button) findViewById(R.id.dateButton);
		date = new Date();
		dateButton.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(date));
		
		// for the number picker
		np = (NumberPicker) findViewById(R.id.np);
		String[] nums = new String[25];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i);
		np.setMinValue(0);
		np.setMaxValue(24);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(nums);
		np.setValue(7);
		dayBuilder = new Day.DayBuilder(date, prefs.getString("connection_username", ""));
		dayBuilder.sleepHours(7);
		
		np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		np.setOnValueChangedListener(new OnValueChangeListener() {	
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				dayBuilder.sleepHours(newVal);
			}
		});
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

			// commented out because of frustration.
			// NavUtils.navigateUpFromSameTask(this);
			// instead hard coded
			startActivity(new Intent(this, MainActivity.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setDateClick(View view) {
	    DialogFragment newFragment = new DatePickerFragment();
	    ((DatePickerFragment) newFragment).setDate(date);
	    newFragment.show(this.getFragmentManager(), "Velg dato for logging");
	}
	
	public void dateSetCallback(Date date) {
		this.date = date;
		dateButton.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(date));
		dayBuilder.changeDate(date);
	}

	/*
	 * Method saveAndClose Called when clicked the "save" button. Brings up a
	 * toast for confirmation. As of yet, it brings the home screen to the
	 * front, but it will save the data later.
	 */

	public void saveAndClose(View view) {
		//ask the user for his password
		PasswordDialogBuilder alert = new PasswordDialogBuilder(this);

		alert.show();
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
					startActivity(new Intent(SleepLogActivity.this,MainActivity.class));
				}
			}, 2000);
		}
	}

}
