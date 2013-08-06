package com.geniusgithub.lookaround.model;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class ServerUrlBuilder {

	private static final CommonLog log = LogFactory.createLog();
	
	public static final String baseURL = "http://www.runapp.cn/Topics/public/App/";
//	public static final String baseURL = "http://192.168.1.108/Topics/public/App/";
	
	public static final String registerURL = "Register";
	public static final String loginURL = "Login";
	
	public static String getServerURL(int action){
		switch(action){
		case PublicType.USER_REGISTER_MASID:
			return getRegisterURL();
		case PublicType.USER_LOGIN_MASID:
			return getLoginURL();
		}
		
		return "";
	}
	
	
	public static String getRegisterURL(){
		return baseURL + registerURL;
	}
	
	public static String getLoginURL(){
		return baseURL + loginURL;
	}
	
}
