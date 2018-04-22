package com.bp.nalandacustomerapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bp.nalandacustomerapp.R;
import com.bp.nalandacustomerapp.services.models.OrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomMyOrderAdapter_1 extends BaseAdapter {
    private Context context;
    private ArrayList<OrderModel> items;
    private DatabaseHelper db;

    public CustomMyOrderAdapter_1() {
    }

    public CustomMyOrderAdapter_1(Context context, ArrayList<OrderModel> items) {
        this.context = context;
        this.items = items;
        db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.order_layout, parent, false);
        }
        final OrderModel orderItem = (OrderModel) getItem(position);

        TextView oidTxt = (TextView) convertView.findViewById(R.id.order_id);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.order_date);
        TextView amrTxt = (TextView) convertView.findViewById(R.id.order_amount);
        //TextView totaltxt = (TextView) convertView.findViewById(R.id.cartItemTatalPrice);
        //ImageButton btnRemove = (ImageButton) convertView.findViewById(R.id.cartItemRemoveBtn);

        oidTxt.setText(orderItem.getOno());
        dateTxt.setText(orderItem.getDate());
        amrTxt.setText(orderItem.getAmount());

        return convertView;
    }
}
