package com.perandersen.moodclient.model;

import java.util.ArrayList;
import java.util.Date;

/*
 * Class Day
 * A representation of a day entry. Has a builder because there are a lot of parameters. Many optional.
 * It is created through the internal class DayBuilder and has boolean values indicating if the fields have been set
 */

//Builder Pattern
public class Day {
	private final Date date; // required
	private final String username; // required
	// the preceding fields are optional, but at least one should be in the
	// object or it makes no sense
	private final Integer sleepHours;			//Integer to check for null values
	private final Integer moodHigh;
	private final Integer moodLow;
	private final ArrayList<String> triggers;
	private final String diaryText;

	public static class DayBuilder {
		private final Date date; // required
		private final String username; // required

		private Integer sleepHours;
		private Integer moodHigh;
		private Integer moodLow;
		private ArrayList<String> triggers = new ArrayList<String>();
		private String diaryText;

		public DayBuilder(Date date, String username) {
			this.date = date;
			this.username = username;
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
	}

	private Day(DayBuilder builder) {
		date = builder.date;
		username = builder.username;
		moodLow = builder.moodLow;
		moodHigh =builder.moodHigh;
		sleepHours = builder.sleepHours;
		diaryText = builder.diaryText;
		triggers = builder.triggers;
		
	}

	public String JSON() {
		// return the json string using http://json.org/java/
		
		return null;
	}
}
