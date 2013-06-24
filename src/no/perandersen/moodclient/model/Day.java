package no.perandersen.moodclient.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 * Class Day
 * A representation of a day entry. Has a builder because there are a lot of parameters. Many optional.
 * It is created through the internal class DayBuilder and has boolean values indicating if the fields have been set
 */

//Builder Pattern
public class Day implements JSONable {
	private static final String TAG = "Day";
	private final Date date; // required
	private final String username; // required
	
	//must be set before building
	private final String password;
	
	// the preceding fields are optional, but at least one should be in the
	// object or it makes no sense
	private final Integer sleepHours;			//Integer to check for null values
	private final Integer moodHigh;
	private final Integer moodLow;
	private final ArrayList<String> triggers;
	private final String diaryText;

	public static class DayBuilder implements Serializable {
		private Date date; // required but not necessarily final
		private final String username; // required
		
		private String password;
		private Integer sleepHours;
		private Integer moodHigh;
		private Integer moodLow;
		private ArrayList<String> triggers = new ArrayList<String>();
		private String diaryText;

		public DayBuilder(Date date, String username) {
			this.date = date;
			this.username = username;
		}
		
		public DayBuilder changeDate(Date date) {
			this.date = date;
			return this;
		}
		
		public DayBuilder password(String pass) {
			password = pass;
			return this;
		}
		public DayBuilder sleepHours(int val) {
			sleepHours = val;
			return this;
		}
		public DayBuilder moodHigh(int val) {
			moodHigh = val;
			return this;
		}
		public DayBuilder moodLow(int val) {
			moodLow = val;
			return this;
		}
		public DayBuilder addTrigger(String trigger) {
			triggers.add(trigger);
			return this;
		}
		
		public DayBuilder setTriggers(ArrayList<String> triggers) {
			this.triggers = triggers;
			return this;
		}
		
		public DayBuilder diaryText(String text) {
			diaryText = text;
			return this;
		}
		
		public Date getDate() {
			return date;
		}
		
		public Day build() {
			return new Day(this);
		}
	}

	private Day(DayBuilder builder) {
		password = builder.password;
		date = builder.date;
		username = builder.username;
		moodLow = builder.moodLow;
		moodHigh =builder.moodHigh;
		sleepHours = builder.sleepHours;
		diaryText = builder.diaryText;
		triggers = builder.triggers;
		
	}

		/**
		 * When using toJSONObject, remember to put password on to object before sending but not
		 * before serializing
		 */
	@Override
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		//date to be dd.mm.yyyy
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String dateString = sdf.format(date);
		Log.v(TAG, "date was formatted as " + sdf.format(date));
		jo.put("date", dateString);
		jo.put("username", username);
		jo.put("password", password);
		if(sleepHours != null) {
			jo.put("sleepHours", sleepHours.toString());
		}
		if(moodLow != null) {
			jo.put("moodMin", moodLow.toString());
		}
		if(moodHigh != null) {
			jo.put("moodMax", moodHigh.toString());
		}
		if(!triggers.isEmpty()) {
			jo.put("triggers", new JSONArray(triggers));
		}
		if(diaryText != null && !diaryText.equals("")) {
			jo.put("diaryText", diaryText);
		}
		return jo;
	}

	@Override
	public void fromJSONObject(JSONObject src) {
		// TODO Auto-generated method stub
		
	}
}
