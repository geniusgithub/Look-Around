package com.geniusgithub.lookaround.datastore;

import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


/** 
 * DAO for table INFO_ITEM.
*/
public class InfoItemDao extends AbstractDao<BaseType.InfoItem, String> {

    public static final String TABLENAME = "INFO_ITEM";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property KEY_ID = new Property(0, String.class, BaseType.InfoItem.KEY_ID, true, BaseType.InfoItem.KEY_ID);
        public final static Property KEY_TITLE = new Property(1, String.class, BaseType.InfoItem.KEY_TITLE, true, BaseType.InfoItem.KEY_TITLE);
        public final static Property KEY_CONTENT = new Property(2, String.class, BaseType.InfoItem.KEY_CONTENT, true, BaseType.InfoItem.KEY_CONTENT);
        public final static Property KEY_TIME = new Property(3, String.class, BaseType.InfoItem.KEY_TIME, true, BaseType.InfoItem.KEY_TIME);
        public final static Property KEY_USERNAME = new Property(4, String.class, BaseType.InfoItem.KEY_USERNAME, true, BaseType.InfoItem.KEY_USERNAME);       
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
        				"'_id' INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	                "'id' TEXT ," +
	                "'title' TEXT ," + 
	                "'content' TEXT," + 
	                "'time' TEXT," + 
	                "'userName' TEXT);"); 
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INFO_ITEM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BaseType.InfoItem entity) {
        stmt.clearBindings();
 

        stmt.bindString(1, entity.getMKeyID());  
        stmt.bindString(2, entity.getMTitle());
        stmt.bindString(3, entity.getMContent());  
        stmt.bindString(4, entity.getMTime());
        stmt.bindString(5, entity.getMUserName());
       
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BaseType.InfoItem readEntity(Cursor cursor, int offset) {
    	BaseType.InfoItem entity = new BaseType.InfoItem(cursor.getString(offset + 0), 
											            cursor.getString(offset + 1),
											            cursor.getString(offset + 2), 
											            cursor.getString(offset + 3),
				    									cursor.getString(offset + 4));
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BaseType.InfoItem entity, int offset) {
        entity.setMKeyID(cursor.getString(offset + 0));
        entity.setMTitle(cursor.getString(offset + 1));
        entity.setMContent(cursor.getString(offset + 2));
        entity.setMTime(cursor.getString(offset + 3));
        entity.setMUserName(cursor.getString(offset + 4));
    }



	@Override
	protected String getKey(InfoItem entity) {
		return entity.getMKeyID();
	}

	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	@Override
	protected String updateKeyAfterInsert(InfoItem entity, long rowId) {

		return entity.getMKeyID();
	}
    
}
