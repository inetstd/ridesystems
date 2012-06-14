package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteTrackingCriteriaDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteTrackingCriteria;

/**
 * 
 * Full implementation of Vehicle Data Access Object.
 * 
 *
 */
public class RouteTrackingCriteriaDAOSqlite extends ADAOSqllite<RouteTrackingCriteria, Integer> implements IRouteTrackingCriteriaDAO {

	
	public RouteTrackingCriteriaDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(RouteTrackingCriteria entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("description", entity.getDescription());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("iorder", entity.getOrder());
		contentValues.put("trackingLevelId", entity.getTrackingLevelId());
		contentValues.put("routeTrackingCriteriaId", entity.getRouteTrackingCriteriaId());
		contentValues.put("includeInMainCount", entity.isIncludeInMainCount() ? 1 : 0);
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RouteTrackingCriteria fromCursor(Cursor cursor) {
		RouteTrackingCriteria criteria = new RouteTrackingCriteria();
		criteria.setId(cursor.getInt(cursor.getColumnIndex("id")));
		criteria.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		criteria.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		criteria.setOrder(cursor.getInt(cursor.getColumnIndex("iorder")));
		criteria.setTrackingLevelId(cursor.getInt(cursor.getColumnIndex("trackingLevelId")));		
		criteria.setRouteTrackingCriteriaId(cursor.getInt(cursor.getColumnIndex("routeTrackingCriteriaId")));
		criteria.setIncludeInMainCount(cursor.getInt(cursor.getColumnIndex("includeInMainCount")) == 1);
		return criteria;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RouteTrackingCriteria> getAllByTrackingLevelId(int id) {
		List<RouteTrackingCriteria> list = new ArrayList<RouteTrackingCriteria>();
		SQLiteDatabase db = getDB().getReadableDatabase();		
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL + " WHERE trackingLevelId = ? ORDER BY iorder ASC"), new String[] { id + ""});
		if (cursor.moveToFirst()) {
			do {
				RouteTrackingCriteria entity = this.fromCursor(cursor);
				list.add(entity);				
			} while (cursor.moveToNext());		
		}
		cursor.close();
		db.close();		
		return list;
	}	
	
}
