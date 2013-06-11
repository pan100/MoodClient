package no.perandersen.moodclient.fragments;

import no.perandersen.moodclient.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
//http://developer.android.com/guide/topics/ui/dialogs.html
public class LoginDialogFragment extends DialogFragment {
	//@Override
/*	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.dialog_login, null))
			.setPositiveButton(R.string.authenticate, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//send the username and password back to the activity
					
				}
			})
			.setNegativeButton();
		
		return builder.create();
	}*/
}
