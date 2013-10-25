package com.geniusgithub.lookaround.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.geniusgithub.lookaround.FragmentControlCenter;
import com.geniusgithub.lookaround.FragmentModel;
import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.MainLookAroundActivity;
import com.geniusgithub.lookaround.adapter.NavChannelAdapter;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.ListItem;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.umeng.analytics.MobclickAgent;

public class NavigationFragment extends Fragment implements OnItemClickListener{

private static final CommonLog log = LogFactory.createLog();
	
    private Context mContext;
	private View mView;
	private ListView mListView;
	
	private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
	private NavChannelAdapter mAdapter;
	
	private FragmentControlCenter mControlCenter;
	private boolean loginStatus = false;
	
	public NavigationFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		log.e("NavigationFragment onCreate");
		mContext = LAroundApplication.getInstance();
		mControlCenter = FragmentControlCenter.getInstance(getActivity());
	    loginStatus = LAroundApplication.getInstance().getLoginStatus();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		
		log.e("NavigationFragment onDestroy");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log.e("NavigationFragment onCreateView");
		
		mView = inflater.inflate(R.layout.listview_layout, null);
		return mView;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("NavigationFragment onActivityCreated");
		
		if (loginStatus){
			setupViews();
			initData();
		}
	
	}
	
	
	private void setupViews(){
		mListView = (ListView) mView.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
	}
	
	
	private void initData(){
		mDataList = LAroundApplication.getInstance().getUserLoginResult().mDataList;
		
		mAdapter = new NavChannelAdapter(mContext, mDataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
			
		
		BaseType.ListItem item = (ListItem) adapter.getItemAtPosition(pos);
	//	log.e("pos = " + pos + ", item = " + "\n" + item.getShowString());
		
	
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("typeId", item.mTypeID);
		map.put("typeTitle", item.mTitle);
		MobclickAgent.onEvent(mContext, "UMID0020", map);
		
		CommonFragmentEx fragmentEx = mControlCenter.getCommonFragmentEx(item);
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainLookAroundActivity) {
			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
			ra.switchContent(fragmentEx);
		}
	}



}
