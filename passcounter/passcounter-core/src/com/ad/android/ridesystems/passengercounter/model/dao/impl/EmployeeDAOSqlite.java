package com.ad.android.ridesystems.passengercounter.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.android.ridesystems.passengercounter.model.dao.IEmployeeDAO;
import com.ad.android.ridesystems.passengercounter.model.entities.Employee;

/**
 * 
 * Full implementation of Employee Data Access Object
 * 
 *
 */
public class EmployeeDAOSqlite extends ADAOSqllite<Employee, Integer> implements IEmployeeDAO {

	public EmployeeDAOSqlite(SQLiteOpenHelper sqlitedb) {
		super(sqlitedb);
		this.defaultOrder = "UPPER(firstName) ASC";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues toContentValues(Employee entity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", entity.getId());
		contentValues.put("firstName", entity.getFirstName());
		contentValues.put("uniqueId", entity.getUniqueId());
		contentValues.put("personId", entity.getPersonId());
		contentValues.put("password", entity.getPassword());
		return contentValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Employee fromCursor(Cursor cursor) {
		Employee employee = new Employee();
		employee.setId(cursor.getInt(cursor.getColumnIndex("id")));
		employee.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
		employee.setUniqueId(cursor.getString(cursor.getColumnIndex("uniqueId")));
		employee.setPersonId(cursor.getInt(cursor.getColumnIndex("personId")));
		employee.setPassword(cursor.getString(cursor.getColumnIndex("password")));
		return employee;
	}

	@Override
	public Employee get(String login, String pass) {
		Employee employee = null;
		String query = "SELECT * FROM " + getTableName() + " WHERE firstName = ? AND password = ?";
		SQLiteDatabase db = getDB().getReadableDatabase();
		Cursor cursor = db.rawQuery(query, new String[] {login, pass});
		
		if (cursor != null && cursor.moveToFirst()) {						
			employee = fromCursor(cursor);
			cursor.close();
			
		}
		db.close();		
		return employee;
	}
	
}
