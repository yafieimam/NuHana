package com.yafieimam.nuhanaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class ProsesTrainingActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Bitmap bitmapGambar, resizeBitmap;
    private ProgressDialog progress;
    ImageView mImg;
    Button mBtn, mBtn2;
    Spinner spinner;
    TextView mTxt;
    File mypath;
    public static String tempDir;
    private String uniqueId;
    public String current = null;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d("ProsesTrainingActivity", "OpenCV Not Loaded");
        }else{
            Log.d("ProsesTrainingActivity", "OpenCV Successfully Loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_proses_training);

        mImg = (ImageView) findViewById(R.id.imgView);
        mBtn = (Button) findViewById(R.id.btnSave);
        mBtn2 = (Button) findViewById(R.id.btnView);
        spinner = (Spinner) findViewById(R.id.spinner);
        mTxt = (TextView) findViewById(R.id.txtAksaraJawa);
        mTxt.setVisibility(View.INVISIBLE);

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

        prepareDirectory();
        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
        current = uniqueId + ".png";
        mypath= new File(directory,current);

        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        progress=new ProgressDialog(ProsesTrainingActivity.this);
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

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    Mat mat = new Mat();
                    Bitmap bmp32 = trimBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Utils.bitmapToMat(bmp32, mat);

                    Mat threeChannel = new Mat();

                    Imgproc.cvtColor(mat, threeChannel, Imgproc.COLOR_BGR2GRAY);

                    org.opencv.core.Size s = new Size(3,3);
                    Imgproc.GaussianBlur(threeChannel, threeChannel, s, 0);
                    Imgproc.threshold(threeChannel, threeChannel, 0, 255, Imgproc.THRESH_OTSU);

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    Utils.matToBitmap(threeChannel, bmp32, true);

                    resizeBitmap = Bitmap.createScaledBitmap(bmp32, 100, 100, true);

                    int jumlahData = 0;

                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                jumlahData++;
                            }
                        }
                    }

                    int[][] imageData = new int[jumlahData][2];
                    int[][] binerData = new int[resizeBitmap.getHeight()][resizeBitmap.getWidth()];

                    int i = 0;
                    for (int y = 0; y < resizeBitmap.getHeight(); y++) {
                        for (int x = 0; x < resizeBitmap.getWidth(); x++) {
                            if (resizeBitmap.getPixel(x, y) == Color.BLACK) {
                                imageData[i][0] = x;
                                imageData[i][1] = y;
                                i++;
                            }
                        }
                    }

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

                    int rows = 10;
                    int cols = 10;

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

                    int[] jumlahPixel = new int[splitImages.size()];

                    for(int j = 0; j < splitImages.size(); j++){
                        int jumlah = 0;
                        for(int y = 0; y < splitImages.get(j).getHeight(); y++){
                            for(int x = 0; x < splitImages.get(j).getWidth(); x++){
                                if (splitImages.get(j).getPixel(x, y) == Color.BLACK) {
                                    jumlah++;
                                }
                            }
                        }
                        jumlahPixel[j] = jumlah;
                    }

                    jumpTime += 10;
                    progress.setProgress(jumpTime);

                    int[] binerGambar = new int[jumlahPixel.length];

                    for(int j =0; j < jumlahPixel.length; j++){
                        if(jumlahPixel[j] > 0){
                            binerGambar[j] = 1;
                        }else{
                            binerGambar[j] = 0;
                        }
                    }

                    Log.d("FEATURE", Arrays.toString(binerGambar));

                    progress.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImg.setImageBitmap(resizeBitmap);
                            mBtn2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProsesTrainingActivity.this, ChunkedImageActivity.class);
                                    intent.putParcelableArrayListExtra("image chunks", splitImages);
                                    startActivity(intent);
                                }
                            });
                            mBtn.setOnClickListener(new View.OnClickListener()
                            {
                                public void onClick(View v) {
                                    String label = mTxt.getText().toString();
                                    DatabaseHandler db = new DatabaseHandler(ProsesTrainingActivity.this);
                                    long id_dataset = db.addDataset(Integer.parseInt(label));
                                    long id = db.addFeature(binerGambar, Integer.parseInt(label), (int) id_dataset);
                                    DatasetAksaraJawa dataset_aksara_jawa = db.getFeature((int) id);
                                    Log.d("ID AKSARA JAWA", String.valueOf(dataset_aksara_jawa.getIDAksaraJawa()));
                                    Log.d("ID DATASET", String.valueOf(dataset_aksara_jawa.getIDDataset()));
                                    Log.d("FEATURE 1", dataset_aksara_jawa.getFeature1());
                                    Log.d("FEATURE 2", dataset_aksara_jawa.getFeature2());
                                    Log.d("FEATURE 3", dataset_aksara_jawa.getFeature3());
                                    Log.d("FEATURE 4", dataset_aksara_jawa.getFeature4());
                                    Log.d("FEATURE 5", dataset_aksara_jawa.getFeature5());
                                    Log.d("FEATURE 6", dataset_aksara_jawa.getFeature6());
                                    Log.d("FEATURE 7", dataset_aksara_jawa.getFeature7());
                                    Log.d("FEATURE 8", dataset_aksara_jawa.getFeature8());
                                    Log.d("FEATURE 9", dataset_aksara_jawa.getFeature9());
                                    Log.d("FEATURE 10", dataset_aksara_jawa.getFeature10());
                                    Log.d("FEATURE 11", dataset_aksara_jawa.getFeature11());
                                    Log.d("FEATURE 12", dataset_aksara_jawa.getFeature12());
                                    Log.d("FEATURE 13", dataset_aksara_jawa.getFeature13());
                                    Log.d("FEATURE 14", dataset_aksara_jawa.getFeature14());
                                    Log.d("FEATURE 15", dataset_aksara_jawa.getFeature15());
                                    Log.d("FEATURE 16", dataset_aksara_jawa.getFeature16());
                                    Log.d("FEATURE 17", dataset_aksara_jawa.getFeature17());
                                    Log.d("FEATURE 18", dataset_aksara_jawa.getFeature18());
                                    Log.d("FEATURE 19", dataset_aksara_jawa.getFeature19());
                                    Log.d("FEATURE 20", dataset_aksara_jawa.getFeature20());
                                    Log.d("FEATURE 21", dataset_aksara_jawa.getFeature21());
                                    Log.d("FEATURE 22", dataset_aksara_jawa.getFeature22());
                                    Log.d("FEATURE 23", dataset_aksara_jawa.getFeature23());
                                    Log.d("FEATURE 24", dataset_aksara_jawa.getFeature24());
                                    Log.d("FEATURE 25", dataset_aksara_jawa.getFeature25());
                                    Log.d("FEATURE 26", dataset_aksara_jawa.getFeature26());
                                    Log.d("FEATURE 27", dataset_aksara_jawa.getFeature27());
                                    Log.d("FEATURE 28", dataset_aksara_jawa.getFeature28());
                                    Log.d("FEATURE 29", dataset_aksara_jawa.getFeature29());
                                    Log.d("FEATURE 30", dataset_aksara_jawa.getFeature30());
                                    Log.d("FEATURE 31", dataset_aksara_jawa.getFeature31());
                                    Log.d("FEATURE 32", dataset_aksara_jawa.getFeature32());
                                    Log.d("FEATURE 33", dataset_aksara_jawa.getFeature33());
                                    Log.d("FEATURE 34", dataset_aksara_jawa.getFeature34());
                                    Log.d("FEATURE 35", dataset_aksara_jawa.getFeature35());
                                    Log.d("FEATURE 36", dataset_aksara_jawa.getFeature36());
                                    Log.d("FEATURE 37", dataset_aksara_jawa.getFeature37());
                                    Log.d("FEATURE 38", dataset_aksara_jawa.getFeature38());
                                    Log.d("FEATURE 39", dataset_aksara_jawa.getFeature39());
                                    Log.d("FEATURE 40", dataset_aksara_jawa.getFeature40());
                                    Log.d("FEATURE 41", dataset_aksara_jawa.getFeature41());
                                    Log.d("FEATURE 42", dataset_aksara_jawa.getFeature42());
                                    Log.d("FEATURE 43", dataset_aksara_jawa.getFeature43());
                                    Log.d("FEATURE 44", dataset_aksara_jawa.getFeature44());
                                    Log.d("FEATURE 45", dataset_aksara_jawa.getFeature45());
                                    Log.d("FEATURE 46", dataset_aksara_jawa.getFeature46());
                                    Log.d("FEATURE 47", dataset_aksara_jawa.getFeature47());
                                    Log.d("FEATURE 48", dataset_aksara_jawa.getFeature48());
                                    Log.d("FEATURE 49", dataset_aksara_jawa.getFeature49());
                                    Log.d("FEATURE 50", dataset_aksara_jawa.getFeature50());
                                    Log.d("FEATURE 51", dataset_aksara_jawa.getFeature51());
                                    Log.d("FEATURE 52", dataset_aksara_jawa.getFeature52());
                                    Log.d("FEATURE 53", dataset_aksara_jawa.getFeature53());
                                    Log.d("FEATURE 54", dataset_aksara_jawa.getFeature54());
                                    Log.d("FEATURE 55", dataset_aksara_jawa.getFeature55());
                                    Log.d("FEATURE 56", dataset_aksara_jawa.getFeature56());
                                    Log.d("FEATURE 57", dataset_aksara_jawa.getFeature57());
                                    Log.d("FEATURE 58", dataset_aksara_jawa.getFeature58());
                                    Log.d("FEATURE 59", dataset_aksara_jawa.getFeature59());
                                    Log.d("FEATURE 60", dataset_aksara_jawa.getFeature60());
                                    Log.d("FEATURE 61", dataset_aksara_jawa.getFeature61());
                                    Log.d("FEATURE 62", dataset_aksara_jawa.getFeature62());
                                    Log.d("FEATURE 63", dataset_aksara_jawa.getFeature63());
                                    Log.d("FEATURE 64", dataset_aksara_jawa.getFeature64());
                                    Log.d("FEATURE 65", dataset_aksara_jawa.getFeature65());
                                    Log.d("FEATURE 66", dataset_aksara_jawa.getFeature66());
                                    Log.d("FEATURE 67", dataset_aksara_jawa.getFeature67());
                                    Log.d("FEATURE 68", dataset_aksara_jawa.getFeature68());
                                    Log.d("FEATURE 69", dataset_aksara_jawa.getFeature69());
                                    Log.d("FEATURE 70", dataset_aksara_jawa.getFeature70());
                                    Log.d("FEATURE 71", dataset_aksara_jawa.getFeature71());
                                    Log.d("FEATURE 72", dataset_aksara_jawa.getFeature72());
                                    Log.d("FEATURE 73", dataset_aksara_jawa.getFeature73());
                                    Log.d("FEATURE 74", dataset_aksara_jawa.getFeature74());
                                    Log.d("FEATURE 75", dataset_aksara_jawa.getFeature75());
                                    Log.d("FEATURE 76", dataset_aksara_jawa.getFeature76());
                                    Log.d("FEATURE 77", dataset_aksara_jawa.getFeature77());
                                    Log.d("FEATURE 78", dataset_aksara_jawa.getFeature78());
                                    Log.d("FEATURE 79", dataset_aksara_jawa.getFeature79());
                                    Log.d("FEATURE 80", dataset_aksara_jawa.getFeature80());
                                    Log.d("FEATURE 81", dataset_aksara_jawa.getFeature81());
                                    Log.d("FEATURE 82", dataset_aksara_jawa.getFeature82());
                                    Log.d("FEATURE 83", dataset_aksara_jawa.getFeature83());
                                    Log.d("FEATURE 84", dataset_aksara_jawa.getFeature84());
                                    Log.d("FEATURE 85", dataset_aksara_jawa.getFeature85());
                                    Log.d("FEATURE 86", dataset_aksara_jawa.getFeature86());
                                    Log.d("FEATURE 87", dataset_aksara_jawa.getFeature87());
                                    Log.d("FEATURE 88", dataset_aksara_jawa.getFeature88());
                                    Log.d("FEATURE 89", dataset_aksara_jawa.getFeature89());
                                    Log.d("FEATURE 90", dataset_aksara_jawa.getFeature90());
                                    Log.d("FEATURE 91", dataset_aksara_jawa.getFeature91());
                                    Log.d("FEATURE 92", dataset_aksara_jawa.getFeature92());
                                    Log.d("FEATURE 93", dataset_aksara_jawa.getFeature93());
                                    Log.d("FEATURE 94", dataset_aksara_jawa.getFeature94());
                                    Log.d("FEATURE 95", dataset_aksara_jawa.getFeature95());
                                    Log.d("FEATURE 96", dataset_aksara_jawa.getFeature96());
                                    Log.d("FEATURE 97", dataset_aksara_jawa.getFeature97());
                                    Log.d("FEATURE 98", dataset_aksara_jawa.getFeature98());
                                    Log.d("FEATURE 99", dataset_aksara_jawa.getFeature99());
                                    Log.d("FEATURE 100", dataset_aksara_jawa.getFeature100());
                                }
                            });
                        }
                    });

                    Log.d("ProsesTrainingActivity", "Ukuran Gambar = " + resizeBitmap.getWidth() + " dan " + resizeBitmap.getHeight());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<AksaraJawa> labels = db.getAllAksaraJawa();
        List<String> list = new ArrayList<String>();
        for (AksaraJawa aksara_jawa : labels) {
            list.add(aksara_jawa.getAksaraJawa());
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

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:",String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return(String.valueOf(currentTime));

    }

    private boolean prepareDirectory()
    {
        try
        {
            if (makedirs())
            {
                return true;
            } else {
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean makedirs()
    {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory())
        {
            File[] files = tempdir.listFiles();
            for (File file : files)
            {
                if (!file.delete())
                {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(ProsesTrainingActivity.this);
        AksaraJawa aksaraJawa = db.getIDAksaraJawa(label);
        mTxt.setText(String.valueOf(aksaraJawa.getID()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
