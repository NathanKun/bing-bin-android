package io.bingbin.bingbinandroid.views.classifyActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.CommonUtil;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera_cameraview)
    CameraView cameraView;
    @BindView(R.id.camera_flash)
    ImageButton flashBtn;
    @BindView(R.id.camera_stillshot)
    ImageButton stillshotBtn;
    @BindView(R.id.camera_control_layout)
    RelativeLayout cameraCameraControlLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        cameraView.setMethod(CameraKit.Constants.METHOD_STILL);
        cameraView.setCropOutput(false);
        cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
        cameraView.setFocus(CameraKit.Constants.FOCUS_TAP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @OnClick({R.id.camera_flash, R.id.camera_stillshot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.camera_flash:
                switch (cameraView.getFlash()) {
                    case CameraKit.Constants.FLASH_AUTO:
                        cameraView.setFlash(CameraKit.Constants.FLASH_ON);
                        flashBtn.setImageResource(R.drawable.cam_action_flash);
                        break;
                    case CameraKit.Constants.FLASH_ON:
                        cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                        flashBtn.setImageResource(R.drawable.cam_action_flash_off);
                        break;
                    case CameraKit.Constants.FLASH_OFF:
                        cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
                        flashBtn.setImageResource(R.drawable.cam_action_flash_auto);
                        break;
                }
                break;

            case R.id.camera_stillshot:
                cameraView.captureImage(cameraKitImage -> {
                    Bitmap img = cameraKitImage.getBitmap();
                    cameraView.stop();
                    String filename = CommonUtil.saveBitmap(this, img); // save img

                    Intent intent = new Intent(this, PreviewActivity.class);
                    intent.putExtra("filename", filename);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT); // pass result to preview activity
                    startActivity(intent);
                    finish();
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
