package leebeno.com.leebeno.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.BuildConfig;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.List;

import leebeno.com.leebeno.Model.MsgGetterSetter;

public class DatabaseLocal extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chat_database";
    public static final String TABLE_CONTACTS = "contact_list";
    public static final String LEE_CONTACTS = "lee_contact_list";
    public static final String MSG_CHAT = "message_chat";
    public static final String GROUP_CHAT = "group_chat";
    public static final String CONTACT_DETAIL = "contact_detail";

    public DatabaseLocal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT,contact_id TEXT" +
                ",contact_number TEXT,contact_name TEXT,contact_profile TEXT,lee_profile TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LEE_CONTACTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT,lee_contact_id TEXT,profile_img TEXT" +
                ",lee_chat_status TEXT,block_status TEXT,last_seen TEXT,online_status TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + CONTACT_DETAIL + "(id INTEGER PRIMARY KEY AUTOINCREMENT,lee_contact_id TEXT,profile_img TEXT" +
                ",lee_chat_status TEXT,block_status TEXT,last_seen TEXT,online_status TEXT,contact_number TEXT,contact_name TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MSG_CHAT + "(id INTEGER PRIMARY KEY AUTOINCREMENT,message_id DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ",chat_type TEXT,group_id TEXT,sender_msg TEXT,receiver_msg TEXT,message_type TEXT,msg_read_status TEXT,msg_time_stamp TEXT,contact_id TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + GROUP_CHAT + "(id INTEGER PRIMARY KEY AUTOINCREMENT,group_id TEXT" +
                ",group_title TEXT,group_image TEXT,sender_msg TEXT,receiver_msg TEXT,message_type TEXT,msg_read_status TEXT,msg_time_stamp TEXT)");


    }


    /*   public void addLeeContacts(String lee_contact_id, String profile_img, String lee_chat_status, String block_status, String last_seen, String online_status) {
           SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
           ContentValues contentValues = new ContentValues();
           contentValues.put("lee_contact_id", lee_contact_id);
           contentValues.put("profile_img", profile_img);
           contentValues.put("lee_chat_status", lee_chat_status);
           contentValues.put("block_status", block_status);
           contentValues.put("last_seen", last_seen);
           contentValues.put("online_status", online_status);

   //        Log.e("getContact",""+contentValues);
           sqLiteDatabase.insert(LEE_CONTACTS, "lee_contact_id", contentValues);*//*
        sqLiteDatabase.insert(LEE_CONTACTS, "profile_img", contentValues);
        sqLiteDatabase.insert(LEE_CONTACTS, "lee_chat_status", contentValues);
        sqLiteDatabase.insert(LEE_CONTACTS, "block_status", contentValues);
        sqLiteDatabase.insert(LEE_CONTACTS, "online_status", contentValues);*//*

        sqLiteDatabase.close();

    }


    public void addContacts(String contact_id, String contact_number, String contact_name, String contact_profile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_id", contact_id);
        contentValues.put("contact_number", contact_number);
        contentValues.put("contact_name", contact_name);
        contentValues.put("contact_profile", contact_profile);

//        Log.e("getContact",""+contentValues);
        sqLiteDatabase.insert(TABLE_CONTACTS, "contact_id", contentValues);
        *//*
        sqLiteDatabase.insert(TABLE_CONTACTS, "contact_number", contentValues);
        sqLiteDatabase.insert(TABLE_CONTACTS, "contact_name", contentValues);
        sqLiteDatabase.insert(TABLE_CONTACTS, "contact_profile", contentValues);*//*

        sqLiteDatabase.close();

    }


    *//*get Data*/
    /*
    public Cursor getContects() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("select * from " + TABLE_CONTACTS, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
        return null;
    }

    public Cursor getLeeContects() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("select * from " + LEE_CONTACTS, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
        return null;
    }*/
    public void addContactDetail(String lee_contact_id, String profile_img, String lee_chat_status, String block_status, String last_seen,
                                 String online_status, String contact_number, String contact_name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lee_contact_id", lee_contact_id);
        contentValues.put("profile_img", profile_img);
        contentValues.put("lee_chat_status", lee_chat_status);
        contentValues.put("block_status", block_status);
        contentValues.put("last_seen", last_seen);
        contentValues.put("online_status", online_status);
        contentValues.put("contact_number", contact_number);
        contentValues.put("contact_name", contact_name);
        sqLiteDatabase.insert(CONTACT_DETAIL, "lee_contact_id", contentValues);

        sqLiteDatabase.close();

    }


    public void addMsgChat(String message_id, String chat_type, String group_id, String sender_msg, String receiver_msg,
                           String message_type, String msg_read_status, String msg_time_stamp, String contact_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("message_id", message_id);
        contentValues.put("chat_type", chat_type);
        contentValues.put("group_id", group_id);
        contentValues.put("sender_msg", sender_msg);
        contentValues.put("receiver_msg", receiver_msg);
        contentValues.put("message_type", message_type);
        contentValues.put("msg_read_status", msg_read_status);
        contentValues.put("msg_time_stamp", msg_time_stamp);
        contentValues.put("contact_id", contact_id);
        Log.e("getMsgChat", "" + contentValues);
        sqLiteDatabase.insert(MSG_CHAT, "message_id", contentValues);
        sqLiteDatabase.close();
    }

    public Cursor getContactDetail() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("select * from " + CONTACT_DETAIL, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
        return null;
    }

    public Cursor getMsgChat() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("select * from " + MSG_CHAT, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
        return null;
    }


    public MsgGetterSetter getLeeConnections(String contectId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT profile_img,online_status,last_seen,contact_name FROM " + CONTACT_DETAIL + " WHERE lee_contact_id= " + contectId;
        String profile_img = null, online_status = null;
        MsgGetterSetter msgGetterSetter = new MsgGetterSetter();

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            msgGetterSetter.setProfileUrl(cursor.getString(cursor.getColumnIndex("profile_img")));
            msgGetterSetter.setOnlineStatus(cursor.getString(cursor.getColumnIndex("online_status")));
            msgGetterSetter.setLastSeen(cursor.getString(cursor.getColumnIndex("last_seen")));
            msgGetterSetter.setNickname(cursor.getString(cursor.getColumnIndex("contact_name")));
//                Log.e("lfdfbkdfbd",profile_img);
//            }
        }

        return msgGetterSetter;
    }

    public MsgGetterSetter getMsgSingleUser(String contectId) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT message_id,chat_type,sender_msg,receiver_msg,message_type,msg_time_stamp FROM " + MSG_CHAT + " WHERE lee_contact_id= " + contectId;
        String profile_img = null, online_status = null;
        MsgGetterSetter msgGetterSetter = new MsgGetterSetter();

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            msgGetterSetter.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
            msgGetterSetter.setChatType(cursor.getString(cursor.getColumnIndex("chat_type")));
            msgGetterSetter.setSender_msg(cursor.getString(cursor.getColumnIndex("sender_msg")));
            msgGetterSetter.setReceiver_msg(cursor.getString(cursor.getColumnIndex("receiver_msg")));
            msgGetterSetter.setMsg_time_stamp(cursor.getString(cursor.getColumnIndex("msg_time_stamp")));
//                Log.e("lfdfbkdfbd",profile_img);
//            }
        }

        return msgGetterSetter;
    }


    public MsgGetterSetter getlastMsgTime(String contectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        MsgGetterSetter msgGetterSetter = new MsgGetterSetter();
        String query = "SELECT sender_msg,receiver_msg,group_id,msg_time_stamp,message_id FROM " + MSG_CHAT + " WHERE contact_id=" + contectId +
                " ORDER BY message_id LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            msgGetterSetter.setSender_msg(cursor.getString(cursor.getColumnIndex("sender_msg")));
            msgGetterSetter.setReceiver_msg(cursor.getString(cursor.getColumnIndex("receiver_msg")));
            msgGetterSetter.setMsg_time_stamp(cursor.getString(cursor.getColumnIndex("msg_time_stamp")));
            msgGetterSetter.setMessage_id(cursor.getString(cursor.getColumnIndex("message_id")));
            Log.e("lfdfbkdfbd", "" + cursor.getString(cursor.getColumnIndex("sender_msg")));
        }
//        }

        return msgGetterSetter;
    }


    public void resetTable(String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(tableName, null, null);

        } catch (Exception e) {
            Log.e("deleteDataException", "" + e);
        }
    }

    public void deleteTable(String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(tableName, null, null);

        } catch (Exception e) {
            Log.e("deleteDataException", "" + e);
        }
    }

    public void deleteMessages(String[] msgId) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MSG_CHAT, " message_id=? ", msgId);

        } catch (Exception e) {
            Log.e("deleteDataException", "" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
