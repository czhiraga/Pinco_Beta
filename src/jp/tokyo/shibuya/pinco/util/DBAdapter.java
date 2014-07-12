package jp.tokyo.shibuya.pinco.util;

import java.util.HashSet;
import java.util.Iterator;

import jp.tokyo.shibuya.pinco.entity.MediaEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

    static final String DATABASE_NAME = "pinco.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "pinco_items";
    public static final String COL_ID = "id";
    public static final String COL_APITYPE = "api_type";
    public static final String COL_DATE = "get_date";
    public static final String COL_USERID = "user_id";
    public static final String COL_MEDIAID = "media_id";
    public static final String COL_URL = "url";
    public static final String COL_COMMENT = "comment";
    public static final String COL_SUB1 = "sub1";
    public static final String COL_SUB2 = "sub2";
    public static final String COL_STATUS = "status";

    protected final Context context;
    protected DBHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public HashSet<MediaEntity> getAllData(boolean status) {
        HashSet<MediaEntity> mediaHS = new HashSet<MediaEntity>();
        try {
            Cursor c = null;
            if (status) {
                c = db.query(TABLE_NAME, null, null, null, null, null, null);
            } else {
                c = db.query(TABLE_NAME, null, COL_STATUS + "==1", null, null,
                        null, null);
            }
            mediaHS = toHashSet(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaHS;
    }

    public HashSet<MediaEntity> getAllData() {
        return getAllData(false);
    }

    public String insertData(MediaEntity me) {
        String error = "";
        String apiType = me.getAPIType();
        String userID = me.getUserID();
        String mediaID = me.getMediaID();
        String url = me.getURL();
        String comment = me.getComment();
        String sub1 = me.getSub1();
        String sub2 = me.getSub2();
        int status = me.getStatus();
        ContentValues values = new ContentValues();
        values.put(COL_APITYPE, apiType);
        values.put(COL_USERID, userID);
        values.put(COL_MEDIAID, mediaID);
        values.put(COL_URL, url);
        values.put(COL_COMMENT, comment);
        values.put(COL_SUB1, sub1);
        values.put(COL_SUB2, sub2);
        values.put(COL_STATUS, status);
        db.insertOrThrow(TABLE_NAME, null, values);
        try {
            db.insertOrThrow(TABLE_NAME, null, values);
        } catch (Exception e) {
            error = e + "\n" + error;
        }
        return error;
    }

    public String insertData(HashSet<MediaEntity> meHS) {
        String error = "";
        Iterator<MediaEntity> it = meHS.iterator();
        while (it.hasNext()) {
            MediaEntity me = it.next();
            error = insertData(me);
        }
        return error;
    }

    public String deleteData(MediaEntity me) {
        String error = "";
        int id = me.getID();
        try {
            db.delete(TABLE_NAME, COL_ID + "=" + id, null);
        } catch (Exception e) {
            error = e + "\n" + error;
        }
        return error;
    }

    public String deleteData(HashSet<MediaEntity> meHS) {
        String error = "";
        Iterator<MediaEntity> it = meHS.iterator();
        while (it.hasNext()) {
            MediaEntity me = it.next();
            error = deleteData(me);
        }
        return error;
    }

    public String updateData(MediaEntity me) {
        String error = "";
        int id = me.getID();
        String apiType = me.getAPIType();
        String userID = me.getUserID();
        String mediaID = me.getMediaID();
        String url = me.getURL();
        String comment = me.getComment();
        String sub1 = me.getSub1();
        String sub2 = me.getSub2();
        int status = me.getStatus();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, "datetime('now', 'localtime')");
        values.put(COL_APITYPE, apiType);
        values.put(COL_USERID, userID);
        values.put(COL_MEDIAID, mediaID);
        values.put(COL_URL, url);
        values.put(COL_COMMENT, comment);
        values.put(COL_SUB1, sub1);
        values.put(COL_SUB2, sub2);
        values.put(COL_STATUS, status);
        try {
            db.update(TABLE_NAME, values, COL_ID + "=" + id, null);
        } catch (Exception e) {
            error = e + "\n" + error;
        }
        return error;
    }

    public String updateData(HashSet<MediaEntity> meHS) {
        String error = "";
        Iterator<MediaEntity> it = meHS.iterator();
        while (it.hasNext()) {
            MediaEntity me = it.next();
            error = updateData(me);
        }
        return error;
    }

    private HashSet<MediaEntity> toHashSet(Cursor c) {
        HashSet<MediaEntity> mediaHS = new HashSet<MediaEntity>();
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(COL_ID));
                String apiType = c.getString(c.getColumnIndex(COL_APITYPE));
                String date = c.getString(c.getColumnIndex(COL_DATE));
                String userID = c.getString(c.getColumnIndex(COL_USERID));
                String mediaID = c.getString(c.getColumnIndex(COL_MEDIAID));
                String url = c.getString(c.getColumnIndex(COL_URL));
                String comment = c.getString(c.getColumnIndex(COL_COMMENT));
                String sub1 = c.getString(c.getColumnIndex(COL_SUB1));
                String sub2 = c.getString(c.getColumnIndex(COL_SUB2));
                int status = c.getInt(c.getColumnIndex(COL_STATUS));
                MediaEntity me = new MediaEntity(id, apiType, date, userID,
                        mediaID, url, comment, sub1, sub2, status);
                mediaHS.add(me);
            } while (c.moveToNext());
        }
        return mediaHS;
    }
}
