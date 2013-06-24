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
		// create RangeSeekBar as Integer range between 20 and 75
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 100,
				getBaseContext());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				TextView tLow = (TextView) findViewById(R.id.minValText);
				
				dayBuilder.moodLow(minValue);
				dayBuilder.moodHigh(maxValue);
				
				if (minValue < 10) {
					tLow.setText(R.string.moodScale1);
				} else if (minValue >= 10 && minValue < 20) {
					tLow.setText(R.string.moodScale2);
				} else if (minValue >= 20 && minValue < 30) {
					tLow.setText(R.string.moodScale3);
				} else if (minValue >= 30 && minValue < 40) {
					tLow.setText(R.string.moodScale4);
				} else if (minValue >= 40 && minValue < 60) {
					tLow.setText(R.string.moodScale5);
				} else if (minValue >= 60 && minValue < 70) {
					tLow.setText(R.string.moodScale6);
				} else if (minValue >= 70 && minValue < 80) {
					tLow.setText(R.string.moodScale7);
				} else if (minValue >= 80 && minValue < 90) {
					tLow.setText(R.string.moodScale8);
				} else if (minValue >= 90 && minValue <= 100) {
					tLow.setText(R.string.moodScale9);
				}
				TextView tHigh = (TextView) findViewById(R.id.maxValText);
				if (maxValue < 10) {
					tHigh.setText(R.string.moodScale1);
				} else if (maxValue >= 10 && maxValue < 20) {
					tHigh.setText(R.string.moodScale2);
				} else if (maxValue >= 20 && maxValue < 30) {
					tHigh.setText(R.string.moodScale3);
				} else if (maxValue >= 30 && maxValue < 40) {
					tHigh.setText(R.string.moodScale4);
				} else if (maxValue >= 40 && maxValue < 60) {
					tHigh.setText(R.string.moodScale5);
				} else if (maxValue >= 60 && maxValue < 70) {
					tHigh.setText(R.string.moodScale6);
				} else if (maxValue >= 70 && maxValue < 80) {
					tHigh.setText(R.string.moodScale7);
				} else if (maxValue >= 80 && maxValue < 90) {
					tHigh.setText(R.string.moodScale8);
				} else if (maxValue >= 90 && maxValue <= 100) {
					tHigh.setText(R.string.moodScale9);
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
		
	}

	@Override
	public void save(String password) {
		// TODO Auto-generated method stub
		//this method might be removed from interface later (no need in this activity)
	}


}
