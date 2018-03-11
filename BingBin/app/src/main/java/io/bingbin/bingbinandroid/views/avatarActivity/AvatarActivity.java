package io.bingbin.bingbinandroid.views.avatarActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    private int maxAllowRabbitId;
    private int maxAllowLeafId;

    private int selectedRabbitId;
    private int selectedLeafId;

    private SmartUser currentUser;

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

        // show all rabbits and leaves
        avatarRabbitGridview.post(() -> avatarRabbitGridview.setAdapter(new GridviewImageAdapter(this,
                AvatarHelper.getRabbitBitmapsForChangingAvatar(this, currentUser.getEcoPoint()))));
        avatarRabbitGridview.post(() -> avatarLeafGridview.setAdapter(new GridviewImageAdapter(this,
                AvatarHelper.getLeafBitmapsForChangingAvatar(this, currentUser.getSunPoint()))));

        // show user current avatar
        generateAvatar();

        // rabbit grid items onClick listeners
        avatarRabbitGridview.setOnItemClickListener((parent, view, position, id) -> {
            if(position <= maxAllowRabbitId) {
                selectedRabbitId = position + 1;
                generateAvatar();
            } else {
                Toast.makeText(this.getApplicationContext(), "Vous devez avoir plus d'EcoPoint pour avoir cet avatar", Toast.LENGTH_SHORT).show();
            }
        });

        // leaf grid items onClick listeners
        avatarLeafGridview.setOnItemClickListener((parent, view, position, id) -> {
            if(position <= maxAllowLeafId) {
                selectedLeafId = position + 1;
                generateAvatar();
            } else {
                Toast.makeText(this.getApplicationContext(), "Vous devez avoir plus de SunPoint pour avoir ce badge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.avatar_footer_ok_btn)
    void okOnClick(View view) {

        (new Thread(() -> {
            Response[] responses = bbh.modifyAvatar(currentUser.getToken(), selectedRabbitId, selectedLeafId);

            if(responses == null) {
                return;
            }

            Response resRabbit = responses[0];
            Response resLeaf = responses[1];

            if( !(resRabbit.isSuccessful() && resLeaf.isSuccessful()) ) {
                runOnUiThread(() -> Toast.makeText(AvatarActivity.this.getApplicationContext(),
                        R.string.bingbinhttp_onresponsenotsuccess, Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                String bodyRabbit = resRabbit.body().string();
                String bodyLeaf = resLeaf.body().string();

                Log.d("modifyRabbit", bodyRabbit);
                Log.d("modifyLeaf", bodyLeaf);

                JSONObject jsonRabbit = new JSONObject(bodyRabbit);
                JSONObject jsonLeaf = new JSONObject(bodyLeaf);
                if( !(jsonRabbit.getBoolean("valid") && jsonLeaf.getBoolean("valid")) ) {
                    runOnUiThread(() -> Toast.makeText(AvatarActivity.this.getApplicationContext(),
                            "Not valid", Toast.LENGTH_SHORT).show());
                    return;
                }

                // all valid
                finish();

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AvatarActivity.this.getApplicationContext(),
                        "Response IOException", Toast.LENGTH_SHORT).show());
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AvatarActivity.this.getApplicationContext(),
                        R.string.bingbinhttp_onjsonparseerror, Toast.LENGTH_SHORT).show());
            }

            finish();
        })).start();
    }

    private void generateAvatar() {
        Glide.with(this)
                .load(AvatarHelper.generateAvatar(this, selectedRabbitId, selectedLeafId, 2))
                .into(avatarFooterAvatarImageview);
    }
}
