package studios.codelight.smartloginlibrary;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;

/**
 * Copyright (c) 2016 Codelight Studios
 * Created by Kalyan on 9/9/2015.
 */
@SuppressWarnings("all")
public class SmartLoginConfig {

    private String facebookAppId;
    private ArrayList<String> facebookPermissions;
    private GoogleSignInClient googleSignInClient;
    private final Activity activity;
    private final SmartLoginCallbacks callback;


    public SmartLoginConfig(@NonNull Activity activity, SmartLoginCallbacks callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public Activity getActivity() {
        return activity;
    }

    public SmartLoginCallbacks getCallback() {
        return callback;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
        this.googleSignInClient = googleSignInClient;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public ArrayList<String> getFacebookPermissions() {
        return facebookPermissions;
    }

    public void setFacebookPermissions(ArrayList<String> facebookPermissions) {
        this.facebookPermissions = facebookPermissions;
    }

    public static ArrayList<String> getDefaultFacebookPermissions() {
        ArrayList<String> defaultPermissions = new ArrayList<>();
        defaultPermissions.add("public_profile");
        defaultPermissions.add("email");
        defaultPermissions.add("user_birthday");
        return defaultPermissions;
    }

}
