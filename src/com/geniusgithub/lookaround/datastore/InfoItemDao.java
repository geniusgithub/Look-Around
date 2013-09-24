package com.geniusgithub.lookaround.datastore;

import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.model.BaseType;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


/** 
 * DAO for table INFO_ITEM.
*/
public class InfoItemDao extends AbstractDao<BaseType.InfoItemEx, String> {

    public static final String TABLENAME = "INFO_ITEM";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property KEY_ID = new Property(0, String.class, BaseType.InfoItem.KEY_ID, true, BaseType.InfoItem.KEY_ID);
        public final static Property KEY_BANNERTYPE = new Property(1, Integer.class, BaseType.InfoItem.KEY_BANNERTYPE, false, BaseType.InfoItem.KEY_BANNERTYPE);
        public final static Property KEY_TITLE = new Property(2, String.class, BaseType.InfoItem.KEY_TITLE, false, BaseType.InfoItem.KEY_TITLE);
        public final static Property KEY_CONTENT = new Property(3, String.class, BaseType.InfoItem.KEY_CONTENT, false, BaseType.InfoItem.KEY_CONTENT);
        public final static Property KEY_TIME = new Property(4, String.class, BaseType.InfoItem.KEY_TIME, false, BaseType.InfoItem.KEY_TIME);
        public final static Property KEY_COMMENTCOUNT = new Property(5, String.class, BaseType.InfoItem.KEY_COMMENTCOUNT, false, BaseType.InfoItem.KEY_COMMENTCOUNT);
        public final static Property KEY_LIKECOUNT = new Property(6, String.class, BaseType.InfoItem.KEY_LIKECOUNT, false, BaseType.InfoItem.KEY_LIKECOUNT);
        public final static Property KEY_USERNAME = new Property(7, String.class, BaseType.InfoItem.KEY_USERNAME, false, BaseType.InfoItem.KEY_USERNAME);  
        public final static Property KEY_SOURCEFROM = new Property(8, String.class, BaseType.InfoItem.KEY_SOURCEFROM, false, BaseType.InfoItem.KEY_SOURCEFROM);
        public final static Property KEY_SOURCEURL = new Property(9, String.class, BaseType.InfoItem.KEY_SOURCEURL, false, BaseType.InfoItem.KEY_SOURCEURL);
        public final static Property KEY_HEADPATH = new Property(10, String.class, BaseType.InfoItem.KEY_HEADPATH, false, BaseType.InfoItem.KEY_HEADPATH);       
        public final static Property KEY_IMAGES = new Property(11, String.class, BaseType.InfoItem.KEY_IMAGES, false, BaseType.InfoItem.KEY_IMAGES);       
        public final static Property KEY_IMAGESTHUMBANIL = new Property(12, String.class, BaseType.InfoItem.KEY_IMAGESTHUMBANIL, false, BaseType.InfoItem.KEY_IMAGESTHUMBANIL);       
    };
    
	
    public InfoItemDao(DaoConfig config) {
        super(config);
    }
    
    public InfoItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

	
    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'INFO_ITEM' (" + 
	                "'id' TEXT ," +
	                "'barnnerType' INTEGER ," +
	                "'title' TEXT ," + 
	                "'content' TEXT," + 
	                "'time' TEXT," + 
	                "'commentCount' TEXT," + 
	                "'likeCount' TEXT," + 
	                "'sourceFrom' TEXT," +
	                "'sourceUrl' TEXT," + 
	                "'userName' TEXT," + 
	                "'headPath' TEXT," + 
	                "'images' TEXT," + 
	                "'imagesThumbnail' TEXT);"); 
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INFO_ITEM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BaseType.InfoItemEx entity) {
        stmt.clearBindings();
 

        stmt.bindString(1, entity.mKeyID);  
        stmt.bindLong(2, entity.mBannerType);  
        stmt.bindString(3, entity.mTitle);
        stmt.bindString(4, entity.mContent);  
        stmt.bindString(5, entity.mTime);
        stmt.bindString(6, entity.mCommentCount);
        stmt.bindString(7, entity.mLinkCount);
        stmt.bindString(8, entity.mUserName);
        stmt.bindString(9, entity.mSourceFrom);
        stmt.bindString(10, entity.mSourceUrl);
        stmt.bindString(11, entity.mHeadPath);
        stmt.bindString(12, entity.mImageURL_STRING);
        stmt.bindString(13, entity.mThumbnaiURL_STRING);
 
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BaseType.InfoItemEx readEntity(Cursor cursor, int offset) {
    	BaseType.InfoItemEx entity = new BaseType.InfoItemEx(cursor.getString(offset + 0), 
    													cursor.getInt(offset + 1),
											            cursor.getString(offset + 2),
											            cursor.getString(offset + 3), 
											            cursor.getString(offset + 4),
											            cursor.getString(offset + 5),
											            cursor.getString(offset + 6),
											            cursor.getString(offset + 7),
											            cursor.getString(offset + 8),
											            cursor.getString(offset + 9),
											            cursor.getString(offset + 10),
											            cursor.getString(offset + 11),
											            cursor.getString(offset + 12)
    													);

    	entity.updateURLS();
    	
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BaseType.InfoItemEx entity, int offset) {
        entity.mKeyID = cursor.getString(offset + 0);
        entity.mBannerType = cursor.getInt(offset + 1);
        entity.mTitle = cursor.getString(offset + 2);
        entity.mContent = cursor.getString(offset +3);
        entity.mTime =  cursor.getString(offset + 4);
        entity.mCommentCount = cursor.getString(offset + 5);
        entity.mLinkCount = cursor.getString(offset + 6);
        entity.mUserName =  cursor.getString(offset + 7);
        entity.mSourceFrom =  cursor.getString(offset + 8);
        entity.mSourceUrl =  cursor.getString(offset + 9);
        entity.mHeadPath = cursor.getString(offset + 10);
        entity.mImageURL_STRING = cursor.getString(offset + 11);
        entity.mThumbnaiURL_STRING = cursor.getString(offset + 12);
      
    	entity.updateURLS();
    }
    
 

	@Override
	protected String getKey(BaseType.InfoItemEx entity) {
		return entity.mKeyID;
	}

	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	@Override
	protected String updateKeyAfterInsert(BaseType.InfoItemEx entity, long rowId) {

		return entity.mKeyID;
	}
	

	public boolean isCollect(BaseType.InfoItemEx item){
		BaseType.InfoItemEx itemEx = load(item.mKeyID);
		
		return itemEx != null ? true : false;
	}
}
