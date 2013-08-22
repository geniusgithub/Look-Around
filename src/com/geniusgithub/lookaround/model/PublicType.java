package com.geniusgithub.lookaround.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class PublicType {

	private static final CommonLog log = LogFactory.createLog();
	
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
	
	
	// 用户注册回应
	public static class UserRegisterResult implements IParseJson{

		public final static String KEY_KEYS = "keys";
		
		public String mKeys = "";

		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
				
			mKeys = jsonObject.getString(KEY_KEYS);
			return true;

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
	
	// 用户登录回应
	public static class UserLoginResult implements IParseJson{


		public final static String KEY_DATALIST = "DataList";
		public final static String KEY_ADMIN = "isAdmin";
		public final static String KEY_ADTYPE = "adType";
		
		
	
		public List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
		public int mIsAdmin = 0;
		public int mAdType = 0;
		
		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {

			mIsAdmin = jsonObject.getInt(KEY_ADMIN);
			mAdType = jsonObject.getInt(KEY_ADTYPE);
			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_DATALIST);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++){
				JSONObject tmp = jsonArray.getJSONObject(i);
				BaseType.ListItem item = new BaseType.ListItem();
				try {
					item.parseJson(tmp);
					mDataList.add(item);

				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
			return true;

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

		
		public String mArcTypeID = "";
		public String mPage = "";

		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);	
			mapValue.put(KEY_PAGE, mPage);	
			return mapValue;
		}
	}
	
	// 获取资讯回应
	public static class GetInfoResult  implements IParseJson{

		public final static String KEY_DATALIST = "DataList";
		
		public List<BaseType.InfoItem> mDataList = new ArrayList<BaseType.InfoItem>();

		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
			
			JSONArray jsonArray = jsonObject.optJSONArray(KEY_DATALIST);
			if (jsonArray == null){
				return true;
			}
			int size = jsonArray.length();
			for(int i = 0; i < size; i++){
				JSONObject tmp = jsonArray.getJSONObject(i);
				BaseType.InfoItem item = new BaseType.InfoItem();
				try {
					item.parseJson(tmp);
					mDataList.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
			return true;
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
