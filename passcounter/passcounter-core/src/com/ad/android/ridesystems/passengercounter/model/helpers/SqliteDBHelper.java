package com.ad.android.ridesystems.passengercounter.model.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ad.android.ridesystems.passengercounter.common.Config;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;

/**
 * Database Helper.   
 *
 */
public class SqliteDBHelper extends SQLiteOpenHelper {

	Context mcontext;
	String schema = "";
	
	/**
	 * Constructor.
	 * @param context
	 */
	public SqliteDBHelper(Context context) {
		super(context, 
				Config.getInstance().getDataBaseName(), 
				null, 
				Config.getInstance().getDataBaseVersion());
		mcontext = context;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {		
		try {			
			createDB(db);	
		} catch (SQLException e) {
			Log.e("test", "eee" + e.getMessage());
		} finally {

		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
		db.execSQL("DROP TABLE IF EXISTS employee");
		db.execSQL("DROP TABLE IF EXISTS vehicle");
		db.execSQL("DROP TABLE IF EXISTS route");
		db.execSQL("DROP TABLE IF EXISTS routestop");
		db.execSQL("DROP TABLE IF EXISTS trackinglevel");
		db.execSQL("DROP TABLE IF EXISTS routetrackingcriteria");
		db.execSQL("DROP TABLE IF EXISTS routeinstance");
		db.execSQL("DROP TABLE IF EXISTS routeinstancedetail");
		db.execSQL("DROP TABLE IF EXISTS vehicleroute");
		createDB(db);
	}
	
	private void createDB(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(Employee.SQL_CREATE_TABLE);
		db.execSQL(Vehicle.SQL_CREATE_TABLE);
		db.execSQL(Route.SQL_CREATE_TABLE);
		db.execSQL(RouteStop.SQL_CREATE_TABLE);
		db.execSQL(TrackingLevel.SQL_CREATE_TABLE);
		db.execSQL(RouteTrackingCriteria.SQL_CREATE_TABLE);
		db.execSQL(RouteInstance.SQL_CREATE_TABLE);
		db.execSQL(RouteInstanceDetail.SQL_CREATE_TABLE);
		db.execSQL(VehicleRoute.SQL_CREATE_TABLE);
		
		SharedPreferences settings = mcontext.getSharedPreferences(Config.C_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(Config.C_LAST_APP_DATA_SYNC, "");
		editor.commit();
	}



}
