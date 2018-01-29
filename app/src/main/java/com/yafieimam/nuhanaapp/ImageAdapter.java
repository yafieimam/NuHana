package com.yafieimam.nuhanaapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

//The adapter class associated with the ChunkedImageActivity class
public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Bitmap> imageChunks;
	private int imageWidth, imageHeight;
	
	//constructor
	public ImageAdapter(Context c, ArrayList<Bitmap> images){
		mContext = c;
		imageChunks = images;
		imageWidth = images.get(0).getWidth();
		imageHeight = images.get(0).getHeight();
	}
	
	@Override
	public int getCount() {
		return imageChunks.size();
	}

	@Override
	public Object getItem(int position) {
		return imageChunks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView image;
		if(convertView == null){
			image = new ImageView(mContext);
			image.setLayoutParams(new GridView.LayoutParams(imageWidth - 10 , imageHeight));
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			image.setPadding(5, 5, 5, 5);
		}else{
			image = (ImageView) convertView;
		}
		image.setImageBitmap(imageChunks.get(position));
		return image;
	}
}
