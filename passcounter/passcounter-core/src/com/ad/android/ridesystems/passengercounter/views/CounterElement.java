package com.ad.android.ridesystems.passengercounter.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;

/**
 * Counter element.
 * Set default listeners for +/- buttons 
 *
 */
public class CounterElement extends RelativeLayout {
	
	private Button minusButton = null;
	
	private Button plusButton = null;
	
	private TextView title = null;
	
	private TextView score = null;	
	
	private Integer iscore = 0;

	private Vibrator vibrator = null;
	
	private MediaPlayer mediaPlayer = null;
	
	private RouteInstanceDetail routeTrackingResult = null;
	
	private OnChangeListener changeListener = null;
	
	/**
	 * Set basic layout 
	 * @param context
	 * @param attrs
	 */
	public CounterElement(Context context, AttributeSet attrs) {
		super(context, attrs);		
		setGravity(Gravity.CENTER);	
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);		
		 
	}
	
	/**
	 * Setting actions to counter 
	 * @param criteria 
	 */
	public void initCounter(RouteTrackingCriteria criteria, RouteInstanceDetail result) {
		
		this.routeTrackingResult = result;
		minusButton = (Button) findViewById(R.id.counter_element_button_down);
		plusButton = (Button) findViewById(R.id.counter_element_button_up);
		
		title = (TextView) findViewById(R.id.counter_element_title);
		score = (TextView) findViewById(R.id.counter_element_score);
		
		
		title.setText(criteria.getDescription());		
		
		minusButton.setOnClickListener(minusButtonClick);
		plusButton.setOnClickListener(plusButtonClick);
		
		this.iscore = result.getQuantity();	
		updateScore();				
	}
	
	/**
	 * Update label with score
	 */
	private void updateScore() {
		score.setText(iscore.toString());
		this.routeTrackingResult.setQuantity(iscore);
		if (changeListener != null) changeListener.onChange(this);
	}
	
	/**
	 * Reset element score to 0
	 */
	public void resetScore() {
		iscore = 0;
		updateScore();
	}
		
	public Integer getIscore() {
		return iscore;
	}

	public void setIscore(Integer iscore) {
		this.iscore = iscore;
		updateScore();
	}




	/**
	 * On plus click handler
	 */
	private View.OnClickListener minusButtonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mediaPlayer = MediaPlayer.create(CounterElement.this.getContext(), R.raw.minus);   
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
			if (iscore > 0) { 
				iscore--;
				updateScore();				
			}
			if (vibrator != null) vibrator.vibrate(50);
		}
	};

	/**
	 * On minus click handler
	 */
	private View.OnClickListener plusButtonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mediaPlayer = MediaPlayer.create(CounterElement.this.getContext(), R.raw.add);   
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
			iscore++;
			updateScore();
			if (vibrator != null) vibrator.vibrate(50);
		}
	};
	
	public interface OnChangeListener {		
		void onChange(CounterElement ce);
	}

	public OnChangeListener getChangeListener() {
		return changeListener;
	}

	public void setChangeListener(OnChangeListener changeListener) {
		this.changeListener = changeListener;
	}
	
	
	
	
}
