package com.ad.android.ridesystems.passengercounter.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ad.android.ridesystems.passengercounter.R;
import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;
import com.ad.android.ridesystems.passengercounter.model.vo.IPCDataVO;
import com.ad.android.ridesystems.passengercounter.model.vo.LoginScreenViewType;
import com.ad.android.ridesystems.passengercounter.views.EmployeeSpinnerAdapter;

/**
 * 
 * Application main activity.
 * Checks is data fetched ready.
 *
 */
public class LoginActivity extends ABaseActivity {

	EditText loginField = null;

	EditText passwordField = null;

	Spinner loginSpinnerField = null;

	Button exitBtn = null;

	Button submitBtn = null;

	LoginScreenViewType type = null;


	/**
	 * Constructor. Pass view template. 
	 */
	public LoginActivity() {
		super(R.layout.layout_login);		
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences(Config.C_PREFS_NAME, 0);
		String loginType = settings.getString(Config.C_LOGIN_TYPE, "");

		try {
			type = LoginScreenViewType.valueOf(loginType);
		} catch (Exception e) {
			type = LoginScreenViewType.USERNAME;
		}

		List<Employee> employees = getManagerHolder().getEmployeeManager().getAll();
		loginSpinnerField.setAdapter(new EmployeeSpinnerAdapter(this, employees));
		loginSpinnerField.setSelection(Integer.MAX_VALUE); // set default message as first item
		loginSpinnerField.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				loginSpinnerField.setSelection(0);
				return false;
			}
		});
		
		// show or hide fields depend on selected view type
		if (type.equals(LoginScreenViewType.USERNAME_PASSWORD)) {			
			findViewById(R.id.login_plain_login_container).setVisibility(View.GONE);
			findViewById(R.id.login_spinner_login_container).setVisibility(View.VISIBLE);
			findViewById(R.id.login_password_container).setVisibility(View.VISIBLE);
		} else {			
			findViewById(R.id.login_plain_login_container).setVisibility(View.GONE);
			findViewById(R.id.login_spinner_login_container).setVisibility(View.VISIBLE);
			findViewById(R.id.login_password_container).setVisibility(View.GONE);			
		}



	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpViews() {	    
		exitBtn = (Button) findViewById(R.id.login_exit);
		submitBtn = (Button) findViewById(R.id.login_submit);

		loginField = (EditText) findViewById(R.id.login);
		loginSpinnerField = (Spinner) findViewById(R.id.login_spinner);

		passwordField = (EditText) findViewById(R.id.password);

		exitBtn.setOnClickListener(exitBtnOnclick);
		submitBtn.setOnClickListener(submitBtnOnclick);									

		manageOnlineStatus();
	}

	/**
	 * Set listeners to bottom menu buttons
	 */
	public void setUpMenu() {
		super.setUpMenu();
		Button back = (Button) findViewById(R.id.menu_btn_back);
		Button next = (Button) findViewById(R.id.menu_btn_next);

		back.setOnClickListener(exitBtnOnclick);
		back.setText("Exit");
		next.setOnClickListener(submitBtnOnclick);
		next.setText("Log in");
	}

	/**
	 * exit button handler 
	 */
	private View.OnClickListener exitBtnOnclick = new View.OnClickListener() {		
		@Override
		public void onClick(View v) {			

			IPCDataVO dataVO = (IPCDataVO) getIntent().getSerializableExtra(IPCDataVO.KEY);			
			dataVO.setEmployee(null);

			Intent resultIntent = new Intent();			
			resultIntent.putExtra(IPCDataVO.KEY, dataVO);			
			setResult(RESULT_CANCELED, resultIntent);			
			finish();
		}
	};

	/**
	 * submit button handler 
	 */
	private View.OnClickListener submitBtnOnclick = new View.OnClickListener() {	

		@Override
		public void onClick(View v) {


			Employee employee = null;

			// authenticate user depend on LoginScreenViewType
			if (type.equals(LoginScreenViewType.USERNAME_PASSWORD)) {							
				String password = passwordField.getText().toString();				
				employee = (Employee) loginSpinnerField.getSelectedItem();
				boolean passwordIsOk = employee != null && employee.getPassword().equals(password);
				if (!passwordIsOk) {
					employee = null;
				}
			} else if (type.equals(LoginScreenViewType.USERNAME)) {
				employee = (Employee) loginSpinnerField.getSelectedItem();
			}


			// if auth ok - go back with result ok
			if (employee != null) {

				Intent resultIntent = new Intent();

				IPCDataVO dataVO = (IPCDataVO) getIntent().getSerializableExtra(IPCDataVO.KEY);
				dataVO.setEmployee(employee);				
				resultIntent.putExtra(IPCDataVO.KEY, dataVO);
				logi("login successfull");
				setResult(RESULT_OK, resultIntent);
				logi("close (go to landing with )" + employee.getId());
				finish();				

				// otherwise show dialog
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setMessage("Wrong login or password")
				.setCancelable(false)
				.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						passwordField.setText("");
					}
				})
				.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						LoginActivity.this.setResult(RESULT_CANCELED);
						LoginActivity.this.finish();

					}
				});	    	
				AlertDialog alert = builder.create();
				alert.show();
			}
		}		
	};

}