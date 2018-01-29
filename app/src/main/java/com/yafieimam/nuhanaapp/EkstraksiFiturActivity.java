package com.yafieimam.nuhanaapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class EkstraksiFiturActivity extends AppCompatActivity {
    Bitmap bitmap1, bitmap2;
    ImageView mImg, mImg2;
    ArrayList<ArrayList<ArrayList<Integer>>> dataGambar_Pixel = new ArrayList<ArrayList<ArrayList<Integer>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_ekstraksi_fitur);
        mImg = (ImageView) findViewById(R.id.imageViewHasil);
        mImg2 = (ImageView) findViewById(R.id.imageViewHasil2);

        bitmap1 = getIntent().getParcelableExtra("BitmapImage1");
        bitmap2 = getIntent().getParcelableExtra("BitmapImage2");

        int[][] dataBitmap1 = new int[bitmap1.getHeight()][bitmap1.getWidth()];
        int[][] dataBitmap2 = new int[bitmap2.getHeight()][bitmap2.getWidth()];

        for (int y = 0; y < dataBitmap1.length; y++) {
            for (int x = 0; x < dataBitmap1[y].length; x++) {
                if (bitmap1.getPixel(x, y) == Color.BLACK) {
                    dataBitmap1[y][x] = 1;
                } else {
                    dataBitmap1[y][x] = 0;
                }
            }
        }

        for (int y = 0; y < dataBitmap2.length; y++) {
            for (int x = 0; x < dataBitmap2[y].length; x++) {
                if (bitmap2.getPixel(x, y) == Color.BLACK) {
                    dataBitmap2[y][x] = 1;
                } else {
                    dataBitmap2[y][x] = 0;
                }
            }
        }

        doZhangSuenThinning(dataBitmap1);
        doZhangSuenThinning(dataBitmap2);

        for (int y = 0; y < dataBitmap1.length; y++) {
            for (int x = 0; x < dataBitmap1[y].length; x++) {
                if (dataBitmap1[y][x] == 1) {
                    bitmap1.setPixel(x, y, Color.BLACK);
                } else {
                    bitmap1.setPixel(x, y, Color.WHITE);
                }
            }
        }

        for (int y = 0; y < dataBitmap2.length; y++) {
            for (int x = 0; x < dataBitmap2[y].length; x++) {
                if (dataBitmap2[y][x] == 1) {
                    bitmap2.setPixel(x, y, Color.BLACK);
                } else {
                    bitmap2.setPixel(x, y, Color.WHITE);
                }
            }
        }

        int rows = 10;
        int cols = 10;
        int chunks = rows * cols;
        ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>(chunks);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth(), bitmap1.getHeight(), true);
        int chunkWidth = bitmap1.getWidth() / cols;
        int chunkHeight = bitmap1.getHeight() / rows;

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

        for(int i = 0; i < splitImages.size(); i++){
            int jumlah = 0;
            for(int y = 0; y < splitImages.get(i).getHeight(); y++){
                for(int x = 0; x < splitImages.get(i).getWidth(); x++){
                    if (splitImages.get(i).getPixel(x, y) == Color.BLACK) {
                        jumlah++;
                    }
                }
            }
            jumlahPixel[i] = jumlah;
        }

        Log.d("EkstraksiFiturActivity", "Array Jumlah Pixel: " + Arrays.toString(jumlahPixel));

//        for(int i = 0; i < splitImages.size(); i++){
//            for(int j = 0; j < jumlahPixel[i]; j++){
//                ArrayList<ArrayList<Integer>> dataPixel_Gambar = new ArrayList<ArrayList<Integer>>();
//                for(int y = 0; y < splitImages.get(i).getHeight(); y++) {
//                    for (int x = 0; x < splitImages.get(i).getWidth(); x++) {
//                        if (splitImages.get(i).getPixel(x, y) == Color.BLACK) {
//                            ArrayList<Integer> dataPixel = new ArrayList<Integer>();
//                            dataPixel.add(x);
//                            dataPixel.add(y);
//                            dataPixel_Gambar.add(dataPixel);
//                        }
//                    }
//                }
//                dataGambar_Pixel.add(dataPixel_Gambar);
//            }
//        }

        Log.d("EkstraksiFiturActivity", "List Data Pixel Gambar: " + dataGambar_Pixel.toString());

        int[] binerGambar = new int[jumlahPixel.length];

        for(int i =0; i < jumlahPixel.length; i++){
            if(jumlahPixel[i] > 0){
                binerGambar[i] = 1;
            }else{
                binerGambar[i] = 0;
            }
        }

        mImg.setImageBitmap(splitImages.get(0));
        mImg2.setImageBitmap(splitImages.get(10));
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
