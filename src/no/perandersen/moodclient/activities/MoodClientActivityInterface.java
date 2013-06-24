package no.perandersen.moodclient.activities;

import java.util.Date;

import android.view.View;

public interface MoodClientActivityInterface {
	public void setDateClick(View view);
	public void dateSetCallback(Date date);
	public void save(String password);
}
