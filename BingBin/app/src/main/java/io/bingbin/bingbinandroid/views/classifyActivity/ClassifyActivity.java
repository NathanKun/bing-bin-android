package io.bingbin.bingbinandroid.views.classifyActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

    private Category category;

    @Inject
    BingBinHttp bbh;

    @BindView(R.id.iv_classify_blurimg)
    ImageView ivClassifyBlurimg;
    @BindView(R.id.spinner_classify_category)
    Spinner spinnerClassifyCategory;
    @BindView(R.id.btn_classify_confirmresult)
    Button btnClassifyConfirmresult;
    @BindView(R.id.tv_instruction_title)
    TextView tvInstructionTitle;
    @BindView(R.id.iv_instruction_trashbin)
    ImageView ivInstructionTrashbin;
    @BindView(R.id.instruction_trier_btn)
    Button instructionTrierBtn;
    @BindView(R.id.instruction_recycle_btn)
    Button instructionRecycleBtn;
    @BindView(R.id.constraintlayout_classify_select)
    ConstraintLayout constraintlayoutClassifySelect;
    @BindView(R.id.constraintlayout_classify_instruction)
    ConstraintLayout constraintlayoutClassifyInstruction;
    @BindView(R.id.classify_progress_bar)
    ProgressBar classifyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        constraintlayoutClassifySelect.setVisibility(View.INVISIBLE);
        constraintlayoutClassifyInstruction.setVisibility(View.INVISIBLE);

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
                AsyncTask.execute(() -> initComponents(CommonUtil.uriToFile(
                        Uri.parse(data.getDataString()), this)
                ));
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        //iv_blurImg.setImageBitmap(bitmap);

        // classify
        String classifyResultStr = ClassifyHelper.classify(this, imgFile);

        // Init spinner
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
                ClassifyActivity.this, R.layout.style_spinner_category, Category.values()) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(24);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }

        };

        runOnUiThread(() -> {
            spinnerClassifyCategory.setAdapter(adapter);
            // set selected to result
            spinnerClassifyCategory.setSelection(adapter.getPosition(Category.saveValueOf(classifyResultStr)));
        });

        // init btn "recycle"
        btnClassifyConfirmresult.setOnClickListener((View view) -> {
            category = (Category) spinnerClassifyCategory.getSelectedItem();
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
                                int ecoPoint = json.getJSONObject("data").getInt("gain_eco_point");
                                showRecycleInstruction(category, ecoPoint);
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

        // run after layout rendered, show blurred image
        constraintlayoutClassifySelect.post((new Runnable() {
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

                constraintlayoutClassifySelect.setVisibility(View.VISIBLE);
            }
        }).init(bitmap, ivClassifyBlurimg));
    }

    /**
     * show instruction layout
     *
     * @param category category enum
     */
    private void showRecycleInstruction(Category category, int ecoPoint) {
        String title = "Poubelle " + category.getColor().getFrenchName();

        tvInstructionTitle.setText(title);

        Bitmap trashbinImg = BitmapFactory.decodeResource(getResources(), category.getColor().getImageResource());
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

        constraintlayoutClassifyInstruction.setVisibility(View.VISIBLE);
        constraintlayoutClassifySelect.setVisibility(View.GONE);
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
}
