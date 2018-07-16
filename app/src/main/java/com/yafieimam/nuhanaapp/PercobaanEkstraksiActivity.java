package com.yafieimam.nuhanaapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import ALI.ClusteringLib;
import ALI.VectorLib;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class PercobaanEkstraksiActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    Spinner spinner;
    Bitmap bitmapGambar, resizeBitmap;
    ImageView mImg;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_percobaan_ekstraksi);

        mImg = (ImageView) findViewById(R.id.imgView);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        progress=new ProgressDialog(PercobaanEkstraksiActivity.this);
        progress.setMax(100);
        progress.setMessage("Loading...Training Data...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();
        progress.setCancelable(false);

        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                try {
                    sleep(1000);
                    byte[] byteArray = getIntent().getByteArrayExtra("BitmapImage");
                    bitmapGambar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    Bitmap trimBitmap = trim(bitmapGambar, Color.WHITE);

                    jumpTime += 20;
                    progress.setProgress(jumpTime);

                    resizeBitmap = Bitmap.createScaledBitmap(trimBitmap, 100, 100, true);

                    int[][] imageData = new int[resizeBitmap.getHeight()*resizeBitmap.getWidth()][3];

//                  -------------------------------Ekstraksi Fitur Satu------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil: " + minValueX + " dan " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar: " + maxValueX + " dan " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    int jumlahDataHitam_Satu = 0;
//                    int jumlahDataHitam_Dua = 0;
//                    int jumlahDataHitam_Tiga = 0;
//                    int jumlahDataHitam_Empat = 0;
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                if (x >= minValueX && x <= Math.round(nilai_centroid[0]) - 1 && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Satu++;
//                                } else if (x >= minValueX && x <= Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Dua++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Tiga++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Empat++;
//                                }
//                            }
//                        }
//                    }
//
//                    int[][] imageDataHitam_Satu = new int[jumlahDataHitam_Satu][2];
//                    int[][] imageDataHitam_Dua = new int[jumlahDataHitam_Dua][2];
//                    int[][] imageDataHitam_Tiga = new int[jumlahDataHitam_Tiga][2];
//                    int[][] imageDataHitam_Empat = new int[jumlahDataHitam_Empat][2];
//
//                    int a = 0, b = 0, c = 0, d = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) - 1 && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Satu[a][0] = imageData[j][0];
//                                imageDataHitam_Satu[a][1] = imageData[j][1];
//                                a++;
//                            } else if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Dua[b][0] = imageData[j][0];
//                                imageDataHitam_Dua[b][1] = imageData[j][1];
//                                b++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Tiga[c][0] = imageData[j][0];
//                                imageDataHitam_Tiga[c][1] = imageData[j][1];
//                                c++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Empat[d][0] = imageData[j][0];
//                                imageDataHitam_Empat[d][1] = imageData[j][1];
//                                d++;
//                            }
//                        }
//                    }
//
//                    double[] nilai_centroid_satu = clib.getCentroid(imageDataHitam_Satu);
//                    double[] nilai_centroid_dua = clib.getCentroid(imageDataHitam_Dua);
//                    double[] nilai_centroid_tiga = clib.getCentroid(imageDataHitam_Tiga);
//                    double[] nilai_centroid_empat = clib.getCentroid(imageDataHitam_Empat);
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Satu: " + Arrays.toString(nilai_centroid_satu));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Dua: " + Arrays.toString(nilai_centroid_dua));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Tiga: " + Arrays.toString(nilai_centroid_tiga));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Empat: " + Arrays.toString(nilai_centroid_empat));
//
//                    double hasil = 0, total_satu = 0;
//                    for (i = 0; i < imageDataHitam_Satu.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_centroid_satu[j],2)));
//                        }
//                        total_satu = total_satu + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_dua = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Dua.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_centroid_dua[j],2)));
//                        }
//                        total_dua = total_dua + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_tiga = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_centroid_tiga[j],2)));
//                        }
//                        total_tiga = total_tiga + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_empat = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Empat.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_centroid_empat[j],2)));
//                        }
//                        total_empat = total_empat + hasil;
//                        hasil = 0;
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Hasil: " + total_satu + ", " + total_dua + ", " + total_tiga + ", " + total_empat);

//                    -------------------------------Ekstraksi Fitur Dua------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil: " + minValueX + " dan " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar: " + maxValueX + " dan " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    int jumlahDataHitam_Satu = 0;
//                    int jumlahDataHitam_Dua = 0;
//                    int jumlahDataHitam_Tiga = 0;
//                    int jumlahDataHitam_Empat = 0;
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                if (x >= minValueX && x <= Math.round(nilai_centroid[0]) - 1 && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Satu++;
//                                } else if (x >= minValueX && x <= Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Dua++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Tiga++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Empat++;
//                                }
//                            }
//                        }
//                    }
//
//                    int[][] imageDataHitam_Satu = new int[jumlahDataHitam_Satu][2];
//                    int[][] imageDataHitam_Dua = new int[jumlahDataHitam_Dua][2];
//                    int[][] imageDataHitam_Tiga = new int[jumlahDataHitam_Tiga][2];
//                    int[][] imageDataHitam_Empat = new int[jumlahDataHitam_Empat][2];
//
//                    int a = 0, b = 0, c = 0, d = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) - 1 && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Satu[a][0] = imageData[j][0];
//                                imageDataHitam_Satu[a][1] = imageData[j][1];
//                                a++;
//                            } else if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Dua[b][0] = imageData[j][0];
//                                imageDataHitam_Dua[b][1] = imageData[j][1];
//                                b++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Tiga[c][0] = imageData[j][0];
//                                imageDataHitam_Tiga[c][1] = imageData[j][1];
//                                c++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Empat[d][0] = imageData[j][0];
//                                imageDataHitam_Empat[d][1] = imageData[j][1];
//                                d++;
//                            }
//                        }
//                    }
//
//                    double hasil = 0, total_satu = 0;
//                    for (i = 0; i < imageDataHitam_Satu.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_centroid[j],2)));
//                        }
//                        total_satu = total_satu + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_dua = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Dua.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_centroid[j],2)));
//                        }
//                        total_dua = total_dua + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_tiga = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_centroid[j],2)));
//                        }
//                        total_tiga = total_tiga + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_empat = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Empat.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_centroid[j],2)));
//                        }
//                        total_empat = total_empat + hasil;
//                        hasil = 0;
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Hasil: " + total_satu + ", " + total_dua + ", " + total_tiga + ", " + total_empat);

//                    -------------------------------Ekstraksi Fitur Tiga------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil: " + minValueX + " dan " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar: " + maxValueX + " dan " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    int jumlahDataHitam_Satu = 0;
//                    int jumlahDataHitam_Dua = 0;
//                    int jumlahDataHitam_Tiga = 0;
//                    int jumlahDataHitam_Empat = 0;
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                if (x >= minValueX && x <= Math.round(nilai_centroid[0]) - 1 && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Satu++;
//                                } else if (x >= minValueX && x <= Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Dua++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_Tiga++;
//                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
//                                    jumlahDataHitam_Empat++;
//                                }
//                            }
//                        }
//                    }
//
//                    int[][] imageDataHitam_Satu = new int[jumlahDataHitam_Satu][2];
//                    int[][] imageDataHitam_Dua = new int[jumlahDataHitam_Dua][2];
//                    int[][] imageDataHitam_Tiga = new int[jumlahDataHitam_Tiga][2];
//                    int[][] imageDataHitam_Empat = new int[jumlahDataHitam_Empat][2];
//
//                    int a = 0, b = 0, c = 0, d = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) - 1 && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Satu[a][0] = imageData[j][0];
//                                imageDataHitam_Satu[a][1] = imageData[j][1];
//                                a++;
//                            } else if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Dua[b][0] = imageData[j][0];
//                                imageDataHitam_Dua[b][1] = imageData[j][1];
//                                b++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
//                                imageDataHitam_Tiga[c][0] = imageData[j][0];
//                                imageDataHitam_Tiga[c][1] = imageData[j][1];
//                                c++;
//                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
//                                imageDataHitam_Empat[d][0] = imageData[j][0];
//                                imageDataHitam_Empat[d][1] = imageData[j][1];
//                                d++;
//                            }
//                        }
//                    }
//
//                    double[] nilai_pojok_satu = new double[]{minValueX, minValueY};
//                    double[] nilai_pojok_dua = new double[]{minValueX, maxValueY};
//                    double[] nilai_pojok_tiga = new double[]{maxValueX, maxValueY};
//                    double[] nilai_pojok_empat = new double[]{maxValueX, minValueY};
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Satu: " + Arrays.toString(nilai_pojok_satu));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Dua: " + Arrays.toString(nilai_pojok_dua));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Tiga: " + Arrays.toString(nilai_pojok_tiga));
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Empat: " + Arrays.toString(nilai_pojok_empat));
//
//                    double hasil = 0, total_satu = 0;
//                    for (i = 0; i < imageDataHitam_Satu.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_pojok_satu[j],2)));
//                        }
//                        total_satu = total_satu + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_dua = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Dua.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_pojok_dua[j],2)));
//                        }
//                        total_dua = total_dua + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_tiga = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_pojok_tiga[j],2)));
//                        }
//                        total_tiga = total_tiga + hasil;
//                        hasil = 0;
//                    }
//
//                    double total_empat = 0;
//                    hasil = 0;
//                    for (i = 0; i < imageDataHitam_Empat.length; i++){
//                        for (int j = 0; j < 2; j++) {
//                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_pojok_empat[j],2)));
//                        }
//                        total_empat = total_empat + hasil;
//                        hasil = 0;
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Hasil: " + total_satu + ", " + total_dua + ", " + total_tiga + ", " + total_empat);

//                    -------------------------------Ekstraksi Fitur Empat------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    double jarijari_min;
//                    double jarijari_max = 50;
//                    double jarijari = jarijari_max / 8;
//                    double hasil, jarijari_kuadran;
//                    List<List<Double>> imageDataHitam_Satu = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Dua = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tiga = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Empat = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Lima = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Enam = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tujuh = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Delapan = new ArrayList<List<Double>>();
//                    List<Double> koordinat = null;
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Satu.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Dua.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tiga.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Empat.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Lima.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Enam.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tujuh.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//
//                        if(hasil >= jarijari_min){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Delapan.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    double total = 0;
//                    double[] nilai_total = new double[8];
//
//                    for (i = 0; i<imageDataHitam_Satu.size(); i++){
//                        total = total + imageDataHitam_Satu.get(i).get(2);
//                    }
//                    nilai_total[0] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Dua.size(); i++){
//                        total = total + imageDataHitam_Dua.get(i).get(2);
//                    }
//                    nilai_total[1] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tiga.size(); i++){
//                        total = total + imageDataHitam_Tiga.get(i).get(2);
//                    }
//                    nilai_total[2] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Empat.size(); i++){
//                        total = total + imageDataHitam_Empat.get(i).get(2);
//                    }
//                    nilai_total[3] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Lima.size(); i++){
//                        total = total + imageDataHitam_Lima.get(i).get(2);
//                    }
//                    nilai_total[4] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Enam.size(); i++){
//                        total = total + imageDataHitam_Enam.get(i).get(2);
//                    }
//                    nilai_total[5] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tujuh.size(); i++){
//                        total = total + imageDataHitam_Tujuh.get(i).get(2);
//                    }
//                    nilai_total[6] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Delapan.size(); i++){
//                        total = total + imageDataHitam_Delapan.get(i).get(2);
//                    }
//                    nilai_total[7] = total;
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

//                    -------------------------------Ekstraksi Fitur Lima - Satu ------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    double jarijari_min;
//                    double jarijari_max = maxValueX - nilai_centroid[0];
//                    double jarijari = jarijari_max / 9;
//                    double hasil, jarijari_kuadran;
//                    List<List<Double>> imageDataHitam_Satu = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Dua = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tiga = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Empat = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Lima = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Enam = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tujuh = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Delapan = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Sembilan = new ArrayList<List<Double>>();
//                    List<Double> koordinat = null;
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Satu.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Dua.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tiga.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Empat.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Lima.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Enam.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tujuh.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Delapan.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//
//                        if(hasil >= jarijari_min){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Sembilan.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    double total = 0;
//                    double[] nilai_total = new double[9];
//
//                    for (i = 0; i<imageDataHitam_Satu.size(); i++){
//                        total = total + imageDataHitam_Satu.get(i).get(2);
//                    }
//                    nilai_total[0] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Dua.size(); i++){
//                        total = total + imageDataHitam_Dua.get(i).get(2);
//                    }
//                    nilai_total[1] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tiga.size(); i++){
//                        total = total + imageDataHitam_Tiga.get(i).get(2);
//                    }
//                    nilai_total[2] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Empat.size(); i++){
//                        total = total + imageDataHitam_Empat.get(i).get(2);
//                    }
//                    nilai_total[3] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Lima.size(); i++){
//                        total = total + imageDataHitam_Lima.get(i).get(2);
//                    }
//                    nilai_total[4] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Enam.size(); i++){
//                        total = total + imageDataHitam_Enam.get(i).get(2);
//                    }
//                    nilai_total[5] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tujuh.size(); i++){
//                        total = total + imageDataHitam_Tujuh.get(i).get(2);
//                    }
//                    nilai_total[6] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Delapan.size(); i++){
//                        total = total + imageDataHitam_Delapan.get(i).get(2);
//                    }
//                    nilai_total[7] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Sembilan.size(); i++){
//                        total = total + imageDataHitam_Sembilan.get(i).get(2);
//                    }
//                    nilai_total[8] = total;
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

//                    -------------------------------Ekstraksi Fitur Lima - Dua ------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    double jarijari_min;
//                    double jarijari_max = maxValueX - nilai_centroid[0];
//                    double jarijari = jarijari_max / 5;
//                    double hasil, jarijari_kuadran;
//                    List<List<Double>> imageDataHitam_Satu = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Dua = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tiga = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Empat = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Lima = new ArrayList<List<Double>>();
//                    List<Double> koordinat = null;
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Satu.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Dua.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tiga.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Empat.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//
//                        if(hasil >= jarijari_min){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Lima.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    double total = 0;
//                    double[] nilai_total = new double[5];
//
//                    for (i = 0; i<imageDataHitam_Satu.size(); i++){
//                        total = total + imageDataHitam_Satu.get(i).get(2);
//                    }
//                    nilai_total[0] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Dua.size(); i++){
//                        total = total + imageDataHitam_Dua.get(i).get(2);
//                    }
//                    nilai_total[1] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tiga.size(); i++){
//                        total = total + imageDataHitam_Tiga.get(i).get(2);
//                    }
//                    nilai_total[2] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Empat.size(); i++){
//                        total = total + imageDataHitam_Empat.get(i).get(2);
//                    }
//                    nilai_total[3] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Lima.size(); i++){
//                        total = total + imageDataHitam_Lima.get(i).get(2);
//                    }
//                    nilai_total[4] = total;

//                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

//                    -------------------------------Ekstraksi Fitur Enam ------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    double jarijari_min;
//                    double jarijari_max = maxValueX - nilai_centroid[0];
//                    double jarijari = jarijari_max / 5;
//                    double hasil, jarijari_kuadran;
//                    List<List<Double>> imageDataHitam_Satu = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Dua = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tiga = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Empat = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Lima = new ArrayList<List<Double>>();
//                    List<Double> koordinat = null;
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Satu.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Dua.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tiga.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Empat.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//
//                        if(hasil >= jarijari_min){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Lima.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    double total = 0;
//                    double[] nilai_total = new double[5];
//
//                    for (i = 0; i<imageDataHitam_Satu.size(); i++){
//                        total = total + imageDataHitam_Satu.get(i).get(2);
//                    }
//                    if(total == 0){
//                        nilai_total[0] = total;
//                    }else{
//                        nilai_total[0] = total / imageDataHitam_Satu.size();
//                    }
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Dua.size(); i++){
//                        total = total + imageDataHitam_Dua.get(i).get(2);
//                    }
//                    if(total == 0){
//                        nilai_total[1] = total;
//                    }else{
//                        nilai_total[1] = total / imageDataHitam_Dua.size();
//                    }
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tiga.size(); i++){
//                        total = total + imageDataHitam_Tiga.get(i).get(2);
//                    }
//                    if(total == 0){
//                        nilai_total[2] = total;
//                    }else{
//                        nilai_total[2] = total / imageDataHitam_Tiga.size();
//                    }
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Empat.size(); i++){
//                        total = total + imageDataHitam_Empat.get(i).get(2);
//                    }
//                    if(total == 0){
//                        nilai_total[3] = total;
//                    }else{
//                        nilai_total[3] = total / imageDataHitam_Empat.size();
//                    }
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Lima.size(); i++){
//                        total = total + imageDataHitam_Lima.get(i).get(2);
//                    }
//                    if(total == 0){
//                        nilai_total[4] = total;
//                    }else{
//                        nilai_total[4] = total / imageDataHitam_Lima.size();
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

//                    -------------------------------Ekstraksi Fitur Tujuh ------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueX = imageData[0][0];
//                    double maxValueY = imageData[0][1];
//                    double minValueX = imageData[0][0];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][0] > maxValueX){
//                            maxValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][0] < minValueX){
//                            minValueX = imageData[i][0];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    double jarijari_min;
//                    double jarijari_max = 20;
//                    double jarijari = jarijari_max / 4;
//                    double hasil, jarijari_kuadran;
//                    List<List<Double>> imageDataHitam_Satu = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Dua = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Tiga = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_Empat = new ArrayList<List<Double>>();
//                    List<Double> koordinat = null;
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Satu.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Dua.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Tiga.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_Empat.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    double total = 0;
//                    double[] nilai_total = new double[4];
//
//                    for (i = 0; i<imageDataHitam_Satu.size(); i++){
//                        total = total + imageDataHitam_Satu.get(i).get(2);
//                    }
//                    nilai_total[0] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Dua.size(); i++){
//                        total = total + imageDataHitam_Dua.get(i).get(2);
//                    }
//                    nilai_total[1] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Tiga.size(); i++){
//                        total = total + imageDataHitam_Tiga.get(i).get(2);
//                    }
//                    nilai_total[2] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_Empat.size(); i++){
//                        total = total + imageDataHitam_Empat.get(i).get(2);
//                    }
//                    nilai_total[3] = total;
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

//                    -------------------------------Ekstraksi Fitur Delapan------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[][] imageDataHitam = new int[jumlahDataHitam][2];
//
//                    i = 0;
//                    for (int j = 0; j < imageData.length; j++){
//                        if(imageData[j][2] == 1){
//                            imageDataHitam[i][0] = imageData[j][0];
//                            imageDataHitam[i][1] = imageData[j][1];
//                            i++;
//                        }
//                    }
//
//                    ClusteringLib clib=new ClusteringLib();
//
//                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    double maxValueY = imageData[0][1];
//                    double minValueY = imageData[0][1];
//
//                    for (i = 1; i < imageData.length; i++){
//                        if(imageData[i][1] > maxValueY){
//                            maxValueY = imageData[i][1];
//                        }
//                        if(imageData[i][1] < minValueY){
//                            minValueY = imageData[i][1];
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil Y: " + minValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar Y: " + maxValueY);
//                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));
//
//                    int jumlahDataHitam_A = 0;
//                    int jumlahDataHitam_B = 0;
//
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                if (x == Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
//                                    jumlahDataHitam_A++;
//                                } else if (x == Math.round(nilai_centroid[0]) && y >= minValueY && y <= Math.round(nilai_centroid[1])) {
//                                    jumlahDataHitam_B++;
//                                }
//                            }
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Jumlah Data A dan B: " + jumlahDataHitam_A + " dan " + jumlahDataHitam_B);
//
//                    int[][] imageDataHitam_A = new int[jumlahDataHitam_A][2];
//                    int[][] imageDataHitam_B = new int[jumlahDataHitam_B][2];
//
//                    int j = 0, k = 0;
//                    for (i = 0; i < imageData.length; i++){
//                        if(imageData[i][2] == 1) {
//                            if (imageData[i][0] == Math.round(nilai_centroid[0]) && imageData[i][1] >= Math.round(nilai_centroid[1]) && imageData[i][1] <= maxValueY) {
//                                imageDataHitam_A[j][0] = imageData[i][0];
//                                imageDataHitam_A[j][1] = imageData[i][1];
//                                j++;
//                            } else if(imageData[i][0] == Math.round(nilai_centroid[0]) && imageData[i][1] >= minValueY && imageData[i][1] <= Math.round(nilai_centroid[1])){
//                                imageDataHitam_B[k][0] = imageData[i][0];
//                                imageDataHitam_B[k][1] = imageData[i][1];
//                                k++;
//                            }
//                        }
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Data A Sebelum Sort: " + Arrays.deepToString(imageDataHitam_A));
//                    Log.d("PercobaanEkstraksiFitur", "Data B Sebelum Sort: " + Arrays.deepToString(imageDataHitam_B));
//
//                    Arrays.sort(imageDataHitam_A, new Comparator<int[]>() {
//                        @Override
//                        public int compare(int[] ints, int[] t1) {
//                            return Double.compare(ints[1], t1[1]);
//                        }
//                    });
//                    Arrays.sort(imageDataHitam_B, new Comparator<int[]>() {
//                        @Override
//                        public int compare(int[] ints, int[] t1) {
//                            return Double.compare(ints[1], t1[1]);
//                        }
//                    });
//
//                    Log.d("PercobaanEkstraksiFitur", "Data A Setelah Sort: " + Arrays.deepToString(imageDataHitam_A));
//                    Log.d("PercobaanEkstraksiFitur", "Data B Setelah Sort: " + Arrays.deepToString(imageDataHitam_B));
//
//                    double total_a = 0, total_b = 0;
//                    if(jumlahDataHitam_A > 0){
//                        for (i = 0; i < 2; i++) {
//                            total_a = Math.sqrt((Math.pow(total_a, 2)) + (Math.pow(imageDataHitam_A[0][i] - nilai_centroid[i],2)));
//                        }
//                    }else if(jumlahDataHitam_A == 0){
//                        total_a = 0;
//                    }
//
//                    if(jumlahDataHitam_B > 0) {
//                        for (i = 0; i < 2; i++) {
//                            total_b = Math.sqrt((Math.pow(total_b, 2)) + (Math.pow(imageDataHitam_B[0][i] - nilai_centroid[i], 2)));
//                        }
//                    }else if(jumlahDataHitam_B == 0){
//                        total_b = 0;
//                    }
//
//                    Log.d("PercobaanEkstraksiFitur", "Hasil: " + total_a + ", " + total_b);

//                    -------------------------------Ekstraksi Fitur Sembilan------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int rows = 3;
//                    int cols = 5;
//
//                    int chunks = rows * cols;
//                    ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>(chunks);
//
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
//                    int chunkWidth = resizeBitmap.getWidth() / cols;
//                    int chunkHeight = resizeBitmap.getHeight() / rows;
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int yCoord = 0;
//                    for(int x = 0; x < rows; x++) {
//                        int xCoord = 0;
//                        for(int y = 0; y < cols; y++) {
//                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
//                            xCoord += chunkWidth;
//                        }
//                        yCoord += chunkHeight;
//                    }
//
//                    int[] jumlahPixel = new int[splitImages.size()];
//
//                    for(int j = 0; j < splitImages.size(); j++){
//                        int jumlah = 0;
//                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
//                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
//                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
//                                    jumlah++;
//                                }
//                            }
//                        }
//                        jumlahPixel[j] = jumlah;
//                    }
//
//                    Log.d("Nilai Titik:", Arrays.toString(jumlahPixel));

//                    -------------------------------Ekstraksi Fitur Sepuluh------------------------------------------
//
//                    int jumlahData = 0;
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                jumlahData++;
//                            }
//                        }
//                    }
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int rows = 3;
//                    int cols = 5;
//
//                    int chunks = rows * cols;
//                    ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>(chunks);
//
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
//                    int chunkWidth = resizeBitmap.getWidth() / cols;
//                    int chunkHeight = resizeBitmap.getHeight() / rows;
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int yCoord = 0;
//                    for(int x = 0; x < rows; x++) {
//                        int xCoord = 0;
//                        for(int y = 0; y < cols; y++) {
//                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
//                            xCoord += chunkWidth;
//                        }
//                        yCoord += chunkHeight;
//                    }
//
//                    double[] jumlahPixel = new double[splitImages.size()];
//
//                    for(int j = 0; j < splitImages.size(); j++){
//                        double jumlah = 0;
//                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
//                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
//                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
//                                    jumlah++;
//                                }
//                            }
//                        }
//                        jumlah = jumlah / jumlahData;
//                        jumlahPixel[j] = jumlah;
//                    }
//
//                    Log.d("Nilai Titik:", Arrays.toString(jumlahPixel));

//                    -------------------------------Ekstraksi Fitur Sebelas------------------------------------------
//
//                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                binerData[y][x] = 1;
//                            } else {
//                                binerData[y][x] = 0;
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    doZhangSuenThinning(binerData);
//
//                    jumpTime += 30;
//                    progress.setProgress(jumpTime);
//
//                    for (int y = 0; y < binerData.length; y++) {
//                        for (int x = 0; x < binerData[y].length; x++) {
//                            if (binerData[y][x] == 1) {
//                                resizeBitmap.setPixel(x, y, Color.BLACK);
//                            } else {
//                                resizeBitmap.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    int rows = 10;
//                    int cols = 20;
//
//                    int chunks = rows * cols;
//                    ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>(chunks);
//
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
//                    int chunkWidth = resizeBitmap.getWidth() / cols;
//                    int chunkHeight = resizeBitmap.getHeight() / rows;
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int yCoord = 0;
//                    for(int x = 0; x < rows; x++) {
//                        int xCoord = 0;
//                        for(int y = 0; y < cols; y++) {
//                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
//                            xCoord += chunkWidth;
//                        }
//                        yCoord += chunkHeight;
//                    }
//
//                    int[] jumlahPixel = new int[splitImages.size()];
//
//                    for(int j = 0; j < splitImages.size(); j++){
//                        int jumlah = 0;
//                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
//                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
//                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
//                                    jumlah++;
//                                }
//                            }
//                        }
//                        jumlahPixel[j] = jumlah;
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int[] binerGambar = new int[jumlahPixel.length];
//
//                    for(int j =0; j < jumlahPixel.length; j++){
//                        if(jumlahPixel[j] > 0){
//                            binerGambar[j] = 1;
//                        }else{
//                            binerGambar[j] = 0;
//                        }
//                    }
//
//                    Log.d("Nilai Titik:", Arrays.toString(binerGambar));

//                    -------------------------------Ekstraksi Fitur Gabungan------------------------------------------

//                    double[] nilai_total = new double[263];
                    double[] nilai_total = new double[54];

                    int jumlahData = 0;

                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                jumlahData++;
                            }
                        }
                    }

                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];

                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                binerData[y][x] = 1;
                            } else {
                                binerData[y][x] = 0;
                            }
                        }
                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    doZhangSuenThinning(binerData);

                    jumpTime += 30;
                    progress.setProgress(jumpTime);

                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (binerData[y][x] == 1) {
                                resizeBitmap.setPixel(x, y, Color.BLACK);
                            } else {
                                resizeBitmap.setPixel(x, y, Color.WHITE);
                            }
                        }
                    }

                    int i = 0, jumlahDataHitam = 0;
                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                imageData[i][0] = x;
                                imageData[i][1] = y;
                                imageData[i][2] = 1;
                                jumlahDataHitam++;
                            } else if(resizeBitmap.getPixel(x,y) == Color.WHITE){
                                imageData[i][0] = x;
                                imageData[i][1] = y;
                                imageData[i][2] = 0;
                            }
                            i++;
                        }
                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    int[][] imageDataHitam = new int[jumlahDataHitam][2];

                    i = 0;
                    for (int j = 0; j < imageData.length; j++){
                        if(imageData[j][2] == 1){
                            imageDataHitam[i][0] = imageData[j][0];
                            imageDataHitam[i][1] = imageData[j][1];
                            i++;
                        }
                    }

                    ClusteringLib clib=new ClusteringLib();

                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    double maxValueX = imageData[0][0];
                    double maxValueY = imageData[0][1];
                    double minValueX = imageData[0][0];
                    double minValueY = imageData[0][1];

                    for (i = 1; i < imageData.length; i++){
                        if(imageData[i][0] > maxValueX){
                            maxValueX = imageData[i][0];
                        }
                        if(imageData[i][1] > maxValueY){
                            maxValueY = imageData[i][1];
                        }
                        if(imageData[i][0] < minValueX){
                            minValueX = imageData[i][0];
                        }
                        if(imageData[i][1] < minValueY){
                            minValueY = imageData[i][1];
                        }
                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);


                    Log.d("PercobaanEkstraksiFitur", "Nilai Terkecil: " + minValueX + " dan " + minValueY);
                    Log.d("PercobaanEkstraksiFitur", "Nilai Terbesar: " + maxValueX + " dan " + maxValueY);
                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid: " + Arrays.toString(nilai_centroid));

                    int jumlahDataHitam_Satu = 0;
                    int jumlahDataHitam_Dua = 0;
                    int jumlahDataHitam_Tiga = 0;
                    int jumlahDataHitam_Empat = 0;

                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                if (x >= minValueX && x <= Math.round(nilai_centroid[0]) - 1 && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
                                    jumlahDataHitam_Satu++;
                                } else if (x >= minValueX && x <= Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
                                    jumlahDataHitam_Dua++;
                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
                                    jumlahDataHitam_Tiga++;
                                } else if (x >= Math.round(nilai_centroid[0]) && x <= maxValueX && y >= minValueY && y <= Math.round(nilai_centroid[1]) - 1) {
                                    jumlahDataHitam_Empat++;
                                }
                            }
                        }
                    }

                    int[][] imageDataHitam_Satu = new int[jumlahDataHitam_Satu][2];
                    int[][] imageDataHitam_Dua = new int[jumlahDataHitam_Dua][2];
                    int[][] imageDataHitam_Tiga = new int[jumlahDataHitam_Tiga][2];
                    int[][] imageDataHitam_Empat = new int[jumlahDataHitam_Empat][2];

                    int a = 0, b = 0, c = 0, d = 0;
                    for (int j = 0; j < imageData.length; j++){
                        if(imageData[j][2] == 1){
                            if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) - 1 && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
                                imageDataHitam_Satu[a][0] = imageData[j][0];
                                imageDataHitam_Satu[a][1] = imageData[j][1];
                                a++;
                            } else if (imageData[j][0] >= minValueX && imageData[j][0] <= Math.round(nilai_centroid[0]) && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
                                imageDataHitam_Dua[b][0] = imageData[j][0];
                                imageDataHitam_Dua[b][1] = imageData[j][1];
                                b++;
                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= Math.round(nilai_centroid[1]) && imageData[j][1] <= maxValueY) {
                                imageDataHitam_Tiga[c][0] = imageData[j][0];
                                imageDataHitam_Tiga[c][1] = imageData[j][1];
                                c++;
                            } else if (imageData[j][0] >= Math.round(nilai_centroid[0]) && imageData[j][0] <= maxValueX && imageData[j][1] >= minValueY && imageData[j][1] <= Math.round(nilai_centroid[1]) - 1) {
                                imageDataHitam_Empat[d][0] = imageData[j][0];
                                imageDataHitam_Empat[d][1] = imageData[j][1];
                                d++;
                            }
                        }
                    }

//                    ---------------------EKSTRAKSI FITUR SATU--------------------------

                    double[] nilai_centroid_satu = clib.getCentroid(imageDataHitam_Satu);
                    double[] nilai_centroid_dua = clib.getCentroid(imageDataHitam_Dua);
                    double[] nilai_centroid_tiga = clib.getCentroid(imageDataHitam_Tiga);
                    double[] nilai_centroid_empat = clib.getCentroid(imageDataHitam_Empat);

                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Satu: " + Arrays.toString(nilai_centroid_satu));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Dua: " + Arrays.toString(nilai_centroid_dua));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Tiga: " + Arrays.toString(nilai_centroid_tiga));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Centroid Empat: " + Arrays.toString(nilai_centroid_empat));

                    double hasil = 0, total_satu = 0;
                    for (i = 0; i < imageDataHitam_Satu.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_centroid_satu[j],2)));
                        }
                        total_satu = total_satu + hasil;
                        hasil = 0;
                    }
                    nilai_total[0] = total_satu;

                    double total_dua = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Dua.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_centroid_dua[j],2)));
                        }
                        total_dua = total_dua + hasil;
                        hasil = 0;
                    }
                    nilai_total[1] = total_dua;

                    double total_tiga = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_centroid_tiga[j],2)));
                        }
                        total_tiga = total_tiga + hasil;
                        hasil = 0;
                    }
                    nilai_total[2] = total_tiga;

                    double total_empat = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Empat.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_centroid_empat[j],2)));
                        }
                        total_empat = total_empat + hasil;
                        hasil = 0;
                    }
                    nilai_total[3] = total_empat;

//                    ---------------------EKSTRAKSI FITUR DUA--------------------------

                    hasil = 0;
                    total_satu = 0;
                    for (i = 0; i < imageDataHitam_Satu.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_centroid[j],2)));
                        }
                        total_satu = total_satu + hasil;
                        hasil = 0;
                    }
                    nilai_total[4] = total_satu;

                    total_dua = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Dua.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_centroid[j],2)));
                        }
                        total_dua = total_dua + hasil;
                        hasil = 0;
                    }
                    nilai_total[5] = total_dua;

                    total_tiga = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_centroid[j],2)));
                        }
                        total_tiga = total_tiga + hasil;
                        hasil = 0;
                    }
                    nilai_total[6] = total_tiga;

                    total_empat = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Empat.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_centroid[j],2)));
                        }
                        total_empat = total_empat + hasil;
                        hasil = 0;
                    }
                    nilai_total[7] = total_empat;

//                    ---------------------EKSTRAKSI FITUR TIGA--------------------------

                    double[] nilai_pojok_satu = new double[]{minValueX, minValueY};
                    double[] nilai_pojok_dua = new double[]{minValueX, maxValueY};
                    double[] nilai_pojok_tiga = new double[]{maxValueX, maxValueY};
                    double[] nilai_pojok_empat = new double[]{maxValueX, minValueY};

                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Satu: " + Arrays.toString(nilai_pojok_satu));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Dua: " + Arrays.toString(nilai_pojok_dua));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Tiga: " + Arrays.toString(nilai_pojok_tiga));
                    Log.d("PercobaanEkstraksiFitur", "Nilai Pojok Empat: " + Arrays.toString(nilai_pojok_empat));

                    hasil = 0;
                    total_satu = 0;
                    for (i = 0; i < imageDataHitam_Satu.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Satu[i][j] - nilai_pojok_satu[j],2)));
                        }
                        total_satu = total_satu + hasil;
                        hasil = 0;
                    }
                    nilai_total[8] = total_satu;

                    total_dua = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Dua.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Dua[i][j] - nilai_pojok_dua[j],2)));
                        }
                        total_dua = total_dua + hasil;
                        hasil = 0;
                    }
                    nilai_total[9] = total_dua;

                    total_tiga = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Tiga.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Tiga[i][j] - nilai_pojok_tiga[j],2)));
                        }
                        total_tiga = total_tiga + hasil;
                        hasil = 0;
                    }
                    nilai_total[10] = total_tiga;

                    total_empat = 0;
                    hasil = 0;
                    for (i = 0; i < imageDataHitam_Empat.length; i++){
                        for (int j = 0; j < 2; j++) {
                            hasil = Math.sqrt((Math.pow(hasil, 2)) + (Math.pow(imageDataHitam_Empat[i][j] - nilai_pojok_empat[j],2)));
                        }
                        total_empat = total_empat + hasil;
                        hasil = 0;
                    }
                    nilai_total[11] = total_empat;

//                    ---------------------EKSTRAKSI FITUR EMPAT--------------------------

                    double jarijari_min;
                    double jarijari_max = 50;
                    double jarijari = jarijari_max / 8;
                    double jarijari_kuadran;
                    List<List<Double>> imageDataHitam_A = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_B = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_C = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_D = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_E = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_F = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_G = new ArrayList<List<Double>>();
                    List<List<Double>> imageDataHitam_H = new ArrayList<List<Double>>();
                    List<Double> koordinat = null;

                    for (i = 0; i < imageDataHitam.length; i++){
                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
                        hasil = Math.sqrt(hitung1 +  hitung2);

                        jarijari_min = 0;
                        jarijari_kuadran = jarijari_min + jarijari;
                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_A.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_B.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_C.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_D.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_E.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_F.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_G.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;

                        if(hasil >= jarijari_min){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_H.add(koordinat);
                            continue;
                        }
                    }

                    double total = 0;

                    for (i = 0; i<imageDataHitam_A.size(); i++){
                        total = total + imageDataHitam_A.get(i).get(2);
                    }
                    nilai_total[12] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_B.size(); i++){
                        total = total + imageDataHitam_B.get(i).get(2);
                    }
                    nilai_total[13] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_C.size(); i++){
                        total = total + imageDataHitam_C.get(i).get(2);
                    }
                    nilai_total[14] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_D.size(); i++){
                        total = total + imageDataHitam_D.get(i).get(2);
                    }
                    nilai_total[15] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_E.size(); i++){
                        total = total + imageDataHitam_E.get(i).get(2);
                    }
                    nilai_total[16] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_F.size(); i++){
                        total = total + imageDataHitam_F.get(i).get(2);
                    }
                    nilai_total[17] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_G.size(); i++){
                        total = total + imageDataHitam_G.get(i).get(2);
                    }
                    nilai_total[18] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_H.size(); i++){
                        total = total + imageDataHitam_H.get(i).get(2);
                    }
                    nilai_total[19] = total;

//                    ---------------------EKSTRAKSI FITUR LIMA - SATU--------------------------
//
//                    jarijari_max = maxValueX - nilai_centroid[0];
//                    jarijari = jarijari_max / 9;
//                    imageDataHitam_A = new ArrayList<List<Double>>();
//                    imageDataHitam_B = new ArrayList<List<Double>>();
//                    imageDataHitam_C = new ArrayList<List<Double>>();
//                    imageDataHitam_D = new ArrayList<List<Double>>();
//                    imageDataHitam_E = new ArrayList<List<Double>>();
//                    imageDataHitam_F = new ArrayList<List<Double>>();
//                    imageDataHitam_G = new ArrayList<List<Double>>();
//                    imageDataHitam_H = new ArrayList<List<Double>>();
//                    List<List<Double>> imageDataHitam_I = new ArrayList<List<Double>>();
//
//                    for (i = 0; i < imageDataHitam.length; i++){
//                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
//                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
//                        hasil = Math.sqrt(hitung1 +  hitung2);
//
//                        jarijari_min = 0;
//                        jarijari_kuadran = jarijari_min + jarijari;
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_A.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_B.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_C.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_D.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_E.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_F.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_G.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//                        jarijari_kuadran = jarijari_kuadran + jarijari;
//
//                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_H.add(koordinat);
//                            continue;
//                        }
//
//                        jarijari_min = jarijari_kuadran;
//
//                        if(hasil >= jarijari_min){
//                            koordinat = new ArrayList<Double>();
//                            koordinat.add((double) imageDataHitam[i][0]);
//                            koordinat.add((double) imageDataHitam[i][1]);
//                            koordinat.add(hasil);
//                            imageDataHitam_I.add(koordinat);
//                            continue;
//                        }
//                    }
//
//                    total = 0;
//
//                    for (i = 0; i<imageDataHitam_A.size(); i++){
//                        total = total + imageDataHitam_A.get(i).get(2);
//                    }
//                    nilai_total[20] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_B.size(); i++){
//                        total = total + imageDataHitam_B.get(i).get(2);
//                    }
//                    nilai_total[21] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_C.size(); i++){
//                        total = total + imageDataHitam_C.get(i).get(2);
//                    }
//                    nilai_total[22] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_D.size(); i++){
//                        total = total + imageDataHitam_D.get(i).get(2);
//                    }
//                    nilai_total[23] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_E.size(); i++){
//                        total = total + imageDataHitam_E.get(i).get(2);
//                    }
//                    nilai_total[24] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_F.size(); i++){
//                        total = total + imageDataHitam_F.get(i).get(2);
//                    }
//                    nilai_total[25] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_G.size(); i++){
//                        total = total + imageDataHitam_G.get(i).get(2);
//                    }
//                    nilai_total[26] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_H.size(); i++){
//                        total = total + imageDataHitam_H.get(i).get(2);
//                    }
//                    nilai_total[27] = total;
//                    total = 0;
//                    for (i = 0; i<imageDataHitam_I.size(); i++){
//                        total = total + imageDataHitam_I.get(i).get(2);
//                    }
//                    nilai_total[28] = total;

//                    ---------------------EKSTRAKSI FITUR LIMA - DUA--------------------------

                    jarijari_max = maxValueX - nilai_centroid[0];
                    jarijari = jarijari_max / 5;
                    imageDataHitam_A = new ArrayList<List<Double>>();
                    imageDataHitam_B = new ArrayList<List<Double>>();
                    imageDataHitam_C = new ArrayList<List<Double>>();
                    imageDataHitam_D = new ArrayList<List<Double>>();
                    imageDataHitam_E = new ArrayList<List<Double>>();

                    for (i = 0; i < imageDataHitam.length; i++){
                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
                        hasil = Math.sqrt(hitung1 +  hitung2);

                        jarijari_min = 0;
                        jarijari_kuadran = jarijari_min + jarijari;
                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_A.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_B.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_C.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_D.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;

                        if(hasil >= jarijari_min){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_E.add(koordinat);
                            continue;
                        }
                    }

                    total = 0;

                    for (i = 0; i<imageDataHitam_A.size(); i++){
                        total = total + imageDataHitam_A.get(i).get(2);
                    }
                    nilai_total[20] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_B.size(); i++){
                        total = total + imageDataHitam_B.get(i).get(2);
                    }
                    nilai_total[21] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_C.size(); i++){
                        total = total + imageDataHitam_C.get(i).get(2);
                    }
                    nilai_total[22] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_D.size(); i++){
                        total = total + imageDataHitam_D.get(i).get(2);
                    }
                    nilai_total[23] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_E.size(); i++){
                        total = total + imageDataHitam_E.get(i).get(2);
                    }
                    nilai_total[24] = total;

//                    ---------------------EKSTRAKSI FITUR ENAM--------------------------

                    jarijari_max = maxValueX - nilai_centroid[0];
                    jarijari = jarijari_max / 5;
                    imageDataHitam_A = new ArrayList<List<Double>>();
                    imageDataHitam_B = new ArrayList<List<Double>>();
                    imageDataHitam_C = new ArrayList<List<Double>>();
                    imageDataHitam_D = new ArrayList<List<Double>>();
                    imageDataHitam_E = new ArrayList<List<Double>>();

                    for (i = 0; i < imageDataHitam.length; i++){
                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
                        hasil = Math.sqrt(hitung1 +  hitung2);

                        jarijari_min = 0;
                        jarijari_kuadran = jarijari_min + jarijari;
                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_A.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_B.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_C.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_D.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;

                        if(hasil >= jarijari_min){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_E.add(koordinat);
                            continue;
                        }
                    }

                    total = 0;

                    for (i = 0; i<imageDataHitam_A.size(); i++){
                        total = total + imageDataHitam_A.get(i).get(2);
                    }
                    if(total == 0){
                        nilai_total[25] = total;
                    }else{
                        nilai_total[25] = total / imageDataHitam_A.size();
                    }
                    total = 0;
                    for (i = 0; i<imageDataHitam_B.size(); i++){
                        total = total + imageDataHitam_B.get(i).get(2);
                    }
                    if(total == 0){
                        nilai_total[26] = total;
                    }else{
                        nilai_total[26] = total / imageDataHitam_B.size();
                    }
                    total = 0;
                    for (i = 0; i<imageDataHitam_C.size(); i++){
                        total = total + imageDataHitam_C.get(i).get(2);
                    }
                    if(total == 0){
                        nilai_total[27] = total;
                    }else{
                        nilai_total[27] = total / imageDataHitam_C.size();
                    }
                    total = 0;
                    for (i = 0; i<imageDataHitam_D.size(); i++){
                        total = total + imageDataHitam_D.get(i).get(2);
                    }
                    if(total == 0){
                        nilai_total[28] = total;
                    }else{
                        nilai_total[28] = total / imageDataHitam_D.size();
                    }
                    total = 0;
                    for (i = 0; i<imageDataHitam_E.size(); i++){
                        total = total + imageDataHitam_E.get(i).get(2);
                    }
                    if(total == 0){
                        nilai_total[29] = total;
                    }else{
                        nilai_total[29] = total / imageDataHitam_E.size();
                    }

//                    ---------------------EKSTRAKSI FITUR TUJUH--------------------------

                    jarijari_max = 20;
                    jarijari = jarijari_max / 4;
                    imageDataHitam_A = new ArrayList<List<Double>>();
                    imageDataHitam_B = new ArrayList<List<Double>>();
                    imageDataHitam_C = new ArrayList<List<Double>>();
                    imageDataHitam_D = new ArrayList<List<Double>>();

                    for (i = 0; i < imageDataHitam.length; i++){
                        double hitung1 = Math.pow(imageDataHitam[i][0] - nilai_centroid[0],2);
                        double hitung2 = Math.pow(imageDataHitam[i][1] - nilai_centroid[1],2);
                        hasil = Math.sqrt(hitung1 +  hitung2);

                        jarijari_min = 0;
                        jarijari_kuadran = jarijari_min + jarijari;
                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_A.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_B.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_C.add(koordinat);
                            continue;
                        }

                        jarijari_min = jarijari_kuadran;
                        jarijari_kuadran = jarijari_kuadran + jarijari;

                        if(hasil >= jarijari_min && hasil < jarijari_kuadran){
                            koordinat = new ArrayList<Double>();
                            koordinat.add((double) imageDataHitam[i][0]);
                            koordinat.add((double) imageDataHitam[i][1]);
                            koordinat.add(hasil);
                            imageDataHitam_D.add(koordinat);
                            continue;
                        }
                    }

                    total = 0;

                    for (i = 0; i<imageDataHitam_A.size(); i++){
                        total = total + imageDataHitam_A.get(i).get(2);
                    }
                    nilai_total[30] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_B.size(); i++){
                        total = total + imageDataHitam_B.get(i).get(2);
                    }
                    nilai_total[31] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_C.size(); i++){
                        total = total + imageDataHitam_C.get(i).get(2);
                    }
                    nilai_total[32] = total;
                    total = 0;
                    for (i = 0; i<imageDataHitam_D.size(); i++){
                        total = total + imageDataHitam_D.get(i).get(2);
                    }
                    nilai_total[33] = total;

//                    ---------------------EKSTRAKSI FITUR DELAPAN--------------------------

                    jumlahDataHitam_Satu = 0;
                    jumlahDataHitam_Dua = 0;


                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if(resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                if (x == Math.round(nilai_centroid[0]) && y >= Math.round(nilai_centroid[1]) && y <= maxValueY) {
                                    jumlahDataHitam_Satu++;
                                } else if (x == Math.round(nilai_centroid[0]) && y >= minValueY && y <= Math.round(nilai_centroid[1])) {
                                    jumlahDataHitam_Dua++;
                                }
                            }
                        }
                    }

                    Log.d("PercobaanEkstraksiFitur", "Jumlah Data Satu dan Dua: " + jumlahDataHitam_Satu + " dan " + jumlahDataHitam_Dua);

                    imageDataHitam_Satu = new int[jumlahDataHitam_Satu][2];
                    imageDataHitam_Dua = new int[jumlahDataHitam_Dua][2];

                    int j = 0, k = 0;
                    for (i = 0; i < imageData.length; i++){
                        if(imageData[i][2] == 1) {
                            if (imageData[i][0] == Math.round(nilai_centroid[0]) && imageData[i][1] >= Math.round(nilai_centroid[1]) && imageData[i][1] <= maxValueY) {
                                imageDataHitam_Satu[j][0] = imageData[i][0];
                                imageDataHitam_Satu[j][1] = imageData[i][1];
                                j++;
                            } else if(imageData[i][0] == Math.round(nilai_centroid[0]) && imageData[i][1] >= minValueY && imageData[i][1] <= Math.round(nilai_centroid[1])){
                                imageDataHitam_Dua[k][0] = imageData[i][0];
                                imageDataHitam_Dua[k][1] = imageData[i][1];
                                k++;
                            }
                        }
                    }

                    Log.d("PercobaanEkstraksiFitur", "Data A Sebelum Sort: " + Arrays.deepToString(imageDataHitam_Satu));
                    Log.d("PercobaanEkstraksiFitur", "Data B Sebelum Sort: " + Arrays.deepToString(imageDataHitam_Dua));

                    Arrays.sort(imageDataHitam_Satu, new Comparator<int[]>() {
                        @Override
                        public int compare(int[] ints, int[] t1) {
                            return Double.compare(ints[1], t1[1]);
                        }
                    });
                    Arrays.sort(imageDataHitam_Dua, new Comparator<int[]>() {
                        @Override
                        public int compare(int[] ints, int[] t1) {
                            return Double.compare(ints[1], t1[1]);
                        }
                    });

                    Log.d("PercobaanEkstraksiFitur", "Data A Setelah Sort: " + Arrays.deepToString(imageDataHitam_Satu));
                    Log.d("PercobaanEkstraksiFitur", "Data B Setelah Sort: " + Arrays.deepToString(imageDataHitam_Dua));

                    double total_a = 0, total_b = 0;
                    if(jumlahDataHitam_Satu > 0){
                        for (i = 0; i < 2; i++) {
                            total_a = Math.sqrt((Math.pow(total_a, 2)) + (Math.pow(imageDataHitam_Satu[0][i] - nilai_centroid[i],2)));
                        }
                    }else if(jumlahDataHitam_Satu == 0){
                        total_a = 0;
                    }
                    nilai_total[34] = total_a;

                    if(jumlahDataHitam_Dua > 0) {
                        for (i = 0; i < 2; i++) {
                            total_b = Math.sqrt((Math.pow(total_b, 2)) + (Math.pow(imageDataHitam_Dua[0][i] - nilai_centroid[i], 2)));
                        }
                    }else if(jumlahDataHitam_Dua == 0){
                        total_b = 0;
                    }
                    nilai_total[35] = total_b;

//                    ---------------------EKSTRAKSI FITUR SEMBILAN--------------------------

                    int rows = 3;
                    int cols = 5;

                    int chunks = rows * cols;
                    ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>(chunks);

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
                    int chunkWidth = resizeBitmap.getWidth() / cols;
                    int chunkHeight = resizeBitmap.getHeight() / rows;

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    int yCoord = 0;
                    for(int x = 0; x < rows; x++) {
                        int xCoord = 0;
                        for(int y = 0; y < cols; y++) {
                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                            xCoord += chunkWidth;
                        }
                        yCoord += chunkHeight;
                    }

                    i = 36;

                    for(j = 0; j < splitImages.size(); j++){
                        int jumlah = 0;
                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
                                    jumlah++;
                                }
                            }
                        }
                        if(j >=3 && j<= 11) {
                            nilai_total[i] = jumlah;
                            i++;
                        }
                    }

//                    ---------------------EKSTRAKSI FITUR SEPULUH--------------------------

                    rows = 3;
                    cols = 5;

                    chunks = rows * cols;
                    splitImages = new ArrayList<Bitmap>(chunks);

                    scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
                    chunkWidth = resizeBitmap.getWidth() / cols;
                    chunkHeight = resizeBitmap.getHeight() / rows;

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    yCoord = 0;
                    for(int x = 0; x < rows; x++) {
                        int xCoord = 0;
                        for(int y = 0; y < cols; y++) {
                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                            xCoord += chunkWidth;
                        }
                        yCoord += chunkHeight;
                    }

                    i = 45;

                    for(j = 0; j < splitImages.size(); j++){
                        double jumlah = 0;
                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
                                    jumlah++;
                                }
                            }
                        }
                        jumlah = jumlah / jumlahData;
                        if(j >= 3 && j <= 11) {
                            nilai_total[i] = jumlah;
                            i++;
                        }
                    }

//                    ---------------------EKSTRAKSI FITUR SEBELAS--------------------------
//
//                    rows = 10;
//                    cols = 20;
//
//                    chunks = rows * cols;
//                    splitImages = new ArrayList<Bitmap>(chunks);
//
//                    scaledBitmap = Bitmap.createScaledBitmap(resizeBitmap, resizeBitmap.getWidth(), resizeBitmap.getHeight(), true);
//                    chunkWidth = resizeBitmap.getWidth() / cols;
//                    chunkHeight = resizeBitmap.getHeight() / rows;
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    yCoord = 0;
//                    for(int x = 0; x < rows; x++) {
//                        int xCoord = 0;
//                        for(int y = 0; y < cols; y++) {
//                            splitImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
//                            xCoord += chunkWidth;
//                        }
//                        yCoord += chunkHeight;
//                    }
//
//                    int[] jumlahPixel = new int[splitImages.size()];
//
//                    for(j = 0; j < splitImages.size(); j++){
//                        int jumlah = 0;
//                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
//                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
//                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
//                                    jumlah++;
//                                }
//                            }
//                        }
//                        jumlahPixel[j] = jumlah;
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    i = 63;
//
//                    for(j =0; j < jumlahPixel.length; j++){
//                        if(jumlahPixel[j] > 0){
//                            nilai_total[i] = 1;
//                        }else{
//                            nilai_total[i] = 0;
//                        }
//                        i++;
//                    }

                    Log.d("PercobaanEkstraksiFitur", "Nilai Total: " + Arrays.toString(nilai_total));

                    DatabaseHandler dbHandler = new DatabaseHandler(PercobaanEkstraksiActivity.this);
                    double[][] data = dbHandler.getFeatureDataTraining(nilai_total);
                    int[] label = dbHandler.getLabelDataTraining();

                    VectorLib vlib = new VectorLib();
                    data = vlib.Normalization("minmax", data, 0, 1);

                    ArrayList<ArrayList<Double>> data_list = new ArrayList<ArrayList<Double>>();

                    for(i = 0; i < data.length; i++){
                        ArrayList<Double> list = new ArrayList<>();
                        for(j = 0; j < data[i].length; j++){
                            list.add(data[i][j]);
                        }
                        data_list.add(list);
                    }

                    Double[] fitur_datates = new Double[data_list.get(data_list.size()-1).size()];
                    fitur_datates = data_list.get(data_list.size()-1).toArray(fitur_datates);

//                    Log.d("PercobaanEkstraksi", "Data Fitur 1 : " + Arrays.toString(fitur_datates));

                    double[] fitur_data_tes = ArrayUtils.toPrimitive(fitur_datates);
                    Log.d("PercobaanEkstraksi", "Data Fitur Tes : " + Arrays.toString(fitur_data_tes));

                    data_list.remove(data_list.size()-1);

                    double[][] fitur_datatraining = new double[data_list.size()][fitur_data_tes.length];

                    for(i = 0; i < data_list.size(); i++){
                        Double[] fitur_temp = new Double[fitur_data_tes.length];
                        fitur_temp = data_list.get(i).toArray(fitur_temp);
                        double[] temp_fitur = new double[fitur_temp.length];
                        temp_fitur = ArrayUtils.toPrimitive(fitur_temp);
                        fitur_datatraining[i] = temp_fitur;
                    }

                    double[][] weightLayerOne = dbHandler.getDataWeightLayerOne();
                    double[][] weightLayerTwo = dbHandler.getDataWeightLayerTwo();
                    double[][] weightLayerThree = dbHandler.getDataWeightLayerThree();

//                    double[] layerOne = vlib.initArray(weightLayerOne[0].length,0.0);
//                    double[] layerTwo = vlib.initArray(weightLayerTwo[0].length,0.0);
//                    double[] layerThree = vlib.initArray(weightLayerThree[0].length,0.0);
//
//                    for(i = 0; i < weightLayerOne.length; i++){
//                        for(j = 0; j < weightLayerOne[i].length; j++){
//                            if(i == weightLayerOne.length - 1){
//                                layerOne[j] = layerOne[j] + (1 * weightLayerOne[i][j]);
//                            }else {
//                                layerOne[j] = layerOne[j] + (fitur_data_tes[i] * weightLayerOne[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    double total_nilai = 0;
//                    double total_kuadrat = 0;
//                    double exp = 2.718281828;
//                    for(i = 0; i < layerOne.length; i++) {
//                        total_nilai = total_nilai + layerOne[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerOne[i], 2);
//                    }
//                    double average = total_nilai / layerOne.length;
//                    double total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    double varian = ((layerOne.length * total_kuadrat) - total_nilai_kuadrat) / (layerOne.length * (layerOne.length - 1));
//                    double standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerOne.length; i++){
//                        double x = (layerOne[i] - average) / standar_deviasi;
//                        layerOne[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER SATU : " + Arrays.toString(layerOne));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER SATU : " + layerOne.length);
//
//                    for(i = 0; i < weightLayerTwo.length; i++){
//                        for(j = 0; j < weightLayerTwo[i].length; j++){
//                            if(i == weightLayerTwo.length - 1){
//                                layerTwo[j] = layerTwo[j] + (1 * weightLayerTwo[i][j]);
//                            }else {
//                                layerTwo[j] = layerTwo[j] + (layerOne[i] * weightLayerTwo[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    total_nilai = 0;
//                    total_kuadrat = 0;
//                    exp = 2.718281828;
//                    for(i = 0; i < layerTwo.length; i++){
//                        total_nilai = total_nilai + layerTwo[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerTwo[i], 2);
//                    }
//                    average = total_nilai / layerTwo.length;
//                    total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    varian = ((layerTwo.length * total_kuadrat) - total_nilai_kuadrat) / (layerTwo.length * (layerTwo.length - 1));
//                    standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerTwo.length; i++){
//                        double x = (layerTwo[i] - average) / standar_deviasi;
//                        layerTwo[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER TWO : " + Arrays.toString(layerTwo));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER DUA : " + layerTwo.length);
//
//                    for(i = 0; i < weightLayerThree.length; i++){
//                        for(j = 0; j < weightLayerThree[i].length; j++){
//                            if(i == weightLayerThree.length - 1){
//                                layerThree[j] = layerThree[j] + (1 * weightLayerThree[i][j]);
//                            }else {
//                                layerThree[j] = layerThree[j] + (layerTwo[i] * weightLayerThree[i][j]);
//                            }
//                        }
//                    }
//
//                    //Normalisasi Sigmoidal
//                    total_nilai = 0;
//                    total_kuadrat = 0;
//                    exp = 2.718281828;
//                    for(i = 0; i < layerThree.length; i++){
//                        total_nilai = total_nilai + layerThree[i];
//                        total_kuadrat = total_kuadrat + Math.pow(layerThree[i], 2);
//                    }
//                    average = total_nilai / layerThree.length;
//                    total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    varian = ((layerThree.length * total_kuadrat) - total_nilai_kuadrat) / (layerThree.length * (layerThree.length - 1));
//                    standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < layerThree.length; i++){
//                        double x = (layerThree[i] - average) / standar_deviasi;
//                        layerThree[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }
//
//                    Log.d("PercobaanEkstraksi", "LAYER OUTPUT : " + Arrays.toString(layerThree));
//                    Log.d("PercobaanEkstraksi", "JUMLAH LAYER TIGA : " + layerThree.length);
//
//                    for(i = 0; i < layerThree.length; i++){
//                        if(layerThree[i] <= 1 || layerThree[i] >= 0.9){
//                            layerThree[i] = 1;
//                        } else if(layerThree[i] <= 0.1 || layerThree[i] >= -0.1){
//                            layerThree[i] = 0;
//                        }
//                    }

//                    Log.d("PercobaanEkstraksi", "HASIL PENGENALAN ADALAH : " + Arrays.toString(layerThree));

                    rows = fitur_datatraining.length;
                    cols = fitur_datatraining[0].length;

                    int kelas=20;

                    int[][] target = vlib.initArray(rows, kelas, 0);
                    int counter = 1;
                    int col = 0;
                    for (i = 0; i < rows; i++) {
                        target[i][col] = 1;
                        counter++;
                        if (counter > 20) {
                            counter = 1;
                            col++;
                        }
                    }

                    int jumlah_sample = 23, jumlah_output = target[0].length;
                    boolean ketemu;
                    int z = 0, y = 0;
                    int[][] hasil_output = vlib.initArray(jumlah_output, jumlah_sample, -1);
                    double dist, err=0, precision;
                    for (k = 0; k < fitur_datatraining.length; k++) {
                        double[] layerOne = vlib.initArray(weightLayerOne[0].length,0.0);
                        double[] layerTwo = vlib.initArray(weightLayerTwo[0].length,0.0);
                        double[] layerThree = vlib.initArray(weightLayerThree[0].length,0.0);

                        for(i = 0; i < weightLayerOne.length; i++){
                            for(j = 0; j < weightLayerOne[i].length; j++){
                                if(i == weightLayerOne.length - 1){
                                    layerOne[j] = layerOne[j] + (1 * weightLayerOne[i][j]);
                                }else {
                                    layerOne[j] = layerOne[j] + (fitur_datatraining[k][i] * weightLayerOne[i][j]);
                                }
                            }
                        }

                        //Normalisasi Sigmoidal
                        double total_nilai = 0;
                        double total_kuadrat = 0;
                        double exp = 2.718281828;
                        for(i = 0; i < layerOne.length; i++) {
                            total_nilai = total_nilai + layerOne[i];
                            total_kuadrat = total_kuadrat + Math.pow(layerOne[i], 2);
                        }
                        double average = total_nilai / layerOne.length;
                        double total_nilai_kuadrat = Math.pow(total_nilai, 2);
                        double varian = ((layerOne.length * total_kuadrat) - total_nilai_kuadrat) / (layerOne.length * (layerOne.length - 1));
                        double standar_deviasi = Math.sqrt(varian);
                        for(i = 0; i < layerOne.length; i++){
                            double x = (layerOne[i] - average) / standar_deviasi;
                            layerOne[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
                        }

                        Log.d("PercobaanEkstraksi", "LAYER SATU : " + Arrays.toString(layerOne));
                        Log.d("PercobaanEkstraksi", "JUMLAH LAYER SATU : " + layerOne.length);

                        for(i = 0; i < weightLayerTwo.length; i++){
                            for(j = 0; j < weightLayerTwo[i].length; j++){
                                if(i == weightLayerTwo.length - 1){
                                    layerTwo[j] = layerTwo[j] + (1 * weightLayerTwo[i][j]);
                                }else {
                                    layerTwo[j] = layerTwo[j] + (layerOne[i] * weightLayerTwo[i][j]);
                                }
                            }
                        }

                        //Normalisasi Sigmoidal
                        total_nilai = 0;
                        total_kuadrat = 0;
                        exp = 2.718281828;
                        for(i = 0; i < layerTwo.length; i++){
                            total_nilai = total_nilai + layerTwo[i];
                            total_kuadrat = total_kuadrat + Math.pow(layerTwo[i], 2);
                        }
                        average = total_nilai / layerTwo.length;
                        total_nilai_kuadrat = Math.pow(total_nilai, 2);
                        varian = ((layerTwo.length * total_kuadrat) - total_nilai_kuadrat) / (layerTwo.length * (layerTwo.length - 1));
                        standar_deviasi = Math.sqrt(varian);
                        for(i = 0; i < layerTwo.length; i++){
                            double x = (layerTwo[i] - average) / standar_deviasi;
                            layerTwo[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
                        }

                        Log.d("PercobaanEkstraksi", "LAYER TWO : " + Arrays.toString(layerTwo));
                        Log.d("PercobaanEkstraksi", "JUMLAH LAYER DUA : " + layerTwo.length);

                        for(i = 0; i < weightLayerThree.length; i++){
                            for(j = 0; j < weightLayerThree[i].length; j++){
                                if(i == weightLayerThree.length - 1){
                                    layerThree[j] = layerThree[j] + (1 * weightLayerThree[i][j]);
                                }else {
                                    layerThree[j] = layerThree[j] + (layerTwo[i] * weightLayerThree[i][j]);
                                }
                            }
                        }

                        //Normalisasi Sigmoidal
                        total_nilai = 0;
                        total_kuadrat = 0;
                        exp = 2.718281828;
                        for(i = 0; i < layerThree.length; i++){
                            total_nilai = total_nilai + layerThree[i];
                            total_kuadrat = total_kuadrat + Math.pow(layerThree[i], 2);
                        }
                        average = total_nilai / layerThree.length;
                        total_nilai_kuadrat = Math.pow(total_nilai, 2);
                        varian = ((layerThree.length * total_kuadrat) - total_nilai_kuadrat) / (layerThree.length * (layerThree.length - 1));
                        standar_deviasi = Math.sqrt(varian);
                        for(i = 0; i < layerThree.length; i++){
                            double x = (layerThree[i] - average) / standar_deviasi;
                            layerThree[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
                        }

//                        Log.d("PercobaanEkstraksi", "LAYER OUTPUT : " + Arrays.toString(layerThree));
//                        Log.d("PercobaanEkstraksi", "JUMLAH LAYER TIGA : " + layerThree.length);

//                        for(i = 0; i < layerThree.length; i++){
//                            if(layerThree[i] <= 1 || layerThree[i] >= 0.9){
//                                layerThree[i] = 1;
//                            } else if(layerThree[i] <= 0.1 || layerThree[i] >= -0.1){
//                                layerThree[i] = 0;
//                            }
//                        }

                        double data_terbesar = 0;
                        double data_i = 0;
                        for(i = 0; i < layerThree.length; i++){
                            if(i == 0) {
                                data_terbesar = layerThree[i];
                                data_i = i;
                            }else{
                                if(data_terbesar < layerThree[i]){
                                    data_terbesar = layerThree[i];
                                    data_i = i;
                                }
                            }
                        }

                        for(i = 0; i < layerThree.length; i++){
                            if(data_i == i){
                                layerThree[i] = 1;
                            }else{
                                layerThree[i] = 0;
                            }
                        }

                        Log.d("PercobaanEkstraksi", "HASIL PENGENALAN : " + Arrays.toString(layerThree));

                        dist=vlib.getDistance(layerThree, target[k]);
                        if (dist>0) err++;
                        ketemu = false;
                        j = 0;
                        while ((j < target[0].length) && (!ketemu)) {
                            if (layerThree[j] == 1.0) {
                                ketemu = true;
                            } else {
                                j++;
                            }
                        }
                        if (ketemu) hasil_output[y][z] = (int) j+1;

                        z++;
                        if (z == jumlah_sample) {
                            z = 0;
                            y++;
                        }
                    }

                    Log.d("PercobaanEkstraksi", "HASIL KESELURUHAN : " + Arrays.deepToString(hasil_output));

//                    double data_terbesar = 0;
//                    double data_i = 0;
//                    for(i = 0; i < layerThree.length; i++){
//                        if(i == 0) {
//                            data_terbesar = layerThree[i];
//                            data_i = i;
//                        }else{
//                            if(data_terbesar < layerThree[i]){
//                                data_terbesar = layerThree[i];
//                                data_i = i;
//                            }
//                        }
//                    }

//                    Log.d("PercobaanEkstraksi", "HASIL PENGENALAN ADALAH : " + data_i);

                    progress.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImg.setImageBitmap(resizeBitmap);
                        }
                    });
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public int[][] doZhangSuenThinning(final int[][] givenImage) {
        int[][] binaryImage;
        //binaryImage = givenImage;
        binaryImage = givenImage.clone();
        int a, b;
        List<Point> pointsToChange = new LinkedList();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        //binaryImage[y][x] = 0;
                        hasChange = true;
                    }
                }
            }
            for (com.yafieimam.nuhanaapp.Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
        } while (hasChange);
        return binaryImage;
    }

    private int getA(int[][] binaryImage, int y, int x) {
        int count = 0;
//p2 p3
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
//p3 p4
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
//p4 p5
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
//p5 p6
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
//p6 p7
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
//p7 p8
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
//p8 p9
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
//p9 p2
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
            count++;
        }
        return count;
    }

    private int getB(int[][] binaryImage, int y, int x) {
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }

    private Bitmap trim(Bitmap bitmap, int trimColor){
        int minX = Integer.MAX_VALUE;
        int maxX = 0;
        int minY = Integer.MAX_VALUE;
        int maxY = 0;

        for(int x = 0; x < bitmap.getWidth(); x++){
            for(int y = 0; y < bitmap.getHeight(); y++){
                if(bitmap.getPixel(x, y) != trimColor){
                    if(x < minX){
                        minX = x;
                    }
                    if(x > maxX){
                        maxX = x;
                    }
                    if(y < minY){
                        minY = y;
                    }
                    if(y > maxY){
                        maxY = y;
                    }
                }
            }
        }
        return Bitmap.createBitmap(bitmap, minX - 1, minY - 1, maxX - minX + 1, maxY - minY + 1);
    }

    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> list = new ArrayList<String>();
        for(int i = 1; i <= 12; i++) {
            list.add("Ekstraksi Fitur " + i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String label = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
