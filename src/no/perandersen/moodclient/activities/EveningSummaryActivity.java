package no.perandersen.moodclient.activities;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.R.layout;
import no.perandersen.moodclient.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EveningSummaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evening_summary);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evening_summary, menu);
		return true;
	}

}
