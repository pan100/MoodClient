package no.perandersen.moodclient.system;

import java.util.ArrayList;

import no.perandersen.moodclient.model.Day;
import android.app.Application;
import android.content.res.Configuration;

public class MoodApplication extends Application {
	private static MoodApplication singleton;
	
	private ArrayList<Day> unsubmittedDays;
	
	public MoodApplication getInstance() {
		return singleton;
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		unsubmittedDays = new ArrayList<Day>();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public void submitDay(Day day) {
		//attempt to send the day, or store it in the unsubmitted days and in db
	}
	
	public boolean hasNonPersistedDays() {
		if(unsubmittedDays.isEmpty()) {
			return false;
		}
		else return true;
	}
	
	public void persistNotPersisted(String password) {
		//if there are any unpersisted days, persist them. Else, do nothing
		if(!unsubmittedDays.isEmpty()) {
			//persist each one
			for (Day day : unsubmittedDays) {
				day.persist(password);
			}
		}
	}
}
