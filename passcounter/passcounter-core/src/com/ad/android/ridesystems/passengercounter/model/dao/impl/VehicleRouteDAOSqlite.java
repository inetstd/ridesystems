package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.VehicleRoute;

/**
 * 
 * Full implementation of VehicleRoute Data Access Object.
 * 
 *
 */
public class VehicleRouteDAOSqlite extends ADAOSqllite<VehicleRoute, Integer> implements IVehicleRouteDAO {

	
	public VehicleRouteDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(VehicleRoute entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("personId", entity.getPersonId());
		contentValues.put("routeId", entity.getRouteId());
		contentValues.put("vehicleId", entity.getVehicleId());		
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VehicleRoute fromCursor(Cursor cursor) {
		VehicleRoute vehicle = new VehicleRoute();
		vehicle.setId(cursor.getInt(cursor.getColumnIndex("id")));
		vehicle.setPersonId(cursor.getInt(cursor.getColumnIndex("personId")));
		vehicle.setRouteId(cursor.getInt(cursor.getColumnIndex("routeId")));
		vehicle.setVehicleId(cursor.getInt(cursor.getColumnIndex("vehicleId")));
		return vehicle;
	}

	
}
