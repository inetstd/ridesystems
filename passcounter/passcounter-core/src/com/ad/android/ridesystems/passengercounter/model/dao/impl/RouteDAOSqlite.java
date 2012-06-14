package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Route;

/**
 * 
 * Full implementation of Route Data Access Object
 * 
 *
 */
public class RouteDAOSqlite extends ADAOSqllite<Route, Integer> implements IRouteDAO {
	
	
	public RouteDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);				
	}
	
	public Route getFull(Integer id) {
		return this.get(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(Route entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("description", entity.getDescription());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("mapLineColor", entity.getMapLineColor());
		contentValues.put("routeId", entity.getRouteId());
		
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Route fromCursor(Cursor cursor) {
		Route route = new Route();
		route.setId(cursor.getInt(cursor.getColumnIndex("id")));
		route.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		route.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		route.setMapLineColor(cursor.getString(cursor.getColumnIndex("mapLineColor")));
		route.setRouteId(cursor.getInt(cursor.getColumnIndex("routeId")));
		return route;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Route getByRouteId(int routeId) {
		Route entity = null;
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL) + " WHERE routeId = ?" , new String[] {routeId + ""});
		if (cursor.moveToFirst()) {		
			entity = this.fromCursor(cursor);		
		}
		cursor.close();
		db.close();
		return entity;
	}	

}
