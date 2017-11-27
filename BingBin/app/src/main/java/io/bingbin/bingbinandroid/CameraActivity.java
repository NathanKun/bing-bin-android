package io.bingbin.bingbinandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

import java.io.File;

/**
 * Camera activity. Take photo, save, and send path of photo to main activity
 *
 * @author Junyang HE
 */
public class CameraActivity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MaterialCamera(this)
                .audioDisabled(true)
                .stillShot()
                .allowRetry(true)
                .start(CAMERA_RQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {
                //Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("imgPath", data.getDataString());
                setResult(RESULT_OK, intent);
                finish();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}