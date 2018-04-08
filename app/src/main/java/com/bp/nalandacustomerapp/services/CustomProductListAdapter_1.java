package com.bp.nalandacustomerapp.services;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bp.nalandacustomerapp.R;
import com.squareup.picasso.Picasso;

public class CustomProductListAdapter_1 extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] productIdList;
    private final String[] productNameList;
    private final String[] productImgList;
    private final String[] productPriceList;
    private final String[] productStockList;


    public CustomProductListAdapter_1(Activity context, String[] productIdList, String[] productImgList, String[] productNameList, String[] productPriceList, String[] productStockList) {
        super(context, R.layout.product_listview, productNameList);

        this.context = context;
        this.productIdList = productIdList;
        this.productNameList = productNameList;
        this.productImgList = productImgList;
        this.productPriceList = productPriceList ;
        this.productStockList = productStockList ;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.product_listview, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.productImg);
        TextView idtxt = (TextView) rowView.findViewById(R.id.productId);
        TextView nametxt = (TextView) rowView.findViewById(R.id.productName);
        TextView pricetxt = (TextView) rowView.findViewById(R.id.productPrice);
        TextView stocktxt = (TextView) rowView.findViewById(R.id.productStock);


        Picasso.with(context).load(productImgList[position]).into(imageView);
        idtxt.setText(productIdList[position]);
        nametxt.setText(productNameList[position]);
        pricetxt.setText("Rs. "+productPriceList[position]+" /=");
        stocktxt.setText(productStockList[position]+" available");

        return rowView;

    }

    ;
}
