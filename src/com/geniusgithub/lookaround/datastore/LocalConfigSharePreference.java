package com.geniusgithub.lookaround.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalConfigSharePreference {

	public static final String preference_name = "LocalConfigSharePreference";
	public static final String KEYS = "keys";
	
	public static boolean commintKeys(Context context, String key){
	
		SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEYS, key);
		editor.commit();
		return true;
	}
	
	public static String getKeys(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
		String value = sharedPreferences.getString(KEYS, "");
		return value;
	}
	
	public static void clearData(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
}
