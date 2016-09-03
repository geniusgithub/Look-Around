package com.geniusgithub.lookaround.maincontent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.cache.ImageLoaderEx;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements IContentItemClick{

/*
    public static interface onItemClickListener{
        public void onItemClick(BaseType.InfoItem item);
    }
*/


    private static final CommonLog log = LogFactory.createLog();

    private List<BaseType.InfoItem> data = new ArrayList<BaseType.InfoItem>();
    private Context mContext;
    private boolean mBusy = false;
    private ImageLoaderEx mImageLoader;

    public IContentItemClick mOnItemClickListener;

    public InfoAdapter(Context context, List<BaseType.InfoItem> data)
    {
        mContext = context;
        this.data = data;
        mImageLoader = new ImageLoaderEx(context);
        mImageLoader.setDefaultBitmap(mContext.getResources().getDrawable(R.drawable.load_img));
    }

    public void setOnItemClickListener(IContentItemClick listener){
        mOnItemClickListener = listener;
    }

    public void updateLoadMoreViewState(int state){
        log.i("updateLoadMoreViewState state = " + state);
        switch (state){
            case ILoadMoreViewState.LMVS_NORMAL:
            case ILoadMoreViewState.LMVS_LOADING:
            case ILoadMoreViewState.LMVS_OVER:
                mLoadMoreState = state;
                notifyDataSetChanged();
                break;
        }

    }

    public int getLoadMoreViewState(){
        return mLoadMoreState;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case BANNER_VIEW0_TYPE:
                return createBanner0ViewHolder(parent);
            case BANNER_VIEW1_TYPE:
                return createBanner1ViewHolder(parent);
            case BANNER_VIEW2_TYPE:
                return createBanner2ViewHolder(parent);
            case FOOTVIEW_TYPE:
                return createFooterViewHolder(parent);
        }
        return createBanner0ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case BANNER_VIEW0_TYPE:
                bindBanner0ViewHolder(holder, position);
                break;
            case BANNER_VIEW1_TYPE:
                bindBanner1ViewHolder(holder, position);
                break;
            case BANNER_VIEW2_TYPE:
                bindBanner2ViewHolder(holder, position);
                break;
            case FOOTVIEW_TYPE:
                bindFootViewHolder(holder);
                break;
            default:
                bindBanner0ViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() == 0){
            return 0;
        }
        return data.size() + 1;
    }

    private final int FOOTVIEW_TYPE = 100;
    private final int BANNER_VIEW0_TYPE = 0;
    private final int BANNER_VIEW1_TYPE = 1;
    private final int BANNER_VIEW2_TYPE = 2;

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()){
            return FOOTVIEW_TYPE;
        }
        BaseType.InfoItem item = getItem(position);
        return item.mBannerType;
    }

    @Override
    public void onItemClick(BaseType.InfoItem item) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(item);
        }
    }

    public BaseType.InfoItem getItem(int position) {
        if (position < data.size()){
            return data.get(position);
        }

        return null;
    }


    public void refreshData(List<BaseType.InfoItem> data)
    {
        this.data = data;
        notifyDataSetChanged();

    }

    public ImageLoaderEx getImageLoader(){
        return mImageLoader;
    }

    private RecyclerView.ViewHolder createBanner0ViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.content_listitem_layout0, parent, false);
        ContentViewHolder0 viewHolder = new ContentViewHolder0(view, this);
        return viewHolder;
    }
    private RecyclerView.ViewHolder createBanner1ViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.content_listitem_layout1, parent, false);
        ContentViewHolder1 viewHolder = new ContentViewHolder1(view, this);
        return viewHolder;
    }

    private RecyclerView.ViewHolder createBanner2ViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.content_listitem_layout2, parent, false);
        ContentViewHolder2 viewHolder = new ContentViewHolder2(view, this);
        return viewHolder;
    }


    private void bindBanner0ViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BaseType.InfoItem item = getItem(position);
        ContentViewHolder0 VH = (ContentViewHolder0) viewHolder;
        VH.bindInfo(item);
    }

    private void bindBanner1ViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BaseType.InfoItem item = getItem(position);
        ContentViewHolder1 VH = (ContentViewHolder1) viewHolder;
        VH.bindInfo(mImageLoader, item, mBusy);
    }

    private void bindBanner2ViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BaseType.InfoItem item = getItem(position);
        ContentViewHolder2 VH = (ContentViewHolder2) viewHolder;
        VH.bindInfo(mImageLoader, item, mBusy);
    }



    private RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recyclerview_footer, parent, false);
        FooterViewHolder viewHolder = new FooterViewHolder(view);
        return viewHolder;
    }

    private void bindFootViewHolder(RecyclerView.ViewHolder viewHolder) {
        log.i("bindFootViewHolder mLoadMoreState = " + mLoadMoreState);
        FooterViewHolder VH = (FooterViewHolder) viewHolder;
        VH.updateState(mLoadMoreState);
    }


    private int mLoadMoreState = ILoadMoreViewState.LMVS_NORMAL;
    public static interface ILoadMoreViewState
    {
        int LMVS_NORMAL = 0;
        int LMVS_LOADING = 1;
        int LMVS_OVER = 2;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoadMore;
        TextView tvNoData;
        public FooterViewHolder(View itemView) {
            super(itemView);
            pbLoadMore = (ProgressBar) itemView.findViewById(R.id.pb_load_more);
            tvNoData = (TextView) itemView.findViewById(R.id.tv_nodata);
        }

        public void updateState(int state){
            switch (state){
                case ILoadMoreViewState.LMVS_NORMAL:
                case ILoadMoreViewState.LMVS_LOADING:
                    pbLoadMore.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    break;
                case ILoadMoreViewState.LMVS_OVER:
                    pbLoadMore.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

}
