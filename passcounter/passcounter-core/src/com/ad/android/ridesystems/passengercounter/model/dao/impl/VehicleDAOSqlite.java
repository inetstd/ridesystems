package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IVehicleDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Vehicle;

/**
 * 
 * Full implementation of Vehicle Data Access Object.
 * 
 *
 */
public class VehicleDAOSqlite extends ADAOSqllite<Vehicle, Integer> implements IVehicleDAO {

	
	
	public VehicleDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
		defaultOrder = "name ASC";
	}


	@Override
	protected ContentValues toContentValues(Vehicle entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("name", entity.getName());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("vehicleId", entity.getVehicleId());		
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Vehicle fromCursor(Cursor cursor) {
		Vehicle vehicle = new Vehicle();
		vehicle.setId(cursor.getInt(cursor.getColumnIndex("id")));
		vehicle.setName(cursor.getString(cursor.getColumnIndex("name")));
		vehicle.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		vehicle.setVehicleId(cursor.getInt(cursor.getColumnIndex("vehicleId")));
		return vehicle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vehicle getByVehicleId(int vehicleId) {
		Vehicle entity = null;
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL) + " WHERE vehicleId = ? ", new String[] {vehicleId + ""});
		if (cursor.moveToFirst()) {		
			entity = this.fromCursor(cursor);		
		}
		cursor.close();
		db.close();
		return entity;
	}	
	
}
