package com.geniusgithub.lookaround.weibo.sdk;

import java.util.HashMap;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class ShareItem {
	
	private static final CommonLog log = LogFactory.createLog();
	
	public static HashMap<String, Object> reqMap = new HashMap<String, Object>();
	
	public static String imagePath = null;
		
	public ShareItem(){
		
	}
	
	public static void clear(){
		reqMap.clear();
		imagePath = null;
	}
	
	/** 要分享的图片路径 */
	public  static void setShareImagePath(String path) {
		imagePath = path;
	}
	
	public static String getShareImagePath(){
		return imagePath;
	}
	
	/** address是接收人地址，仅在信息和邮件使用，否则可以不提供 */
	public  static void setAddress(String address) {
		reqMap.put("address", address);
	}

	/** title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供 */
	public static void setTitle(String title) {
		reqMap.put("title", title);
	}

	/** titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供 */
	public static void setTitleUrl(String titleUrl) {
		reqMap.put("titleUrl", titleUrl);
	}

	/** text是分享文本，所有平台都需要这个字段 */
	public static void setText(String text) {
		reqMap.put("text", text);
	//	log.e("content = " + text);
	}

	/** imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段 */
	public static void setImagePath(String imagePath) {
		reqMap.put("imagePath", imagePath);
		log.e("imagePath = " + imagePath);
	}

	/** imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段 */
	public static void setImageUrl(String imageUrl) {
		reqMap.put("imageUrl", imageUrl);
		log.e("imageUrl = " + imageUrl);
	}

	/** musicUrl仅在微信（及朋友圈）中使用，是音乐文件的直接地址 */
	public static void serMusicUrl(String musicUrl) {
		reqMap.put("musicUrl", musicUrl);
	}

	/** url仅在微信（包括好友和朋友圈）中使用，否则可以不提供 */
 	public static void setUrl(String url) {
		reqMap.put("url", url);
	}

	/** appPath是待分享应用程序的本地路劲，仅在微信（包括好友和朋友圈）中使用，否则可以不提供 */
	public static void setAppPath(String appPath) {
		reqMap.put("appPath", appPath);
	}

	/** comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供 */
	public static void setComment(String comment) {
		reqMap.put("comment", comment);
	}

	/** site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供 */
	public static void setSite(String site) {
		reqMap.put("site", site);
	}

	/** siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供 */
	public static void setSiteUrl(String siteUrl) {
		reqMap.put("siteUrl", siteUrl);
	}

	/** foursquare分享时的地方名 */
	public static void setVenueName(String venueName) {
		reqMap.put("venueName", venueName);
	}

	/** foursquare分享时的地方描述 */
	public static void setVenueDescription(String venueDescription) {
		reqMap.put("venueDescription", venueDescription);
	}

	/** 分享地纬度，新浪微博、腾讯微博和foursquare支持此字段 */
	public static void setLatitude(float latitude) {
		reqMap.put("latitude", latitude);
	}

	/** 分享地经度，新浪微博、腾讯微博和foursquare支持此字段 */
	public static void setLongitude(float longitude) {
		reqMap.put("longitude", longitude);
	}

	/** 设置编辑页的初始化选中平台 */
	public static void setPlatform(String platform) {
		reqMap.put("platform", platform);
	}
}
