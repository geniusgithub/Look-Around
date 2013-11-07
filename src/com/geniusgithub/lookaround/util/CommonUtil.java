package com.geniusgithub.lookaround.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	private static final CommonLog log = LogFactory.createLog();
	
	public static String getMacAdress(Context context){
	    try {  
		       return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
		    } catch (IOException e) {  
		       e.printStackTrace();  
		       log.e("getMacAdress e = " + e.getMessage());
		       return getMac();
		    }  
	} 
	
	public static  String getMac() {
        String macSerial = "";
        String str = "";
        try {
                Process pp = Runtime.getRuntime().exec(
                                "cat /sys/class/net/wlan0/address");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                for (; null != str;) {
                        str = input.readLine();
                        if (str != null) {
                                macSerial = str.trim();// 去空格
                                break;
                        }
                }
        } catch (IOException ex) {
        	 log.e("getMac e = " + ex.getMessage());
        }
        return macSerial;
	}

	public static String loadFileAsString(String filePath) throws java.io.IOException{  
		StringBuffer fileData = new StringBuffer(1000);  	
		BufferedReader reader = new BufferedReader(new FileReader(filePath));  	
		char[] buf = new char[1024];  
		int numRead=0;  
		while((numRead=reader.read(buf)) != -1){  
			String readData = String.valueOf(buf, 0, numRead);  
			fileData.append(readData);  	
		 }  	
		 
		reader.close();  	
	     return fileData.toString();  
	 }  

	public static boolean isNetworkConnect(Context context) { 
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
		try { 
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			if (connectivity != null) { 
	            // 获取网络连接管理的对象 
	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
	            if (info != null&& info.isConnected()) { 
	                // 判断当前网络是否已经连接
	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
	                    return true; 
	                } 
	            } 
			}
         }catch (Exception e) {
	    	e.printStackTrace();
	    }
        return false; 
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
	
	public static String getSoftCode(Context context){
		
		 PackageManager manager = context.getPackageManager();
		 PackageInfo info;
		 String version = "010001";
		 try {
				info = manager.getPackageInfo(context.getPackageName(), 0);
				int value  = info.versionCode;
				version = String.valueOf(value);
		 } catch (NameNotFoundException e) {			
				e.printStackTrace();
				log.e("getSoftCode error...");
		}
		
		 return version;
		
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
	
	public static String getIMSI(Context context){
		 TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
		 String IMSI = telephonyManager.getSubscriberId();  
		 return IMSI;
	}
	
	public static String getProvidersName(Context context) {  

		 String ProvidersName = null;  
		 
		 TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
		 
		 // 返回唯一的用户ID;就是这张卡的编号神马的  
		 String IMSI = telephonyManager.getSubscriberId();  
		 
		 // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
		 if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
			 	ProvidersName = "中国移动";  
		 } else if (IMSI.startsWith("46001")) {  	
		     	ProvidersName = "中国联通";  
		 } else if (IMSI.startsWith("46003")) {  
			 	ProvidersName = "中国电信";  	
		 }  
	
		 return ProvidersName;  
      }  
	
	public static String getScreeenSize(Context context){
		int width = getScreenWidth(context);
		int height = getScreenHeight(context);
		String value = String.valueOf(width) + "*" + String.valueOf(height);
		return value;
	}
}
