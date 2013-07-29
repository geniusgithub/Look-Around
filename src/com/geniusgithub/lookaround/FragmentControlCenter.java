package com.geniusgithub.lookaround;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.geniusgithub.lookaround.fragment.ConstellationFragment;
import com.geniusgithub.lookaround.fragment.FoodFragment;
import com.geniusgithub.lookaround.fragment.MirrorFragment;
import com.geniusgithub.lookaround.fragment.WomenFragment;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class FragmentControlCenter {

	private static final CommonLog log = LogFactory.createLog();
	
	private static  FragmentControlCenter instance;
	private Context mContext;
	
	private Map<String, FragmentModel> mFragmentModelMaps = new HashMap<String, FragmentModel>();
	private FragmentBuilder mBuilder = new FragmentBuilder();

	private FragmentControlCenter(Context context) {
		mContext = context;
	}
	
	public static synchronized FragmentControlCenter getInstance(Context context) {
		if (instance == null){
			instance  = new FragmentControlCenter(context);
		}
		return instance;
	}

	
	public FragmentModel getMirrorFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(MIRROR_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = mBuilder.getMirrorFragmentModel();
			mFragmentModelMaps.put(MIRROR_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	public FragmentModel getConstellationFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(CONSTELLATION_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = mBuilder.getConstellationFragmentModel();
			mFragmentModelMaps.put(CONSTELLATION_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	public FragmentModel getWomenFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(WOMEN_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = mBuilder.getWomenFragmentModel();
			mFragmentModelMaps.put(WOMEN_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}

	public FragmentModel getFoodFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(FOOD_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = mBuilder.getFoodFragmentModel();
			mFragmentModelMaps.put(FOOD_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}


	
	

	
	
	
	public static final String MIRROR_FRAGMENT = "MIRROR_FRAGMENT";
	public static final String CONSTELLATION_FRAGMENT = "CONSTELLATION_FRAGMENT";
	public static final String WOMEN_FRAGMENT = "WOMEN_FRAGMENT";
	public static final String FOOD_FRAGMENT = "FOOD_FRAGMENT";
	
	public  class FragmentBuilder{

		public  FragmentModel  getMirrorFragmentModel(){		
			MirrorFragment fragment = new MirrorFragment();
			FragmentModel fragmentModel = new FragmentModel(getStringFromRes(R.string.mirror), fragment);
			return fragmentModel;
		}
		
		public  FragmentModel getConstellationFragmentModel(){
			ConstellationFragment fragment = new ConstellationFragment();
			FragmentModel fragmentModel = new FragmentModel(getStringFromRes(R.string.constellation), fragment);
			return fragmentModel;
		}
		
		public  FragmentModel getWomenFragmentModel(){
			WomenFragment fragment = new WomenFragment();
			FragmentModel fragmentModel = new FragmentModel(getStringFromRes(R.string.women), fragment);
			return fragmentModel;
		}
		
		public  FragmentModel getFoodFragmentModel(){
			FoodFragment fragment = new FoodFragment();
			FragmentModel fragmentModel = new FragmentModel(getStringFromRes(R.string.food), fragment);
			return fragmentModel;
		}
		

		
		public String getStringFromRes(int id){
			return mContext.getResources().getString(id);
		}
	}
}
