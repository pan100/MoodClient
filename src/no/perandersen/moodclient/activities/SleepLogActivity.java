package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.R.id;
import no.perandersen.moodclient.R.layout;
import no.perandersen.moodclient.R.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SleepLogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_log);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		//for the number picker
		NumberPicker np = (NumberPicker) findViewById(R.id.np);
	    String[] nums = new String[25];
	    for(int i=0; i<nums.length; i++)
	           nums[i] = Integer.toString(i);
	    np.setMinValue(0);
	    np.setMaxValue(24);
	    np.setWrapSelectorWheel(false);
	    np.setDisplayedValues(nums);
	    np.setValue(7);
	    np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sleep_log, menu);
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
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Method saveAndClose
	 * Called when clicked the "save" button. Brings up a toast for confirmation. As of yet, it brings the home screen
	 * to the front, but it will save the data later.
	 */
	
	public void saveAndClose(View view) {
		
		//show a small toast
		Context context = getApplicationContext();
		CharSequence text = "Søvndata lagret... Lukker aktivitet, ha en fin dag!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		//disable save button
		Button saveButton = (Button)findViewById(R.id.button2);
		saveButton.setEnabled(false);
		
	    // SLEEP 2 SECONDS HERE ...
	    Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() {
	        	 //then we bring the home activity to the front
	     		Intent intent = new Intent(Intent.ACTION_MAIN);
	    		intent.addCategory(Intent.CATEGORY_HOME);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(intent);	        	 
	         } 
	    }, 2000);
	}

}
