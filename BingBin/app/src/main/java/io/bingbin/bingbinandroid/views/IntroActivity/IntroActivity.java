package io.bingbin.bingbinandroid.views.IntroActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.views.mainActivity.MainActivity;

public class IntroActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.intro_slider)
    SliderLayout slider;
    @BindView(R.id.intro_passer_textview)
    TextView introPasserTextview;
    private int[] imagesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        imagesId = new int[]{R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3, R.drawable.intro_4,
                R.drawable.intro_5, R.drawable.intro_6, R.drawable.intro_7, R.drawable.intro_8,
                R.drawable.intro_9, R.drawable.intro_10, R.drawable.intro_11, R.drawable.intro_12,
                R.drawable.intro_13};

        for (int image : imagesId) {
            DefaultSliderView sliderView = new DefaultSliderView(this);

            // initialize SliderLayout
            sliderView.image(image)
                    .setRequestOption((new RequestOptions()).centerInside())
                    .setProgressBarVisible(true)
                    .setBackgroundColor(Color.TRANSPARENT);

            slider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        //slider.setPresetTransformer(SliderLayout.Transformer.Default);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.addOnPageChangeListener(this);
        slider.stopAutoCycle();
    }

    @OnClick(R.id.intro_passer_textview)
    void passerOnClick(View v) {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) introPasserTextview.getLayoutParams();
        params = new ConstraintLayout.LayoutParams(params); // copy the original ones
        if(position == 0) {
            params.verticalBias = 0.98f;
        } else {
            params.verticalBias = 0.05f;
        }
        introPasserTextview.setLayoutParams(params);

        if(position == imagesId.length - 1) {
            introPasserTextview.setText(R.string.intro_commercer_textbutton);
        } else {
            introPasserTextview.setText(R.string.intro_passer_textbutton);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
