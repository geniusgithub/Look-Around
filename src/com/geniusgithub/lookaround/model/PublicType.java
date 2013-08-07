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
	public static class UserRegister extends AbstractBaseProtocol
	{	

		public final static String KEY_MAC = "mac";
		public final static String KEY_IMSI = "imsi";
		public final static String KEY_MFG = "mfg";
		public final static String KEY_BRAND = "brand";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "ver";
		public final static String KEY_SCREENSIZE = "screensize";

	
		public String mMac = "";
		public String mImsi = "";
		public String mMfg = "";
		public String mBrand = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";
		public String mScreensize = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_MAC, mMac);
			mapValue.put(KEY_IMSI, mImsi);
			mapValue.put(KEY_MFG, mMfg);
			mapValue.put(KEY_BRAND, mBrand);
			mapValue.put(KEY_MODEL, mModel);
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			mapValue.put(KEY_SCREENSIZE, mScreensize);
			return mapValue;
		}
	}
	
	
	// 用户登录
	public final static int USER_LOGIN_MASID = 0x0003;
	public static class UserLogin extends AbstractBaseProtocol
	{
		

		public final static String KEY_CONN = "conn";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "ver";
	

		public String mConn = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_CONN, mConn);
			mapValue.put(KEY_MODEL, mModel);	
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			return mapValue;
		}
	}
	
	// 绑定TOKEN
	public final static int BIND_TOKEN_MSGID  = 0x0005;
	public static class BindToken extends AbstractBaseProtocol
	{
		
		public final static String KEY_TOKEN = "token";

		public String mToken = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_TOKEN, mToken);		
			return mapValue;
		}
	}
	
	// 点击广告
	public final static int AD_CLICK_MSGID  = 0x0007;
	public static class AdClick extends AbstractBaseProtocol
	{

		public final static String KEY_ADTYPE = "adType";	

		public String mAdType = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ADTYPE, mAdType);		
			return mapValue;
		}
	}
	
	// About页面
	public final static int ABOUT_MSGID  = 0x0009;
	public static class AboutPage extends AbstractBaseProtocol
	{
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			return mapValue;
		}
	}
	
	// 获取资讯类型列表
	public final static int GET_TYPELIST_MSGID  = 0x0011;
	public static class GetTypeList extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	

		public String mArcTypeID = "";
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);		
			return mapValue;
		}
	}
	
	// 获取资讯
	public final static int GET_INFO_MSGID  = 0x0013;
	public static class GetInfo extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	
		public final static String KEY_PAGE = "page";	
		public final static String KEY_COUNT = "count";	
		
		public String mArcTypeID = "";
		public String mPage = "";
		public String mCount = "";
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);	
			mapValue.put(KEY_PAGE, mPage);	
			mapValue.put(KEY_COUNT, mCount);	
			return mapValue;
		}
	}
	
	// 删除资讯
	public final static int DELETE_INFO_MSGID  = 0x0015;
	public static class DeleteInfo extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	
		public final static String KEY_TOPICID = "topic_id";		
		
		public String mArcTypeID = "";
		public String mTopicID = "";
	
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);	
			mapValue.put(KEY_TOPICID, mTopicID);		
			return mapValue;
		}
	}
}
