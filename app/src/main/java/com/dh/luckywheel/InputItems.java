package com.dh.luckywheel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by dh on 16-8-18.
 */
public class InputItems extends AppCompatActivity implements View.OnClickListener {

    private EditText item_1;
    private EditText item_2;
    private EditText item_3;
    private EditText item_4;
    private EditText item_5;
    private EditText item_6;
    private Button btnReset;
    private Button btnSubmit;
    private String item_1_text, item_2_text, item_3_text, item_4_text, item_5_text, item_6_text;
    private String items;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_items);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reset:

                //...

                break;
            case R.id.btn_submit:


                item_1 = (EditText) findViewById(R.id.item_1);
                item_2 = (EditText) findViewById(R.id.item_2);
                item_3 = (EditText) findViewById(R.id.item_3);
                item_4 = (EditText) findViewById(R.id.item_4);
                item_5 = (EditText) findViewById(R.id.item_5);
                item_6 = (EditText) findViewById(R.id.item_6);

                item_1_text = item_1.getText().toString();
                item_2_text = item_2.getText().toString();
                item_3_text = item_3.getText().toString();
                item_4_text = item_4.getText().toString();
                item_5_text = item_5.getText().toString();
                item_6_text = item_6.getText().toString();

                items = item_1_text+"|"+item_2_text+"|"+item_3_text+"|"+
                        item_4_text+"|"+item_5_text+"|"+item_6_text;

                Log.d("items", items);

                Intent intent = new Intent(InputItems.this, MainActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
                break;
        }
    }
}
