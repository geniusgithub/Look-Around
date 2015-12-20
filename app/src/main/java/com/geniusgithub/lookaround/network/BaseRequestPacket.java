package com.geniusgithub.lookaround.network;

import android.content.Context;

import com.geniusgithub.lookaround.model.IToStringMap;

public  class BaseRequestPacket {
	public Context context;
	public int action;
	public IToStringMap object;
	public Object extra;
	
	public BaseRequestPacket(){
		
	}
}
