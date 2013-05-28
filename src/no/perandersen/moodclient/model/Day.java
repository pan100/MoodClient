/**
 * 
 */
package no.perandersen.moodclient.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Per-Øivin Berg Andersen 2013
 *
 */
public class Day {
	
	private Date date;
	private String username;
	private int sleep;
	private int moodLow;
	private int moodHigh;
	private ArrayList<String> triggers;
	private String diaryText;
	private boolean sleepIsset, moodIsset, diaryIsset;
	
	public Day(String username) {
		date = new Date();
		this.username = username;
		
		sleepIsset = false;
		moodIsset = false;
		diaryIsset = false;		
		sleep = 0;
		moodLow = 0;
		moodHigh = 0;
		
		triggers = new ArrayList<String>();
		
		diaryText = "";
	}
	
	public Day(String username, Date date) {
		this.date = date;
		this.username = username;
		
		sleepIsset = false;
		moodIsset = false;
		
		sleep = 0;
		moodLow = 0;
		moodHigh = 0;
		
		triggers = new ArrayList<String>();
	}
	
	public void persist(String password) {
		String json = "{";
		json += "'username' : '" + username + "'";
		if (sleepIsset) {
			json += ", ";
			json += "'sleepHours' : '" + Integer.toString(sleep) + "'";
		}
		
		if(moodIsset) {
			json += ", ";
			json += "'moodMin': '" + Integer.toString(moodLow) + "'";
			json += ", ";
			json += "'moodMax': '" + Integer.toString(moodHigh) + "'";
		}
		
		if(!getDiaryText().isEmpty()) {
			for(String triggertext : getTriggers()) {
				
			}
		}
		
		json += "}";
		
	}
	

	public String getDiaryText() {
		return diaryText;
	}

	public void setDiaryText(String diaryText) {
		diaryIsset = true;
		this.diaryText = diaryText;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
		sleepIsset = true;
	}

	public int getMoodLow() {
		return moodLow;
	}

	public void setMoodLow(int moodLow) {
		this.moodLow = moodLow;
		moodIsset = true;
	}

	public int getMoodHigh() {
		return moodHigh;
	}

	public void setMoodHigh(int moodHigh) {
		this.moodHigh = moodHigh;
		moodIsset = true;
	}

	public boolean isMoodIsset() {
		return moodIsset;
	}

	public void setMoodIsset(boolean moodIsset) {
		this.moodIsset = moodIsset;
	}
	
	public void addTrigger(String triggertext) {
		triggers.add(triggertext);
	}
	
	public ArrayList<String> getTriggers() {
		return triggers;
	}
	
	public boolean sleepIsset() {
		return sleepIsset;
	}
	public boolean moodIsset() {
		return moodIsset;
	}
	public boolean diaryIsset() {
		return diaryIsset;
	}
	
}
