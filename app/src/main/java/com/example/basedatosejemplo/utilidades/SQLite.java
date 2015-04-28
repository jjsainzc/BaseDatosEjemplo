package com.example.basedatosejemplo.utilidades;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("NewApi")
public class SQLite extends SQLiteOpenHelper {

	private File pathDatabase;
	@SuppressWarnings("unused")
	private String database;
	@SuppressWarnings("unused")
	private Integer version;
	private Set<String> tablas;

	public SQLite(Context context, String database, int version, Set<String> tablas) {
		super(context, database, null, version);
		this.database = database;
		this.version = version;
		this.tablas = tablas;
		pathDatabase = context.getDatabasePath(database);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if ((tablas != null) && !tablas.isEmpty()) {
			for (Iterator<String> iterator = this.tablas.iterator(); iterator.hasNext();) {
				String tabla = iterator.next();
				db.execSQL(tabla);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public static ContentValues convertMapToValues(Map<String, ?> datos) {
		ContentValues valores = new ContentValues();
		String v;
		String k;

		for (Object d : datos.keySet()) {
			k = (String) d;

			if (datos.get(k) instanceof Date) {
				v = new java.sql.Date(((Date) datos.get(k)).getTime())
						.toString();
			} else if (datos.get(k) instanceof Boolean) {
				v = ((Boolean) datos.get(k)) ? "1" : "0";
			} else {
				v = datos.get(k).toString();
			}
			valores.put(k, v);
		}
		// Log.i("DATOS", valores.toString());
		return valores;
	}

	public int count(String tabla) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount= db.rawQuery("select count(*) from "+tabla, null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		return count;
	}
	
	public void insert(String tabla, Map<String, ?> datos) {
		SQLiteDatabase db = getWritableDatabase();
		if ((db != null) && (datos != null) && (datos.size() > 0)) {
			db.insert(tabla, null, convertMapToValues(datos));
		}
		if (db != null)
			db.close();
	}

	public void update(String tabla, String pkName, String pkValue,
			Map<String, ?> datos) {
		SQLiteDatabase db = getWritableDatabase();
		if ((db != null) && (datos != null) && (datos.size() > 0)
				&& (pkName != null) && !pkName.isEmpty() && (pkValue != null)
				&& !pkValue.isEmpty()) {

			db.update(tabla, convertMapToValues(datos), pkName + "=\""
					+ pkValue + "\"", null);
		}
		if (db != null)
			db.close();
	}

	public void delete(String tabla, String pkName, String pkValue) {
		SQLiteDatabase db = getWritableDatabase();

		if (db != null) {
			if ((pkName != null) && !pkName.isEmpty() && (pkValue != null)
					&& !pkValue.isEmpty()) {

				db.delete(tabla, pkName + "=\"" + pkValue + "\"", null);
			} else {
				db.delete(tabla, null, null);
			}
		}

		if (db != null)
			db.close();
	}

	public static List<Map<String, ?>> cursorToLista (Cursor c) {
		List<Map<String, ?>> resultado = new ArrayList<Map<String, ?>>();
		
		Map<String, String> registro;
		
		if (c != null) {
			try {
				c.moveToFirst();

				do {
					registro = new LinkedHashMap<String, String>();
					int i = 0;
					for (String colName : c.getColumnNames()) {
						registro.put(colName, c.getString(i++));
					}
					resultado.add(registro);

				} while (c.moveToNext());
			} catch (CursorIndexOutOfBoundsException e) {
				resultado = null;
			}
			
		}
		
		return resultado;
	}
	
	public  List<Map<String, ?>> select(String tabla, Set<String> columnas, String pkName, String pkValue) {
		List<Map<String, ?>> resultado = new ArrayList<Map<String, ?>>();
		SQLiteDatabase db = getReadableDatabase();
		String[] cols;
		String where = null;
		

		if ((columnas != null) && !columnas.isEmpty()) {
			cols = (String[]) columnas.toArray(new String[columnas.size()]);
		} else {
			cols = null;
		}

		if ((pkName != null) && !pkName.isEmpty() && (pkValue != null)	&& !pkValue.isEmpty()) {

			where = pkName + "=\"" + pkValue + "\"";
		}

		if (db != null) {
			Cursor c = db.query(tabla, cols, where, null, null, null, null);
			resultado = cursorToLista(c);
			c.close();
			db.close();
			
		}

		return resultado;
	}

	public String getPathDatabase() {
		return pathDatabase.getAbsolutePath();
	}

}
