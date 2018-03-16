package io.bingbin.bingbinandroid.views.classifyActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.Category;
import io.bingbin.bingbinandroid.utils.BingBinCallback;
import io.bingbin.bingbinandroid.utils.BingBinCallbackAction;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.utils.ClassifyHelper;
import io.bingbin.bingbinandroid.utils.CommonUtil;
import io.bingbin.bingbinandroid.views.instructionActivity.InstructionActivity;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;
import studios.codelight.smartloginlibrary.UserSessionManager;

public class ClassifyActivity extends AppCompatActivity {

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
    ImageView classifyFinishTrashbinImageview;
    @BindView(R.id.classify_finish_trier_btn)
    Button classifyFinishTrierBtn;
    @BindView(R.id.classify_finish_recycle_btn)
    Button classifyFinishRecycleItBtn;
    @BindView(R.id.classify_finish_rectangle)
    View classifyFinishRectangle;
    @BindView(R.id.classify_finish_constraintlayout)
    ConstraintLayout classifyFinishConstraintLayout;
    @BindView(R.id.classify_finish_longtext)
    TextView classifyFinishLongtext;

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
    @BindView(R.id.classify_select_img_9)
    AppCompatImageView classifySelectImg9;
    @BindView(R.id.classify_select_img_10)
    AppCompatImageView classifySelectImg10;
    @BindView(R.id.classify_select_img_11)
    AppCompatImageView classifySelectImg11;
    @BindView(R.id.classify_select_img_12)
    AppCompatImageView classifySelectImg12;
    @BindView(R.id.classify_select_other)
    TextView classifySelectOther;

    @BindView(R.id.classify_blurimg_imageview)
    ImageView classifyBlurimgImageview;
    @BindView(R.id.classify_progress_bar)
    ProgressBar classifyProgressBar;
    private Bitmap blurImg;

    AppCompatImageView[] iconsImageviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        classifyYesnoConstraintLayout.setVisibility(View.INVISIBLE);
        classifyFinishConstraintLayout.setVisibility(View.INVISIBLE);
        showLoader(true);

        (new Thread(() -> {
            Intent intent = getIntent();
            Uri uri = intent.getParcelableExtra("uri");
            if(uri != null) { // from gallery
                try {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        initComponents(bitmap);
                    } catch (OutOfMemoryError e) { // try again with sample bitmap
                        e.printStackTrace();
                        System.gc();

                        // detect screen size and sample bitmap that bitmap smaller than screen
                        int screenHeight = getResources().getDisplayMetrics().heightPixels;
                        int screenWidth = getResources().getDisplayMetrics().widthPixels;

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),
                                null, options);
                        int imgWidth = options.outWidth;
                        int imgHeigh = options.outHeight;
                        int sample = 1;
                        while(imgWidth > sample * screenWidth || imgHeigh > sample * screenHeight) {
                            sample *= 2;
                        }

                        options.inSampleSize = sample;
                        options.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),
                                null, options);
                        initComponents(bitmap);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else { // from camera
                String filename = intent.getStringExtra("filename");
                try {
                    initComponents(CommonUtil.loadBitmap(this, filename, 2));
                } catch (OutOfMemoryError e) { // try again with sample = 2
                    e.printStackTrace();
                    System.gc();

                    Bitmap bitmap = CommonUtil.loadBitmap(this, filename, 4);
                    initComponents(bitmap);
                }
            }
        })).start();
    }

    // ============
    // Methods
    // ============

    /**
     * show blurred image and result
     *
     * @param bitmap image to show
     */
    private void initComponents(Bitmap bitmap) {
        // classify result
        String classifyResultStr = ClassifyHelper.classify(this, bitmap);

        /*
         * YesNo
         */

        // run after layout rendered, show blurred image and category result
        classifyYesnoConstraintLayout.post((() -> {
            final int ivWidth = classifyBlurimgImageview.getMeasuredWidth();
            final int ivHeight = classifyBlurimgImageview.getMeasuredHeight();
            Log.d("blur", String.valueOf(ivWidth));
            Log.d("blur", String.valueOf(ivHeight));

            blurImg = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            // bitmap.recycle(); // image will be upload later, should not recycle here
            blurImg = CommonUtil.rsBlur(ClassifyActivity.this, blurImg, 25);

            Glide.with(this)
                    .load(blurImg)
                    .into(classifyBlurimgImageview);
            classifyYesnoResultTextview.setText(Category.getFrenchNameByName(classifyResultStr));

            classifyYesnoConstraintLayout.setVisibility(View.VISIBLE);
            showLoader(false);
        }));

        // init btn "OUI"
        classifYesnoYesbtn.setOnClickListener((View view) -> {
            category = Category.fromName(classifyResultStr);
            BingBinCallbackAction action = new BingBinCallbackAction() {
                @Override
                public void onFailure() {
                    runOnUiThread(() -> Toast.makeText(ClassifyActivity.this.getApplicationContext(),
                            R.string.bingbinhttp_onfailure, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponseNotSuccess() {
                    runOnUiThread(() -> Toast.makeText(ClassifyActivity.this.getApplicationContext(),
                            R.string.bingbinhttp_onresponsenotsuccess, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onJsonParseError() {
                    runOnUiThread(() -> Toast.makeText(ClassifyActivity.this.getApplicationContext(),
                            R.string.bingbinhttp_onjsonparseerror, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onNotValid(String errorStr) {
                    runOnUiThread(() -> Toast.makeText(ClassifyActivity.this.getApplicationContext(),
                            errorStr, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onTokenNotValid(String errorStr) {
                    runOnUiThread(() -> Toast.makeText(ClassifyActivity.this.getApplicationContext(),
                        errorStr, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onValid(JSONObject json) throws JSONException {
                    int ecoPoint = json.getInt("gain_eco_point");
                    runOnUiThread(() -> {
                        showFinishLayout(category, ecoPoint);
                        showLoader(false);
                    });
                }

                @Override
                public void onAnyError() {
                    runOnUiThread(() -> showLoader(false));
                }
            };


            showLoader(true);
            bbh.uploadscan(new BingBinCallback(action),
                    UserSessionManager.getCurrentUser(this).getToken(),
                    category.name(), String.valueOf(category.getCategoryId()), bitmap);
        });

        // init btn "NON"
        classifYesnoNobtn.setOnClickListener((View view) -> { // goto Select
            // set gridlayout height
            classifyYesnoConstraintLayout.setVisibility(View.GONE);
            classifySelectConstraintLayout.setVisibility(View.VISIBLE);

            classifySelectConstraintLayout.post(() -> {
                ViewGroup.LayoutParams params = classifySelectGridGridlayout.getLayoutParams();
                params.height = classifySelectConfirmBtn.getTop()
                        - classifySelectSelectedbarLayout.getBottom() - 75;
                params.width = classifySelectRectangle.getWidth();

                if (params.height >= 340) {
                    classifySelectGridGridlayout.setLayoutParams(params);
                    Log.d("Adjust Gridlayout", "Adjusted");
                } else {
                    Log.d("Adjust Gridlayout", "Vertical Space not enough");
                }

                int[] bigImgIds = {R.drawable.catg_1_plastic, R.drawable.catg_2_metal, R.drawable.catg_3_cardboard,
                        R.drawable.catg_4_paper, R.drawable.catg_5_glass, R.drawable.catg_6_food,
                        R.drawable.catg_7_lightbulb, R.drawable.catg_8_cumbersome, R.drawable.catg_9_electronic,
                        R.drawable.catg_10_battery, R.drawable.catg_11_clothe, R.drawable.catg_12_medicine};

                iconsImageviews = new AppCompatImageView[] {classifySelectImg1, classifySelectImg2, classifySelectImg3,
                        classifySelectImg4, classifySelectImg5, classifySelectImg6,
                        classifySelectImg7, classifySelectImg8, classifySelectImg9,
                        classifySelectImg10, classifySelectImg11, classifySelectImg12};

                for(int i = 0; i < iconsImageviews.length; i++) {
                    Glide.with(classifySelectGridGridlayout)
                            .load(bigImgIds[i])
                            .into(iconsImageviews[i]);
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
        classifyFinishLongtext.setText(category.getText());

        Glide.with(this)
                .load(category.getTrashbin().getImageResource())
                .into(classifyFinishTrashbinImageview);

        classifyFinishTrierBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ClassifyActivity.this, MainActivity.class);
            intent.putExtra("ecopoint", ecoPoint);
            setResult(RESULT_OK, intent);
            finish();
            blurImg.recycle();
        });
        classifyFinishRecycleItBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ClassifyActivity.this, InstructionActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });

        classifyFinishConstraintLayout.setVisibility(View.VISIBLE);
        classifyYesnoConstraintLayout.setVisibility(View.GONE);

        classifyFinishConstraintLayout.post(() -> {
            int imageHeight = classifyFinishTrashbinImageview.getHeight();
            if(imageHeight < 100) {
                classifyFinishLongtext.setTextSize(14);
            }
        });
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
            R.id.classify_select_img_7, R.id.classify_select_img_8, R.id.classify_select_img_9,
            R.id.classify_select_img_10, R.id.classify_select_img_11, R.id.classify_select_img_12,
            R.id.classify_select_other})
    void imageOnClick(View view) {
        int res = 0;
        String catgName = null;
        switch (view.getId()) {
            case R.id.classify_select_other:
                res = R.drawable.bingbin_icon_no_bg;
                catgName = Category.getFrenchNameById(99);
                break;
            case R.id.classify_select_img_1:
                res = R.drawable.catg_1_plastic;
                catgName = Category.getFrenchNameById(1);
                break;
            case R.id.classify_select_img_2:
                res = R.drawable.catg_2_metal;
                catgName = Category.getFrenchNameById(2);
                break;
            case R.id.classify_select_img_3:
                res = R.drawable.catg_3_cardboard;
                catgName = Category.getFrenchNameById(3);
                break;
            case R.id.classify_select_img_4:
                res = R.drawable.catg_4_paper;
                catgName = Category.getFrenchNameById(4);
                break;
            case R.id.classify_select_img_5:
                res = R.drawable.catg_5_glass;
                catgName = Category.getFrenchNameById(5);
                break;
            case R.id.classify_select_img_6:
                res = R.drawable.catg_6_food;
                catgName = Category.getFrenchNameById(6);
                break;
            case R.id.classify_select_img_7:
                res = R.drawable.catg_7_lightbulb;
                catgName = Category.getFrenchNameById(7);
                break;
            case R.id.classify_select_img_8:
                res = R.drawable.catg_8_cumbersome;
                catgName = Category.getFrenchNameById(8);
                break;
            case R.id.classify_select_img_9:
                res = R.drawable.catg_9_electronic;
                catgName = Category.getFrenchNameById(9);
                break;
            case R.id.classify_select_img_10:
                res = R.drawable.catg_10_battery;
                catgName = Category.getFrenchNameById(10);
                break;
            case R.id.classify_select_img_11:
                res = R.drawable.catg_11_clothe;
                catgName = Category.getFrenchNameById(11);
                break;
            case R.id.classify_select_img_12:
                res = R.drawable.catg_12_medicine;
                catgName = Category.getFrenchNameById(12);
                break;
        }
        Glide.with(this)
                .load(res)
                .into(classifySelectSelectedImageview);
        classifySelectSelectedTextview.setText(catgName);
    }
}
