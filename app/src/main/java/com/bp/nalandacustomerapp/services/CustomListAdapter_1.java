package com.bp.nalandacustomerapp.services;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bp.nalandacustomerapp.HomeActivity;
import com.bp.nalandacustomerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by bp on 12/22/2017.
 */

public class CustomListAdapter_1 extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productNameList;
    private final String[] productPriceList;
    private final String[] productImgList;

    public CustomListAdapter_1(Activity context, String[] productNameList, String[] productPriceList, String[] productImgList) {
        super(context, R.layout.custom_list_1, productNameList);

        this.context = context;
        this.productNameList = productNameList;
        this.productPriceList = productPriceList;
        this.productImgList = productImgList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_1, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.product_img);
        TextView nametxt = (TextView) rowView.findViewById(R.id.product_name);
        TextView pricetxt = (TextView) rowView.findViewById(R.id.product_price);

        Picasso.with(context).load(productImgList[position]).into(imageView);
        nametxt.setText(productNameList[position]);
        pricetxt.setText(productPriceList[position]);

        return rowView;

    }

    ;
}
