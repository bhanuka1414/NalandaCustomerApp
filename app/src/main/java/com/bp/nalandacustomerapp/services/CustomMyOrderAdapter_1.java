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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomMyOrderAdapter_1 extends BaseAdapter {
    private Context context;
    private ArrayList<CartModel> items;
    private DatabaseHelper db;

    public CustomCartAdapter_1() {
    }

    public CustomCartAdapter_1(Context context, ArrayList<CartModel> items) {
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
                    inflate(R.layout.cart_layout, parent, false);
        }
        final CartModel cartItem = (CartModel) getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.cartItemImg);
        TextView nametxt = (TextView) convertView.findViewById(R.id.cartItemName);
        TextView pricetxt = (TextView) convertView.findViewById(R.id.cartItemPrice);
        TextView qtytxt = (TextView) convertView.findViewById(R.id.cartItemQty);
        TextView totaltxt = (TextView) convertView.findViewById(R.id.cartItemTatalPrice);
        ImageButton btnRemove = (ImageButton) convertView.findViewById(R.id.cartItemRemoveBtn);

        final int id = position;

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeCartItemById(cartItem.getId());
                items.remove(id);
                notifyDataSetChanged();
            }

        });

        Picasso.with(context).load(cartItem.getImg()).into(imageView);
        nametxt.setText(cartItem.getName());
        pricetxt.setText(cartItem.getPrice());
        qtytxt.setText(cartItem.getQty());
        totaltxt.setText(String.valueOf(Double.parseDouble(cartItem.getPrice()) * Integer.parseInt(cartItem.getQty())));

        return convertView;
    }
}
