package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.R.id;
import no.perandersen.moodclient.R.layout;
import no.perandersen.moodclient.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

public class EditTodayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_today);
		
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
		getMenuInflater().inflate(R.menu.activity_debug_main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, SettingsActivity.class));
		return true;
		
	}

}
