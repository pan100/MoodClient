package no.perandersen.moodclient.guiwidgets;

import no.perandersen.moodclient.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

//https://github.com/krishnalalstha/Spannable/blob/master/src/com/krishna/widget/CustomMultiAutoCompleteTextView.java
//http://stackoverflow.com/questions/3482981/how-to-replace-the-comma-with-a-space-when-i-use-the-multiautocompletetextview
//http://www.kpbird.com/2013/02/android-chips-edittext-token-edittext.html (mostly this one)
//UNFINISHED WORK
public class BubbleMultiAutoCompleteTextView extends MultiAutoCompleteTextView {

	private Context context;
	private LayoutInflater li;
	private String[] triggers;

	public BubbleMultiAutoCompleteTextView(Context context) {
		super(context);
		init(context);
	}
	
	 // Constructor 
	 public BubbleMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  init(context);
	 }
	 // Constructor 
	 public BubbleMultiAutoCompleteTextView(Context context, AttributeSet attrs,
	   int defStyle) {
	  super(context, attrs, defStyle);
	  init(context);
	 }

	public void init(Context context) {
		this.context = context;
		li = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.setTokenizer(new TriggerSpaceTokenizer());
		this.addTextChangedListener(watcher);

	}

	public String[] getTriggers() {
		return triggers;
	}

	public void setTriggersArray(String[] triggers) {
		this.triggers = triggers;
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (count >= 1) {
				if (s.charAt(start) == ' ') {
					bubbleWord();
				}
			}

		}

		private void bubbleWord() {
			int numberOfBubbles = 0;

			String triggersString = getText().toString();
			if (triggersString.contains(" ")) {
				SpannableStringBuilder ssb = new SpannableStringBuilder(
						getText());
				BubbleMultiAutoCompleteTextView.this
						.setTriggersArray(triggersString.trim().split(" "));

				String[] triggers = BubbleMultiAutoCompleteTextView.this
						.getTriggers();
				for (String trigger : triggers) {
					LayoutInflater lf = (LayoutInflater) getContext()
							.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					TextView textView = (TextView) lf.inflate(
							R.layout.bubble_edit, null);
					textView.setText(trigger); // set text
					int spec = MeasureSpec.makeMeasureSpec(0,
							MeasureSpec.UNSPECIFIED);
					textView.measure(spec, spec);
					textView.layout(0, 0, textView.getMeasuredWidth(),
							textView.getMeasuredHeight());
					Bitmap b = Bitmap.createBitmap(textView.getWidth(),
							textView.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(b);
					canvas.translate(-textView.getScrollX(),
							-textView.getScrollY());
					textView.draw(canvas);
					textView.setDrawingCacheEnabled(true);
					Bitmap cacheBmp = textView.getDrawingCache();
					Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888,
							true);
					textView.destroyDrawingCache(); // destory drawable
					// create bitmap drawable for imagespan
					@SuppressWarnings("deprecation")
					BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
					bmpDrawable.setBounds(0, 0,
							bmpDrawable.getIntrinsicWidth(),
							bmpDrawable.getIntrinsicHeight());
					// create and set imagespan
					ssb.setSpan(new ImageSpan(bmpDrawable), numberOfBubbles,
							numberOfBubbles + trigger.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					numberOfBubbles = numberOfBubbles + trigger.length() + 1;
				}
				// set chips span
				setText(ssb);
				// move cursor to last
				setSelection(getText().length());
			}
		}

	};

	private class TriggerSpaceTokenizer implements Tokenizer {

		@Override
		public int findTokenEnd(CharSequence text, int cursor) {
			int i = cursor;

			while (i > 0 && text.charAt(i - 1) != ' ') {
				i--;
			}
			while (i < cursor && text.charAt(i) == ' ') {
				i++;
			}

			return i;
		}

		@Override
		public int findTokenStart(CharSequence text, int cursor) {
			// TODO Auto-generated method stub
			int i = cursor;
			int len = text.length();

			while (i < len) {
				if (text.charAt(i) == ' ') {
					return i;
				} else {
					i++;
				}
			}

			return len;
		}

		@Override
		public CharSequence terminateToken(CharSequence text) {
			// TODO Auto-generated method stub
			int i = text.length();

			while (i > 0 && text.charAt(i - 1) == ' ') {
				i--;
			}

			if (i > 0 && text.charAt(i - 1) == ' ') {
				return text;
			} else {
				if (text instanceof Spanned) {
					SpannableString sp = new SpannableString(text + " ");
					TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
							Object.class, sp, 0);
					return sp;
				} else {
					return text + " ";
				}
			}
		}

	}

}
