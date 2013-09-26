package no.perandersen.moodclient.activities;

import java.text.DateFormat;
import java.util.Date;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.application.MoodApplication;
import no.perandersen.moodclient.fragments.DatePickerFragment;
import no.perandersen.moodclient.guiwidgets.RangeSeekBar;
import no.perandersen.moodclient.guiwidgets.RangeSeekBar.OnRangeSeekBarChangeListener;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MoodActivity extends Activity implements MoodClientActivityInterface{
	
	private Day.DayBuilder dayBuilder;
	private SharedPreferences prefs;
	private Button dateButton;
	private static final String TAG = "MoodActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_evening);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(getIntent().hasExtra("DAY_BUILDER")) {
			dayBuilder = (DayBuilder) getIntent().getSerializableExtra("DAY_BUILDER");
		}
		else {
			dayBuilder = new Day.DayBuilder(new Date(), prefs.getString("connection_username", ""));
		}
		
		dateButton = (Button) findViewById(R.id.dateButton);
		dateButton.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(dayBuilder.getDate()));
		dayBuilder.moodLow(0);
		dayBuilder.moodHigh(100);
		// create RangeSeekBar as Integer range
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 100,
				getBaseContext());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				TextView tLow = (TextView) findViewById(R.id.minValText);
				Log.v(TAG, "value of min: " + minValue + " and value of max: " + maxValue);
				dayBuilder.moodLow(minValue);
				dayBuilder.moodHigh(maxValue);
				
				if (minValue < 12) {
					tLow.setText(R.string.moodScale1);
				} else if (minValue >= 12 && minValue < 25) {
					tLow.setText(R.string.moodScale2);
				} else if (minValue >= 25 && minValue < 37) {
					tLow.setText(R.string.moodScale3);
				} else if (minValue >= 37 && minValue < 62) {
					tLow.setText(R.string.moodScale4);
				} else if (minValue >= 62 && minValue < 75) {
					tLow.setText(R.string.moodScale5);
				} else if (minValue >= 75 && minValue < 88) {
					tLow.setText(R.string.moodScale6);
				} else if (minValue >= 88) {
					tLow.setText(R.string.moodScale7);
				}
				TextView tHigh = (TextView) findViewById(R.id.maxValText);
				if (maxValue < 10) {
					tHigh.setText(R.string.moodScale1);
				} else if (maxValue >= 12 && maxValue < 25) {
					tHigh.setText(R.string.moodScale2);
				} else if (maxValue >= 25 && maxValue < 37) {
					tHigh.setText(R.string.moodScale3);
				} else if (maxValue >= 37 && maxValue < 62) {
					tHigh.setText(R.string.moodScale4);
				} else if (maxValue >= 62 && maxValue < 75) {
					tHigh.setText(R.string.moodScale5);
				} else if (maxValue >= 75 && maxValue < 88) {
					tHigh.setText(R.string.moodScale6);
				} else if (maxValue >= 88) {
					tHigh.setText(R.string.moodScale7);
				}
			}
		});

		// add RangeSeekBar to pre-defined layout
		ViewGroup layout = (ViewGroup) findViewById(R.id.groupForRange);
		layout.addView(seekBar);
	}
	
	public void setDateClick(View view) {
	    DialogFragment newFragment = new DatePickerFragment();
	    ((DatePickerFragment) newFragment).setDate(dayBuilder.getDate());
	    newFragment.show(this.getFragmentManager(), "Velg dato for logging");
	}
	
	public void dateSetCallback(Date date) {
		dateButton.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(date));
		dayBuilder.changeDate(date);
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
	
	public void nextButtonClick(View w) {
		Intent startActivityIntent = new Intent(MoodActivity.this, TriggersDiaryActivity.class);
		startActivityIntent.putExtra("DAY_BUILDER", dayBuilder);
		MoodActivity.this.startActivity(startActivityIntent);
		finish();
		
	}

	@Override
	public void save(String password) {
		// TODO Auto-generated method stub
		//this method might be removed from interface later (no need in this activity)
	}


}
