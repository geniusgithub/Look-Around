package com.geniusgithub.lookaround.detailcontent;


import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class DetailCache {

	private static final CommonLog log = LogFactory.createLog();
	
	private static DetailCache mInstance;

	private BaseType.ListItem mTypeItem = new BaseType.ListItem();
	private BaseType.InfoItemEx mInfoItem = new BaseType.InfoItemEx();
	
	public DetailCache(){
		
	}
	
	public synchronized static DetailCache getInstance(){
		if (mInstance == null){
			mInstance = new DetailCache();
		}
		
		return mInstance;
	}
	
	public void setTypeItem(BaseType.ListItem item){
		mTypeItem = item;
	}
	
	public BaseType.ListItem getTypeItem(){
		return mTypeItem;
	}

	public void setInfoItem(BaseType.InfoItemEx item){
		mInfoItem = item;
	}
	
	public BaseType.InfoItemEx getInfoItem(){
		return mInfoItem;
	}
	
}
