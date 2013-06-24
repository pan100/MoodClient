package no.perandersen.moodclient.fragments;

import java.util.Calendar;
import java.util.Date;

import no.perandersen.moodclient.activities.MoodClientActivityInterface;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private Date date;
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
		if(date != null) {
			c.setTime(date);
		}
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    	Calendar c = Calendar.getInstance();
    	c.set(year, month, day);
    	date = c.getTime();
        ((MoodClientActivityInterface) getActivity()).dateSetCallback(date);
    	
    }
    
    public void setDate(Date date) {
    	this.date = date;
    }

}
