package no.perandersen.moodclient.application;

import no.perandersen.moodclient.R;
import no.perandersen.moodclient.model.Day;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

/**
 * Not in use
 * @author Per
 *
 */
public class SubmitDayAsyncTask extends AsyncTask<Day, Void, Void> {

	@Override
	protected Void doInBackground(Day... day) {
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
//		//show a small toast
//				Context context = getApplicationContext();
//				CharSequence text = "Søvndata lagret... Lukker aktivitet, ha en fin dag!";
//				int duration = Toast.LENGTH_SHORT;
//
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
//				
//				//disable save button
//				Button saveButton = (Button)findViewById(R.id.button2);
//				saveButton.setEnabled(false);
//				
//			    // SLEEP 2 SECONDS HERE ...
//			    Handler handler = new Handler(); 
//			    handler.postDelayed(new Runnable() { 
//			         public void run() {
//			        	 //then we bring the home activity to the front
//			     		Intent intent = new Intent(Intent.ACTION_MAIN);
//			    		intent.addCategory(Intent.CATEGORY_HOME);
//			    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			    		startActivity(intent);	        	 
//			         } 
//			    }, 2000);
	}

}
