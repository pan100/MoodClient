package no.perandersen.moodclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TextAndImageLogActivity extends Activity {
	
    private String selectedImagePath;
    private ImageButton imgButton;
	private Uri selectedImageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_and_image_log);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		imgButton = (ImageButton)findViewById(R.id.imageView1);
		
		if(selectedImageUri == null) {
			imgButton.setImageResource(R.drawable.img_placeholder);
		}
		else {
			imgButton.setImageURI(selectedImageUri);
		}
		
		imgButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_text_and_image_log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setImage(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 1);
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                imgButton.setImageURI(selectedImageUri);
            }
        }
		//show a small toast
		Context context = getApplicationContext();
		CharSequence text = "Bilde valgt";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    }

    public String getPath(Uri uri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if(cursor.moveToFirst()){;
           int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
           res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
	
	
