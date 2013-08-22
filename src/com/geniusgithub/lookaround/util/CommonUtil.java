package com.geniusgithub.lookaround.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
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
	
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}
	
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
}
