package no.perandersen.moodclient.activities;

import java.util.Date;

public interface MoodClientActivity {
	public void dateSetCallback(Date date);
	public void save(String password);
}
