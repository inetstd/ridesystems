package com.ad.android.ridesystems.passengercounter.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ad.android.ridesystems.passengercounter.R;

/**
 * Counter element.
 * Set default listeners for +/- buttons 
 *
 */
public class NumericalKeyboard extends LinearLayout {

	public static int MAX = 99;	
	Vibrator vibrator = null;
	List<Button> buttons = new ArrayList<Button>();
	private OnChangeListener changeListener = null; 
	private int currentValue = 0;	
	private MediaPlayer mediaPlayer = null;	
	private boolean tablet = false;	
	private boolean hvga = false;
	
	private WindowManager wm;
	private DisplayMetrics dm;

	/**
	 * Set basic layout 
	 * @param context
	 * @param attrs
	 */
	public NumericalKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);		
		setGravity(Gravity.CENTER);	
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);        
        tablet = dm.heightPixels > 800;
        hvga   = dm.heightPixels < 800;               
	}
	


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		LinearLayout buttonsCont = (LinearLayout) findViewById(R.id.keypad_num_buttons); 
		// getting all buttons from view
		for (int i = 0; i < buttonsCont.getChildCount(); i++) {			
			LinearLayout ll = (LinearLayout) buttonsCont.getChildAt(i); //button row			
			for (int j = 0; j < ll.getChildCount(); j++) {
				if (ll.getChildAt(j) instanceof Button) { //button  
					buttons.add((Button) ll.getChildAt(j));  
				}
			}
		}
						
		
		if (tablet) {
			for (Button button : buttons) {
				LayoutParams lp = (LayoutParams) button.getLayoutParams();
				lp.height = dm.widthPixels / 4;
				button.setLayoutParams(lp);
			}
		}
		
		if (hvga) {
			for (Button button : buttons) {
				button.setTextSize(28);
			}
		}
		
		buttons.add((Button) findViewById(R.id.keypad_minus_button));
		buttons.add((Button) findViewById(R.id.keypad_plus_button));
		
		
		
		

		for (Button btn : buttons) {
			String btnName = btn.getText().toString();
			if (btnName.equals("+")) {
				btn.setOnClickListener(plusClickListener);
			} else if (btnName.equals("-")) {
				btn.setOnClickListener(minusClickListener);
			} else if (btnName.toLowerCase().equals("clear")) {
				btn.setOnClickListener(clearClickListener);
			} else {				
				try {
					// if button is number
					Integer.parseInt(btnName);
					btn.setOnClickListener(numberClickListener);
				} catch (NumberFormatException e) {

				}
			}
		}
		
	}

	protected void triggerOnChange(int currentValue) {
		if (changeListener != null) changeListener.onChange(this);
	}


	public int getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(int v) {
		currentValue = v;
		triggerOnChange(currentValue);
	}

	public void setOnChangeListener(OnChangeListener changeListener) {
		this.changeListener = changeListener;
	}



	public interface OnChangeListener {		
		void onChange(NumericalKeyboard keypad);
	}
	
	

	OnClickListener numberClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String key = ((Button) v).getText().toString();
			int buttonValue = -1;			
			try {
				mediaPlayer = MediaPlayer.create(NumericalKeyboard.this.getContext(), R.raw.numclick);   
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    mp.release();
	                }
	            });
				buttonValue = Integer.parseInt(key);
				if (currentValue > 9) return;
				currentValue = Integer.parseInt(currentValue + "" + buttonValue);
//				if (currentValue > MAX) currentValue = MAX;
			} catch (NumberFormatException e) {
				currentValue = MAX;					
			}							
			triggerOnChange(currentValue);	
		}
	};
	
	OnClickListener minusClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentValue > 0){ 
				triggerOnChange(--currentValue);
				mediaPlayer = MediaPlayer.create(NumericalKeyboard.this.getContext(), R.raw.minus);   
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    mp.release();
	                }
	            });
			}
		}
	};
	
	OnClickListener plusClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if (currentValue < MAX) {
				triggerOnChange(++currentValue);
				mediaPlayer = MediaPlayer.create(NumericalKeyboard.this.getContext(), R.raw.add);   
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    mp.release();
	                }
	            });
			}
		}
	};
	
	OnClickListener clearClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mediaPlayer = MediaPlayer.create(NumericalKeyboard.this.getContext(), R.raw.control);   
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
			triggerOnChange((currentValue = 0));	
		}
	};

}
