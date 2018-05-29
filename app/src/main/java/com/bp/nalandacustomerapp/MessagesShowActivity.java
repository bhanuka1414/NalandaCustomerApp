package com.bp.nalandacustomerapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesShowActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayList<HashMap<String, String>> msgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_show);

        databaseHelper = new DatabaseHelper(MessagesShowActivity.this);
        msgList = new ArrayList<HashMap<String, String>>();

        findViewById(R.id.back_button_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Cursor res = databaseHelper.getMsgs();
        if (res.getCount() > 0) {

            while (res.moveToNext()) {
                HashMap<String, String> msg = new HashMap<>();
                msg.put("msg", res.getString(1));
                msg.put("title", res.getString(2));
                msg.put("subject", res.getString(3));
                msgList.add(msg);

            }

            //Toast.makeText(MessagesShowActivity.this,msgList.toString(),Toast.LENGTH_LONG).show();
            ListView lv = (ListView) findViewById(R.id.msgListView);
            ListAdapter adapter = new SimpleAdapter(MessagesShowActivity.this, msgList, R.layout.msg_layout, new String[]{"msg", "title", "subject"}, new int[]{R.id.msgMessageTxt, R.id.msgTitleTxt, R.id.msgSubjectTxt});
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(MessagesShowActivity.this, "No any Notifications", Toast.LENGTH_LONG).show();
        }



    }
}
