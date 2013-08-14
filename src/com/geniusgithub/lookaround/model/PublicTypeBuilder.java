package com.geniusgithub.lookaround.model;

import android.content.Context;

import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.SecuteUtil;

public class PublicTypeBuilder {

	private static final String TOKEN_KEY = "6B3767B7-CDF3-1016-A586-DC073C5C0F62";
	
	public static  PublicType.UserRegister buildUserRegister(Context context){
		PublicType.UserRegister object = new PublicType.UserRegister();
		object.mKeys = "0";
		object.mStd = "A1.0";		
		object.mMac = CommonUtil.getMacAdress(context).replace(":", "");
		object.mBrand = "Android";
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}
	
	public static  PublicType.UserLogin buildUserLogin(Context context, String keys){
		PublicType.UserLogin object = new PublicType.UserLogin();
		object.mKeys = keys;
		object.mConn = "Wifi";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}

	public static  PublicType.BindToken buildBindToken(Context context){
		PublicType.BindToken object = new PublicType.BindToken();
		object.mKeys = "0";
		object.mToken = "";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}

	public static  PublicType.AdClick buildAdClick(Context context){
		PublicType.AdClick object = new PublicType.AdClick();
		object.mKeys = "0c7742fef985a8245a62b41b7e0debfa";
		object.mAdType = "1";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}
	
	public static  PublicType.AboutPage buildAboutPage(Context context){
		PublicType.AboutPage object = new PublicType.AboutPage();
		object.mKeys = "0";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}
	
	
	public static  PublicType.GetInfo buildGetInfo(Context context){
		PublicType.GetInfo object = new PublicType.GetInfo();
		object.mKeys = "0c7742fef985a8245a62b41b7e0debfa";
		object.mArcTypeID = "1";
		object.mPage = "0";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}
	
	public static  PublicType.DeleteInfo buildDeleteInfo(Context context){
		PublicType.DeleteInfo object = new PublicType.DeleteInfo();
		object.mKeys = "0";
		object.mArcTypeID = "0";
		object.mTopicID = "0";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}
}
