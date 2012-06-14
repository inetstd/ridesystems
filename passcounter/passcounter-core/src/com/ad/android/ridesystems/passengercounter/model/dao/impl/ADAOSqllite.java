package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ad.android.ridesystems.passengercounter.model.dao.IDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.AEntity;

/**
 * 
 * Implement DAO interface for android sqlite storage. 
 * 
 *
 * @param <E> Entity class
 * @param <ID> Id class
 */
public abstract class ADAOSqllite<E extends AEntity, ID extends Serializable> implements IDAO<E, ID> {
	
	protected String defaultOrder = null;

	public static final String C_TABLE_NAME_TPL = "{table_name}";

	public static final String C_ID_VALUE_TPL = "{id}";

	protected String SQL_SELECT_ALL = "SELECT * FROM " + C_TABLE_NAME_TPL;

	protected String SQL_SELECT_BY_ID = "SELECT * FROM " + C_TABLE_NAME_TPL + " WHERE id = " + C_ID_VALUE_TPL;

	protected String SQL_DELETE_ALL = "DELETE FROM " + C_TABLE_NAME_TPL;

	protected String SQL_DELETE_BY_ID = "DELETE FROM " + C_TABLE_NAME_TPL + " WHERE id = " + C_ID_VALUE_TPL;

	SQLiteOpenHelper db = null;

	public ADAOSqllite(SQLiteOpenHelper sqlitedb) {
		this.db = sqlitedb;
	}

	/**
	 * Get Database instance
	 */
	protected SQLiteOpenHelper getDB() {
		return this.db;
	}

	/**
	 * {@inheritDoc}
	 */
	public E get(ID id) {
		E entity = null;
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_BY_ID, id), null);
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
	public E getFull(ID id) {		
		return get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<E> getAll() {
		List<E> list = new ArrayList<E>();
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.query(getTableName(), null, null, null, null, null, defaultOrder, null);
		//Cursor cursor = db.rawQuery(processQuery(SQL_SELECT_ALL ), new String[] {defaultOrder});
		if (cursor.moveToFirst()) {
			do {
				E entity = this.fromCursor(cursor);
				list.add(entity);				
			} while (cursor.moveToNext());		
		}
		cursor.close();
		db.close();	
	
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(ID id) {
		SQLiteDatabase db = getDB().getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("PRAGMA foreign_keys=ON;");
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
	public void deleteAll() {
		SQLiteDatabase db =  getDB().getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("PRAGMA foreign_keys=ON;");
			db.execSQL(processQuery(SQL_DELETE_ALL));		
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}	
		
	}

	/**
	 * {@inheritDoc}
	 */
	public E insert(E entity) {
		SQLiteDatabase db =  getDB().getWritableDatabase();
		ContentValues cv = this.toContentValues(entity);
		cv.remove("id");
		db.beginTransaction();
		try {
			Long id = (Long) db.insert(getTableName(), null, cv);
			db.setTransactionSuccessful();
			entity.setId(Integer.parseInt(id.toString()));
		} finally {
			db.endTransaction();
			db.close();
		}		
		
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(E entity) {
		SQLiteDatabase db =  getDB().getWritableDatabase();
		ContentValues cv = this.toContentValues(entity);
		cv.remove("id");		
		db.beginTransaction();
		try {			
			db.update(getTableName(), cv, "id = ?", new String[] {entity.getId().toString()});
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}		
	}

	/**
	 * Creates an object from cursor
	 * @param cursor - mysql cursor
	 * @return instance
	 */
	abstract protected E fromCursor(Cursor cursor);

	/**
	 * fill ContentValues from entity for update query 
	 * @param entity - entity
	 * @return ContentValues 
	 */
	abstract protected  ContentValues toContentValues(E entity);


	/**
	 * Gets table name. In fact - class name in lower case; 
	 * 'com.foo.Bar' class it will be 'bar' 
	 * @return table name
	 */
	protected String getTableName() {		
		return getEntityClass().getSimpleName().toLowerCase();
	}

	/**
	 * Prepare query - sets table name
	 * @param sql - query template
	 * @return query to execute
	 */
	protected String processQuery(String sql) {
		sql = sql.replace(C_TABLE_NAME_TPL, getTableName());		
		return sql;
	}

	/**
	 * Prepare query that has id statament; 
	 * @param sql - query template
	 * @param id id
	 * @return
	 */
	protected String processQuery(String sql, ID id) {			
		return processQuery(sql).replace(C_ID_VALUE_TPL, id.toString());
	}

	/**
	 * get class of generic params
	 * @return class 
	 */
	@SuppressWarnings("rawtypes")
	public Class getEntityClass() {	
		Class result = null;
		Type type = this.getClass().getGenericSuperclass();

		if(type instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType) type;
			Type[] fieldArgTypes = pt.getActualTypeArguments();
			result = (Class) fieldArgTypes[0];			
		}
		return result;
	}


}
