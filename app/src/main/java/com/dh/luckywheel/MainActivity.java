package com.dh.luckywheel;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private LuckyWheel mLuckyWheel;
    private ImageView mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        String getItem = intent.getStringExtra("items");
        String[] items = getItem.split("\\|");

        Log.d("MainActivity", getItem);


        Log.d("MainActivity", String.valueOf(items.length));

        mLuckyWheel = (LuckyWheel) findViewById(R.id.id_lucky_wheel);
        mStartBtn = (ImageView) findViewById(R.id.id_start_btn);

        mLuckyWheel.putItems(items);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mLuckyWheel.isStart()){
                    int index = (int)(1+Math.random()*6);
                    mLuckyWheel.luckyStart(index);
                    Log.d("MainActivity",String.valueOf(index));
                    mStartBtn.setImageResource(R.mipmap.stop);
                } else {
                    if( !mLuckyWheel.isShouldEnd()){
                        mLuckyWheel.luckyEnd();
                        mStartBtn.setImageResource(R.mipmap.start);
                    }
                }
            }
        });
    }
}