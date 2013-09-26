package no.perandersen.moodclient.system;

import no.perandersen.moodclient.application.MoodApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * when the network is connected, attempt to send held data
		 */
		
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
           
           class SendDays extends AsyncTask<Void, Void, Void> {
        	  private Context context; 
        	public SendDays(Context context) {
        		this.context = context;
        	}
        	
        	   
			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				MoodApplication app = (MoodApplication)context.getApplicationContext();
				app.persister.sendDaysInTempFile();
				return null;
			}
        	   
           }
           
           new SendDays(context).execute();
           
        }
	}
}
