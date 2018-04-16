package com.bp.nalandacustomerapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CustomCartAdapter_1;
import com.bp.nalandacustomerapp.services.DatabaseHelper;
import com.bp.nalandacustomerapp.services.models.CartModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    DatabaseHelper db;

    private ArrayList<CartModel> cartItems;

    private ListView cartList;
    private Button crtbtnorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new DatabaseHelper(this);
        cartItems = new ArrayList<>();

        cartList = (ListView)findViewById(R.id.cart_items);
        crtbtnorder = (Button)findViewById(R.id.crtbtnorder);


        Cursor res = db.getCartItems();
        if (res.getCount() > 0){

            while (res.moveToNext()){

                cartItems.add(new CartModel(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)));

            }

            CustomCartAdapter_1 adapter = new CustomCartAdapter_1(CartActivity.this, cartItems);
            cartList.setAdapter(adapter);
        }

        crtbtnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
                final EditText input = new EditText(CartActivity.this);
                alertDialog.setTitle("Put A Short Message To Deliveriman"); //Set Alert dialog title here
                alertDialog.setMessage("Enter Your Message Here");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//You will get as string input data in this variable.
// here we convert the input to a string and show in a toast.
                        String srt = input.getEditableText().toString();
                        Toast.makeText(CartActivity.this,srt,Toast.LENGTH_LONG).show();
                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
// Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
    }
}
