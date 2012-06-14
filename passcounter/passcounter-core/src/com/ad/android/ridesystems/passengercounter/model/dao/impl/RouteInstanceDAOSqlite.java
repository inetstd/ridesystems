package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstance;

/**
 * 
 * Full implementation of RouteInstance Data Access Object
 * 
 *
 */
public class RouteInstanceDAOSqlite extends ADAOSqllite<RouteInstance, Integer> implements IRouteInstanceDAO {

	public RouteInstanceDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(RouteInstance entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("routeDate", entity.getRouteDate());
		contentValues.put("employeeId", entity.getEmployeeId());
		contentValues.put("routeId", entity.getRouteId());
		contentValues.put("vehicleId", entity.getVehicleId());
		contentValues.put("finished", entity.isFinished() ? 1: 0);
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RouteInstance fromCursor(Cursor cursor) {
		RouteInstance routeInstance = new RouteInstance();
		routeInstance.setId(cursor.getInt(cursor.getColumnIndex("id")));
		routeInstance.setEmployeeId(cursor.getInt(cursor.getColumnIndex("employeeId")));		
		routeInstance.setRouteId(cursor.getInt(cursor.getColumnIndex("routeId")));
		routeInstance.setVehicleId(cursor.getInt(cursor.getColumnIndex("vehicleId")));
		routeInstance.setFinished(cursor.getInt(cursor.getColumnIndex("finished")) == 1);
		routeInstance.setRouteDate(cursor.getString(cursor.getColumnIndex("routeDate")));
		return routeInstance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RouteInstance getNotFinishedByEmployee(int employeeId) {
		RouteInstance entity = null;
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL) + " WHERE finished = 0 AND employeeId = " + employeeId + " LIMIT 0, 1", null);
		if (cursor.moveToFirst()) { 		
			entity = this.fromCursor(cursor);		
		}
		cursor.close();
		db.close();
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Integer id) {
		SQLiteDatabase db = getDB().getWritableDatabase();
		db.beginTransaction();
		try {			
			db.execSQL("DELETE FROM routeinstancedetail WHERE routeInstanceId = ?", new String[] {id + ""});
			db.execSQL(processQuery(SQL_DELETE_BY_ID, id));			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();			
			db.close();
		}	
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RouteInstance getNotFinished() {
		RouteInstance entity = null;
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL) + " WHERE finished = 0 LIMIT 0, 1", null);
		if (cursor.moveToFirst()) { 		
			entity = this.fromCursor(cursor);		
		}
		cursor.close();
		db.close();
		return entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RouteInstance> getAllFinished() {
		List<RouteInstance> list = new ArrayList<RouteInstance>();		
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL) + " WHERE finished = 1", null);
		if (cursor.moveToFirst()) {
			do {
				RouteInstance entity = this.fromCursor(cursor);
				list.add(entity);				
			} while (cursor.moveToNext());		
		}
		cursor.close();
		db.close();		
		return list;
	}
}
