package com.geniusgithub.lookaround.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
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
	
	public static void showToast(String  text, Context context){
		Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
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
	
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	public static String toHexString(int num)
	{
		String string = "0x" + Integer.toHexString(num);
		return string;
	}
	
	public static String getSoftVersion(Context context){
	
		 PackageManager manager = context.getPackageManager();
		 PackageInfo info;
		 String version = "00.00.01";
		 try {
				info = manager.getPackageInfo(context.getPackageName(), 0);
				version  = info.versionName;
		 } catch (NameNotFoundException e) {			
				e.printStackTrace();
		}
		
		 return version;
		
	}

	public static String getOSVersion()
	{
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static String getDeviceManufacturer()
	{
		return android.os.Build.MANUFACTURER;
	}
	
	public static String getDeviceModel()
	{
		return android.os.Build.MODEL;
	}
}
