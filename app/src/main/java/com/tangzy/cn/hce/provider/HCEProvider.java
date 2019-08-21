package com.tangzy.cn.hce.provider;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class HCEProvider extends ContentProvider{
	
	 // 若不匹配采用UriMatcher.NO_MATCH(-1)返回  
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);  
  
    // 匹配码  
    private static final int CODE_NOPARAM = 1;  
    private static final int CODE_PARAM = 2;

	private SharedPreferences sharedPreferences;  
  
    static  
    {  
        // 对等待匹配的URI进行匹配操作，必须符合cn.xyCompany.providers.personProvider/person格式  
        // 匹配返回CODE_NOPARAM，不匹配返回-1  
        MATCHER.addURI("com.citic.providers.hceProvider", "card", CODE_NOPARAM);  
  
        // #表示数字 cn.xyCompany.providers.personProvider/person/10  
        // 匹配返回CODE_PARAM，不匹配返回-1  
        MATCHER.addURI("com.citic.providers.hceProvider", "card/#", CODE_PARAM);  
    }  
  
//	com.citic.providers.hceProvider

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		sharedPreferences = getContext().getSharedPreferences("citic_hce", Activity.MODE_PRIVATE);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("isOpen", (Boolean) values.get("isOpen"));
		editor.putString("provideUri",  (String) values.get("provideUri"));
		editor.commit();
		return 0;
	}

}
