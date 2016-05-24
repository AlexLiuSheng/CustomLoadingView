package com.allenliu.loadingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
private LoadingView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view= (LoadingView) findViewById(R.id.loadingview);
      //  view.setBallOneColor(getResources().getColor(R.color.colorAccent));
        view.setMaxRadius(50);
//        view.setBallTwoColor(getResources().getColor(R.color.colorAccent));
    }
}
