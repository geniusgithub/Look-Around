package com.geniusgithub.lookaround.fragment;

import com.geniusgithub.lookaround.model.BaseType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommonFragmentEx extends CommonFragment{

	private BaseType.ListItem mData;
	
	public CommonFragmentEx(BaseType.ListItem data){
		mData = data;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		setupViews();
	}
	
	private void setupViews(){
	
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
	
	
	public BaseType.ListItem getData(){
		return mData;
	}
}
