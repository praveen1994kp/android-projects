package com.app.com.undeafener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    ListView lv;
    Button doneButton;
    static String itemVal="en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        doneButton=(Button) findViewById(R.id.button2);
        lv=(ListView)findViewById(R.id.listView);
        String[] values = new String[] {
                "de_DE",
                "en_IN",
                "en_US",
                "en_AU",
                "es_ES",
                "fr_FR",
                "hi_IN",
                "it_IT",
                "ja_JP",
                "kn_IN",
                "ko_",
                "nl_NL",
                "pa_IN",
                "ta_IN",
                "te_IN",
                "zh_CN"
        };

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                itemVal = (String) lv.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),itemVal+" is selected",Toast.LENGTH_LONG);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });


    }



}
