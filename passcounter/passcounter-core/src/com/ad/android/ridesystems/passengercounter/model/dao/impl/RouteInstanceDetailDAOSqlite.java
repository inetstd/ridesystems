package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IRouteInstanceDetailDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.RouteInstanceDetail;

/**
 * 
 * Full implementation of RouteInstanceDetail Data Access Object
 * 
 *
 */
public class RouteInstanceDetailDAOSqlite extends ADAOSqllite<RouteInstanceDetail, Integer> implements IRouteInstanceDetailDAO {

	public RouteInstanceDetailDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(RouteInstanceDetail entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("routeDate", entity.getRouteDate());
		contentValues.put("quantity", entity.getQuantity());
		contentValues.put("routeInstanceId", entity.getRouteInstanceId());
		contentValues.put("routeStopId", entity.getRouteStopId());
		contentValues.put("routeTrackingCriteriaId", entity.getRouteTrackingCriteriaId());
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RouteInstanceDetail fromCursor(Cursor cursor) {
		RouteInstanceDetail routeInstanceDetail = new RouteInstanceDetail();
		routeInstanceDetail.setId(cursor.getInt(cursor.getColumnIndex("id")));
		routeInstanceDetail.setRouteDate(cursor.getString(cursor.getColumnIndex("routeDate")));		
		routeInstanceDetail.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
		routeInstanceDetail.setRouteInstanceId(cursor.getInt(cursor.getColumnIndex("routeInstanceId")));
		routeInstanceDetail.setRouteStopId(cursor.getInt(cursor.getColumnIndex("routeStopId")));
		routeInstanceDetail.setRouteTrackingCriteriaId(cursor.getInt(cursor.getColumnIndex("routeTrackingCriteriaId")));		
		return routeInstanceDetail;
	}

	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<RouteInstanceDetail> getAllByRouteInstanceId(int routeId) {
		 List<RouteInstanceDetail> list = new ArrayList<RouteInstanceDetail>();
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL + " WHERE routeInstanceId = ?"), new String[] { routeId + ""});
		if (cursor.moveToFirst()) {
			do {
				RouteInstanceDetail entity = this.fromCursor(cursor);				
				list.add(entity);				
			} while (cursor.moveToNext());		
		}
		cursor.close();
		db.close();		
		return list;	
	}


}
