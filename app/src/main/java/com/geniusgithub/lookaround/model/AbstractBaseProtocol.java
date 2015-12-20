package com.geniusgithub.lookaround.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseProtocol implements IToStringMap{

	public Map<String, String> mapValue = new HashMap<String, String>();
	
	public final static String KEY_KEYS = "keys";
	public final static String KEY_TIMESTAMP = "timestamp";
	public final static String KEY_SIGN = "sign";
	public final static String KEY_STD = "std";

	public String mKeys = "";
	public String mTimeStamp = "";
	public String mSign = "";
	public String mStd = "";
	
	@Override
	public Map<String, String> toStringMap() {	
		mapValue.put(KEY_KEYS, mKeys);
		mapValue.put(KEY_TIMESTAMP, mTimeStamp);
		mapValue.put(KEY_SIGN, mSign);
		mapValue.put(KEY_STD, mStd);
		return mapValue;
	}
}
