package com.yafieimam.nuhanaapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

public class BrowseImageActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    Button buttonLoadImage, buttonProses;
    ImageView imageViewGambar;
    TextView mTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_browse_image);

        mTxt = (TextView) findViewById(R.id.txtPath);
        buttonLoadImage = (Button) findViewById(R.id.getimage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        buttonProses = (Button) findViewById(R.id.proses_gambar);
        buttonProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BitmapDrawable drawable = (BitmapDrawable) imageViewGambar.getDrawable();
                Bitmap bitmapGambar = drawable.getBitmap();

                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                bitmapGambar.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();

                Intent anotherIntent = new Intent(BrowseImageActivity.this, PercobaanEkstraksiActivity.class);
                anotherIntent.putExtra("BitmapImage", byteArray);
                startActivity(anotherIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mTxt.setText(getRealPathFromURI(selectedImage));
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageViewGambar = (ImageView) findViewById(R.id.imageViewGambar);
            imageViewGambar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
