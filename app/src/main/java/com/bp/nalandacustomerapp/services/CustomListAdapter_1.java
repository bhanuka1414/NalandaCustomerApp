/*package com.bp.nalandacustomerapp.services;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bp.nalandacustomerapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by bp on 12/22/2017.
 */
/*
public class CustomListAdapter_1 extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] catIdList;
    private final String[] catNameList;
    private final String[] catImgList;


    public CustomListAdapter_1(Activity context, String[] catIdList, String[] catImgList, String[] catNameList) {
        super(context, R.layout.custom_list_1, catNameList);

        this.context = context;
        this.catIdList = catIdList;
        this.catNameList = catNameList;
        this.catImgList = catImgList;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_1, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_img);
        TextView idtxt = (TextView) rowView.findViewById(R.id.cat_id);
        TextView nametxt = (TextView) rowView.findViewById(R.id.cat_name);


        Picasso.with(context).load(catImgList[position]).into(imageView);
        idtxt.setText(catIdList[position]);
        nametxt.setText(catNameList[position]);


        return rowView;

    }

    ;
}
*/