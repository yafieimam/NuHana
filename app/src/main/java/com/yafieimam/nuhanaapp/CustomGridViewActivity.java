package com.yafieimam.nuhanaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by YAFIE IMAM A on 5/14/2018.
 */

public class CustomGridViewActivity extends BaseAdapter {
    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public CustomGridViewActivity(Context context, String[] gridViewString, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridViewAndroid;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, viewGroup, false);

        } else {
            gridViewAndroid = (View) view;
        }
        TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
        ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
        textViewAndroid.setText(gridViewString[i]);
        imageViewAndroid.setImageResource(gridViewImageId[i]);
        return gridViewAndroid;
    }
}
