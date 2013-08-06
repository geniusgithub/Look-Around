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
	
	public static  PublicType.UserLogin buildUserLogin(Context context){
		PublicType.UserLogin object = new PublicType.UserLogin();
		object.mKeys = "0";
		object.mConn = "Wifi";
		object.mStd = "A1.0";		
		object.mTimeStamp = String.valueOf(System.currentTimeMillis());
		object.mSign = SecuteUtil.getSignString(object.mKeys, PublicTypeBuilder.TOKEN_KEY, object.mTimeStamp);
		return object;
	}



}
