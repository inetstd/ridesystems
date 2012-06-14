package com.ad.android.ridesystems.passengercounter.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.common.InAppDebug;
import com.ad.android.ridesystems.passengercounter.model.vo.LoginScreenViewType;

/**
 * Application status. Show dates of last sync with WS
 *
 */
public class ApplicationSettingsActivity extends ABaseActivity {

	
	private final int LOG_DIALOG = 112312;
	
	Spinner spinner;
	TextView token;
	TextView wsUrl;	
	TextView deviceName;	
	Button showLogsbutton;
	Button resetLogsbutton;
	
	/**
	 * Constructor.
	 */
	public ApplicationSettingsActivity() {
		super(R.layout.layout_application_settings);
	}
	
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case LOG_DIALOG:	    	
			dialog = new Dialog(this);			
			dialog.setContentView(R.layout.layout_log);
			dialog.setTitle("Logs");						
		    break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
	    case LOG_DIALOG:	    					
			TextView text = (TextView) dialog.findViewById(R.id.log_text);		
			if (text != null) text.setText(InAppDebug.read(this));				
	        break;
	    default:
	        dialog = null;
	    }
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpViews() {
		spinner = (Spinner) findViewById(R.id.application_settings_login_screen_type_spinner);
		token = (TextView) findViewById(R.id.application_settings_ws_token_value);
		wsUrl = (TextView) findViewById(R.id.application_settings_ws_url_value);		
		deviceName = (TextView) findViewById(R.id.application_settings_devive_name);
		showLogsbutton = (Button) findViewById(R.id.application_settings_logs); 				
		resetLogsbutton = (Button) findViewById(R.id.application_settings_reset_logs);
		
		showLogsbutton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {							
				showDialog(LOG_DIALOG);		        
			}
		});
		
		 				
		resetLogsbutton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				InAppDebug.clean(ApplicationSettingsActivity.this);	        
			}
		});
		
		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
		String loginType = settings.getString(Config.C_LOGIN_TYPE, "");

		// login screen type management	    
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.application_settings_login_screen_type_array));
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	    spinner.setAdapter(adapter);
	    
	    LoginScreenViewType type;
	    try {
			type = LoginScreenViewType.valueOf(loginType);
		} catch (Exception e) {
			type = LoginScreenViewType.USERNAME;
		}
	    spinner.setSelection(LoginScreenViewType.indexOf(type));	    
	    showAuthDialog();	    
	}
	
	/**
	 * Check admin login
	 */
	private void showAuthDialog() {
		
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.application_settings_password_prompt_title);	    	   
	    LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    final LinearLayout passwordContainer = (LinearLayout) layoutInflater.inflate(R.layout.layout_password_prompt, null);	    
	    builder.setView(passwordContainer);
	    builder.setCancelable(false);	   
	    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
	    	String value = ((EditText)passwordContainer.findViewById(R.id.password)).getText().toString();
	    	if (value.equals(Config.getInstance().getApplicationSettingsConfigPassword())) {
	    		dialog.dismiss();
	    	} else {
	    		Toast.makeText(ApplicationSettingsActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
	    		showAuthDialog();
	    	}
	    	
	      }
	    });

	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int whichButton) {
	        dialog.dismiss();
	        setResult(RESULT_CANCELED);
	        finish();
	      }
	    });
	    
	    AlertDialog dialog = builder.create();
	    
	    dialog.show();

	    // hide background
	    
	    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();  
	    lp.dimAmount=0.95f;  
	    dialog.getWindow().setAttributes(lp);
	    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	      
	}
	
	/**
	 * re read config on reopening activity
	 */
	@Override
	protected void onResume() { 
		super.onResume();
		readConfig();
	    token.setText(Config.getInstance().gebWebServiceToken());
	    wsUrl.setText(Config.getInstance().getWebServiceUrl());	   
	    deviceName.setText(Config.getInstance().getDeviceName());
	}
	
	/**
	 * Override bottom menu.
	 */
	@Override
	public void setUpMenu() {
		super.setUpMenu();
		
		Button back = (Button) findViewById(R.id.menu_btn_back);		
		Button next = (Button) findViewById(R.id.menu_btn_next);
		Button menu = (Button) findViewById(R.id.menu_btn_menu);
		
		menu.setVisibility(View.GONE);
				
		if (back != null) {
			back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					ApplicationSettingsActivity.this.finish();
				}
			});
		}
		
		if (next != null) {	
			next.setText("Save");
			next.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(Config.C_LOGIN_TYPE, LoginScreenViewType.values()[(int) spinner.getSelectedItemId()].toString());
					editor.putString(Config.C_WS_TOKEN, token.getText().toString());					
					editor.putString(Config.C_WS_URL, wsUrl.getText().toString());
					editor.putString(Config.C_DEVICE_NAME, deviceName.getText().toString());
					editor.commit();
					Toast.makeText(ApplicationSettingsActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
					ApplicationSettingsActivity.this.readConfig();
					setResult(RESULT_OK);
					finish();
				}
			});
			
		}	
	}

}
