package com.tangzy.cn.hce.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseDao {
	
	private Context context;
	private DatabaseHelper userDB;
	public static SQLiteDatabase db;
	
	public DatabaseDao(Context context) {
		this.context = context;
		userDB = new DatabaseHelper(context);
		
	}
	
	public void add(JSONObject jsonObject) throws Exception{
		SQLiteDatabase db = userDB.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL(
					"insert into log (timeMillis,codeNum,atc) values (?,?,?)",
//					object);
//			new Object[] { "1440570184333", "0C4C6FEC", "0004","1" });
			new Object[] { jsonObject.getString("timeMillis"), jsonObject.getString("codeNum"), jsonObject.getString("atc") });
			db.close();
		}
	}
	
	public List<JSONObject> findAll(){
		SQLiteDatabase db = userDB.getWritableDatabase();
		if (db.isOpen()) {
//			Cursor cursor = db.rawQuery("select timeMillis, codeNum, atc from log where type = ?",
//					new String[] { "1" });
			String [] columns = {"timeMillis", "codeNum", "atc"};
			Cursor cursor = db.query("log", columns, null, null, null, null, null);
			List<JSONObject> list = new ArrayList<JSONObject>();
			JSONObject jsonObject;
			while (cursor.moveToNext()) {
				jsonObject= new JSONObject();
				try {
					jsonObject.put("timeMillis", cursor.getString(0));
					jsonObject.put("codeNum", cursor.getString(1));
					jsonObject.put("atc", cursor.getString(2));
					list.add(jsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			cursor.close();

			db.close();
			return list;
		}
		return null;
	}
	

	public void qure(){
		SQLiteDatabase db = userDB.getWritableDatabase();
		if (db.isOpen()) {
			String [] columns = {"timeMillis", "codeNum", "atc"};
			Cursor cursor = db.query("log", columns, null, null, null, null, null);
			
			System.out.println();
			while (cursor.moveToNext()) {
				
				System.out.println();
			}
			
		}
	}
	
}
