package com.example.ero_sol_pc_04.androidassets.Database;

/**
 * We are using method for insert upadte and delete in db so for this purpose we are passing classes as a parameter
 * this classes contains all the information needs to be inserted in to the database so
 * we just create another method which takes a class as input and set all data to the class members
 * and this methods are used to insert data like this   getDBManager().insertVoipMessage(wrapedVoipMessage);
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;package com.erosol.voip.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;
import com.erosol.voip.model.ImageMedia;
import com.erosol.voip.model.VoipMediaBundle;
import com.erosol.voip.model.VoipMessage;
import com.erosol.voip.model.VoipMessageBody;
import com.erosol.voip.model.VopipMessageMedia;
import com.erosol.voip.model.VoipCall;
import com.erosol.voip.model.VoipChat;
import com.erosol.voip.model.VoipContact;
import com.erosol.voip.model.VoipGroup;
import com.erosol.voip.model.VoipGroupMember;
import com.erosol.voip.system.VoipSystem;
import com.erosol.voip.util.Logger;
import com.erosol.voip.util.VoipConstants;


public class DBManager implements VoipSystem.IVoipSystem.IDBManager{
    private static final String TAG = DBManager.class.getSimpleName();
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "voip_client_db";

    // Creating tables queries run at the start
    private static final String CREATE_MESSAGES_TABLE_QUERY = "CREATE TABLE messages ( "
            + "id INTEGER PRIMARY KEY,"
            + "message_id VARCHAR(45),"
            + "message_type INTEGER NOT NULL," // 0 Message 1 Call
            + "media_status INTEGER NOT NULL," // 0 Not present 1 Present
            + "body VARCHAR(45),"
            + "media_reference VARCHAR(45),"
            + "media_type VARCHAR(45),"
            + "media_size VARCHAR(45),"
            + "sender_id VARCHAR(45) NOT NULL,"
            + "recipient_id VARCHAR(45) NOT NULL,"
            + "direction INTEGER NOT NULL," // 0 Incoming 1 Outgoing
            + "status INTEGER NOT NULL," // Pending, Sent, Failed, Delivered
            + "local_status INTEGER NOT NULL," // 0 Not Seen 1 Seen
            + "timestamp TIMESTAMP NOT NULL);";
    private static final String CREATE_CALLS_TABLE_QUERY = "CREATE TABLE calls ( "
            + "id INTEGER PRIMARY KEY,"
            + "call_id VARCHAR(45),"
            + "remote_user_id VARCHAR(45),"
            + "start_time TIMESTAMP NOT NULL,"
            + "established_time TIMESTAMP NOT NULL,"
            + "ended_time TIMESTAMP NOT NULL,"
            + "call_ended_cause INTEGER NOT NULL,"
            + "is_video_offered INTEGER NOT NULL,"
            + "call_direction INTEGER NOT NULL," // 0 Incoming 1 Outgoing
            + "count INTEGER,"
            + "timestamp TIMESTAMP NOT NULL);";
    private static final String CREATE_CONTACTS_TABLE_QUERY = "CREATE TABLE contacts ( "
            + "id INTEGER PRIMARY KEY,"
            + "name VARCHAR(45),"
            + "mobile VARCHAR(45) NOT NULL,"
            + "email VARCHAR(45),"
            + "username VARCHAR(45),"
            + "mood VARCHAR(45),"
            + "gender VARCHAR(45),"
            + "birthday VARCHAR(45),"
            + "lat VARCHAR(45),"
            + "lng VARCHAR(45),"
            + "city VARCHAR(45),"
            + "country VARCHAR(45),"
            + "status VARCHAR(45),"
            + "dp_id VARCHAR(45));";

    private static final String CREATE_GROUPS_TABLE_QUERY = "CREATE TABLE groups ( "
            + "id INTEGER PRIMARY KEY,"
            + "name VARCHAR(45) NOT NULL,"
            + "created_by TIMESTAMP NOT NULL,"
            + "dp_id VARCHAR(45),"
            + "status VARCHAR(45) NOT NULL,"
            + "created_at TIMESTAMP NOT NULL);";

    private static final String CREATE_GROUP_MEMBERS_TABLE_QUERY = "CREATE TABLE group_members ( "
            + "id INTEGER PRIMARY KEY,"
            + "group_id VARCHAR(45) NOT NULL,"
            + "user_id VARCHAR(45) NOT NULL,"
            + "role VARCHAR(45) NOT NULL,"
            + "status VARCHAR(45) NOT NULL,"
            + "created_at TIMESTAMP NOT NULL);";
    private static final String CREATE_CHATS_TABLE_QUERY = "CREATE TABLE chats ( "
            + "id INTEGER PRIMARY KEY,"
            + "chat_id VARCHAR(45) NOT NULL,"
            + "type INTEGER NOT NULL,"
            + "name VARCHAR(45),"
            + "last_updated TIMESTAMP NOT NULL,"
            + "muted INTEGER NOT NULL,"  // 0 not muted 1 muted
            + "pending VARCHAR(45) NOT NULL,"
            + "last_message_id VARCHAR(45));";

    // Tables name
    public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_CALLS = "calls";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_GROUPS = "groups";
    public static final String TABLE_GROUP_MEMBERS = "group_members";
    public static final String TABLE_CHATS = "chats";

    // Columns name
    private static final String ID = "id";
    private static final String MESSAGE_ID = "message_id";
    private static final String MESSAGE_TYPE = "message_type";
    private static final String MESSAGE_BODY = "body";
    private static final String MESSAGE_MEDIA_REFERENCE = "media_reference";
    private static final String MESSAGE_MEDIA_TYPE = "media_type";
    private static final String MESSAGE_MEDIA_SIZE = "media_size";
    private static final String MESSAGE_SENDER_ID = "sender_id";
    private static final String MESSAGE_RECIPIENT_ID = "recipient_id";
    private static final String MESSAGE_DIRECTION = "direction";
    private static final String MESSAGE_STATUS = "status";
    private static final String MESSAGE_LOCAL_STATUS = "local_status";
    private static final String MESSAGE_MEDIA_STATUS = "media_status";
    private static final String MESSAGE_TIMESTAMP = "timestamp";

    private static final String CALL_ID = "call_id";
    private static final String CALL_REMOTE_USER_ID = "remote_user_id";
    private static final String CALL_START_TIME = "start_time";
    private static final String CALL_ESTABLISHED_TIME = "established_time";
    private static final String CALL_ENDED_TIME = "ended_time";
    private static final String CALL_ENDED_CAUSE = "call_ended_cause";
    private static final String CALL_VIDEO_OFFERED = "is_video_offered";
    private static final String CALL_DIRECTION = "call_direction";
    private static final String CALL_COUNT = "count";
    private static final String CALL_TIMESTAMP = "timestamp";


    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String DELETED_AT = "deleted_at";

    private static final String CONTACT_NAME                         = "name";
    private static final String CONTACT_MOBILE                       = "mobile";
    private static final String CONTACT_EMAIL                        = "email";
    private static final String CONTACT_USERNAME                     = "username";
    private static final String CONTACT_MOOD                         = "mood";
    private static final String CONTACT_GENDER                       = "gender";
    private static final String CONTACT_BIRTHDAY                     = "birthday";
    private static final String CONTACT_LAT                          = "lat";
    private static final String CONTACT_LNG                          = "lng";
    private static final String CONTACT_CITY                         = "city";
    private static final String CONTACT_COUNTRY                      = "country";
    private static final String CONTACT_STATUS                       = "status";
    private static final String CONTACT_DP_ID                        = "dp_id";

    private static final String GROUP_NAME                           = "name";
    private static final String GROUP_CREATED_BY                     = "created_by";
    private static final String GROUP_DP_ID                          = "dp_id";
    private static final String GROUP_STATUS                         = "status";
    private static final String GROUP_CREATED_AT                     = "created_at";

    private static final String GROUP_MEMBER_GROUP_ID                = "group_id";
    private static final String GROUP_MEMBER_USER_ID                 = "user_id";
    private static final String GROUP_MEMBER_ROLE                    = "role";
    private static final String GROUP_MEMBER_STATUS                  = "status";
    private static final String GROUP_MEMBER_CREATED_AT              = "created_at";

    private static final String CHAT_ID                              = "chat_id";
    private static final String CHAT_TYPE                            = "type";
    private static final String CHAT_NAME                            = "name";
    private static final String CHAT_LAST_UPDATED                    = "last_updated";
    private static final String CHAT_MUTED                            = "muted";
    private static final String CHAT_PENDING                         = "pending";
    private static final String CHAT_LAST_MESSAGE_ID                 = "last_message_id";

    private Context context;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqLiteDatabase;
    private static AtomicInteger mDbOpenCounter = new AtomicInteger(0);

    public DBManager(Context context) {
        this.context = context;
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
    }

    public synchronized DBManager openDB() {
        if (sqLiteDatabase == null){
            if (mDbOpenCounter.incrementAndGet() == 1) {
                sqLiteDatabase = databaseHelper.getWritableDatabase();
            }
            Log.d(TAG, "opening database, counter: " + mDbOpenCounter.get());
        }
        return this;
    }

    public synchronized void closeDB() {
        if (sqLiteDatabase != null) {
            // Close DB only if last user of DB..
            if (mDbOpenCounter.decrementAndGet() == 0) {
                // databaseHelper.close();
            }
        }
        Log.d(TAG, "closing database, counter: " + mDbOpenCounter.get());

    }

    /**
     * This is a private class which creates the database when the application
     * starts or upgrades it if it already exist by removing the last version of
     * the databases Create database .. and tables
     *
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "Creating the DB");
            db.execSQL(CREATE_MESSAGES_TABLE_QUERY);
            db.execSQL(CREATE_CALLS_TABLE_QUERY);
            db.execSQL(CREATE_CONTACTS_TABLE_QUERY);
            db.execSQL(CREATE_GROUPS_TABLE_QUERY);
            db.execSQL(CREATE_GROUP_MEMBERS_TABLE_QUERY);
            db.execSQL(CREATE_CHATS_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Updating DB from previous version " + oldVersion
                    + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALLS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_MEMBERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
            onCreate(db);

        }
    }

    /************************************
     * MESSAGES
     *************************************/

    @Override
    public void insertVoipMessage(VoipMessage message) {
        Log.d(TAG, "DB - insert message: " +message.toString());
        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, message.getMessageId());
        values.put(MESSAGE_TYPE, message.getMessageType());
        if (message.isHasMedia()){
            values.put(MESSAGE_MEDIA_STATUS, VoipMessage.MEDIA);
            values.put(MESSAGE_MEDIA_REFERENCE, message.getMediaReference());
            values.put(MESSAGE_MEDIA_TYPE, message.getVoipMessageBody().getVopipMessageMedia().getType().toString());
            values.put(MESSAGE_MEDIA_SIZE, message.getVoipMessageBody().getVopipMessageMedia().getSize());
        } else {
            values.put(MESSAGE_MEDIA_STATUS, VoipMessage.NO_MEDIA);
            values.put(MESSAGE_BODY, message.getVoipMessageBody().getTextBody());
        }
        values.put(MESSAGE_SENDER_ID, message.getSenderId());
        if (message.getRecipientIdsList().size() == 1){
            values.put(MESSAGE_RECIPIENT_ID, message.getRecipientIdsList().get(0));
        } else {
            values.put(MESSAGE_RECIPIENT_ID, message.getHeaders().get(VoipConstants.HEADER_GROUP_ID));
        }
        values.put(MESSAGE_DIRECTION, message.getDirection());
        values.put(MESSAGE_STATUS, message.getStatus());
        values.put(MESSAGE_LOCAL_STATUS, message.getLocalStatus());
        values.put(MESSAGE_TIMESTAMP, message.getTimeStamp());
        openDB();
        sqLiteDatabase.insert(TABLE_MESSAGES, null, values);
        closeDB();
    }

    @Override
    public void updatePendingVoipMessages(String senderId, String recipientId) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_LOCAL_STATUS, VoipMessage.SEEN);
        openDB();
        int i =  sqLiteDatabase.update(TABLE_MESSAGES, values, "(" + MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?"
                        + " OR " + MESSAGE_RECIPIENT_ID + "=?" + " AND " + MESSAGE_SENDER_ID + "=?" + ")",
                new String[] {String.valueOf(senderId), String.valueOf(recipientId), String.valueOf(senderId), String.valueOf(recipientId) });
        closeDB();
    }

    @Override
    public void updateVoipMessageStatus(int status, String messageId) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_STATUS, status);
        openDB();
        int i =  sqLiteDatabase.update(TABLE_MESSAGES, values, MESSAGE_ID + "=?",
                new String[] {String.valueOf(messageId)});
        closeDB();
    }

    @Override
    public List<VoipMessage> getVoipMessagesForSingleChat(String senderId, String recipientId) {
        List<VoipMessage> messages = new ArrayList<>();
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {
                        ID, MESSAGE_ID, MESSAGE_TYPE, MESSAGE_MEDIA_STATUS, MESSAGE_BODY, MESSAGE_MEDIA_REFERENCE,
                        MESSAGE_MEDIA_TYPE, MESSAGE_MEDIA_SIZE, MESSAGE_SENDER_ID, MESSAGE_RECIPIENT_ID,
                        MESSAGE_DIRECTION, MESSAGE_STATUS, MESSAGE_LOCAL_STATUS, MESSAGE_TIMESTAMP },
                MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?" + " OR " + MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?",
                new String[] {String.valueOf(senderId), String.valueOf(recipientId), String.valueOf(recipientId), String.valueOf(senderId)}, null,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                VoipMessage message = new VoipMessage();
                VoipMessageBody voipMessageBody;
                message.setMessageId(cursor.getString(1));
                message.setMessageType(cursor.getInt(2));
                int hasMedia = cursor.getInt(3);
                if (hasMedia == 0){
                    message.setHasMedia(false);
                    voipMessageBody = new VoipMessageBody();
                    voipMessageBody.setTextBody(cursor.getString(4));
                } else {
                    message.setHasMedia(true);
                    voipMessageBody = new VoipMessageBody();
                    message.setMediaReference(cursor.getString(5));
                    VopipMessageMedia vopipMessageMedia = new VopipMessageMedia();
                    String type = cursor.getString(6);
                    if (type.equals(VopipMessageMedia.ContentType.IMAGE.toString())) {
                        vopipMessageMedia.setType(VopipMessageMedia.ContentType.IMAGE);
                    }
                    vopipMessageMedia.setSize(cursor.getString(7));
                    voipMessageBody.setVopipMessageMedia(vopipMessageMedia);
                }
                message.setVoipMessageBody(voipMessageBody);
                message.setSenderId(cursor.getString(8));
                List<String> rList = new ArrayList<>();
                rList.add(cursor.getString(9));
                message.setRecipientIdsList(rList);
                message.setDirection(cursor.getInt(10));
                message.setStatus((cursor.getInt(11)));
                message.setLocalStatus((cursor.getInt(12)));
                message.setTimeStamp(cursor.getLong(13));
                messages.add(message);
            } while (cursor.moveToNext());
        }
        closeDB();
        return messages;
    }

    @Override
    public List<VoipMessage> getVoipMessagesForGroupChat(String groupId) {
        List<VoipMessage> messages = new ArrayList<>();
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {
                        ID, MESSAGE_ID, MESSAGE_TYPE, MESSAGE_MEDIA_STATUS, MESSAGE_BODY, MESSAGE_MEDIA_REFERENCE,
                        MESSAGE_MEDIA_TYPE, MESSAGE_MEDIA_SIZE, MESSAGE_SENDER_ID, MESSAGE_RECIPIENT_ID,
                        MESSAGE_DIRECTION, MESSAGE_STATUS, MESSAGE_LOCAL_STATUS,MESSAGE_TIMESTAMP },
                MESSAGE_RECIPIENT_ID + "=?",
                new String[] {groupId}, null,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                VoipMessage message = new VoipMessage();
                VoipMessageBody voipMessageBody;
                message.setMessageId(cursor.getString(1));
                message.setMessageType(cursor.getInt(2));
                int hasMedia = cursor.getInt(3);
                if (hasMedia == 0){
                    message.setHasMedia(false);
                    voipMessageBody = new VoipMessageBody();
                    voipMessageBody.setTextBody(cursor.getString(4));
                } else {
                    message.setHasMedia(true);
                    voipMessageBody = new VoipMessageBody();
                    message.setMediaReference(cursor.getString(5));
                    VopipMessageMedia vopipMessageMedia = new VopipMessageMedia();
                    String type = cursor.getString(6);
                    if (type.equals(VopipMessageMedia.ContentType.IMAGE.toString())) {
                        vopipMessageMedia.setType(VopipMessageMedia.ContentType.IMAGE);
                    }
                    vopipMessageMedia.setSize(cursor.getString(7));
                    voipMessageBody.setVopipMessageMedia(vopipMessageMedia);
                }
                message.setVoipMessageBody(voipMessageBody);
                message.setSenderId(cursor.getString(8));
                List<String> rList = new ArrayList<>();
                rList.add(cursor.getString(9));
                message.setRecipientIdsList(rList);
                message.setDirection(cursor.getInt(10));
                message.setStatus((cursor.getInt(11)));
                message.setLocalStatus((cursor.getInt(12)));
                message.setTimeStamp(cursor.getLong(13));
                messages.add(message);
            } while (cursor.moveToNext());
        }
        closeDB();
        return messages;
    }

    @Override
    public VoipMessage getVoipMessageFromMessageId(String messageId) {
        VoipMessage message = null;
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {
                        ID, MESSAGE_ID, MESSAGE_TYPE, MESSAGE_MEDIA_STATUS, MESSAGE_BODY, MESSAGE_MEDIA_REFERENCE, MESSAGE_MEDIA_TYPE,
                        MESSAGE_MEDIA_SIZE, MESSAGE_SENDER_ID, MESSAGE_RECIPIENT_ID,
                        MESSAGE_DIRECTION, MESSAGE_STATUS,MESSAGE_LOCAL_STATUS, MESSAGE_TIMESTAMP },
                MESSAGE_ID + "=?",
                new String[] {messageId}, null,
                null, null);

        if (cursor.moveToFirst()) {
            message = new VoipMessage();
            VoipMessageBody voipMessageBody;
            message.setMessageId(cursor.getString(1));
            message.setMessageType(cursor.getInt(2));
            int hasMedia = cursor.getInt(3);
            if (hasMedia == 0){
                message.setHasMedia(false);
                voipMessageBody = new VoipMessageBody();
                voipMessageBody.setTextBody(cursor.getString(4));
            } else {
                message.setHasMedia(true);
                voipMessageBody = new VoipMessageBody();
                message.setMediaReference(cursor.getString(5));
                VopipMessageMedia vopipMessageMedia = new VopipMessageMedia();
                String type = cursor.getString(6);
                if (type.equals(VopipMessageMedia.ContentType.IMAGE.toString())) {
                    vopipMessageMedia.setType(VopipMessageMedia.ContentType.IMAGE);
                }
                vopipMessageMedia.setSize(cursor.getString(7));
                voipMessageBody.setVopipMessageMedia(vopipMessageMedia);
            }
            message.setVoipMessageBody(voipMessageBody);
            message.setSenderId(cursor.getString(8));
            List<String> rList = new ArrayList<>();
            rList.add(cursor.getString(9));
            message.setRecipientIdsList(rList);
            message.setDirection(cursor.getInt(10));
            message.setStatus((cursor.getInt(11)));
            message.setLocalStatus((cursor.getInt(12)));
            message.setTimeStamp(cursor.getLong(13));
        }
        closeDB();
        return message;
    }

    @Override
    public int getPendingVoipMessagesForSingleChat(String senderId, String recipientId) {
        int count=0;
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {MESSAGE_LOCAL_STATUS},
                MESSAGE_LOCAL_STATUS + "=?" + " AND " + MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?"
                        + " OR " +
                        MESSAGE_LOCAL_STATUS + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?" + " AND " + MESSAGE_SENDER_ID + "=?",
                new String[] {String.valueOf(VoipMessage.NOT_SEEN),String.valueOf(senderId), String.valueOf(recipientId),String.valueOf(VoipMessage.NOT_SEEN), String.valueOf(senderId), String.valueOf(recipientId)}, null,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        closeDB();
        return count;
    }

    @Override
    public int getPendingVoipMessagesForGroupChat(String groupId) {
        int count=0;
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {MESSAGE_LOCAL_STATUS},
                MESSAGE_LOCAL_STATUS + "=?" + " AND " + MESSAGE_RECIPIENT_ID,
                new String[] {String.valueOf(VoipMessage.NOT_SEEN),String.valueOf(groupId)}, null,
                null, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        closeDB();
        return count;
    }

    @Override
    public int getCountVoipMessagesForSingleChat(String senderId, String recipientId) {
        int count=0;
        openDB();
        String selectQuery = "select  * from " + TABLE_MESSAGES + " where " +
                MESSAGE_SENDER_ID + "='" + senderId + "' AND " + MESSAGE_RECIPIENT_ID + "='" + recipientId
                + "' OR " + MESSAGE_SENDER_ID + "='" + recipientId + "' AND " + MESSAGE_RECIPIENT_ID + "='" + senderId + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        closeDB();
        return count;
    }

    @Override
    public int getCountVoipMessagesForGroupChat(String groupId) {
        int count=0;
        openDB();
        String selectQuery = "select  * from " + TABLE_MESSAGES + " where " +
                MESSAGE_RECIPIENT_ID + "='" + groupId + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        closeDB();
        return count;
    }


    /************************************
     * MESSAGE MEDIA
     *************************************/


    @Override
    public VoipMediaBundle getVoipMediaBundleForSingleChat(String senderId, String recipientId) {
        VoipMediaBundle voipMediaBundle = new VoipMediaBundle();
        List<ImageMedia> images = new ArrayList<>();
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {MESSAGE_MEDIA_TYPE, MESSAGE_MEDIA_REFERENCE, MESSAGE_MEDIA_SIZE, MESSAGE_TIMESTAMP},
                MESSAGE_MEDIA_STATUS + "=?" + " AND " + MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?"
                        + " OR " +
                        MESSAGE_MEDIA_STATUS + "=?" + " AND " + MESSAGE_SENDER_ID + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?",
                new String[] {String.valueOf(VoipMessage.MEDIA), String.valueOf(senderId), String.valueOf(recipientId), String.valueOf(VoipMessage.MEDIA),  String.valueOf(recipientId), String.valueOf(senderId)}, null,
                null, null);

        if (cursor.getCount() == 0){
            voipMediaBundle = null;
            return voipMediaBundle;
        }
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equalsIgnoreCase(VopipMessageMedia.ContentType.IMAGE.toString())){
                    images.add(new ImageMedia(cursor.getString(1) ,cursor.getString(1), cursor.getString(2), cursor.getLong(3)));
                }
            } while (cursor.moveToNext());
        }
        voipMediaBundle.setImages(images);
        closeDB();
        return voipMediaBundle;
    }

    @Override
    public VoipMediaBundle getVoipMediaBundleForGroupChat(String groupId) {
        VoipMediaBundle voipMediaBundle = new VoipMediaBundle();
        List<ImageMedia> images = new ArrayList<>();
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {MESSAGE_MEDIA_TYPE, MESSAGE_MEDIA_REFERENCE, MESSAGE_MEDIA_SIZE, MESSAGE_TIMESTAMP},
                MESSAGE_MEDIA_STATUS + "=?" + " AND " + MESSAGE_RECIPIENT_ID + "=?",
                new String[] {String.valueOf(VoipMessage.MEDIA), String.valueOf(groupId)}, null,
                null, null);

        if (cursor.getCount() == 0){
            voipMediaBundle = null;
            return voipMediaBundle;
        }
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equalsIgnoreCase(VopipMessageMedia.ContentType.IMAGE.toString())){
                    images.add(new ImageMedia(cursor.getString(1) ,cursor.getString(1), cursor.getString(2), cursor.getLong(3)));
                }
            } while (cursor.moveToNext());
        }
        voipMediaBundle.setImages(images);
        closeDB();
        return voipMediaBundle;
    }

    @Override
    public VoipMediaBundle getAllVoipMediaBundle() {
        VoipMediaBundle voipMediaBundle = new VoipMediaBundle();
        List<ImageMedia> images = new ArrayList<>();
        openDB();
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGES, new String[] {MESSAGE_MEDIA_TYPE, MESSAGE_MEDIA_REFERENCE, MESSAGE_MEDIA_SIZE, MESSAGE_TIMESTAMP},
                MESSAGE_MEDIA_STATUS + "=?",
                new String[] {String.valueOf(VoipMessage.MEDIA)}, null,
                null, null);

        if (cursor.getCount() == 0){
            voipMediaBundle = null;
            return voipMediaBundle;
        }
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equalsIgnoreCase(VopipMessageMedia.ContentType.IMAGE.toString())){
                    images.add(new ImageMedia(cursor.getString(1) ,cursor.getString(1), cursor.getString(2), cursor.getLong(3)));
                }
            } while (cursor.moveToNext());
        }
        voipMediaBundle.setImages(images);
        closeDB();
        return voipMediaBundle;
    }


    /************************************
     * CONTACTS
     *************************************/
    @Override
    public void insertVoipContact(VoipContact contact){
        Log.d(TAG, "DB - insert contact");
        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(CONTACT_NAME, contact.getName());
        values.put(CONTACT_MOBILE, contact.getMobileNumber());
        values.put(CONTACT_EMAIL, contact.getEmail());
        values.put(CONTACT_USERNAME, contact.getUserName());
        values.put(CONTACT_MOOD, contact.getMood());
        values.put(CONTACT_GENDER, contact.getGender());
        values.put(CONTACT_BIRTHDAY, contact.getDob());
        values.put(CONTACT_LAT, contact.getLat());
        values.put(CONTACT_LNG, contact.getLng());
        values.put(CONTACT_CITY, contact.getCity());
        values.put(CONTACT_COUNTRY, contact.getCountry());
        values.put(CONTACT_STATUS, contact.getStatus());
        values.put(CONTACT_DP_ID, contact.getDpId());

        openDB();
        if (!isExist(contact.getId(), TABLE_CONTACTS)){
            sqLiteDatabase.insert(TABLE_CONTACTS, null, values);
        } else {
            sqLiteDatabase.update(TABLE_CONTACTS, values, ID + "=" + contact.getId(), null);
        }
        closeDB();
    }

    @Override
    public List<VoipContact> getVoipContacts() {
        List<VoipContact> voipContactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select  * from " + TABLE_CONTACTS;
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VoipContact voipContact = new VoipContact();
                voipContact.setId(cursor.getString(0));
                voipContact.setName(cursor.getString(1));
                voipContact.setMobileNumber(cursor.getString(2));
                voipContact.setEmail(cursor.getString(3));
                voipContact.setUserName(cursor.getString(4));
                voipContact.setMood(cursor.getString(5));
                voipContact.setGender(cursor.getString(6));
                voipContact.setDob(cursor.getString(7));
                voipContact.setLat(cursor.getString(8));
                voipContact.setLng(cursor.getString(9));
                voipContact.setCity(cursor.getString(10));
                voipContact.setCountry(cursor.getString(11));
                voipContact.setStatus(cursor.getString(12));
                voipContact.setDpId(cursor.getString(13));
                voipContactList.add(voipContact);
            } while (cursor.moveToNext());
        }
        closeDB();
        return voipContactList;
    }

    @Override
    public VoipContact getVoipContactFromId(String id) {
        VoipContact voipContact = new VoipContact();
        String selectQuery = "select  * from " + TABLE_CONTACTS + " where " + ID + " ='" + id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            voipContact.setId(cursor.getString(0));
            voipContact.setName(cursor.getString(1));
            voipContact.setMobileNumber(cursor.getString(2));
            voipContact.setEmail(cursor.getString(3));
            voipContact.setUserName(cursor.getString(4));
            voipContact.setMood(cursor.getString(5));
            voipContact.setGender(cursor.getString(6));
            voipContact.setDob(cursor.getString(7));
            voipContact.setLat(cursor.getString(8));
            voipContact.setLng(cursor.getString(9));
            voipContact.setCity(cursor.getString(10));
            voipContact.setCountry(cursor.getString(11));
            voipContact.setStatus(cursor.getString(12));
            voipContact.setDpId(cursor.getString(13));
        }
        closeDB();
        return voipContact;
    }

    @Override
    public VoipContact getVoipContactFromUserNameOrMobile(String id) {
        VoipContact voipContact = new VoipContact();
        String selectQuery = "select  * from " + TABLE_CONTACTS + " where " + CONTACT_USERNAME+ " ='" + id  // FIXME we should find a better solution for this
                + "' OR " + CONTACT_MOBILE+ " ='" + id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            voipContact.setId(cursor.getString(0));
            voipContact.setName(cursor.getString(1));
            voipContact.setMobileNumber(cursor.getString(2));
            voipContact.setEmail(cursor.getString(3));
            voipContact.setUserName(cursor.getString(4));
            voipContact.setMood(cursor.getString(5));
            voipContact.setGender(cursor.getString(6));
            voipContact.setDob(cursor.getString(7));
            voipContact.setLat(cursor.getString(8));
            voipContact.setLng(cursor.getString(9));
            voipContact.setCity(cursor.getString(10));
            voipContact.setCountry(cursor.getString(11));
            voipContact.setStatus(cursor.getString(12));
            voipContact.setDpId(cursor.getString(13));
        }
        closeDB();
        return voipContact;
    }

    public String getVoipContactName(String id) {
        String name = null;
        openDB();
        String selectQuery = "select  * from " + TABLE_CONTACTS + " where " + CONTACT_USERNAME+ " ='" + id  // FIXME we should find a better solution for this
                + "' OR " + CONTACT_MOBILE+ " ='" + id + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
        }
        closeDB();
        return name;
    }

    /************************************
     * CALLS
     *************************************/
    @Override
    public void insertVoipCall(VoipCall voipCall) {
        ContentValues values = new ContentValues();
        values.put(CALL_ID, voipCall.getCallId());
        values.put(CALL_REMOTE_USER_ID, voipCall.getRemoteUserId());
        values.put(CALL_START_TIME, voipCall.getStartTime());
        values.put(CALL_ESTABLISHED_TIME, voipCall.getEstTime());
        values.put(CALL_ENDED_TIME, voipCall.getEndTime());
        values.put(CALL_ENDED_CAUSE, voipCall.getCallEndCause());
        values.put(CALL_VIDEO_OFFERED, voipCall.getIsVideoOffered());
        values.put(CALL_DIRECTION, voipCall.getCallDirection());
        values.put(CALL_COUNT, voipCall.getCount());
        values.put(CALL_TIMESTAMP, voipCall.getTimeStamp());
        openDB();
        sqLiteDatabase.insert(TABLE_CALLS, null, values);
        closeDB();
    }

    @Override
    public List<VoipCall> getVoipCalls() {
        List<VoipCall> voipCalls = new ArrayList<>();
        // Select All Query
        String selectQuery = "select  * from " + TABLE_CALLS + " order by " + CALL_TIMESTAMP + " DESC ";;
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VoipCall voipCall = new VoipCall();
                voipCall.setCallId(cursor.getString(1));
                voipCall.setRemoteUserId(cursor.getString(2));
                voipCall.setStartTime(cursor.getLong(3));
                voipCall.setEstTime(cursor.getLong(4));
                voipCall.setEndTime(cursor.getLong(5));
                voipCall.setCallEndCause(cursor.getInt(6));
                voipCall.setIsVideoOffered(cursor.getInt(7));
                voipCall.setCallDirection(cursor.getInt(8));
                voipCall.setCount(cursor.getInt(9));
                voipCall.setTimeStamp(cursor.getLong(10));
                voipCalls.add(voipCall);
            } while (cursor.moveToNext());
        }
        closeDB();
        return voipCalls;
    }

    /************************************
     * GROUPS AND GROUPS MEMBERS
     *************************************/

    @Override
    public void insertVoipGroup(VoipGroup voipGroup) {
        Log.d(TAG, "DB - insert group");
        ContentValues values = new ContentValues();
        values.put(ID, voipGroup.getId());
        values.put(GROUP_NAME, voipGroup.getName());
        values.put(GROUP_CREATED_BY, voipGroup.getCreatedBy());
        values.put(GROUP_DP_ID, voipGroup.getDpId());
        values.put(GROUP_STATUS, voipGroup.getStatus());
        values.put(GROUP_CREATED_AT, voipGroup.getCreatedAt());

        openDB();
        if (!isExist(voipGroup.getId(), TABLE_GROUPS)){
            sqLiteDatabase.insert(TABLE_GROUPS, null, values);
        } else {
            sqLiteDatabase.update(TABLE_GROUPS, values, ID + "=" + voipGroup.getId(), null);
        }
        closeDB();
    }

    @Override
    public void insertVoipGroupMember(VoipGroupMember groupMember) {
        Log.d(TAG, "DB - insert members");
        ContentValues values = new ContentValues();
        values.put(ID, groupMember.getMemberId());
        values.put(GROUP_MEMBER_GROUP_ID, groupMember.getGroupId());
        values.put(GROUP_MEMBER_USER_ID, groupMember.getUserID());
        values.put(GROUP_MEMBER_ROLE, groupMember.getRole());
        values.put(GROUP_MEMBER_STATUS, groupMember.getStatus());
        values.put(GROUP_MEMBER_CREATED_AT, groupMember.getCreatedAt());

        openDB();
        if (!isExist(groupMember.getMemberId(), TABLE_GROUP_MEMBERS)){
            sqLiteDatabase.insert(TABLE_GROUP_MEMBERS, null, values);
        } else {
            sqLiteDatabase.update(TABLE_GROUP_MEMBERS, values, ID + "=" + groupMember.getMemberId(), null);
        }
        closeDB();
    }

    @Override
    public VoipGroup getVoipGroupFromId(String id) {
        VoipGroup voipGroup = new VoipGroup();
        openDB();
        String selectQuery = "select  * from " + TABLE_GROUPS + " where " + ID + " ='" + id + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            voipGroup.setId(cursor.getString(0));
            voipGroup.setName(cursor.getString(1));
            voipGroup.setCreatedBy(cursor.getString(2));
            voipGroup.setDpId(cursor.getString(3));
            voipGroup.setStatus(cursor.getString(4));
            voipGroup.setCreatedAt(cursor.getLong(5));
        }

        voipGroup.setMemberList(getGroupMembersFromGroupId(id));
        closeDB();
        return voipGroup;
    }

    @Override
    public List<VoipGroupMember> getGroupMembersFromGroupId(String gId) {
        List<VoipGroupMember> voipGroupMembers = new ArrayList<>();
        String selectQuery = "select  * from " + TABLE_GROUP_MEMBERS + " where " + GROUP_MEMBER_GROUP_ID + " ='" + gId + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            VoipGroupMember member = new VoipGroupMember();
            member.setMemberId(cursor.getString(0));
            member.setGroupId(cursor.getString(1));
            member.setUserID(cursor.getString(2));
            member.setRole(cursor.getString(3));
            member.setStatus(cursor.getString(4));
            member.setCreatedAt(cursor.getLong(5));
            voipGroupMembers.add(member);
        }
        closeDB();
        return voipGroupMembers;
    }

    @Override
    public String getVoipGroupName(String groupId) {
        String name = null;
        openDB();
        String selectQuery = "select  * from " + TABLE_GROUPS + " where " + ID+ " ='" + groupId + "'";;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
        }
        closeDB();
        return name;
    }

    /************************************
     * CHATS
     *************************************/

    @Override
    public void insertVoipChat(VoipMessage message){
        String chatId;
        int chatType;
        ContentValues values = new ContentValues();
        if (message.getRecipientIdsList().size() == 1){
            if (message.getDirection() == VoipMessage.DIRECTION_INCOMING){
                chatId = message.getSenderId();
            } else {
                chatId = message.getRecipientIdsList().get(0);
            }
            chatType = VoipChat.ONE_TO_ONE;
            values.put(CHAT_ID, chatId);
            values.put(CHAT_NAME, getVoipContactName(chatId));
            values.put(CHAT_TYPE, chatType);
            values.put(CHAT_PENDING, getPendingVoipMessagesForSingleChat(message.getSenderId(), message.getRecipientIdsList().get(0)));
        } else {
            chatId = message.getHeaders().get(VoipConstants.HEADER_GROUP_ID);
            chatType = VoipChat.GROUP;
            values.put(CHAT_ID, chatId);
            values.put(CHAT_NAME, getVoipGroupName(chatId));
            values.put(CHAT_TYPE, chatType);
            values.put(CHAT_PENDING, getPendingVoipMessagesForGroupChat(chatId));
        }
        values.put(CHAT_LAST_UPDATED, message.getTimeStamp());
        values.put(CHAT_MUTED, VoipChat.NOT_MUTED);
        values.put(CHAT_LAST_MESSAGE_ID, message.getMessageId());

        openDB();
        if (!isChatExist(chatId)){
            sqLiteDatabase.insert(TABLE_CHATS, null, values);
        } else {
            sqLiteDatabase.update(TABLE_CHATS, values, CHAT_ID + "=" + chatId, null);
        }
        closeDB();
    }

    @Override
    public void insertVoipGroupChat(VoipGroup group){
        String chatId;
        ContentValues values = new ContentValues();
        chatId = group.getId();
        values.put(CHAT_ID, chatId);
        values.put(CHAT_NAME, getVoipGroupName(chatId));
        values.put(CHAT_TYPE, VoipChat.GROUP);
        values.put(CHAT_PENDING, getPendingVoipMessagesForGroupChat(chatId));
        values.put(CHAT_LAST_UPDATED, group.getCreatedAt());
        values.put(CHAT_MUTED, VoipChat.NOT_MUTED);
        openDB();
        if (!isChatExist(chatId)){
            sqLiteDatabase.insert(TABLE_CHATS, null, values);
        } else {
            sqLiteDatabase.update(TABLE_CHATS, values, ID + "=" + chatId, null);
        }
        //values.put(CHAT_LAST_MESSAGE_ID, null);
        closeDB();
    }

    @Override
    public void updatePendingMessagesVoipChat(String chatId) {
        ContentValues values = new ContentValues();
        values.put(CHAT_PENDING, 0);
        openDB();
        int i =  sqLiteDatabase.update(TABLE_CHATS, values, CHAT_ID + "=?",
                new String[] {String.valueOf(chatId)});
        closeDB();
    }

    @Override
    public List<VoipChat> getVoipChats() {
        List<VoipChat> chats = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CHATS
                + " order by " + CHAT_LAST_UPDATED + " DESC ";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // FIXME we need to remove this check later
                int chatType = cursor.getInt(2);
                String chatId = cursor.getString(1);
                switch (chatType){
                    case VoipChat.ONE_TO_ONE:
                        if (!isContactExist(chatId)){ // FIXME Even if we delete the contact we will still receive msgs from sinch
                            continue;
                        }
                        break;
                    case VoipChat.GROUP:
                        if (!isGroupExist(chatId)){   // FIXME Even if we delete the group we will still receive msgs from sinch
                            continue;
                        }
                        break;
                }

                VoipChat voipChat = new VoipChat();
                voipChat.setId(cursor.getString(0));
                voipChat.setChatId(cursor.getString(1));
                voipChat.setType(cursor.getInt(2));
                voipChat.setName(cursor.getString(3));
                voipChat.setLastUpdated(cursor.getLong(4));
                voipChat.setMuted(cursor.getInt(5));
                voipChat.setPending(cursor.getInt(6));
                if (cursor.getString(7) != null){
                    voipChat.setLastMessage(getVoipMessageFromMessageId(cursor.getString(7)));
                }
                chats.add(voipChat);
            } while (cursor.moveToNext());
        }
        closeDB();
        return chats;
    }

    @Override
    public int getVoipChatType(String userName) {
        int type = 0;
        openDB();
        String selectQuery = "select  * from " + TABLE_CHATS + " where " + CHAT_ID+ " ='" + userName + "'";;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            type = cursor.getInt(2);
        }
        closeDB();
        return type;
    }

    /************************************
     * UTILS
     *************************************/

    @Override
    public boolean isExist(String id, String tableName) {
        String selectQuery = "select  * from " + tableName + " WHERE id = '" + id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        closeDB();
        return exist;
    }

    @Override
    public boolean isContactExist(String id) {
        String selectQuery = "select  * from " + TABLE_CONTACTS + " where " + CONTACT_USERNAME+ " ='" + id
                + "' OR " + CONTACT_MOBILE+ " ='" + id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        closeDB();
        return exist;
    }

    @Override
    public boolean isGroupExist(String id) {
        String selectQuery = "select  * from " + TABLE_GROUPS + " where " + ID+ " ='"  + id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        closeDB();
        return exist;
    }

    @Override
    public boolean isChatExist(String chat_id) {
        String selectQuery = "select * from " + TABLE_CHATS + " WHERE chat_id = '" + chat_id + "'";
        openDB();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        closeDB();
        return exist;
    }
}
