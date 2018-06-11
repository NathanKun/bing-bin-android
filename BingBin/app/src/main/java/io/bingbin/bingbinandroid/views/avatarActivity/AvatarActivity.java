package io.bingbin.bingbinandroid.views.avatarActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.RefWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.BingBinApp;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.utils.AvatarHelper;
import io.bingbin.bingbinandroid.utils.BingBinHttp;
import io.bingbin.bingbinandroid.utils.GridviewImageAdapter;
import okhttp3.Response;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartUser;

public class AvatarActivity extends AppCompatActivity {

    @Inject
    BingBinHttp bbh;

    @BindView(R.id.avatar_rabbit_gridview)
    GridView avatarRabbitGridview;
    @BindView(R.id.avatar_leaf_gridview)
    GridView avatarLeafGridview;
    @BindView(R.id.avatar_footer_avatar_imageview)
    ImageView avatarFooterAvatarImageview;
    @BindView(R.id.avatar_progress_bar)
    ProgressBar avatarProgressBar;

    private int maxAllowRabbitId;
    private int maxAllowLeafId;

    private int selectedRabbitId;
    private int selectedLeafId;

    private SmartUser currentUser;

    private List<Bitmap> rabbitImgs;
    private List<Bitmap> leafImgs;
    private Bitmap footerAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        ((BingBinApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        // get current user
        currentUser = UserSessionManager.getCurrentUser(this);

        // find max allow rabbit/leaf id
        maxAllowRabbitId = AvatarHelper.getAllowMaxRabbitId(currentUser.getEcoPoint());
        maxAllowLeafId = AvatarHelper.getAllowMaxLeafId(currentUser.getSunPoint());

        // get user using rabbit/leaf id
        selectedRabbitId = currentUser.getRabbit();
        selectedLeafId = currentUser.getLeaf();

        rabbitImgs = AvatarHelper.getRabbitBitmapsForChangingAvatar(this, currentUser.getEcoPoint());
        leafImgs = AvatarHelper.getLeafBitmapsForChangingAvatar(this, currentUser.getSunPoint());

        // show all rabbits and leaves
        avatarRabbitGridview.post(() -> avatarRabbitGridview.setAdapter(new GridviewImageAdapter(this,
                rabbitImgs)));
        avatarRabbitGridview.post(() -> avatarLeafGridview.setAdapter(new GridviewImageAdapter(this,
                leafImgs)));

        // show user current avatar
        generateAvatar();

        // rabbit grid items onClick listeners
        avatarRabbitGridview.setOnItemClickListener((parent, view, position, id) -> {
            if (position < maxAllowRabbitId) {
                selectedRabbitId = position + 1;
                generateAvatar();
            } else {
                Toast.makeText(this.getApplicationContext(), "Vous devez avoir plus d'EcoPoint pour avoir cet avatar", Toast.LENGTH_SHORT).show();
            }
        });

        // leaf grid items onClick listeners
        avatarLeafGridview.setOnItemClickListener((parent, view, position, id) -> {
            if (position < maxAllowLeafId) {
                selectedLeafId = position + 1;
                generateAvatar();
            } else {
                Toast.makeText(this.getApplicationContext(), "Vous devez avoir plus de SunPoint pour avoir ce badge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.avatar_footer_ok_btn)
    void okOnClick(View view) {
        showLoader(true);
        (new Thread(() -> {
            Response[] responses = bbh.modifyAvatar(currentUser.getToken(), selectedRabbitId, selectedLeafId);

            if (responses == null) {
                return;
            }

            Response resRabbit = responses[0];
            Response resLeaf = responses[1];

            if (!(resRabbit.isSuccessful() && resLeaf.isSuccessful())) {
                    runOnUiThread(() -> {
                        Toast.makeText(AvatarActivity.this.getApplicationContext(),
                            R.string.bingbinhttp_onresponsenotsuccess, Toast.LENGTH_SHORT).show();
                        showLoader(false);
                });
                return;
            }

            try {
                String bodyRabbit = resRabbit.body().string();
                String bodyLeaf = resLeaf.body().string();

                Log.d("modifyRabbit", bodyRabbit);
                Log.d("modifyLeaf", bodyLeaf);

                JSONObject jsonRabbit = new JSONObject(bodyRabbit);
                JSONObject jsonLeaf = new JSONObject(bodyLeaf);
                if (!(jsonRabbit.getBoolean("valid") && jsonLeaf.getBoolean("valid"))) {
                    runOnUiThread(() -> {
                        Toast.makeText(AvatarActivity.this.getApplicationContext(),
                            "Not valid", Toast.LENGTH_SHORT).show();
                        showLoader(false);
                    });
                    return;
                }

                // all valid
                finish();
                runOnUiThread(() -> recycleBitmaps());

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(AvatarActivity.this.getApplicationContext(),
                            "Response IOException", Toast.LENGTH_SHORT).show();
                    showLoader(false);
                });
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(AvatarActivity.this.getApplicationContext(),
                            R.string.bingbinhttp_onjsonparseerror, Toast.LENGTH_SHORT).show();
                    showLoader(false);
                });
            }
        })).start();
    }

    private void generateAvatar() {
        footerAvatar = AvatarHelper.generateAvatar(this, selectedRabbitId, selectedLeafId, 2);
        Glide.with(this)
                .load(footerAvatar)
                .into(avatarFooterAvatarImageview);
    }

    private void showLoader(boolean show) {
        if (show) {
            avatarProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            avatarProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        recycleBitmaps();
    }

    private void recycleBitmaps() {
        avatarRabbitGridview.setAdapter(null);
        avatarLeafGridview.setAdapter(null);
        avatarFooterAvatarImageview.setImageBitmap(null);
        footerAvatar.recycle();
        for(Bitmap bm : rabbitImgs) {
            bm.recycle();
        }
        for(Bitmap bm : leafImgs) {
            bm.recycle();
        }
    }
}
