package com.geniusgithub.lookaround.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicType {

	// 用户注册
	public final static int USER_REGISTER_MASID = 0x0001;
	public static class UserRegister implements IToStringMap
	{
		
		public final static String KEY_KEYS = "keys";
		public final static String KEY_MAC = "mac";
		public final static String KEY_IMSI = "imsi";
		public final static String KEY_MFG = "mfg";
		public final static String KEY_BRAND = "brand";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "ver";
		public final static String KEY_SCREENSIZE = "screensize";
		public final static String KEY_TIMESTAMP = "timestamp";
		public final static String KEY_SIGN = "sign";
		public final static String KEY_STD = "std";
	
		public String mKeys = "";
		public String mMac = "";
		public String mImsi = "";
		public String mMfg = "";
		public String mBrand = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";
		public String mScreensize = "";
		public String mTimeStamp = "";
		public String mSign = "";
		public String mStd = "";

		@Override
		public Map<String, String> toStringMap() {
			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue.put(KEY_KEYS, mKeys);
			mapValue.put(KEY_MAC, mMac);
			mapValue.put(KEY_IMSI, mImsi);
			mapValue.put(KEY_MFG, mMfg);
			mapValue.put(KEY_BRAND, mBrand);
			mapValue.put(KEY_MODEL, mModel);
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			mapValue.put(KEY_SCREENSIZE, mScreensize);
			mapValue.put(KEY_TIMESTAMP, mTimeStamp);
			mapValue.put(KEY_SIGN, mSign);
			mapValue.put(KEY_STD, mStd);
			return mapValue;
		}
	}
	
	
	// 用户登录
	public final static int USER_LOGIN_MASID = 0x0003;
	public static class UserLogin implements IToStringMap
	{
		
		public final static String KEY_KEYS = "keys";
		public final static String KEY_CONN = "conn";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "ver";
		public final static String KEY_TIMESTAMP = "timestamp";
		public final static String KEY_SIGN = "sign";
		public final static String KEY_STD = "std";
	
		public String mKeys = "";
		public String mConn = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";
		public String mTimeStamp = "";
		public String mSign = "";
		public String mStd = "";

		@Override
		public Map<String, String> toStringMap() {
			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue.put(KEY_KEYS, mKeys);
			mapValue.put(KEY_CONN, mConn);
			mapValue.put(KEY_MODEL, mModel);	
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			mapValue.put(KEY_TIMESTAMP, mTimeStamp);
			mapValue.put(KEY_SIGN, mSign);
			mapValue.put(KEY_STD, mStd);
			return mapValue;
		}
	}
	
}
