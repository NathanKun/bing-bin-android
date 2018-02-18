package io.bingbin.bingbinandroid.views.mainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.views.loginActivity.LoginActivity;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;


/**
 * User fragment.
 * Contains user's information and logout button.
 *
 * @author Junyang HE
 */
public class RankFragment extends Fragment {

    private Unbinder unbinder;

    public RankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RankFragment.
     */
    public static RankFragment newInstance() {
        return new RankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        // show avatar
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        String avatarUrl = activity.getCurrentUser().getAvatarUrl();
        if (StringUtils.isNotBlank(avatarUrl)) {
            Uri uri = Uri.parse(avatarUrl);
            //userAvatar.setImageURI(uri);
        }
        // if user has no avatar, do nothing, keep the default image holder

        // show user info
        //usernameTextview.setText(activity.getCurrentUser().getUsername());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
