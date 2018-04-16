package com.bp.nalandacustomerapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CustomCartAdapter_1;
import com.bp.nalandacustomerapp.services.DatabaseHelper;
import com.bp.nalandacustomerapp.services.models.CartModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    DatabaseHelper db;

    /*private String[] productIdList;
    private String[] productNameList;
    private String[] productImgList;
    private String[] productPriceList;
    private String[] productQtyList;
    private String[] cartIdList;*/
    private ArrayList<CartModel> cartItems;

    private ListView cartList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new DatabaseHelper(this);
        cartItems = new ArrayList<>();

        cartList = (ListView)findViewById(R.id.cart_items);

        Cursor res = db.getCartItems();
        if (res.getCount() > 0){
           /* productIdList=new String[res.getCount()];
            productNameList=new String[res.getCount()];
            productImgList=new String[res.getCount()];
            productPriceList=new String[res.getCount()];
            productQtyList=new String[res.getCount()];
            cartIdList=new String[res.getCount()];*/

            while (res.moveToNext()){

                /*cartIdList[i] = res.getString(0);
                productIdList[i] = res.getString(2);
                productNameList[i] = res.getString(1);
                productImgList[i] = res.getString(5);
                productPriceList[i] = res.getString(4);
                productQtyList[i] = res.getString(3);*/
                cartItems.add(new CartModel(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)));

            }
            //((TextView)findViewById(R.id.textView4)).setText(sb.toString());
            //Toast.makeText(CartActivity.this,sb.toString(), Toast.LENGTH_LONG).show();
            CustomCartAdapter_1 adapter = new CustomCartAdapter_1(CartActivity.this, cartItems);
            cartList.setAdapter(adapter);
        }

        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    Toast.makeText(CartActivity.this,"bc",Toast.LENGTH_LONG).show();

            }
        });
    }
}
