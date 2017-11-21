package io.bingbin.bingbinandroid;

import android.content.Context;
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

import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        // show avatar
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        Uri uri = Uri.parse(activity.getCurrentUser().getAvatarUrl());
        SimpleDraweeView avatar = activity.findViewById(R.id.user_avatar);
        avatar.setImageURI(uri);

        // show user info
        TextView userName = activity.findViewById(R.id.user_name);
        userName.setText(activity.getCurrentUser().getUsername());

        // show logout button
        Button logoutButton = activity.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            SmartUser currentUser = activity.getCurrentUser();
            SmartLogin smartLogin;
            if (currentUser != null) {
                if (currentUser instanceof SmartFacebookUser) {
                    smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                } else if (currentUser instanceof SmartGoogleUser) {
                    smartLogin = SmartLoginFactory.build(LoginType.Google);
                } else {
                    smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                }
                boolean result = smartLogin.logout(activity);
                if (result) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                    activity.finish();
                } else {
                    Log.d("Smart Login", "Logout failed");
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
