package no.perandersen.moodclient.fragments;

import no.perandersen.moodclient.activities.MoodClientActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

public class PasswordDialogBuilder extends AlertDialog.Builder {

	public PasswordDialogBuilder(final Context context) {
		super(context);
		setTitle("Passord");
		setMessage("Du må trykke inn ditt passord for å lagre dataene på Stemningsloggens server");

		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		input.setTransformationMethod(PasswordTransformationMethod.getInstance());
		setView(input);

		setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  ((MoodClientActivity) context).save(value);
		  }
		});

		setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
	}

}
