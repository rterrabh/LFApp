package com.ufla.lfapp.activities.graph;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.BaseAdapter;

import com.ufla.lfapp.R;

/**
 * Created by carlos on 9/28/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mQId = new String[]{"q0", "q1", "q2", "q3", "q4", "a"};

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
//        StateViewB stateViewB = new StateViewB(mContext);
//        stateViewB.setLayoutParams(new GridView.LayoutParams(130, 130));
//        new GridView.Lay
//        stateViewB.setVertexLabel(mQId[position]);
//        return stateViewB;
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {

    };
}
