package io.bingbin.bingbinandroid.views.classifyActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.Category;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.utils.ClassifyHelper;
import io.bingbin.bingbinandroid.utils.CommonUtil;
import io.bingbin.bingbinandroid.views.instructionActivity.InstructionActivity;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studios.codelight.smartloginlibrary.UserSessionManager;

public class ClassifyActivity extends AppCompatActivity {
    private final int CAMERA_RQ = 2333;
    private final int PERMISSIONS_REQUEST = 23333;
    private final int CLASSIFY_END_TRIER = 66;
    private final int CLASSIFY_END_CANCEL = 666;

    private Category category;

    @Inject
    BingBinHttp bbh;

    @BindView(R.id.classify_yesno_yes_btn)
    Button classifYesnoYesbtn;
    @BindView(R.id.classify_yesno_no_btn)
    Button classifYesnoNobtn;
    @BindView(R.id.classify_yesno_result_textview)
    TextView classifyYesnoResultTextview;
    @BindView(R.id.classify_yesno_constraintlayout)
    ConstraintLayout classifyYesnoConstraintLayout;

    @BindView(R.id.classify_finish_title_textview)
    TextView classifyFinishTitleTextview;
    @BindView(R.id.classify_finish_trashbin_imageview)
    ImageView ivInstructionTrashbin;
    @BindView(R.id.classify_finish_trier_btn)
    Button instructionTrierBtn;
    @BindView(R.id.classify_finish_recycle_btn)
    Button instructionRecycleBtn;
    @BindView(R.id.classify_finish_constraintlayout)
    ConstraintLayout classifyFininshConstraintLayout;

    @BindView(R.id.classify_select_header)
    TextView classifySelectHeader;
    @BindView(R.id.classify_select_confirm_btn)
    Button classifySelectConfirmBtn;
    @BindView(R.id.classify_select_selected_imageview)
    AppCompatImageView classifySelectSelectedImageview;
    @BindView(R.id.classify_select_selected_textview)
    TextView classifySelectSelectedTextview;
    @BindView(R.id.classify_select_selectedbar_layout)
    LinearLayout classifySelectSelectedbarLayout;
    @BindView(R.id.classify_select_grid_gridlayout)
    GridLayout classifySelectGridGridlayout;
    @BindView(R.id.classify_select_constraintlayout)
    ConstraintLayout classifySelectConstraintLayout;
    @BindView(R.id.classify_select_rectangle)
    View classifySelectRectangle;

    @BindView(R.id.classify_select_img_1)
    AppCompatImageView classifySelectImg1;
    @BindView(R.id.classify_select_img_2)
    AppCompatImageView classifySelectImg2;
    @BindView(R.id.classify_select_img_3)
    AppCompatImageView classifySelectImg3;
    @BindView(R.id.classify_select_img_4)
    AppCompatImageView classifySelectImg4;
    @BindView(R.id.classify_select_img_5)
    AppCompatImageView classifySelectImg5;
    @BindView(R.id.classify_select_img_6)
    AppCompatImageView classifySelectImg6;
    @BindView(R.id.classify_select_img_7)
    AppCompatImageView classifySelectImg7;
    @BindView(R.id.classify_select_img_8)
    AppCompatImageView classifySelectImg8;
    @BindView(R.id.classify_select_img_10)
    AppCompatImageView classifySelectImg10;
    @BindView(R.id.classify_select_img_11)
    AppCompatImageView classifySelectImg11;
    @BindView(R.id.classify_select_img_12)
    AppCompatImageView classifySelectImg12;
    @BindView(R.id.classify_select_img_13)
    AppCompatImageView classifySelectImg13;

    @BindView(R.id.classify_blurimg_imageview)
    ImageView classifyBlurimgImageview;
    @BindView(R.id.classify_progress_bar)
    ProgressBar classifyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        classifyYesnoConstraintLayout.setVisibility(View.INVISIBLE);
        classifyFininshConstraintLayout.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String uriStr = intent.getStringExtra("uri");

        // uri exists, mean
        if (uriStr.equals("")) {
            // start camera
            startCameraActivity();
        } else {
            // show image and ask if is good category
            initComponents(CommonUtil.uriToFile(Uri.parse(uriStr), this));
        }
    }

    // ============
    // On results
    // ============

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                // show image and ask if is good category
                initComponents(CommonUtil.uriToFile(
                        Uri.parse(data.getDataString()), this));
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } else { // if camera fragment canceled
                Intent intent = new Intent(ClassifyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                setResult(CLASSIFY_END_CANCEL, intent);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity();
                } else {
                    // permission denied
                    Log.d("request permission", "not ok");
                    Toast.makeText(this, "Permission of camera and storage is needed", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    // ============
    // Methods
    // ============

    /**
     * show blurred image and result
     *
     * @param imgFile image file to show
     */
    private void initComponents(File imgFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());
        // classify result
        String classifyResultStr = ClassifyHelper.classify(this, imgFile);

        /*
         * YesNo
         */

        // run after layout rendered, show blurred image and category result
        classifyYesnoConstraintLayout.post((new Runnable() {
            ImageView iv;
            Bitmap bm;

            private Runnable init(Bitmap bm, ImageView iv) {
                this.bm = bm;
                this.iv = iv;
                return this;
            }

            @Override
            public void run() {
                final int ivWidth = iv.getMeasuredWidth();
                final int ivHeight = iv.getMeasuredHeight();
                Log.d("blur", String.valueOf(ivWidth));
                Log.d("blur", String.valueOf(ivHeight));
                bm = CommonUtil.scaleBitmapToCropFill(bm, ivHeight, ivWidth);

                Bitmap blurImg = Bitmap.createBitmap(ivWidth, ivHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(blurImg);
                canvas.translate(-iv.getLeft(), -iv.getTop());
                Paint paint = new Paint();
                paint.setFlags(Paint.FILTER_BITMAP_FLAG);
                canvas.drawBitmap(bm, 0, 0, paint);

                blurImg = CommonUtil.rsBlur(ClassifyActivity.this, blurImg, 25);
                iv.setImageBitmap(blurImg);

                classifyYesnoResultTextview.setText(Category.getFrenchNameByName(classifyResultStr));

                classifyYesnoConstraintLayout.setVisibility(View.VISIBLE);
            }
        }).init(bitmap, classifyBlurimgImageview));

        // init btn "OUI"
        classifYesnoYesbtn.setOnClickListener((View view) -> {
            category = Category.fromFrenchName(classifySelectSelectedTextview.getText().toString());
            Callback cb = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(ClassifyActivity.this,
                                "Erreur de connextion", Toast.LENGTH_SHORT).show();
                        showLoader(false);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        try {
                            JSONObject json = new JSONObject(res);
                            if (json.getBoolean("valid")) {
                                int ecoPoint = json.getInt("gain_eco_point");
                                runOnUiThread(() -> showFinishLayout(category, ecoPoint));
                            } else {
                                String errorMsg = json.getString("error");
                                runOnUiThread(() -> Toast.makeText(ClassifyActivity.this,
                                        errorMsg, Toast.LENGTH_SHORT).show());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(ClassifyActivity.this,
                                    "json parse error", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(ClassifyActivity.this,
                                "Request not successful", Toast.LENGTH_SHORT).show());
                    }

                    runOnUiThread(() -> showLoader(false));
                }
            };

            showLoader(true);
            bbh.uploadscan(cb, UserSessionManager.getCurrentUser(this).getToken(),
                    category.name(), String.valueOf(category.getCategoryId()), imgFile);
        });

        // init btn "NON"
        classifYesnoNobtn.setOnClickListener((View view) -> { // goto Select
            // set gridlayout height
            classifyYesnoConstraintLayout.setVisibility(View.GONE);
            classifySelectConstraintLayout.setVisibility(View.VISIBLE);

            classifySelectGridGridlayout.post(() -> {
                ViewGroup.LayoutParams params = classifySelectGridGridlayout.getLayoutParams();
                params.height = classifySelectConfirmBtn.getTop()
                        - classifySelectSelectedbarLayout.getBottom() - 75;
                params.width = classifySelectRectangle.getWidth();
                Log.d("Adjust Grid btn top", String.valueOf(classifySelectConfirmBtn.getTop()));
                Log.d("Adjust Grid text btm", String.valueOf(classifySelectSelectedbarLayout.getBottom()));
                if (params.height >= 340) {
                    classifySelectGridGridlayout.setLayoutParams(params);
                    Log.d("Adjust Gridlayout", "Adjusted");
                } else {
                    Log.d("Adjust Gridlayout", "Vertical Space not enough");
                }
            });
        });

        /*
         * Select
         */
        classifySelectConfirmBtn.setOnClickListener((View view) -> {
            classifySelectConstraintLayout.setVisibility(View.GONE);

            classifyYesnoResultTextview.setText(classifySelectSelectedTextview.getText());
            classifyYesnoConstraintLayout.setVisibility(View.VISIBLE);
        });

        /*
         * Finish
         */
        // set in showFinishLayout(Category category, int ecoPoint)
    }

    /**
     * show finish layout
     *
     * @param category category enum
     */
    private void showFinishLayout(Category category, int ecoPoint) {
        String title = category.getTrashbin().getFrenchName();

        classifyFinishTitleTextview.setText(title);

        Bitmap trashbinImg = BitmapFactory.decodeResource(getResources(), category.getTrashbin().getImageResource());
        ivInstructionTrashbin.setImageBitmap(trashbinImg);


        instructionTrierBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ClassifyActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            intent.putExtra("ecopoint", ecoPoint);
            setResult(CLASSIFY_END_TRIER, intent);
            finish();
        });
        instructionRecycleBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ClassifyActivity.this, InstructionActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });

        classifyFininshConstraintLayout.setVisibility(View.VISIBLE);
        classifyYesnoConstraintLayout.setVisibility(View.GONE);
    }

    /**
     * Check permissions and start camera activity
     */
    private void startCameraActivity() {
        // check and request permission, onRequestPermissionsResult method in MainActivity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    this.PERMISSIONS_REQUEST);
            return;
        }
        startCamera();
    }

    private void startCamera() {
        new MaterialCamera(this)
                .audioDisabled(true)
                .stillShot()
                .allowRetry(true)
                .start(CAMERA_RQ);
    }

    private void showLoader(boolean show) {
        if (show) {
            classifyProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            classifyProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @OnClick({R.id.classify_select_img_1, R.id.classify_select_img_2, R.id.classify_select_img_3,
            R.id.classify_select_img_4, R.id.classify_select_img_5, R.id.classify_select_img_6,
            R.id.classify_select_img_7, R.id.classify_select_img_8, R.id.classify_select_img_10,
            R.id.classify_select_img_11, R.id.classify_select_img_12, R.id.classify_select_img_13,
            R.id.classify_select_other})
    void imageOnClick(View view) {
        switch(view.getId()) {
            case R.id.classify_select_other:
                classifySelectSelectedImageview.setImageResource(R.drawable.btn_ecopoint);
                classifySelectSelectedTextview.setText("Autre");
                break;
            case R.id.classify_select_img_1:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_1_plastic);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(1));
                break;
            case R.id.classify_select_img_2:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_2_metal);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(2));
                break;
            case R.id.classify_select_img_3:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_3_cardboard);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(3));
                break;
            case R.id.classify_select_img_4:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_4_paper);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(4));
                break;
            case R.id.classify_select_img_5:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_5_glass);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(5));
                break;
            case R.id.classify_select_img_6:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_6_food);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(6));
                break;
            case R.id.classify_select_img_7:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_7_toxic);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(7));
                break;
            case R.id.classify_select_img_8:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_8_cumbersome);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(8));
                break;
            case R.id.classify_select_img_10:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_10_electronic);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(10));
                break;
            case R.id.classify_select_img_11:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_11_battery_lightball);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(11));
                break;
            case R.id.classify_select_img_12:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_12_clothe);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(12));
                break;
            case R.id.classify_select_img_13:
                classifySelectSelectedImageview.setImageResource(R.drawable.catg_13_medicine);
                classifySelectSelectedTextview.setText(Category.getFrenchNameById(13));
                break;
        }
    }
}
