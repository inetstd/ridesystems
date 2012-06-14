package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteStopDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteStop;

/**
 * 
 * Full implementation of RouteStop Data Access Object
 * 
 *
 */
public class RouteStopDAOSqlite extends ADAOSqllite<RouteStop, Integer> implements IRouteStopDAO {

	public RouteStopDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(RouteStop entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("description", entity.getDescription());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("routeId", entity.getRouteId());
		contentValues.put("routeStopId", entity.getRouteStopId());
		contentValues.put("iorder", entity.getOrder());
		contentValues.put("schedules", entity.getSchedules());
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RouteStop fromCursor(Cursor cursor) {
		RouteStop routeStop = new RouteStop();
		routeStop.setId(cursor.getInt(cursor.getColumnIndex("id")));
		routeStop.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		routeStop.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		routeStop.setRouteId(cursor.getInt(cursor.getColumnIndex("routeId")));
		routeStop.setRouteStopId(cursor.getInt(cursor.getColumnIndex("routeStopId")));
		routeStop.setOrder(cursor.getInt(cursor.getColumnIndex("iorder")));
		routeStop.setSchedules(cursor.getString(cursor.getColumnIndex("schedules")));
		return routeStop;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RouteStop> getAllByRouteId(int routeId) {
		List<RouteStop> list = new ArrayList<RouteStop>();
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL + " WHERE routeId = ? ORDER BY iorder ASC"), new String[] { routeId + ""});
		if (cursor.moveToFirst()) {
			do {
				RouteStop entity = this.fromCursor(cursor);				
				list.add(entity);				
			} while (cursor.moveToNext());		
		}
		cursor.close();
		db.close();		
		return list;		
	}

}
