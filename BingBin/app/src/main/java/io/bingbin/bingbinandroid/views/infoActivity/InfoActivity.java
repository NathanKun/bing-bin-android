package io.bingbin.bingbinandroid.views.infoActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.leakcanary.RefWatcher;

import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
