package com.geniusgithub.lookaround.fragment;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.geniusgithub.lookaround.FragmentControlCenter;
import com.geniusgithub.lookaround.FragmentModel;
import com.geniusgithub.lookaround.MainLookAroundActivity;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class NavigationFragment extends Fragment implements OnCheckedChangeListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private View mView;
	private RadioGroup  m_radioGroup;
	
	private FragmentControlCenter mControlCenter;
	
	public NavigationFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		log.e("NavigationFragment onCreate");
		
		mControlCenter = FragmentControlCenter.getInstance(getActivity());
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		log.e("NavigationFragment onDestroy");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log.e("NavigationFragment onCreateView");
		
		mView = inflater.inflate(R.layout.navitation_channel_layout, null);
		return mView;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("NavigationFragment onActivityCreated");
		
		setupViews();
	}
	
	
	private void setupViews(){
		m_radioGroup = (RadioGroup) mView.findViewById(R.id.nav_radiogroup);
		((RadioButton) m_radioGroup.getChildAt(0)).toggle();
		
		m_radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int id) {
		switch(id){
		case R.id.rb_mirror:
			goMirrorFragment();
			break;
		case R.id.rb_constellation:
			goConstellationFragment();
			break;
		case R.id.rb_women:
			goWomenFragment();
			break;
		case R.id.rb_food:
			goFoodFragment();
			break;
		}
	}
	
	
	private void goMirrorFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getMirrorFragmentModel();
		if (getActivity() instanceof MainLookAroundActivity) {
			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goConstellationFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getConstellationFragmentModel();
		if (getActivity() instanceof MainLookAroundActivity) {
			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goWomenFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getWomenFragmentModel();
		if (getActivity() instanceof MainLookAroundActivity) {
			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goFoodFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getFoodFragmentModel();
		if (getActivity() instanceof MainLookAroundActivity) {
			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}


	
}
