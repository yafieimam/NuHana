package com.yafieimam.nuhanaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YAFIE IMAM A on 1/18/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dataset_aksara_jawa";

    // Tables name
    private static final String TABLE_AKSARA_JAWA = "aksara_jawa";
    private static final String TABLE_DATASET = "dataset_aksara_jawa";
    private static final String TABLE_FEATURE = "fitur_dataset";

    // Tables Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AKSARA_JAWA = "nama_aksara_jawa";
    private static final String KEY_ID_AKSARA_JAWA = "id_aksara_jawa";
    private static final String KEY_ID_DATASET = "id_dataset";

    private static final String CREATE_AKSARA_JAWA_TABLE = "CREATE TABLE " + TABLE_AKSARA_JAWA + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AKSARA_JAWA + " TEXT" + ")";

    private static final  String CREATE_DATASET_TABLE = "CREATE TABLE " + TABLE_DATASET + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_AKSARA_JAWA + " INTEGER" + ")";

    private static final  String CREATE_FEATURE_TABLE = "CREATE TABLE " + TABLE_FEATURE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_AKSARA_JAWA + " INTEGER," + KEY_ID_DATASET + " INTEGER" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AKSARA_JAWA_TABLE);
        db.execSQL(CREATE_DATASET_TABLE);
        db.execSQL(CREATE_FEATURE_TABLE);
        for(int i = 0; i < 100; i++){
            db.execSQL("ALTER TABLE " + TABLE_FEATURE + " ADD COLUMN feature" + (i+1) + " INTEGER DEFAULT 0");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AKSARA_JAWA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new aksara jawa
    public void addAksaraJawa(AksaraJawa aksara_jawa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AKSARA_JAWA, aksara_jawa.getAksaraJawa()); // Aksara Jawa Name

        // Inserting Row
        db.insert(TABLE_AKSARA_JAWA, null, values);
        db.close(); // Closing database connection
    }

    // Adding new dataset
    public long addDataset(long id_aksara_jawa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_AKSARA_JAWA, id_aksara_jawa);

        long id = db.insert(TABLE_DATASET, null, values);
        db.close(); // Closing database connection

        return id;
    }

    public long addFeature(int dataset[], int id_aksara_jawa, int id_dataset) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_AKSARA_JAWA, id_aksara_jawa);
        values.put(KEY_ID_DATASET, id_dataset);
        for(int i = 0; i< 100; i++){
            values.put("feature" + (i+1), dataset[i]); // Aksara Jawa Name
        }
        long id = db.insert(TABLE_FEATURE, null, values);
        db.close(); // Closing database connection

        return id;
    }

    // Getting single aksara jawa
    public AksaraJawa getAksaraJawa(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AKSARA_JAWA, new String[] { KEY_ID,
                        KEY_AKSARA_JAWA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AksaraJawa aksara_jawa = new AksaraJawa(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return aksara_jawa
        return aksara_jawa;
    }

    // Getting single aksara jawa
    public AksaraJawa getIDAksaraJawa(String aksara_jawa_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AKSARA_JAWA, new String[] { KEY_ID,
                        KEY_AKSARA_JAWA }, KEY_AKSARA_JAWA + "=?",
                new String[] { String.valueOf(aksara_jawa_name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AksaraJawa aksara_jawa = new AksaraJawa(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return aksara_jawa
        return aksara_jawa;
    }

    public DatasetAksaraJawa getFeature(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_DATASET + " WHERE " + KEY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});
        if (cursor != null)
            cursor.moveToFirst();

        DatasetAksaraJawa dataset_aksara_jawa = new DatasetAksaraJawa();
        dataset_aksara_jawa.setID(Integer.parseInt(cursor.getString(0)));
        dataset_aksara_jawa.setIDAksaraJawa(Integer.parseInt(cursor.getString(1)));
        dataset_aksara_jawa.setIDDataset(Integer.parseInt(cursor.getString(2)));
        dataset_aksara_jawa.setFeature1(cursor.getString(3));
        dataset_aksara_jawa.setFeature2(cursor.getString(4));
        dataset_aksara_jawa.setFeature3(cursor.getString(5));
        dataset_aksara_jawa.setFeature4(cursor.getString(6));
        dataset_aksara_jawa.setFeature5(cursor.getString(7));
        dataset_aksara_jawa.setFeature6(cursor.getString(8));
        dataset_aksara_jawa.setFeature7(cursor.getString(9));
        dataset_aksara_jawa.setFeature8(cursor.getString(10));
        dataset_aksara_jawa.setFeature9(cursor.getString(11));
        dataset_aksara_jawa.setFeature10(cursor.getString(12));
        dataset_aksara_jawa.setFeature11(cursor.getString(13));
        dataset_aksara_jawa.setFeature12(cursor.getString(14));
        dataset_aksara_jawa.setFeature13(cursor.getString(15));
        dataset_aksara_jawa.setFeature14(cursor.getString(16));
        dataset_aksara_jawa.setFeature15(cursor.getString(17));
        dataset_aksara_jawa.setFeature16(cursor.getString(18));
        dataset_aksara_jawa.setFeature17(cursor.getString(19));
        dataset_aksara_jawa.setFeature18(cursor.getString(20));
        dataset_aksara_jawa.setFeature19(cursor.getString(21));
        dataset_aksara_jawa.setFeature20(cursor.getString(22));
        dataset_aksara_jawa.setFeature21(cursor.getString(23));
        dataset_aksara_jawa.setFeature22(cursor.getString(24));
        dataset_aksara_jawa.setFeature23(cursor.getString(25));
        dataset_aksara_jawa.setFeature24(cursor.getString(26));
        dataset_aksara_jawa.setFeature25(cursor.getString(27));
        dataset_aksara_jawa.setFeature26(cursor.getString(28));
        dataset_aksara_jawa.setFeature27(cursor.getString(29));
        dataset_aksara_jawa.setFeature28(cursor.getString(30));
        dataset_aksara_jawa.setFeature29(cursor.getString(31));
        dataset_aksara_jawa.setFeature30(cursor.getString(32));
        dataset_aksara_jawa.setFeature31(cursor.getString(33));
        dataset_aksara_jawa.setFeature32(cursor.getString(34));
        dataset_aksara_jawa.setFeature33(cursor.getString(35));
        dataset_aksara_jawa.setFeature34(cursor.getString(36));
        dataset_aksara_jawa.setFeature35(cursor.getString(37));
        dataset_aksara_jawa.setFeature36(cursor.getString(38));
        dataset_aksara_jawa.setFeature37(cursor.getString(39));
        dataset_aksara_jawa.setFeature38(cursor.getString(40));
        dataset_aksara_jawa.setFeature39(cursor.getString(41));
        dataset_aksara_jawa.setFeature40(cursor.getString(42));
        dataset_aksara_jawa.setFeature41(cursor.getString(43));
        dataset_aksara_jawa.setFeature42(cursor.getString(44));
        dataset_aksara_jawa.setFeature43(cursor.getString(45));
        dataset_aksara_jawa.setFeature44(cursor.getString(46));
        dataset_aksara_jawa.setFeature45(cursor.getString(47));
        dataset_aksara_jawa.setFeature46(cursor.getString(48));
        dataset_aksara_jawa.setFeature47(cursor.getString(49));
        dataset_aksara_jawa.setFeature48(cursor.getString(50));
        dataset_aksara_jawa.setFeature49(cursor.getString(51));
        dataset_aksara_jawa.setFeature50(cursor.getString(52));
        dataset_aksara_jawa.setFeature51(cursor.getString(53));
        dataset_aksara_jawa.setFeature52(cursor.getString(54));
        dataset_aksara_jawa.setFeature53(cursor.getString(55));
        dataset_aksara_jawa.setFeature54(cursor.getString(56));
        dataset_aksara_jawa.setFeature55(cursor.getString(57));
        dataset_aksara_jawa.setFeature56(cursor.getString(58));
        dataset_aksara_jawa.setFeature57(cursor.getString(59));
        dataset_aksara_jawa.setFeature58(cursor.getString(60));
        dataset_aksara_jawa.setFeature59(cursor.getString(61));
        dataset_aksara_jawa.setFeature60(cursor.getString(62));
        dataset_aksara_jawa.setFeature61(cursor.getString(63));
        dataset_aksara_jawa.setFeature62(cursor.getString(64));
        dataset_aksara_jawa.setFeature63(cursor.getString(65));
        dataset_aksara_jawa.setFeature64(cursor.getString(66));
        dataset_aksara_jawa.setFeature65(cursor.getString(67));
        dataset_aksara_jawa.setFeature66(cursor.getString(68));
        dataset_aksara_jawa.setFeature67(cursor.getString(69));
        dataset_aksara_jawa.setFeature68(cursor.getString(70));
        dataset_aksara_jawa.setFeature69(cursor.getString(71));
        dataset_aksara_jawa.setFeature70(cursor.getString(72));
        dataset_aksara_jawa.setFeature71(cursor.getString(73));
        dataset_aksara_jawa.setFeature72(cursor.getString(74));
        dataset_aksara_jawa.setFeature73(cursor.getString(75));
        dataset_aksara_jawa.setFeature74(cursor.getString(76));
        dataset_aksara_jawa.setFeature75(cursor.getString(77));
        dataset_aksara_jawa.setFeature76(cursor.getString(78));
        dataset_aksara_jawa.setFeature77(cursor.getString(79));
        dataset_aksara_jawa.setFeature78(cursor.getString(80));
        dataset_aksara_jawa.setFeature79(cursor.getString(81));
        dataset_aksara_jawa.setFeature80(cursor.getString(82));
        dataset_aksara_jawa.setFeature81(cursor.getString(83));
        dataset_aksara_jawa.setFeature82(cursor.getString(84));
        dataset_aksara_jawa.setFeature83(cursor.getString(85));
        dataset_aksara_jawa.setFeature84(cursor.getString(86));
        dataset_aksara_jawa.setFeature85(cursor.getString(87));
        dataset_aksara_jawa.setFeature86(cursor.getString(88));
        dataset_aksara_jawa.setFeature87(cursor.getString(89));
        dataset_aksara_jawa.setFeature88(cursor.getString(90));
        dataset_aksara_jawa.setFeature89(cursor.getString(91));
        dataset_aksara_jawa.setFeature90(cursor.getString(92));
        dataset_aksara_jawa.setFeature91(cursor.getString(93));
        dataset_aksara_jawa.setFeature92(cursor.getString(94));
        dataset_aksara_jawa.setFeature93(cursor.getString(95));
        dataset_aksara_jawa.setFeature94(cursor.getString(96));
        dataset_aksara_jawa.setFeature95(cursor.getString(97));
        dataset_aksara_jawa.setFeature96(cursor.getString(98));
        dataset_aksara_jawa.setFeature97(cursor.getString(99));
        dataset_aksara_jawa.setFeature98(cursor.getString(100));
        dataset_aksara_jawa.setFeature99(cursor.getString(101));
        dataset_aksara_jawa.setFeature100(cursor.getString(102));
        // return dataset_aksara_jawa
        return dataset_aksara_jawa;
    }

    // Getting All Aksara Jawa
    public List<AksaraJawa> getAllAksaraJawa() {
        List<AksaraJawa> aksaraJawaList = new ArrayList<AksaraJawa>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_AKSARA_JAWA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AksaraJawa aksara_jawa = new AksaraJawa();
                aksara_jawa.setID(Integer.parseInt(cursor.getString(0)));
                aksara_jawa.setAksaraJawa(cursor.getString(1));
                // Adding contact to list
                aksaraJawaList.add(aksara_jawa);
            } while (cursor.moveToNext());
        }

        // return aksara jawa list
        return aksaraJawaList;
    }

    // Getting All Dataset
    public List<DatasetAksaraJawa> getAllDataset() {
        List<DatasetAksaraJawa> aksaraJawaList = new ArrayList<DatasetAksaraJawa>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DATASET;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DatasetAksaraJawa dataset_aksara_jawa = new DatasetAksaraJawa();
                // Adding contact to list
                aksaraJawaList.add(dataset_aksara_jawa);
            } while (cursor.moveToNext());
        }

        // return aksara jawa list
        return aksaraJawaList;
    }

    // Updating single aksara jawa
    public int updateAksaraJawa(AksaraJawa aksara_jawa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AKSARA_JAWA, aksara_jawa.getAksaraJawa());

        // updating row
        return db.update(TABLE_AKSARA_JAWA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(aksara_jawa.getID()) });
    }

    // Deleting single aksara jawa
    public void deleteAksaraJawa(AksaraJawa contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AKSARA_JAWA, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting aksara jawa Count
    public int getAksaraJawaCount() {
        String countQuery = "SELECT  * FROM " + TABLE_AKSARA_JAWA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
