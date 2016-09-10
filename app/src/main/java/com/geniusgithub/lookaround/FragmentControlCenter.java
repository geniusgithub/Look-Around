package com.geniusgithub.lookaround;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.geniusgithub.lookaround.maincontent.ContentFragment;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class FragmentControlCenter {

	private static final CommonLog log = LogFactory.createLog();
	
	private static  FragmentControlCenter instance;
	private Context mContext;
	
	private Map<String,ContentFragment> mFragmentModelMaps= new HashMap<String, ContentFragment>();
	

	private FragmentControlCenter(Context context) {
		mContext = context;
	}
	
	public static synchronized FragmentControlCenter getInstance(Context context) {
		if (instance == null){
			instance  = new FragmentControlCenter(context);
		}
		return instance;
	}

	public ContentFragment getCommonFragmentEx(BaseType.ListItem object){
		ContentFragment fragment = mFragmentModelMaps.get(object.mTypeID);
		if (fragment == null){
			fragment = new ContentFragment(object);
			mFragmentModelMaps.put(object.mTypeID, fragment);
		}
		
		return fragment;
	}

	

}
