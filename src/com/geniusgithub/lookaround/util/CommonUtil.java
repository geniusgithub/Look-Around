package com.geniusgithub.lookaround.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class CommonUtil {

	public static String getMacAdress(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo(); 
		return info.getMacAddress(); 
	}
	
	public static void showToast(int stringID, Context context){
		Toast.makeText(context, context.getString(stringID), Toast.LENGTH_SHORT).show();
	}
}
