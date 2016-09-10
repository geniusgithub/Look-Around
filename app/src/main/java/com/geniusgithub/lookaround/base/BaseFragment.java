package com.geniusgithub.lookaround.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

public class BaseFragment extends Fragment {

    protected Activity mParentActivity;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentActivity = getActivity();
    }

    public Activity getmParentActivity(){
        return mParentActivity;
    }

}
