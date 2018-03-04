package io.bingbin.bingbinandroid.views.classifyActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.CommonUtil;

public class PreviewActivity extends AppCompatActivity {


    @BindView(R.id.preview_imageview)
    ImageView previewImageview;

    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        filename = intent.getStringExtra("filename");
        Bitmap img = CommonUtil.loadBitmap(this, filename);
        previewImageview.setImageBitmap(img);
    }

    @OnClick({R.id.preview_retry_btn, R.id.preview_ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.preview_retry_btn:
                backToCamera();
                break;

            case R.id.preview_ok_btn:
                // go to classify activity
                Intent intent = new Intent(this, ClassifyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT); // pass result to classify activity
                intent.putExtra("filename", filename);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backToCamera();
    }

    private void backToCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT); // pass back to camera activity
        startActivity(intent);
        finish();
    }
}
