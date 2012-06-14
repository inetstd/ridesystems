package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.ITrackingLevelDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.TrackingLevel;

/**
 * 
 * Full implementation of Vehicle Data Access Object.
 * 
 *
 */
public class TrackingLevelDAOSqlite extends ADAOSqllite<TrackingLevel, Integer> implements ITrackingLevelDAO {

	
	public TrackingLevelDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(TrackingLevel entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("description", entity.getDescription());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("level", entity.getLevel());
		contentValues.put("trackingLevelId", entity.getTrackingLevelId());		
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TrackingLevel fromCursor(Cursor cursor) {
		TrackingLevel level = new TrackingLevel();
		level.setId(cursor.getInt(cursor.getColumnIndex("id")));
		level.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		level.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		level.setTrackingLevelId(cursor.getInt(cursor.getColumnIndex("trackingLevelId")));		
		level.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
		
		return level;
	}	
	
}
