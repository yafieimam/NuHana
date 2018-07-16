package com.yafieimam.nuhanaapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    List<String> label = new ArrayList<>();
    List<Map<String,String>> dataRow = new ArrayList<>();
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slideshow1, R.drawable.slideshow2, R.drawable.slideshow3};
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        DatabaseHandler db = new DatabaseHandler(this);

        // Enable if permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            SQLiteDatabase dbase = db.getWritableDatabase();

            dbase.execSQL("DROP TABLE IF EXISTS aksara_jawa");
            dbase.execSQL("DROP TABLE IF EXISTS fitur_data_training");
            dbase.execSQL("DROP TABLE IF EXISTS weight_nn_satu");
            dbase.execSQL("DROP TABLE IF EXISTS weight_nn_dua");
            dbase.execSQL("DROP TABLE IF EXISTS weight_nn_tiga");

            db.onCreate(dbase);

            Log.d("Insert: ", "Inserting ..");
            db.addAksaraJawa(new AksaraJawa("Ha")); db.addAksaraJawa(new AksaraJawa("Na"));
            db.addAksaraJawa(new AksaraJawa("Ca")); db.addAksaraJawa(new AksaraJawa("Ra"));
            db.addAksaraJawa(new AksaraJawa("Ka")); db.addAksaraJawa(new AksaraJawa("Da"));
            db.addAksaraJawa(new AksaraJawa("Ta")); db.addAksaraJawa(new AksaraJawa("Sa"));
            db.addAksaraJawa(new AksaraJawa("Wa")); db.addAksaraJawa(new AksaraJawa("La"));
            db.addAksaraJawa(new AksaraJawa("Pa")); db.addAksaraJawa(new AksaraJawa("Dha"));
            db.addAksaraJawa(new AksaraJawa("Ja")); db.addAksaraJawa(new AksaraJawa("Ya"));
            db.addAksaraJawa(new AksaraJawa("Nya")); db.addAksaraJawa(new AksaraJawa("Ma"));
            db.addAksaraJawa(new AksaraJawa("Ga")); db.addAksaraJawa(new AksaraJawa("Ba"));
            db.addAksaraJawa(new AksaraJawa("Tha")); db.addAksaraJawa(new AksaraJawa("Nga"));

            try {
                InputStream is = getAssets().open("dataset_yafie5.csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                InputStream is_weight1 = getAssets().open("weight1.csv");
                BufferedReader br_weight1 = new BufferedReader(new InputStreamReader(is_weight1, "UTF-8"));
                InputStream is_weight2 = getAssets().open("weight2.csv");
                BufferedReader br_weight2 = new BufferedReader(new InputStreamReader(is_weight2, "UTF-8"));
                InputStream is_weight3 = getAssets().open("weight3.csv");
                BufferedReader br_weight3 = new BufferedReader(new InputStreamReader(is_weight3, "UTF-8"));
                String line = "";
                String tableName = "fitur_data_training";
                String tableName_weight1 = "weight_nn_satu";
                String tableName_weight2 = "weight_nn_dua";
                String tableName_weight3 = "weight_nn_tiga";
                String str1 = "INSERT INTO " + tableName + " VALUES(";
                String str2 = "INSERT INTO " + tableName_weight1 + " VALUES(";
                String str3 = "INSERT INTO " + tableName_weight2 + " VALUES(";
                String str4 = "INSERT INTO " + tableName_weight3 + " VALUES(";
                String str5 = ");";
                SQLiteDatabase database = db.getWritableDatabase();
                database.beginTransaction();
                int number = 1;
                while ((line = br.readLine()) != null){
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    for(int i = 0; i <= str.length; i++){
                        if(i == str.length) {
                            sb.append(str[i-1]);
                        } else if(i == 1) {
                            sb.append("'" + str[i-1] + "', ");
                        }else if(i == 0){
                            sb.append(number + ", ");
                            number++;
                        }else {
                            sb.append(str[i-1] + ", ");
                        }
                    }
                    sb.append(str5);
                    database.execSQL(sb.toString());
                }
                number = 1;
                while ((line = br_weight1.readLine()) != null){
                    StringBuilder sb = new StringBuilder(str2);
                    String[] str = line.split(",");
                    for(int i = 0; i <= str.length; i++){
                        if(i == str.length) {
                            sb.append(str[i-1]);
                        }else if(i == 0){
                            sb.append(number + ", ");
                            number++;
                        }else {
                            sb.append(str[i-1] + ", ");
                        }
                    }
                    sb.append(str5);
                    database.execSQL(sb.toString());
                }
                number = 1;
                while ((line = br_weight2.readLine()) != null){
                    StringBuilder sb = new StringBuilder(str3);
                    String[] str = line.split(",");
                    for(int i = 0; i <= str.length; i++){
                        if(i == str.length) {
                            sb.append(str[i-1]);
                        }else if(i == 0){
                            sb.append(number + ", ");
                            number++;
                        }else {
                            sb.append(str[i-1] + ", ");
                        }
                    }
                    sb.append(str5);
                    database.execSQL(sb.toString());
                }
                number = 1;
                while ((line = br_weight3.readLine()) != null){
                    StringBuilder sb = new StringBuilder(str4);
                    String[] str = line.split(",");
                    for(int i = 0; i <= str.length; i++){
                        if(i == str.length) {
                            sb.append(str[i-1]);
                        }else if(i == 0){
                            sb.append(number + ", ");
                            number++;
                        }else {
                            sb.append(str[i-1] + ", ");
                        }
                    }
                    sb.append(str5);
                    database.execSQL(sb.toString());
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                database.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            List<AksaraJawa> allAksaraJawa = db.getAllAksaraJawa();
            for (AksaraJawa aksara_jawa : allAksaraJawa) {
                Log.d("AKSARA JAWA", aksara_jawa.getAksaraJawa());
            }
//            String countQuery = "SELECT * FROM fitur_data_training";
            int count = db.getFeatureCount();
            int count_weight_one = db.getWeightOneCount();
            int count_weight_two = db.getWeightTwoCount();
            int count_weight_three = db.getWeightThreeCount();
//            Cursor cursor = database.rawQuery(countQuery, null);
//            int count = cursor.getCount();
//            cursor.close();
//            long count = DatabaseUtils.queryNumEntries(database, "fitur_data_training");
            Log.d("FITUR DATA TRAINING", "JUMLAHNYA YAITU = " + count);
            Log.d("WEIGHT LAYER 1", "JUMLAHNYA YAITU = " + count_weight_one);
            Log.d("WEIGHT LAYER 2", "JUMLAHNYA YAITU = " + count_weight_two);
            Log.d("WEIGHT LAYER 3", "JUMLAHNYA YAITU = " + count_weight_three);
            double[] data = new double[54];
            SQLiteDatabase database = db.getReadableDatabase();
            String selectQuery = "SELECT * FROM fitur_data_training WHERE id=200";
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();

                int a = 0;
                for(int j = 2; j < columnNames.length-1; j++){
                    data[a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
                    a++;
                }
            }
            cursor.close();
            database.close();
            Log.d("FITUR DATA TRAINING", "Data: " + Arrays.toString(data));
        }

//        SharedPreferences settings = getSharedPreferences("prefs", 0);
//        boolean firstrun = settings.getBoolean("firstrun", true);

        prefs = getSharedPreferences("com.yafieimam.nuhanaapp", MODE_PRIVATE);

//        Log.d("BOOLEAN", "First Run? " + firstrun);

//        DatabaseHandler db = new DatabaseHandler(this);

//        if(prefs.getBoolean("firstrun", true)){
//            //Inserting Aksara Jawa
//            Log.d("Insert: ", "Inserting ..");
//            db.addAksaraJawa(new AksaraJawa("Ha")); db.addAksaraJawa(new AksaraJawa("Na"));
//            db.addAksaraJawa(new AksaraJawa("Ca")); db.addAksaraJawa(new AksaraJawa("Ra"));
//            db.addAksaraJawa(new AksaraJawa("Ka")); db.addAksaraJawa(new AksaraJawa("Da"));
//            db.addAksaraJawa(new AksaraJawa("Ta")); db.addAksaraJawa(new AksaraJawa("Sa"));
//            db.addAksaraJawa(new AksaraJawa("Wa")); db.addAksaraJawa(new AksaraJawa("La"));
//            db.addAksaraJawa(new AksaraJawa("Pa")); db.addAksaraJawa(new AksaraJawa("Dha"));
//            db.addAksaraJawa(new AksaraJawa("Ja")); db.addAksaraJawa(new AksaraJawa("Ya"));
//            db.addAksaraJawa(new AksaraJawa("Nya")); db.addAksaraJawa(new AksaraJawa("Ma"));
//            db.addAksaraJawa(new AksaraJawa("Ga")); db.addAksaraJawa(new AksaraJawa("Ba"));
//            db.addAksaraJawa(new AksaraJawa("Tha")); db.addAksaraJawa(new AksaraJawa("Nga"));
//
//            try {
//                InputStream is = getAssets().open("dataset_yafie3.csv");
//                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                InputStream is_weight1 = getAssets().open("weight1.csv");
//                BufferedReader br_weight1 = new BufferedReader(new InputStreamReader(is_weight1, "UTF-8"));
//                InputStream is_weight2 = getAssets().open("weight2.csv");
//                BufferedReader br_weight2 = new BufferedReader(new InputStreamReader(is_weight2, "UTF-8"));
//                InputStream is_weight3 = getAssets().open("weight3.csv");
//                BufferedReader br_weight3 = new BufferedReader(new InputStreamReader(is_weight3, "UTF-8"));
//                String line = "";
//                String tableName = "fitur_data_training";
//                String tableName_weight1 = "weight_nn_satu";
//                String tableName_weight2 = "weight_nn_dua";
//                String tableName_weight3 = "weight_nn_tiga";
//                String str1 = "INSERT INTO " + tableName + " VALUES(";
//                String str2 = "INSERT INTO " + tableName_weight1 + " VALUES(";
//                String str3 = "INSERT INTO " + tableName_weight2 + " VALUES(";
//                String str4 = "INSERT INTO " + tableName_weight3 + " VALUES(";
//                String str5 = ");";
//                SQLiteDatabase database = db.getWritableDatabase();
//                database.beginTransaction();
//                int number = 1;
//                while ((line = br.readLine()) != null){
//                    StringBuilder sb = new StringBuilder(str1);
//                    String[] str = line.split(",");
//                    for(int i = 0; i <= str.length; i++){
//                        if(i == str.length) {
//                            sb.append(str[i-1]);
//                        } else if(i == 1) {
//                            sb.append("'" + str[i-1] + "', ");
//                        }else if(i == 0){
//                            sb.append(number + ", ");
//                            number++;
//                        }else {
//                            sb.append(str[i-1] + ", ");
//                        }
//                    }
//                    sb.append(str5);
//                    database.execSQL(sb.toString());
//                }
//                number = 1;
//                while ((line = br_weight1.readLine()) != null){
//                    StringBuilder sb = new StringBuilder(str2);
//                    String[] str = line.split(",");
//                    for(int i = 0; i <= str.length; i++){
//                        if(i == str.length) {
//                            sb.append(str[i-1]);
//                        }else if(i == 0){
//                            sb.append(number + ", ");
//                            number++;
//                        }else {
//                            sb.append(str[i-1] + ", ");
//                        }
//                    }
//                    sb.append(str5);
//                    database.execSQL(sb.toString());
//                }
//                number = 1;
//                while ((line = br_weight2.readLine()) != null){
//                    StringBuilder sb = new StringBuilder(str3);
//                    String[] str = line.split(",");
//                    for(int i = 0; i <= str.length; i++){
//                        if(i == str.length) {
//                            sb.append(str[i-1]);
//                        }else if(i == 0){
//                            sb.append(number + ", ");
//                            number++;
//                        }else {
//                            sb.append(str[i-1] + ", ");
//                        }
//                    }
//                    sb.append(str5);
//                    database.execSQL(sb.toString());
//                }
//                number = 1;
//                while ((line = br_weight3.readLine()) != null){
//                    StringBuilder sb = new StringBuilder(str4);
//                    String[] str = line.split(",");
//                    for(int i = 0; i <= str.length; i++){
//                        if(i == str.length) {
//                            sb.append(str[i-1]);
//                        }else if(i == 0){
//                            sb.append(number + ", ");
//                            number++;
//                        }else {
//                            sb.append(str[i-1] + ", ");
//                        }
//                    }
//                    sb.append(str5);
//                    database.execSQL(sb.toString());
//                }
//                database.setTransactionSuccessful();
//                database.endTransaction();
//                database.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////            SharedPreferences.Editor editor = settings.edit();
////            editor.putBoolean("firstrun", false);
////            editor.commit();
////            settings.edit().putBoolean("firstrun", false).commit();
//            prefs.edit().putBoolean("firstrun", false).commit();
//        }else{
//            List<AksaraJawa> allAksaraJawa = db.getAllAksaraJawa();
//            for (AksaraJawa aksara_jawa : allAksaraJawa) {
//                Log.d("AKSARA JAWA", aksara_jawa.getAksaraJawa());
//            }
////            String countQuery = "SELECT * FROM fitur_data_training";
//            int count = db.getFeatureCount();
//            int count_weight_one = db.getWeightOneCount();
//            int count_weight_two = db.getWeightTwoCount();
//            int count_weight_three = db.getWeightThreeCount();
////            Cursor cursor = database.rawQuery(countQuery, null);
////            int count = cursor.getCount();
////            cursor.close();
////            long count = DatabaseUtils.queryNumEntries(database, "fitur_data_training");
//            Log.d("FITUR DATA TRAINING", "JUMLAHNYA YAITU = " + count);
//            Log.d("WEIGHT LAYER 1", "JUMLAHNYA YAITU = " + count_weight_one);
//            Log.d("WEIGHT LAYER 2", "JUMLAHNYA YAITU = " + count_weight_two);
//            Log.d("WEIGHT LAYER 3", "JUMLAHNYA YAITU = " + count_weight_three);
//            double[] data = new double[54];
//            SQLiteDatabase database = db.getReadableDatabase();
//            String selectQuery = "SELECT * FROM fitur_data_training WHERE id=200";
//            Cursor cursor = database.rawQuery(selectQuery, null);
//            if (cursor.moveToFirst()) {
//                String[] columnNames = cursor.getColumnNames();
//
//                int a = 0;
//                for(int j = 2; j < columnNames.length-1; j++){
//                    data[a] = cursor.getDouble(cursor.getColumnIndex(columnNames[j]));
//                    a++;
//                }
//            }
//            cursor.close();
//            database.close();
//            Log.d("FITUR DATA TRAINING", "Data: " + Arrays.toString(data));
//        }

//        VectorLib vlib = new VectorLib();
//        double[][] dataset = vlib.readCSV_double( , "title:rowcol");
//        Log.d("FITUR DATA TRAINING", Arrays.deepToString(label));

        super.setContentView(R.layout.activity_base_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        carouselView.clearOnPageChangeListeners();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah Anda yakin ingin keluar dari Aplikasi?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BaseDrawerActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_nulis) {
            startActivity(new Intent(getApplicationContext(), SignatureActivity.class));
        } else if (id == R.id.nav_sinau) {
            startActivity(new Intent(getApplicationContext(), SinauActivity.class));
        } else if (id == R.id.nav_tutorial) {
            startActivity(new Intent(getApplicationContext(), TestingKlasifikasiActivity.class));
        } else if (id == R.id.nav_pengaturan) {

        } else if (id == R.id.nav_ekstraksifitur) {
            startActivity(new Intent(getApplicationContext(), BrowseImageActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
