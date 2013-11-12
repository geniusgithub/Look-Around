package com.geniusgithub.lookaround.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.content.Context;
import android.provider.ContactsContract.Data;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;


public class RefreshListView extends ListView implements OnScrollListener, OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	public interface IListViewState
	{
		int LVS_NORMAL = 0;					//  普通状态
		int LVS_PULL_REFRESH = 1;			//  下拉刷新状态
		int LVS_RELEASE_REFRESH = 2;		//  松开刷新状态
		int LVS_LOADING = 3;				//  加载状态
	}
	
	
	public interface IOnRefreshListener
	{
		void OnRefresh();
	}
	
	
	private LinearLayout mHeadView;				
	private TextView mRefreshTextview;
	private TextView mLastUpdateTextView;
	private ImageView mArrowImageView;
	private ProgressBar mHeadProgressBar;
	
	private int mHeadContentWidth;
	private int mHeadContentHeight;
	
	
	private IOnRefreshListener mOnRefreshListener;			// 头部刷新监听器
	
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecord = false;
	// 标记的Y坐标值
	private int mStartY = 0;
	// 当前视图能看到的第一个项的索引
	private int mFirstItemIndex = -1;
	// MOVE时保存的Y坐标值
	private int mMoveY = 0;
	// LISTVIEW状态
	private int mViewState = IListViewState.LVS_NORMAL;

	private final static int RATIO = 2;
	
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private boolean mBack = false;
	
	private View mBannerView = null;
	private int mBannerHeight = 0;
	
	
	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void addBannerView(View view){
		mBannerView = view;
		mBannerHeight = mBannerView.getHeight();
		log.e("addBannerView mBannerHeight = " + mBannerHeight);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mHeadView.addView(mBannerView, lp);
	
		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getHeight();
		log.e("addBannerView mHeadContentHeight = " + mHeadContentHeight);
	}
	
	public void removeBannerView(){
		if (mBannerView != null){
			mHeadView.removeView(mBannerView);
			mBannerHeight = 0;
		}
		
		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		mHeadContentWidth = mHeadView.getMeasuredWidth();
		log.e("removeBannerView mHeadContentHeight = " + mHeadContentHeight);
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void setOnRefreshListener(IOnRefreshListener listener)
	{
		mOnRefreshListener = listener;
	}
	
	private void onRefresh()
	{
		if (mOnRefreshListener != null)
		{
			mOnRefreshListener.OnRefresh();
		}
	}
	
	public void onRefreshComplete()
	{
		mHeadView.setPadding(0,  -1 * mHeadContentHeight + mBannerHeight, 0, 0);
		mLastUpdateTextView.setText("最近更新:" + getCurDataTime());
		switchViewState(IListViewState.LVS_NORMAL);
	}
	
	private void init(Context context)
	{
		initHeadView(context);
		
		initLoadMoreView(context);
		
		setOnScrollListener(this);
	}
	
	
	// 初始化headview试图
	private void initHeadView(Context context)
	{
		mHeadView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.head, null);
		

		mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(60);


		mHeadProgressBar= (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
		
		mRefreshTextview = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
		
		mLastUpdateTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		mHeadContentWidth = mHeadView.getMeasuredWidth();

		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mHeadView.invalidate();

		addHeaderView(mHeadView, null, false);
		
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}
	
	// 初始化footview试图
	private void initLoadMoreView(Context context)
	{
		mFootView= LayoutInflater.from(context).inflate(R.layout.loadmore, null);
		
		mLoadMoreView = mFootView.findViewById(R.id.load_more_view);
		
		mLoadMoreTextView = (TextView) mFootView.findViewById(R.id.load_more_tv);
		
		mLoadingView = mFootView.findViewById(R.id.loading_layout);
	
		mLoadMoreView.setOnClickListener(this);
		
		addFooterView(mFootView);

	}
	
	// 此方法直接照搬自网络上的一个下拉刷新的demo，计算headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}


	public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisiableItem;

	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
		
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
				
		if (mOnRefreshListener != null)
		{
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:		
				doActionDown(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				doActionMove(ev);
				break;
			case MotionEvent.ACTION_UP:
				doActionUp(ev);
				break;
			default:
				break;
			}	
		}
		
				
		return super.onTouchEvent(ev);
	}
	
	private void doActionDown(MotionEvent ev)
	{
		if(mIsRecord == false && mFirstItemIndex == 0)
		{
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
	}
	
	private void doActionMove(MotionEvent ev)
	{
		mMoveY = (int) ev.getY();
		
		if(mIsRecord == false && mFirstItemIndex == 0)
		{
			mStartY = (int) ev.getY();
			mIsRecord = true;
		}
		
		if (mIsRecord == false || mViewState == IListViewState.LVS_LOADING)
		{
			return ;
		}	
		
		int offset = (mMoveY - mStartY) / RATIO;	
		
		switch(mViewState)
		{
			case IListViewState.LVS_NORMAL:
			{
				if (offset > 0)
				{		
					mHeadView.setPadding(0, offset - mHeadContentHeight + mBannerHeight, 0, 0);
					switchViewState(IListViewState.LVS_PULL_REFRESH);
				}
			}
				break;
			case IListViewState.LVS_PULL_REFRESH:
			{
				setSelection(0);
				mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
				if (offset < 0)
				{
					switchViewState(IListViewState.LVS_NORMAL);
				}else if (offset > mHeadContentHeight)
				{
					switchViewState(IListViewState.LVS_RELEASE_REFRESH);
				}
			}
				break;
			case IListViewState.LVS_RELEASE_REFRESH:
			{
				setSelection(0);
				mHeadView.setPadding(0, offset - mHeadContentHeight, 0, 0);
				if (offset >= 0 && offset <= mHeadContentHeight)
				{
					mBack = true;
					switchViewState(IListViewState.LVS_PULL_REFRESH);
				}else if (offset < 0)
				{
					switchViewState(IListViewState.LVS_NORMAL);
				}else{
				
				}
				
			}
				break;
			default:
				return;
		};		
		
	}
	
	private void doActionUp(MotionEvent ev)
	{
		mIsRecord = false;
		mBack = false;
		
		if (mViewState == IListViewState.LVS_LOADING)
		{
			return ;
		}
		
		switch(mViewState)
		{
		case IListViewState.LVS_NORMAL:
		
			break;
		case IListViewState.LVS_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadContentHeight + mBannerHeight, 0, 0);
			switchViewState(IListViewState.LVS_NORMAL);
			break;
		case IListViewState.LVS_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(IListViewState.LVS_LOADING);
			onRefresh();
			break;
		}	
		
	}
	

	// 切换headview视图
	private void switchViewState(int state)
	{	
		
		
		switch(state)
		{
			case IListViewState.LVS_NORMAL:
			{
				Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_NORMAL");
				mArrowImageView.clearAnimation();
				mArrowImageView.setImageResource(R.drawable.arrow);
			}
				break;
			case IListViewState.LVS_PULL_REFRESH:
			{
				Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_PULL_REFRESH");
				mHeadProgressBar.setVisibility(View.GONE);
				mArrowImageView.setVisibility(View.VISIBLE);
				mRefreshTextview.setText("下拉可以刷新");
				mArrowImageView.clearAnimation();

				// 是由RELEASE_To_REFRESH状态转变来的
				if (mBack)
				{
					mBack = false;
					mArrowImageView.clearAnimation();
					mArrowImageView.startAnimation(reverseAnimation);
				}
			}
				break;
			case IListViewState.LVS_RELEASE_REFRESH:
			{
				Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_RELEASE_REFRESH");
				mHeadProgressBar.setVisibility(View.GONE);
				mArrowImageView.setVisibility(View.VISIBLE);
				mRefreshTextview.setText("松开获取更多");
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(animation);
			}
				break;
			case IListViewState.LVS_LOADING:
			{
				Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
				mHeadProgressBar.setVisibility(View.VISIBLE);
				mArrowImageView.clearAnimation();
				mArrowImageView.setVisibility(View.GONE);
				mRefreshTextview.setText("载入中...");
			}
				break;
				default:
					return;
		}	
		
		mViewState = state;
		
	}

	
	
	
	
	
	
	private View mFootView;								
	private View mLoadMoreView;
	private TextView mLoadMoreTextView;
	private View mLoadingView;
	private IOnLoadMoreListener mLoadMoreListener;						// 加载更多监听器
	private int mLoadMoreState = IListViewState.LVS_NORMAL;
	
	public interface ILoadMoreViewState
	{
		int LMVS_NORMAL= 0;					//  普通状态
		int LMVS_LOADING = 1;				//  加载状态
		int LMVS_OVER = 2;					//  结束状态
	}
	
	public interface IOnLoadMoreListener
	{
		void OnLoadMore();
	}
	
	public void setOnLoadMoreListener(IOnLoadMoreListener listener)
	{
		mLoadMoreListener = listener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.load_more_view:
			{
				if (mLoadMoreListener != null && mLoadMoreState == IListViewState.LVS_NORMAL)
				{
					updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
					mLoadMoreListener.OnLoadMore();
				}
			}
			break;
		}
	}
	
	// flag 数据是否已全部加载完毕
	public void onLoadMoreComplete(boolean flag)
	{
		if (flag)
		{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_OVER);
		}else{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_NORMAL);
		}
		
	}
	
	
	// 更新footview视图
	private void updateLoadMoreViewState(int state)
	{
		switch(state)
		{
			case ILoadMoreViewState.LMVS_NORMAL:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("查看更多");
				break;
			case ILoadMoreViewState.LMVS_LOADING:
				mLoadingView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setVisibility(View.GONE);
				break;
			case ILoadMoreViewState.LMVS_OVER:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("尼玛的数据都加载完了！");
				break;
				default:
					break;
		}
		
		mLoadMoreState = state;
	}

	
	public void removeFootView()
	{
		removeFooterView(mFootView);
	}
	
	private String getCurDataTime(){

		java.util.Date data = new Date();
		SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();  
		return sdf.format(data);  
	}

}
