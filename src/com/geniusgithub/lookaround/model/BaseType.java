package com.geniusgithub.lookaround.model;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseType {

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
		public String mImageURL = "";
		public String mThumbnaiURL = "";
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
			mImageURL = jsonObject.getString(KEY_IMAGES);
			mThumbnaiURL = jsonObject.getString(KEY_IMAGESTHUMBANIL);
			return true;

		}	
	}
}
