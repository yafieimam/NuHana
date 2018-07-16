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
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "dataset_aksara_jawa";

    // Tables name
    private static final String TABLE_AKSARA_JAWA = "aksara_jawa";
    private static final String TABLE_FEATURE = "fitur_data_training";
    private static final String TABLE_WEIGHT_NN_1 = "weight_nn_satu";
    private static final String TABLE_WEIGHT_NN_2 = "weight_nn_dua";
    private static final String TABLE_WEIGHT_NN_3 = "weight_nn_tiga";

    // Tables Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AKSARA_JAWA = "nama_aksara_jawa";

    private static final String CREATE_AKSARA_JAWA_TABLE = "CREATE TABLE " + TABLE_AKSARA_JAWA + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AKSARA_JAWA + " TEXT" + ")";

    private static final String CREATE_FEATURE_TABLE = "CREATE TABLE " + TABLE_FEATURE + "("
            + KEY_ID + " INTEGER PRIMARY KEY)";

    private static final String CREATE_WEIGHT_NN_1_TABLE = "CREATE TABLE " + TABLE_WEIGHT_NN_1 + "("
            + KEY_ID + " INTEGER PRIMARY KEY)";

    private static final String CREATE_WEIGHT_NN_2_TABLE = "CREATE TABLE " + TABLE_WEIGHT_NN_2 + "("
            + KEY_ID + " INTEGER PRIMARY KEY)";

    private static final String CREATE_WEIGHT_NN_3_TABLE = "CREATE TABLE " + TABLE_WEIGHT_NN_3 + "("
            + KEY_ID + " INTEGER PRIMARY KEY)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AKSARA_JAWA_TABLE);
        db.execSQL(CREATE_FEATURE_TABLE);
        db.execSQL("ALTER TABLE " + TABLE_FEATURE + " ADD COLUMN huruf TEXT");
        for(int i = 1; i <= 54; i++){
            db.execSQL("ALTER TABLE " + TABLE_FEATURE + " ADD COLUMN f" + i + " DOUBLE DEFAULT 0");
        }
        db.execSQL("ALTER TABLE " + TABLE_FEATURE + " ADD COLUMN label INTEGER DEFAULT 0");
        db.execSQL(CREATE_WEIGHT_NN_1_TABLE);
        for(int i = 1; i <= 43; i++){
            db.execSQL("ALTER TABLE " + TABLE_WEIGHT_NN_1 + " ADD COLUMN weight" + i + " DOUBLE DEFAULT 0");
        }
        db.execSQL(CREATE_WEIGHT_NN_2_TABLE);
        for(int i = 1; i <= 32; i++){
            db.execSQL("ALTER TABLE " + TABLE_WEIGHT_NN_2 + " ADD COLUMN weight" + i + " DOUBLE DEFAULT 0");
        }
        db.execSQL(CREATE_WEIGHT_NN_3_TABLE);
        for(int i = 1; i <= 20; i++){
            db.execSQL("ALTER TABLE " + TABLE_WEIGHT_NN_3 + " ADD COLUMN weight" + i + " DOUBLE DEFAULT 0");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AKSARA_JAWA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_NN_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_NN_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_NN_3);

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
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getFeatureCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FEATURE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int getWeightOneCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WEIGHT_NN_1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int getWeightTwoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WEIGHT_NN_2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int getWeightThreeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WEIGHT_NN_3;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public double[][] getFeatureDataTraining(double[] nilai_total){
        final String TABLE_NAME = "fitur_data_training";
        SQLiteDatabase db = this.getReadableDatabase();
        double[][] data = new double[getFeatureCount()+1][54];

        String selectQuery = "SELECT * FROM " + TABLE_FEATURE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String[] columnNames = cursor.getColumnNames();
                int a = 0;
                for(int j = 2; j < columnNames.length-1; j++){
                    data[i][a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
                    a++;
                }
                i++;
            } while (cursor.moveToNext());
            data[i] = nilai_total;
        }
        cursor.close();

        db.close();
        return data;
    }

    public int[] getLabelDataTraining(){
        final String TABLE_NAME = "fitur_data_training";
        SQLiteDatabase db = this.getReadableDatabase();
        int[] data = new int[getFeatureCount()];

        String selectQuery = "SELECT * FROM " + TABLE_FEATURE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                data[i] = cursor.getInt(cursor.getColumnIndex("label"));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return data;
    }

    public double[][] getDataWeightLayerOne(){
        final String TABLE_NAME = "weight_nn_satu";
        SQLiteDatabase db = this.getReadableDatabase();
        double[][] data = new double[getWeightOneCount()][43];

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String[] columnNames = cursor.getColumnNames();
                int a = 0;
                for(int j = 1; j < columnNames.length; j++){
                    data[i][a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
                    a++;
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return data;
    }

    public double[][] getDataWeightLayerTwo(){
        final String TABLE_NAME = "weight_nn_dua";
        SQLiteDatabase db = this.getReadableDatabase();
        double[][] data = new double[getWeightTwoCount()][32];

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String[] columnNames = cursor.getColumnNames();
                int a = 0;
                for(int j = 1; j < columnNames.length; j++){
                    data[i][a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
                    a++;
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return data;
    }

    public double[][] getDataWeightLayerThree(){
        final String TABLE_NAME = "weight_nn_tiga";
        SQLiteDatabase db = this.getReadableDatabase();
        double[][] data = new double[getWeightThreeCount()][20];

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String[] columnNames = cursor.getColumnNames();
                int a = 0;
                for(int j = 1; j < columnNames.length; j++){
                    data[i][a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
                    a++;
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return data;
    }
}
