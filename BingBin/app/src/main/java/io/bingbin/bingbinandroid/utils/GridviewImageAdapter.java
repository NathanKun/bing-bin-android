package io.bingbin.bingbinandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Custom image adapter for grid views in AvatarActivity
 *
 * @author Junyang HE
 * ref; https://developer.android.com/guide/topics/ui/layout/gridview.html
 * Created by Junyang HE on 2018/3/3.
 */

public class GridviewImageAdapter extends BaseAdapter {
    private final Context context;
    private final List<Bitmap> bitmaps;

    public GridviewImageAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    public int getCount() {
        return bitmaps.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        int width = parent.getWidth() / 5;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmaps.get(position));
        return imageView;
    }
}