package io.bingbin.bingbinandroid;

import io.bingbin.bingbinandroid.Models.Category;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.net.URI;

public class ClassifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_classify);
/*
        Intent intent = getIntent();
        String uriStr = intent.getStringExtra("uri");

        // uri exists, mean
        if (uriStr.equals("")) {
            // start camera
        } else {
            // show image and ask if is good category
            //showImgAndAskCategory(CommonUtils.uriToFile(Uri.parse(uriStr)));
        }*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if() {
            // show image and ask if is good category
            showImgAndAskCategory(CommonUtils.uriToFile(Uri.parse(uriStr)));
        }
        */
    }

    private void showImgAndAskCategory(File imaFile) {

    }

    private void showRecycleInstruction(Category category) {
        switch (category) {
            case CARDBOARD:
                break;
            case PAPER:
                break;
            case METAL:
                break;
            case PLASTIC:
                break;
            case GLASS:
                break;
            case OTHER:
                break;
            default:
                break;
        }
    }
}
