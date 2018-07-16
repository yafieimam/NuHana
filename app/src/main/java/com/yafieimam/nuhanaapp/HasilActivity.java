package com.yafieimam.nuhanaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ALI.ClusteringLib;
import ALI.VectorLib;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class HasilActivity extends AppCompatActivity {
    ImageView mImg, mImg2, mImg3, mImg4;
    Button mBtn;
    TextView txtHasil;
    Bitmap b, mutableBitmap, clusterBitmap, bitmap1, bitmap2, bitmap3, bitmap4, bitmapAsli1, bitmapAsli2, bitmap;
    Bitmap bitmapAsli3, bitmapAsli4, resizeBitmapAsli1, resizeBitmapAsli2, resizeBitmapAsli3, resizeBitmapAsli4;
    int jumlahData, bitmapHeight, bitmapWidth;
    double hasil;
    int[] clusters;
    double[][] dataReal;
    private ProgressDialog progress;
    static List<Map<String,Integer>> dataRow = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private double graph2LastXValue = 5d;
    Mat result = new Mat();

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d("HasilActivity", "OpenCV Not Loaded");
        }else{
            Log.d("HasilActivity", "OpenCV Successfully Loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_hasil);

        mImg = (ImageView) findViewById(R.id.imageViewHasil);
        mImg2 = (ImageView) findViewById(R.id.imageViewHasil2);
        mImg3 = (ImageView) findViewById(R.id.imageViewHasil3);
        mImg4 = (ImageView) findViewById(R.id.imageViewHasil4);
        mBtn = (Button) findViewById(R.id.btnEkstraksiFitur);
        progress=new ProgressDialog(HasilActivity.this);
        progress.setMax(100);
        progress.setMessage("Loading...Processing Data...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();
        progress.setCancelable(false);

        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                try {
                    sleep(1000);

                    Bundle bundle = getIntent().getExtras();
                    int[][] binerData = (int[][]) bundle.getSerializable("ArrayBiner");
                    jumlahData = bundle.getInt("JumlahData");
                    bitmapHeight = bundle.getInt("ImageHeight");
                    bitmapWidth = bundle.getInt("ImageWidth");
                    int[][] imageData = new int[bitmapHeight * bitmapWidth][3];

                    Log.d("HasilActivity", "Biner Data = " + Arrays.deepToString(binerData));
                    Log.d("HasilActivity", "Ukuran Bitmap = " + bitmapWidth + " dan " + bitmapHeight);


                    doZhangSuenThinning(binerData);
//                    String filename = getIntent().getStringExtra("BitmapImage");
//                    try {
//                        FileInputStream is = new FileInputStream(new File(filename));
//                        b = BitmapFactory.decodeStream(is);
//                        is.close();
////                        File delete_image = new File(filename);
////                        if (delete_image.canRead()) {
////                            boolean deleteFile = delete_image.delete();
////                            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filename))));
////                            if (deleteFile) {
////                                Toast.makeText(getApplicationContext(), "File deleted", Toast.LENGTH_LONG).show();
////                            }
////                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);

//                    Bitmap trimBitmap = trim(b, Color.WHITE);
                    jumpTime += 10;
                    progress.setProgress(jumpTime);

//                    Mat mat = new Mat();
//                    Bitmap bmp32 = trimBitmap.copy(Bitmap.Config.ARGB_8888, true);
//                    Utils.bitmapToMat(bmp32, mat);
//
//                    Mat threeChannel = new Mat();
//
//                    Imgproc.cvtColor(mat, threeChannel, Imgproc.COLOR_BGR2GRAY);
//
//                    org.opencv.core.Size s = new Size(3,3);
//                    Imgproc.GaussianBlur(threeChannel, threeChannel, s, 0);
//                    Imgproc.threshold(threeChannel, threeChannel, 0, 255, Imgproc.THRESH_OTSU);

//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);

//                    Utils.matToBitmap(threeChannel, bmp32, true);

//                    resizeBitmap = Bitmap.createScaledBitmap(trimBitmap, 100, 100, true);

//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);

//                    int jumlahData = 0;
//
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                jumlahData++;
//                            }
//                        }
//                    }

//                    int[][] imageData = new int[jumlahData][2];
//
//                    int i = 0;
//                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                i++;
//                            }
//                        }
//                    }
//                    bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

                    int i = 0, jumlahDataHitam = 0;
                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (binerData[y][x] == 1) {
                                imageData[i][0] = x;
                                imageData[i][1] = y;
                                imageData[i][2] = 1;
                                jumlahDataHitam++;
                            } else {
                                imageData[i][0] = x;
                                imageData[i][1] = y;
                                imageData[i][2] = 0;
                            }
                            i++;
                        }
                    }

//                    int i = 0, jumlahDataHitam = 0;
//                    for (int y = 0; y < bitmap.getHeight(); y++) {
//                        for (int x = 0; x < bitmap.getWidth(); x++) {
//                            if (bitmap.getPixel(x, y) == Color.BLACK) {
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 1;
//                                jumlahDataHitam++;
//                            } else if(bitmap.getPixel(x,y) == Color.WHITE){
//                                imageData[i][0] = x;
//                                imageData[i][1] = y;
//                                imageData[i][2] = 0;
//                            }
//                            i++;
//                        }
//                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    int[][] imageDataHitam = new int[jumlahDataHitam][2];

                    i = 0;
                    for (int y = 0; y < imageData.length; y++) {
                        if (imageData[y][2] == 1) {
                            imageDataHitam[i][0] = imageData[y][0];
                            imageDataHitam[i][1] = imageData[y][1];
                            i++;
                        }
                    }

                    ClusteringLib clib = new ClusteringLib();

//                    int[] clusters=clib.ShapeIndependentClustering(imageDataHitam);
//                    int[] clusters=clib.AutomaticClustering("single", imageDataHitam);

                    double[] nilai_centroid = clib.getCentroid(imageDataHitam);

                    Log.d("HasilActivity", "Nilai Centroid: " + Arrays.toString(nilai_centroid));

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

                    Log.d("HasilActivity", "Nilai Terkecil: " + minValueX + " dan " + minValueY);
                    Log.d("HasilActivity", "Nilai Terbesar: " + maxValueX + " dan " + maxValueY);

                    int jumlahDataHitam_Satu = 0;
                    int jumlahDataHitam_Dua = 0;
                    int jumlahDataHitam_Tiga = 0;
                    int jumlahDataHitam_Empat = 0;

                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (binerData[y][x] == 1) {
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

                    Log.d("HasilActivity", "JumlahDataHitam: " + jumlahDataHitam);
                    Log.d("HasilActivity", "JumlahDataHitam Per Kuadran: " + jumlahDataHitam_Satu + ", " + jumlahDataHitam_Dua + ", " + jumlahDataHitam_Tiga + ", " + jumlahDataHitam_Empat);

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

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

                    bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (binerData[y][x] == 1) {
                                bitmap.setPixel(x, y, Color.BLACK);
                            } else {
                                bitmap.setPixel(x, y, Color.WHITE);
                            }
                        }
                    }

                    double[] nilai_total = new double[54];

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


                    for (int y = 0; y < binerData.length; y++) {
                        for (int x = 0; x < binerData[y].length; x++) {
                            if (binerData[y][x] == 1) {
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

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    int chunkWidth = bitmap.getWidth() / cols;
                    int chunkHeight = bitmap.getHeight() / rows;

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

                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    chunkWidth = bitmap.getWidth() / cols;
                    chunkHeight = bitmap.getHeight() / rows;

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

                    Log.d("HasilActivity", "Nilai Total: " + Arrays.toString(nilai_total));

                    DatabaseHandler dbHandler = new DatabaseHandler(HasilActivity.this);
//                    ArrayList<ArrayList<Double>> data = dbHandler.getFeatureDataTraining(nilai_total);
                    double[][] data = dbHandler.getFeatureDataTraining(nilai_total);

//                    for(ArrayList<Double> nilaidata : data){
//                        for(Double nilai_data : nilaidata){
//                            Log.d("Hasil Activity", "Data = " + nilai_data);
//                        }
//                    }
//                    Log.d("Hasil Activity", "Jumlah Data dan Fiturnya: " + data.size() + " dan " + data.get(0).size());

//                    double[][] data_array = new double[data.size()][data.get(0).size()];
//                    for (i = 0; i < data.size(); i++){
//                        ArrayList<Double> row = data.get(i);
//                        double[] copy = new double[row.size()];
//                        for (j = 0; j < row.size(); j++) {
//                            copy[j] = row.get(j);
//                        }
//
//                        data_array[i] = copy;
//                    }

                    VectorLib vlib = new VectorLib();
                    data = vlib.Normalization("minmax", data, 0, 1);

//                    Log.d("HasilActivity", "Data Fitur: " + Arrays.toString(data[20]));
//                    Log.d("Hasil Activity", "Jumlah Data dan Fiturnya: " + data.length + " dan " + data[0].length);

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

//                    Log.d("HasilActivity", "Data Fitur 1 : " + Arrays.toString(fitur_datates));

                    double[] fitur_data_tes = ArrayUtils.toPrimitive(fitur_datates);
                    Log.d("HasilActivity", "Data Fitur Tes : " + Arrays.toString(fitur_data_tes));

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

                    double[] layerOne = vlib.initArray(weightLayerOne[0].length,0.0);
                    double[] layerTwo = vlib.initArray(weightLayerTwo[0].length,0.0);
                    double[] layerThree = vlib.initArray(weightLayerThree[0].length,0.0);

                    //Normalisasrrri Sigmoidal
//                    double total_nilai = 0;
//                    double total_kuadrat = 0;
//                    double exp = 2.718281828;
//                    for(i = 0; i < fitur_data_tes.length; i++){
//                        total_nilai = total_nilai + fitur_data_tes[i];
//                        total_kuadrat = total_kuadrat + Math.pow(fitur_data_tes[i], 2);
//                    }
//                    double average = total_nilai / fitur_data_tes.length;
//                    double total_nilai_kuadrat = Math.pow(total_nilai, 2);
//                    double varian = ((fitur_data_tes.length * total_kuadrat) - total_nilai_kuadrat) / (fitur_data_tes.length * (fitur_data_tes.length - 1));
//                    double standar_deviasi = Math.sqrt(varian);
//                    for(i = 0; i < fitur_data_tes.length; i++){
//                        double x = (fitur_data_tes[i] - average) / standar_deviasi;
//                        fitur_data_tes[i] = (1 - Math.pow(exp, -(x))) / (1 + Math.pow(exp, -(x)));
//                    }

                    double[] datates = new double[]{0.441325345,0.736166289,0.602939669,0.916084784,0.411315299,0.812910348,0.460613202,0.871304113,0.176840228,0.476890694,0.633571581,0.826641108,0.532706345,0.251935614,0.225506943,0.556505655,0.498064786,0.293510744,0.454014981,0.767973475,0.303168801,0.170164477,0.564026526,0.293538479,0.840932261,0.520017559,0.695561244,0.850774796,0.381138082,0.543632131,0.465463103,0.329644019,0.205813206,0.178342099,0.801659437,0,0.873417722,0.584415584,0.465753425,0.382022472,0.411764706,0.530120482,0.478873239,0.476923077,0.539473684,1,0.623209168,0.522206305,0.463024372,0.456930511,0.701764072,0.44946601,0.559123,0.490009048};

                    for(i = 0; i < weightLayerOne.length; i++){
                        for(j = 0; j < weightLayerOne[i].length; j++){
                            if(i == weightLayerOne.length - 1){
                                layerOne[j] = layerOne[j] + (1 * weightLayerOne[i][j]);
                            }else {
                                layerOne[j] = layerOne[j] + (datates[i] * weightLayerOne[i][j]);
                            }
                        }
                    }

                    //Normalisasi Sigmoidal
                    double total_nilai = 0;
                    double total_kuadrat = 0;
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
                        layerOne[i] = 1 / (1 + Math.pow(Math.E, -(x)));
                    }

                    Log.d("HasilActivity", "LAYER SATU : " + Arrays.toString(layerOne));
                    Log.d("HasilActivity", "JUMLAH LAYER SATU : " + layerOne.length);

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
                        layerTwo[i] = 1 / (1 + Math.pow(Math.E, -(x)));
                    }

                    Log.d("HasilActivity", "LAYER TWO : " + Arrays.toString(layerTwo));
                    Log.d("HasilActivity", "JUMLAH LAYER DUA : " + layerTwo.length);

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
                        layerThree[i] = 1 / (1 + Math.pow(Math.E, -(x)));
                    }

                    Log.d("HasilActivity", "LAYER OUTPUT : " + Arrays.toString(layerThree));
                    Log.d("HasilActivity", "JUMLAH LAYER TIGA : " + layerThree.length);

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
//
//                    for(i = 0; i < layerThree.length; i++){
//                        if(data_i == i){
//                            layerThree[i] = 1;
//                        }else{
//                            layerThree[i] = 0;
//                        }
//                    }

                    for(i = 0; i < layerThree.length; i++){
                        if(layerThree[i] <= 1 && layerThree[i] >= 0.9){
                            layerThree[i] = 1;
                        } else if(layerThree[i] <= 0.1 && layerThree[i] >= -0.1){
                            layerThree[i] = 0;
                        }
                    }


                    Log.d("HasilActivity", "HASIL PENGENALAN ADALAH : " + Arrays.toString(layerThree));

//                    Log.d("HasilActivity", "Data Fitur 2 : " + Arrays.toString(fitur_data_tes));
//                    Log.d("HasilActivity", "Data Fitur: " + Arrays.deepToString(fitur_datatraining));
//                    Log.d("Hasil Activity", "Jumlah Data dan Fiturnya: " + fitur_datatraining.length + " dan " + fitur_datatraining[0].length);

//                    int[] label = dbHandler.getLabelDataTraining();
//
//                    Log.d("HasilActivity", "Label: " + Arrays.toString(label));

//                    int[][] target = vlib.initArray(fitur_datatraining.length, 20, 0);
//                    int counter = 1;
//                    int col = 0;
//                    for (i = 0; i < rows; i++) {
//                        target[i][col] = 1;
//                        counter++;
//                        if (counter > 20) {
//                            counter = 1;
//                            col++;
//                        }
//                    }

//                    double miu = 0.5;
//                    int[] hidden_unit = {43, 32};
//                    int epoch = 1000;
//                    double[] output;
//
//                    NN nn = new NN(fitur_datatraining, target, miu, hidden_unit);
//                    nn.RunLearning(epoch, false);
//
//                    output = nn.Test(fitur_data_tes);
//                    Log.d("HasilActivity", "Output: " + Arrays.toString(output));

//                    boolean ada = false;
//                    for(int j = 0; j<clusters.length; j++){
//                        if(clusters[j] == 0){
//                            ada = true;
//                            break;
//                        }
//                    }
//
//                    if(ada){
//                        bitmap1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                        Log.d("HasilActivity", "Bitmap 1 ADA");
//                        ada = false;
//                        for(int j = 0; j<clusters.length; j++){
//                            if(clusters[j] == 1){
//                                ada = true;
//                                break;
//                            }
//                        }
//                        if(ada){
//                            bitmap2 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                            ada = false;
//                            for(int j = 0; j<clusters.length; j++){
//                                if(clusters[j] == 2){
//                                    ada = true;
//                                    break;
//                                }
//                            }
//                            if(ada){
//                                bitmap3 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                                ada = false;
//                                for(int j = 0; j<clusters.length; j++){
//                                    if(clusters[j] == 2){
//                                        ada = true;
//                                        break;
//                                    }
//                                }
//                                if(ada){
//                                    bitmap4 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                                }
//                            }
//                        }
//                    }
//                    bitmap1 = Bitmap.createBitmap(resizeBitmap.getWidth(), resizeBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//                    bitmap2 = Bitmap.createBitmap(resizeBitmap.getWidth(), resizeBitmap.getHeight(), Bitmap.Config.ARGB_8888);

//                    for (int y = 0; y < 100; y++) {
//                        for (int x = 0; x < 100; x++) {
//                            int z = 0;
//                            int coor_x = 0;
//                            int coor_y = 0;
//                            if(x == 0){
//                                coor_x = 1;
//                                coor_y = 1;
//                            }else{
//                                coor_x = 0;
//                                coor_y = 0;
//                            }
//                            boolean ketemu = false;
//                            do{
//                                if(bitmap1 != null && x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 0){
//                                    bitmap1.setPixel(x, y, Color.BLACK);
//                                    coor_x = x;
//                                    coor_y = y;
//                                    ketemu = true;
//                                }else if(bitmap2 != null && x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 1){
//                                    bitmap2.setPixel(x, y, Color.BLACK);
//                                    coor_x = x;
//                                    coor_y = y;
//                                    ketemu = true;
//                                }else if(bitmap3 != null && x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 2){
//                                    bitmap3.setPixel(x, y, Color.BLACK);
//                                    coor_x = x;
//                                    coor_y = y;
//                                    ketemu = true;
//                                }else if(bitmap4 != null && x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 3){
//                                    bitmap4.setPixel(x, y, Color.BLACK);
//                                    coor_x = x;
//                                    coor_y = y;
//                                    ketemu = true;
//                                }
//                                z++;
//                            }while(z < jumlahData && ketemu == false);
//                            if(ketemu == false && coor_x != x && coor_y != y){
//                                if(bitmap1 != null){
//                                    bitmap1.setPixel(x, y, Color.WHITE);
//                                }
//                                if(bitmap2 != null){
//                                    bitmap2.setPixel(x, y, Color.WHITE);
//                                }
//                                if(bitmap3 != null){
//                                    bitmap3.setPixel(x, y, Color.WHITE);
//                                }
//                                if(bitmap4 != null){
//                                    bitmap4.setPixel(x, y, Color.WHITE);
//                                }
////                                bitmap1.setPixel(x, y, Color.WHITE);
////                                bitmap2.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }
//
//                    jumpTime += 10;
//                    progress.setProgress(jumpTime);
//
//                    int jumlahDataBitmap1 = 0;
//                    int jumlahDataBitmap2 = 0;
//                    int jumlahDataBitmap3 = 0;
//                    int jumlahDataBitmap4 = 0;
//
//                    if(bitmap1 != null){
//                        for (int y = 0; y < bitmap1.getHeight(); y++) {
//                            for (int x = 0; x < bitmap1.getWidth(); x++) {
//                                if (bitmap1.getPixel(x, y) == Color.BLACK) {
//                                    jumlahDataBitmap1++;
//                                }
//                            }
//                        }
//                    }
//
//                    if(bitmap2 != null){
//                        for (int y = 0; y < bitmap2.getHeight(); y++) {
//                            for (int x = 0; x < bitmap2.getWidth(); x++) {
//                                if (bitmap2.getPixel(x, y) == Color.BLACK) {
//                                    jumlahDataBitmap2++;
//                                }
//                            }
//                        }
//                    }
//
//                    if(bitmap3 != null){
//                        for (int y = 0; y < bitmap3.getHeight(); y++) {
//                            for (int x = 0; x < bitmap3.getWidth(); x++) {
//                                if (bitmap3.getPixel(x, y) == Color.BLACK) {
//                                    jumlahDataBitmap3++;
//                                }
//                            }
//                        }
//                    }
//
//                    if(bitmap4 != null){
//                        for (int y = 0; y < bitmap4.getHeight(); y++) {
//                            for (int x = 0; x < bitmap4.getWidth(); x++) {
//                                if (bitmap4.getPixel(x, y) == Color.BLACK) {
//                                    jumlahDataBitmap4++;
//                                }
//                            }
//                        }
//                    }

//                    for (int y = 0; y < bitmap1.getHeight(); y++) {
//                        for (int x = 0; x < bitmap1.getWidth(); x++) {
//                            if (bitmap1.getPixel(x, y) == Color.BLACK) {
//                                jumlahDataBitmap1++;
//                            }
//                        }
//                    }
//
//                    for (int y = 0; y < bitmap2.getHeight(); y++) {
//                        for (int x = 0; x < bitmap2.getWidth(); x++) {
//                            if (bitmap2.getPixel(x, y) == Color.BLACK) {
//                                jumlahDataBitmap2++;
//                            }
//                        }
//                    }

//                    int[][] dataBitmap1;
//                    int[][] dataBitmap2;
//                    int[][] dataBitmap3;
//                    int[][] dataBitmap4;
//
//                    if(jumlahDataBitmap1 > 0){
//                        dataBitmap1 = new int[jumlahDataBitmap1][2];
//                        int j = 0;
//                        for (int y = 0; y < bitmap1.getHeight(); y++) {
//                            for (int x = 0; x < bitmap1.getWidth(); x++) {
//                                if (bitmap1.getPixel(x, y) == Color.BLACK) {
//                                    dataBitmap1[j][0] = x;
//                                    dataBitmap1[j][1] = y;
//                                    j++;
//                                }
//                            }
//                        }
//
//                        Log.d("HasilActivity", "Jumlah Data Bitmap 1 = " + jumlahDataBitmap1);
//                        Log.d("HasilActivity", "Ukuran Bitmap 1 = " + bitmap1.getWidth() + " dan " + bitmap1.getHeight());
//
//                        bitmapAsli1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//
//                        for (int y = 0; y < bitmapAsli1.getHeight(); y++) {
//                            for (int x = 0; x < bitmapAsli1.getWidth(); x++) {
//                                int z = 0;
//                                boolean ketemu = false;
//                                do{
//                                    if (x == dataBitmap1[z][0] && y == dataBitmap1[z][1]) {
//                                        bitmapAsli1.setPixel(x, y, Color.BLACK);
//                                        ketemu = true;
//                                    }
//                                    z++;
//                                }while(z < jumlahDataBitmap1 && ketemu == false);
//                                if(ketemu == false){
//                                    bitmapAsli1.setPixel(x, y, Color.WHITE);
//                                }
//                            }
//                        }
//                    }
//                    if(jumlahDataBitmap2 > 0){
//                        dataBitmap2 = new int[jumlahDataBitmap2][2];
//                        int j = 0;
//                        for (int y = 0; y < bitmap2.getHeight(); y++) {
//                            for (int x = 0; x < bitmap2.getWidth(); x++) {
//                                if (bitmap2.getPixel(x, y) == Color.BLACK) {
//                                    dataBitmap2[j][0] = x;
//                                    dataBitmap2[j][1] = y;
//                                    j++;
//                                }
//                            }
//                        }
//
//                        Log.d("HasilActivity", "Jumlah Data Bitmap 2 = " + jumlahDataBitmap2);
//                        Log.d("HasilActivity", "Ukuran Bitmap 2 = " + bitmap2.getWidth() + " dan " + bitmap2.getHeight());
//
//                        bitmapAsli2 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//
//                        for (int y = 0; y < bitmapAsli2.getHeight(); y++) {
//                            for (int x = 0; x < bitmapAsli2.getWidth(); x++) {
//                                int z = 0;
//                                boolean ketemu = false;
//                                do{
//                                    if (x == dataBitmap2[z][0] && y == dataBitmap2[z][1]) {
//                                        bitmapAsli2.setPixel(x, y, Color.BLACK);
//                                        ketemu = true;
//                                    }
//                                    z++;
//                                }while(z < jumlahDataBitmap2 && ketemu == false);
//                                if(ketemu == false){
//                                    bitmapAsli2.setPixel(x, y, Color.WHITE);
//                                }
//                            }
//                        }
//                    }
//                    if(jumlahDataBitmap3 > 0){
//                        dataBitmap3 = new int[jumlahDataBitmap3][2];
//                        int j = 0;
//                        for (int y = 0; y < bitmap3.getHeight(); y++) {
//                            for (int x = 0; x < bitmap3.getWidth(); x++) {
//                                if (bitmap3.getPixel(x, y) == Color.BLACK) {
//                                    dataBitmap3[j][0] = x;
//                                    dataBitmap3[j][1] = y;
//                                    j++;
//                                }
//                            }
//                        }
//
//                        Log.d("HasilActivity", "Jumlah Data Bitmap 3 = " + jumlahDataBitmap3);
//                        Log.d("HasilActivity", "Ukuran Bitmap 3 = " + bitmap3.getWidth() + " dan " + bitmap3.getHeight());
//
//                        bitmapAsli3 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//
//                        for (int y = 0; y < bitmapAsli3.getHeight(); y++) {
//                            for (int x = 0; x < bitmapAsli3.getWidth(); x++) {
//                                int z = 0;
//                                boolean ketemu = false;
//                                do{
//                                    if (x == dataBitmap3[z][0] && y == dataBitmap3[z][1]) {
//                                        bitmapAsli3.setPixel(x, y, Color.BLACK);
//                                        ketemu = true;
//                                    }
//                                    z++;
//                                }while(z < jumlahDataBitmap3 && ketemu == false);
//                                if(ketemu == false){
//                                    bitmapAsli3.setPixel(x, y, Color.WHITE);
//                                }
//                            }
//                        }
//                    }
//                    if(jumlahDataBitmap4 > 0){
//                        dataBitmap4 = new int[jumlahDataBitmap4][2];
//                        int j = 0;
//                        for (int y = 0; y < bitmap4.getHeight(); y++) {
//                            for (int x = 0; x < bitmap4.getWidth(); x++) {
//                                if (bitmap4.getPixel(x, y) == Color.BLACK) {
//                                    dataBitmap4[j][0] = x;
//                                    dataBitmap4[j][1] = y;
//                                    j++;
//                                }
//                            }
//                        }
//
//                        Log.d("HasilActivity", "Jumlah Data Bitmap 4 = " + jumlahDataBitmap4);
//                        Log.d("HasilActivity", "Ukuran Bitmap 4 = " + bitmap4.getWidth() + " dan " + bitmap4.getHeight());
//
//                        bitmapAsli4 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//
//                        for (int y = 0; y < bitmapAsli4.getHeight(); y++) {
//                            for (int x = 0; x < bitmapAsli4.getWidth(); x++) {
//                                int z = 0;
//                                boolean ketemu = false;
//                                do{
//                                    if (x == dataBitmap4[z][0] && y == dataBitmap4[z][1]) {
//                                        bitmapAsli4.setPixel(x, y, Color.BLACK);
//                                        ketemu = true;
//                                    }
//                                    z++;
//                                }while(z < jumlahDataBitmap4 && ketemu == false);
//                                if(ketemu == false){
//                                    bitmapAsli4.setPixel(x, y, Color.WHITE);
//                                }
//                            }
//                        }
//                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

//                    double terkecilX1 = dataBitmap1[0][0];
//                    double terkecilY1 = dataBitmap1[0][1];
//                    double terbesarX1 = dataBitmap1[0][0];
//                    double terbesarY1 = dataBitmap1[0][1];
//                    double terkecilX2 = dataBitmap2[0][0];
//                    double terkecilY2 = dataBitmap2[0][1];
//                    double terbesarX2 = dataBitmap2[0][0];
//                    double terbesarY2 = dataBitmap2[0][1];
//
//                    for(int a = 1; a < jumlahDataBitmap1; a++){
//                        if(terkecilX1 > dataBitmap1[a][0] && terkecilY1 > dataBitmap1[a][1]){
//                            terkecilX1 = dataBitmap1[a][0];
//                            terkecilY1 = dataBitmap1[a][1];
//                        }
//
//                        if(terbesarX1 < dataBitmap1[a][0] && terbesarY1 < dataBitmap1[a][1]){
//                            terbesarX1 = dataBitmap1[a][0];
//                            terbesarY1 = dataBitmap1[a][1];
//                        }
//                    }
//
//                    for(int a = 1; a < jumlahDataBitmap2; a++){
//                        if(terkecilX2 > dataBitmap2[a][0] && terkecilY2 > dataBitmap2[a][1]){
//                            terkecilX2 = dataBitmap2[a][0];
//                            terkecilY2 = dataBitmap2[a][1];
//                        }
//
//                        if(terbesarX2 < dataBitmap2[a][0] && terbesarY2 < dataBitmap2[a][1]){
//                            terbesarX2 = dataBitmap2[a][0];
//                            terbesarY2 = dataBitmap2[a][1];
//                        }
//                    }

//                    Log.d("HasilActivity", "Koordinat Terkecil Bitmap 1 = " + terkecilX1 + " dan " + terkecilY1);
//                    Log.d("HasilActivity", "Koordinat Terbesar Bitmap 1 = " + terbesarX1 + " dan " + terbesarY1);
//                    Log.d("HasilActivity", "Koordinat Terkecil Bitmap 2 = " + terkecilX2 + " dan " + terkecilY2);
//                    Log.d("HasilActivity", "Koordinat Terbesar Bitmap 2 = " + terbesarX2 + " dan " + terbesarY2);

//                    bitmapAsli1 = Bitmap.createBitmap((int) terbesarX1 + 10, (int) terbesarY1 + 10, Bitmap.Config.ARGB_8888);
//                    bitmapAsli2 = Bitmap.createBitmap((int) terbesarX2 + 10, (int) terbesarY2 + 10, Bitmap.Config.ARGB_8888);

//                    for (int y = 0; y < bitmapAsli1.getHeight(); y++) {
//                        for (int x = 0; x < bitmapAsli1.getWidth(); x++) {
//                            int z = 0;
//                            boolean ketemu = false;
//                            do{
//                                if (x == dataBitmap1[z][0] && y == dataBitmap1[z][1]) {
//                                    bitmapAsli1.setPixel(x, y, Color.BLACK);
//                                    ketemu = true;
//                                }
//                                z++;
//                            }while(z < jumlahDataBitmap1 && ketemu == false);
//                            if(ketemu == false){
//                                bitmapAsli1.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }

//                    for (int y = 0; y < bitmapAsli2.getHeight(); y++) {
//                        for (int x = 0; x < bitmapAsli2.getWidth(); x++) {
//                            int z = 0;
//                            boolean ketemu = false;
//                            do{
//                                if (x == dataBitmap2[z][0] && y == dataBitmap2[z][1]) {
//                                    bitmapAsli2.setPixel(x, y, Color.BLACK);
//                                    ketemu = true;
//                                }
//                                z++;
//                            }while(z < jumlahDataBitmap2 && ketemu == false);
//                            if(ketemu == false){
//                                bitmapAsli2.setPixel(x, y, Color.WHITE);
//                            }
//                        }
//                    }

//                    bitmapAsli2 = trim_bitmap_kecil(bitmap2, Color.BLACK);

//                    if(bitmapAsli1 != null){
//                        bitmapAsli1 = trim_bitmap_kecil(bitmapAsli1, Color.BLACK);
//                        resizeBitmapAsli1 = Bitmap.createScaledBitmap(bitmapAsli1, 100, 100, true);
//                    }
//                    if(bitmapAsli2 != null){
//                        bitmapAsli2 = trim_bitmap_kecil(bitmapAsli2, Color.BLACK);
//                        resizeBitmapAsli2 = Bitmap.createScaledBitmap(bitmapAsli2, 100, 100, true);
//                    }
//                    if(bitmapAsli3 != null){
//                        bitmapAsli3 = trim_bitmap_kecil(bitmapAsli3, Color.BLACK);
//                        resizeBitmapAsli3 = Bitmap.createScaledBitmap(bitmapAsli3, 100, 100, true);
//                    }
//                    if(bitmapAsli4 != null){
//                        bitmapAsli4 = trim_bitmap_kecil(bitmapAsli4, Color.BLACK);
//                        resizeBitmapAsli4 = Bitmap.createScaledBitmap(bitmapAsli4, 100, 100, true);
//                    }

                    progress.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImg.setImageBitmap(bitmap);
//                            if(resizeBitmapAsli1 != null){
//                                mImg.setImageBitmap(resizeBitmapAsli1);
//                            }
//                            if(resizeBitmapAsli2 != null){
//                                mImg2.setImageBitmap(resizeBitmapAsli2);
//                            }
//                            if(resizeBitmapAsli3 != null){
//                                mImg3.setImageBitmap(resizeBitmapAsli3);
//                            }
//                            if(resizeBitmapAsli4 != null){
//                                mImg4.setImageBitmap(resizeBitmapAsli4);
//                            }
                            mBtn.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v) {
                                    Intent intent = new Intent(HasilActivity.this, EkstraksiFiturActivity.class);
                                    if(resizeBitmapAsli1 != null){
                                        intent.putExtra("BitmapImage1", resizeBitmapAsli1);
                                    }
                                    if(resizeBitmapAsli2 != null){
                                        intent.putExtra("BitmapImage2", resizeBitmapAsli2);
                                    }
                                    if(resizeBitmapAsli3 != null){
                                        intent.putExtra("BitmapImage3", resizeBitmapAsli3);
                                    }
                                    if(resizeBitmapAsli4 != null){
                                        intent.putExtra("BitmapImage4", resizeBitmapAsli4);
                                    }
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();

//        int jumlahData = 0;
//
//        for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//            for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                    jumlahData++;
//                }
//            }
//        }
//
//        int[][] imageData = new int[jumlahData][2];

        //Log.d("HasilActivity", "Size = " + imageData.length);

//        doZhangSuenThinning(imageData);

//        int i = 0;
//        for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//            for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
//                    imageData[i][0] = x;
//                    imageData[i][1] = y;
//                    i++;
//                }
//            }
//        }
//
//        VectorLib vlib = new VectorLib();
//        ClusteringLib clib = new ClusteringLib();
//
//        int[] clusters=clib.ShapeIndependentClustering(imageData);

        //Log.d("HasilActivity", "Size = " + clusters.length);
        //Log.d("Ini vectornya", "arr:" + Arrays.toString(clusters));

//        bitmap1 = Bitmap.createBitmap(resizeBitmap.getWidth(), resizeBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        bitmap2 = Bitmap.createBitmap(resizeBitmap.getWidth(), resizeBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//        for (int y = 0; y < resizeBitmap.getHeight(); y++) {
//            for (int x = 0; x < resizeBitmap.getWidth(); x++) {
//                int z = 0;
//                int coor_x = 0;
//                int coor_y = 0;
//                if(x == 0){
//                    coor_x = 1;
//                    coor_y = 1;
//                }else{
//                    coor_x = 0;
//                    coor_y = 0;
//                }
//                boolean ketemu = false;
//                do{
//                    if(x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 0){
//                        bitmap1.setPixel(x, y, Color.BLACK);
//                        coor_x = x;
//                        coor_y = y;
//                        ketemu = true;
//                    }else if(x == imageData[z][0] && y == imageData[z][1] && clusters[z] == 1){
//                        bitmap2.setPixel(x, y, Color.BLACK);
//                        coor_x = x;
//                        coor_y = y;
//                        ketemu = true;
//                    }
//                    z++;
//                }while(z < jumlahData && ketemu == false);
//                if(ketemu == false && coor_x != x && coor_y != y){
//                    bitmap1.setPixel(x, y, Color.WHITE);
//                    bitmap2.setPixel(x, y, Color.WHITE);
//                }
//            }
//        }
//
//        int jumlahDataBitmap1 = 0;
//        int jumlahDataBitmap2 = 0;
//
//        for (int y = 0; y < bitmap1.getHeight(); y++) {
//            for (int x = 0; x < bitmap1.getWidth(); x++) {
//                if (bitmap1.getPixel(x, y) == Color.BLACK) {
//                    jumlahDataBitmap1++;
//                }
//            }
//        }
//
//        for (int y = 0; y < bitmap2.getHeight(); y++) {
//            for (int x = 0; x < bitmap2.getWidth(); x++) {
//                if (bitmap2.getPixel(x, y) == Color.BLACK) {
//                    jumlahDataBitmap2++;
//                }
//            }
//        }

//        int[][] dataBitmap1 = new int[jumlahDataBitmap1][2];
//        int[][] dataBitmap2 = new int[jumlahDataBitmap2][2];
//
//        int j = 0;
//        for (int y = 0; y < bitmap1.getHeight(); y++) {
//            for (int x = 0; x < bitmap1.getWidth(); x++) {
//                if (bitmap1.getPixel(x, y) == Color.BLACK) {
//                    dataBitmap1[j][0] = x;
//                    dataBitmap1[j][1] = y;
//                    j++;
//                }
//            }
//        }
//
//        int k = 0;
//        for (int y = 0; y < bitmap2.getHeight(); y++) {
//            for (int x = 0; x < bitmap2.getWidth(); x++) {
//                if (bitmap2.getPixel(x, y) == Color.BLACK) {
//                    dataBitmap2[k][0] = x;
//                    dataBitmap2[k][1] = y;
//                    k++;
//                }
//            }
//        }

//        Log.d("HasilActivity", "Jumlah Data Bitmap 1 = " + jumlahDataBitmap1);
//        Log.d("HasilActivity", "Ukuran Bitmap 1 = " + bitmap1.getWidth() + " dan " + bitmap1.getHeight());
//        Log.d("HasilActivity", "Jumlah Data Bitmap 2 = " + jumlahDataBitmap2);
//        Log.d("HasilActivity", "Ukuran Bitmap 2 = " + bitmap2.getWidth() + " dan " + bitmap2.getHeight());

//        double terkecilX1 = dataBitmap1[0][0];
//        double terkecilY1 = dataBitmap1[0][1];
//        double terbesarX1 = dataBitmap1[0][0];
//        double terbesarY1 = dataBitmap1[0][1];
//        double terkecilX2 = dataBitmap2[0][0];
//        double terkecilY2 = dataBitmap2[0][1];
//        double terbesarX2 = dataBitmap2[0][0];
//        double terbesarY2 = dataBitmap2[0][1];
//
//        for(int a = 1; a < jumlahDataBitmap1; a++){
//            if(terkecilX1 > dataBitmap1[a][0] && terkecilY1 > dataBitmap1[a][1]){
//                terkecilX1 = dataBitmap1[a][0];
//                terkecilY1 = dataBitmap1[a][1];
//            }
//
//            if(terbesarX1 < dataBitmap1[a][0] && terbesarY1 < dataBitmap1[a][1]){
//                terbesarX1 = dataBitmap1[a][0];
//                terbesarY1 = dataBitmap1[a][1];
//            }
//        }
//
//        for(int a = 1; a < jumlahDataBitmap2; a++){
//            if(terkecilX2 > dataBitmap2[a][0] && terkecilY2 > dataBitmap2[a][1]){
//                terkecilX2 = dataBitmap2[a][0];
//                terkecilY2 = dataBitmap2[a][1];
//            }
//
//            if(terbesarX2 < dataBitmap2[a][0] && terbesarY2 < dataBitmap2[a][1]){
//                terbesarX2 = dataBitmap2[a][0];
//                terbesarY2 = dataBitmap2[a][1];
//            }
//        }
//
//        Log.d("HasilActivity", "Koordinat Terkecil Bitmap 1 = " + terkecilX1 + " dan " + terkecilY1);
//        Log.d("HasilActivity", "Koordinat Terbesar Bitmap 1 = " + terbesarX1 + " dan " + terbesarY1);
//        Log.d("HasilActivity", "Koordinat Terkecil Bitmap 2 = " + terkecilX2 + " dan " + terkecilY2);
//        Log.d("HasilActivity", "Koordinat Terbesar Bitmap 2 = " + terbesarX2 + " dan " + terbesarY2);
//
//        bitmapAsli1 = Bitmap.createBitmap((int) terbesarX1 + 10, (int) terbesarY1 + 10, Bitmap.Config.ARGB_8888);
//        bitmapAsli2 = Bitmap.createBitmap((int) terbesarX2 + 10, (int) terbesarY2 + 10, Bitmap.Config.ARGB_8888);
//
//        for (int y = 0; y < bitmapAsli1.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli1.getWidth(); x++) {
//                int z = 0;
//                boolean ketemu = false;
//                do{
//                    if (x == dataBitmap1[z][0] && y == dataBitmap1[z][1]) {
//                        bitmapAsli1.setPixel(x, y, Color.BLACK);
//                        ketemu = true;
//                    }
//                    z++;
//                }while(z < jumlahDataBitmap1 && ketemu == false);
//                if(ketemu == false){
//                    bitmapAsli1.setPixel(x, y, Color.WHITE);
//                }
//            }
//        }
//
//        for (int y = 0; y < bitmapAsli2.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli2.getWidth(); x++) {
//                int z = 0;
//                boolean ketemu = false;
//                do{
//                    if (x == dataBitmap2[z][0] && y == dataBitmap2[z][1]) {
//                        bitmapAsli2.setPixel(x, y, Color.BLACK);
//                        ketemu = true;
//                    }
//                    z++;
//                }while(z < jumlahDataBitmap2 && ketemu == false);
//                if(ketemu == false){
//                    bitmapAsli2.setPixel(x, y, Color.WHITE);
//                }
//            }
//        }

//        bitmapAsli1 = trim_bitmap_kecil(bitmap1, Color.BLACK);
//        bitmapAsli2 = trim_bitmap_kecil(bitmap2, Color.BLACK);
//
//        int jumlahDataBitmapAsli1 = 0;
//        int jumlahDataBitmapAsli2 = 0;
//
//        for (int y = 0; y < bitmapAsli1.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli1.getWidth(); x++) {
//                if (bitmapAsli1.getPixel(x, y) == Color.BLACK) {
//                    jumlahDataBitmapAsli1++;
//                }
//            }
//        }
//
//        for (int y = 0; y < bitmapAsli2.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli2.getWidth(); x++) {
//                if (bitmapAsli2.getPixel(x, y) == Color.BLACK) {
//                    jumlahDataBitmapAsli2++;
//                }
//            }
//        }
//
//        Log.d("HasilActivity", "Jumlah Data Bitmap Asli 1 = " + jumlahDataBitmapAsli1);
//        Log.d("HasilActivity", "Jumlah Data Bitmap Asli 2 = " + jumlahDataBitmapAsli2);
//
//        int[][] dataBitmap1 = new int[jumlahDataBitmapAsli1][2];
//        int[][] dataBitmap2 = new int[jumlahDataBitmapAsli2][2];
//
//        int j = 0;
//        for (int y = 0; y < bitmapAsli1.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli1.getWidth(); x++) {
//                if (bitmapAsli1.getPixel(x, y) == Color.BLACK) {
//                    dataBitmap1[j][0] = x;
//                    dataBitmap1[j][1] = y;
//                    j++;
//                }
//            }
//        }
//
//        int k = 0;
//        for (int y = 0; y < bitmapAsli2.getHeight(); y++) {
//            for (int x = 0; x < bitmapAsli2.getWidth(); x++) {
//                if (bitmapAsli2.getPixel(x, y) == Color.BLACK) {
//                    dataBitmap2[k][0] = x;
//                    dataBitmap2[k][1] = y;
//                    k++;
//                }
//            }
//        }
//
//        doZhangSuenThinning(dataBitmap1);
//        doZhangSuenThinning(dataBitmap2);
//
//        mImg.setImageBitmap(bitmapAsli1);
//        mImg2.setImageBitmap(bitmapAsli2);

//        Bitmap resizeBitmap = trim(mutableBitmap, Color.WHITE);
//
//        Bitmap resizeTwoBitmap = Bitmap.createScaledBitmap(resizeBitmap, 100, 60, true);
//
//        mImg.setImageBitmap(resizeTwoBitmap);

//        int[][] clusterData = new int[resizeTwoBitmap.getHeight()][resizeTwoBitmap.getWidth()];
//        int jumlahData = 0;
//
//        for (int y = 0; y < clusterData.length; y++) {
//            for (int x = 0; x < clusterData[y].length; x++) {
//                if (resizeTwoBitmap.getPixel(x, y) == Color.BLACK) {
//                    clusterData[y][x] = 1;
//                } else {
//                    clusterData[y][x] = 0;
//                }
//            }
//        }
//
//        for (int y = 0; y < clusterData.length; y++) {
//            for (int x = 0; x < clusterData[y].length; x++) {
//                if (clusterData[y][x] == 1) {
//                    Map<String,Integer> dataColumn = new HashMap<>();
//                    jumlahData++;
//
//                    dataColumn.put("id",jumlahData);
//                    dataColumn.put("x",x);
//                    dataColumn.put("y",y);
//                    dataColumn.put("cluster",jumlahData);
//
//                    dataRow.add(dataColumn);
//                }
//            }
//        }
//        Proses dilasi
//        Imgproc.dilate(mInput, mInput, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

//        Proses Grayscale
//        Imgproc.cvtColor(source mat, destination mat1, Imgproc.COLOR_RGB2GRAY);

//        dataReal = new double[dataRow.size()][2];
//
//        for(int i = 0; i < dataRow.size(); i++){
//            dataReal[i][0] = dataRow.get(i).get("x");
//            dataReal[i][1] = dataRow.get(i).get("y");
//        }
//
//        Log.d("Ini vector datanya", "arr:" + Arrays.deepToString(dataReal));
//
//        VectorLib vlib = new VectorLib();
//        ClusteringLib clib = new ClusteringLib();

        // Automatic Clustering using Single Linkage with default n_interval=10
//        clusters = clib.AutomaticClustering("single", dataReal);
//        double[] output = clib.getOptimalK("single", dataReal);
//
//        clusters=clib.ShapeIndependentClustering(dataReal);
//        Log.d("Ini vectornya", "arr:" + Arrays.toString(clusters));
//        Log.d("Ini jumlah arraynya", "arr data:" + dataReal.length + " dan arr cluster:" + clusters.length);
//
//        DataPoint[] tes = new DataPoint[clusters.length];
//        for (int i=0; i<clusters.length; i++) {
//            if(clusters[i] == 0){
//                double x = dataReal[i][1];
//                double y = dataReal[i][0];
//                DataPoint v = new DataPoint(x, y);
//                tes[i] = v;
//            }else{
//                DataPoint v = new DataPoint(0, 0);
//                tes[i] = v;
//            }
//        }
//
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(tes);
//        graph.addSeries(series);

//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(1.0);
//        graph.getViewport().setMaxX(clusters.length);
//        graph.getViewport().setMinY(1.0);
//        graph.getViewport().setMaxY(clusters.length);

        //vlib.setDraw(dataReal,clusters);
//
//        Log.v("log_tag", "Number of optimal clusters = " + clusters[0]);
//        Log.v("log_tag", "Accuracy = " + clusters[1]);
//        vlib.view("Number of optimal clusters", output[0]);
//        vlib.view("Accuracy", output[1]);
//        while(jumlahData > 2){Log.v("log_tag", "Panel Saved");
//            double range = 10000;
//            Map<String,Integer> temp = new HashMap<>();
//            Map<String,Integer> temp1 = new HashMap<>();
//
//            for(int i=0; i<dataRow.size(); i++){
//                for(int j=i+1; j<dataRow.size(); j++){
//                    double selisihX = dataRow.get(i).get("x") - dataRow.get(j).get("x");
//                    double selisihY = dataRow.get(i).get("y") - dataRow.get(j).get("y");
//                    double jarak = sqrt(pow(selisihX,2)+pow(selisihY,2));
//                    if(range > jarak && dataRow.get(i).get("cluster") != dataRow.get(j).get("cluster")){
//                        range = jarak;
//                        temp.put("id", dataRow.get(i).get("id"));
//                        temp.put("x", dataRow.get(i).get("x"));
//                        temp.put("y", dataRow.get(i).get("y"));
//                        temp.put("cluster", dataRow.get(i).get("cluster"));
//                        temp1.put("id", dataRow.get(j).get("id"));
//                        temp1.put("x", dataRow.get(j).get("x"));
//                        temp1.put("y", dataRow.get(j).get("y"));
//                        temp1.put("cluster", dataRow.get(j).get("cluster"));
//                    }
//                }
//            }
//
//            int q = jumlahData - 1;
//
//            for(int n=0; n<dataRow.size(); n++){
//                if(dataRow.get(n).get("cluster") == temp1.get("cluster")){
//                    dataRow.get(n).remove("cluster");
//                    dataRow.get(n).put("cluster", temp.get("cluster"));
//                }
//            }
//
//            jumlahData--;
//        }
    }

    public double[] hitung(double[][] weight1, double[][] weight2, double[][] weight3, double[] datates){
        int bias = 1;
        int[] hidden_unit = {43, 32};
        int jumlah_hidden = hidden_unit.length;
        int jumlah_output = 20;
        double[][] weight_input = new double[datates.length + 1][hidden_unit[0]];
        double[][][] weight_hidden = new double[jumlah_hidden][][];
        for(int a = 0; a < jumlah_hidden - 1; a++){
            weight_hidden[a] = new double[hidden_unit[a] + 1][hidden_unit[(a + 1)]];
        }
        weight_hidden[(jumlah_hidden - 1)] = new double[hidden_unit[(jumlah_hidden - 1)]][jumlah_output];

        //update manual sesuai jumlah hidden layer
        weight_input = weight1;
        weight_hidden[0] = weight2;
        weight_hidden[1] = weight3;

        double[] input_layer = new double[datates.length + 1];
        double[] sigmoid_input = new double[hidden_unit[0]];
        double[][] hidden_layer = new double[jumlah_hidden][];
        for(int a = 0; a < jumlah_hidden; a++){
            hidden_layer[a] = new double[hidden_unit[a] + 1];
        }
        double[][] sigmoid_hidden = new double[jumlah_hidden][];
        for(int a = 0; a < jumlah_hidden - 1; a++){
            sigmoid_hidden[a] = new double[hidden_unit[(a + 1)]];
        }
        sigmoid_hidden[(jumlah_hidden - 1)] = new double[jumlah_output];
        double[] output_layer = new double[jumlah_output];

        input_layer[0] = bias;
        System.arraycopy(datates, 0, input_layer, 1, datates.length);
        VectorLib vlib = new VectorLib();
        double[] hitung_input = hitung_data(input_layer, weight_input);
        for(int a = 0; a < hidden_unit[0]; a++){
            sigmoid_input[a] = (1 / (1 + Math.exp(-1 * hitung_input[a])));
        }
        hidden_layer[0][0] = bias;
        System.arraycopy(sigmoid_input, 0, hidden_layer[0], 1, hidden_unit[0]);
        for(int a = 0; a < jumlah_hidden - 1; a++){
            double[] hitung_hidden = hitung_data(hidden_layer[a], weight_hidden[a]);
            for(int b = 0; b < hidden_unit[(a + 1)]; b++){
                sigmoid_hidden[a][b] = (1 / (1 + Math.exp(-1 * hitung_hidden[b])));
            }
            hidden_layer[(a + 1)][0] = bias;
            System.arraycopy(sigmoid_hidden[a], 0, hidden_layer[(a + 1)], 1, hidden_unit[(a + 1)]);
        }

        double[] hitung_hidden = hitung_data(hidden_layer[(jumlah_hidden - 1)], weight_hidden[(jumlah_hidden - 1)]);
        for(int a = 0; a < jumlah_output; a++){
            sigmoid_hidden[(jumlah_hidden - 1)][a] = (1 / (1 + Math.exp(-1 * hitung_hidden[a])));
        }
        System.arraycopy(sigmoid_hidden[(jumlah_hidden - 1)], 0, output_layer, 0, jumlah_output);
        for(int a = 0; a < jumlah_output; a++){
            if(output_layer[a] > 0.8){
                output_layer[a] = 1;
            }
            if(output_layer[a] < 0.2){
                output_layer[a] = 0;
            }
        }

        return output_layer;
    }

    public double[] hitung_data(double[] data, double[][] weight){
        double[] output = null;
        int colB = weight[0].length;
        int colA = data.length;
        int rowA = 1;
        int rowB = weight.length;
        int count = colA;

        if(colA == rowB){
            output = new double[colB];
            for(int c2 = 0; c2 < colB; c2++){
                int c1 = 0;
                output[c2] = 0;
                while(c1 < count){
                    output[c2] += data[c1] * weight[c1][c2];
                    c1++;
                }
            }
        }

        return output;
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    private Bitmap trim_bitmap_kecil(Bitmap bitmap, int trimColor){
        int minX = Integer.MAX_VALUE;
        int maxX = 0;
        int minY = Integer.MAX_VALUE;
        int maxY = 0;

        for(int x = 0; x < bitmap.getWidth(); x++){
            for(int y = 0; y < bitmap.getHeight(); y++){
                if(bitmap.getPixel(x, y) == trimColor){
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
        Log.d("HasilActivity", "Ukuran Bitmap = " + bitmap.getWidth() + " dan " + bitmap1.getHeight());
        Log.d("HasilActivity", "Min X dan Y = " + minX + " dan " + minY);
        Log.d("HasilActivity", "Max X dan Y = " + maxX + " dan " + maxY);
        return Bitmap.createBitmap(bitmap, minX, minY, maxX - minX + 3, maxY - minY + 3);
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
}

