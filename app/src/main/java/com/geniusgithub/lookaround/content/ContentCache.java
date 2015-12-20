package com.geniusgithub.lookaround.content;


import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class ContentCache {

	private static final CommonLog log = LogFactory.createLog();
	
	private static ContentCache mInstance;

	private BaseType.ListItem mTypeItem = new BaseType.ListItem();
	private BaseType.InfoItemEx mInfoItem = new BaseType.InfoItemEx();
	
	public ContentCache(){
		
	}
	
	public synchronized static ContentCache getInstance(){
		if (mInstance == null){
			mInstance = new ContentCache();
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
