package com.geniusgithub.lookaround.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.util.Log;

public class BaseType {
	private static final CommonLog log = LogFactory.createLog();
	// ListItem
	public static class ListItem implements IParseJson{

		public final static String KEY_TYPEINTRO = "typeIntro";
		public final static String KEY_TOPICNAME = "tb_TopicName";
		public final static String KEY_TITLE = "typeTitle";
		public final static String KEY_TYPEID = "typeId";
		public final static String KEY_TYPEAD = "typeAd";

		public String mTypeIntro = "";
		public String mTopicName = "";
		public String mTitle = "";
		public String mTypeID = "";
		public String mTypeAd = "";
		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
				
			mTypeIntro = jsonObject.getString(KEY_TYPEINTRO);
			mTopicName = jsonObject.getString(KEY_TOPICNAME);
			mTitle = jsonObject.getString(KEY_TITLE);
			mTypeID = jsonObject.getString(KEY_TYPEID);
			mTypeAd = jsonObject.getString(KEY_TYPEAD);
			return true;

		}	
		
		public String getShowString(){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("mTypeIntro = " + mTypeIntro + 
								"\nmTopicName = " + mTopicName + 
								"\nmTitle = " + mTitle + 
								"\nmTypeID = " + mTypeID + 
								"\nmTypeAd = " + mTypeAd);
			return stringBuffer.toString();
		}
	}
	
	
	// InfoItem
	public static class InfoItem implements IParseJson{
													
		public final static String KEY_BANNERTYPE = "barnnerType";
		public final static String KEY_ID = "id";
		public final static String KEY_TITLE = "title";
		public final static String KEY_CONTENT = "content";
		public final static String KEY_TIME = "time";
		public final static String KEY_COMMENTCOUNT = "commentCount";
		public final static String KEY_LIKECOUNT = "likeCount";
		public final static String KEY_USERNAME = "userName";
		
		public final static String KEY_HEADPATH = "headPath";
		public final static String KEY_IMAGES = "images";
		public final static String KEY_IMAGESTHUMBANIL = "imagesThumbnail";
		
		public int mBannerType = 0;
		public String mKeyID = "";
		public String mTitle = "";
		public String mContent = "";
		public String mTime = "";
		public String mCommentCount = "";
		public String mLinkCount = "";
		public String mUserName = "";
		public String mHeadPath = "";
		public String mImageURL_STRING = "";
		public String mThumbnaiURL_STRING = "";
		
		public List<String> mImageUrlList = new ArrayList<String>();
		public List<String> mThumbnaiURLList = new ArrayList<String>();
		
		
		public InfoItem(){
		}
		
		public InfoItem(String key, String title, String content, String time, String username){
			mKeyID = key;
			mTitle = title;
			mContent = content;
			mTime = time;
			mUserName = username;
		}
		// javabean
		public void setMKeyID(String key){
			mKeyID = key;
		}
		
		public String getMKeyID(){
			return mKeyID;
		}
		
		public void setMTitle(String title){
			mTitle = title;
		}
		
		public String getMTitle(){
			return mTitle;
		}
		
		public void setMContent(String content){
			mContent = content;
		}
		
		public String getMContent(){
			return mContent;
		}
		
		public void setMTime(String time){
			mTime = time;
		}
		
		public String getMTime(){
			return mTime;
		}
		
		public void setMUserName(String username){
			mUserName = username;
		}
		
		public String getMUserName(){
			return mUserName;
		}
		// javabean
		
		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
				
			mBannerType = jsonObject.getInt(KEY_BANNERTYPE);
			mKeyID = jsonObject.getString(KEY_ID);
			mTitle = jsonObject.getString(KEY_TITLE);
			mContent = jsonObject.getString(KEY_CONTENT);
			mTime = jsonObject.getString(KEY_TIME);
			mCommentCount = jsonObject.getString(KEY_COMMENTCOUNT);
			mLinkCount = jsonObject.getString(KEY_LIKECOUNT);
			mUserName = jsonObject.getString(KEY_USERNAME);
			
			mHeadPath = jsonObject.getString(KEY_HEADPATH);
			mImageURL_STRING = jsonObject.getString(KEY_IMAGES);
			mThumbnaiURL_STRING = jsonObject.getString(KEY_IMAGESTHUMBANIL);
			updateImageURL(mImageURL_STRING);
			updateThumbnaiURL(mThumbnaiURL_STRING);
			updateBannerType();
			return true;

		}	
		
		public String getThumnaiImageURL(int pos){
			if (pos >= mThumbnaiURLList.size()){
				return null;
			}
			
			return mThumbnaiURLList.get(pos);
		}

		
		public int getThumnaiImageCount(){
			return mThumbnaiURLList.size();
		}
		
		public String getImageURL(int pos){
			if (pos >= mImageUrlList.size()){
				return null;
			}
			
			return mImageUrlList.get(pos);
		}
		
		private void updateImageURL(String url){
		
			if (url == null || url.length() < 1){
				return ;
			}
			
			try {
				String []str = url.split(",");
				int size = str.length;
				for(int i = 0; i < size; i++){
					if (str[i].length() > 0){
						mImageUrlList.add(str[i]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		private void updateThumbnaiURL(String url){
			if (url == null || url.length() < 1){
				return ;
			}
			
			try {
				String []str = url.split(",");
				int size = str.length;
				for(int i = 0; i < size; i++){
					if (str[i].length() > 0){
						mThumbnaiURLList.add(str[i]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		private void updateBannerType(){
			boolean isHasContent = mContent.length() > 0 ? true : false;
			boolean isHasThumail = mThumbnaiURLList.size() == 0 ? false : true;
			mBannerType = -1;
			if (isHasContent){
				if (isHasThumail){
					mBannerType = 1;
				}else{
					mBannerType = 0;
				}
			}else{
				if (isHasThumail){
					mBannerType = 2;
				}else{

				}			
			}
		}
		
	}
}
