package io.bingbin.bingbinandroid.utils;

import android.os.Handler;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http connection class for BingBin
 * Created by Junyang HE on 2018/1/31.
 *
 * @author Junyang HE, Yuzhou SONG
 */

public class BingBinHttp {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseUrl = "https://bingbin.io/index.php/";

    // app/registerValidation
    public void register(Callback callback, String email, String firstname, String password) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("pseudo",firstname)
                    .add("email",email)
                    .add("password",password)
                    .add("name",firstname)
                    .add("firstname",firstname)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "app/registerValidation")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // app/loginAuthorize
    public void login(Callback callback, String email, String password) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    // TODO: Change pseudo to email
                    //.add("email",email)
                    .add("pseudo",email)
                    .add("password",password)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "app/loginAuthorize")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Google/authorizeLogin
    public void googleLogin(Callback callback, String googleToken){
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + "app/loginAuthorize" + googleToken)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Facebook/authorizeLogin
    public void facebookLogin(Callback callback, String facebookToken){
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + "app/loginAuthorize" + facebookToken)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Rankinkg/getLadder
    public void getLadder(Callback callback, String BingBinToken, String Duration) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("BingBinToken",BingBinToken)
                    .add("Duration",Duration)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "Rankinkg/getLadder")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // app/getmyinfo
    public void getMyInfo(Callback callback, String BingBinToken) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("BingBinToken",BingBinToken)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "app/getmyinfo")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // app/uploadscan
    public void getMyInfo(Callback callback, String BingBinToken, String trashName, String trashCategory, File image) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("BingBinToken",BingBinToken)
                    .add("trashName",trashName)
                    .add("trashCategory",trashCategory)
                    .add("image",image)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "app/uploadscan")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // app/gettrashescategories
    public void getTrashesCategories(Callback callback, String BingBinToken) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("BingBinToken",BingBinToken)
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "app/gettrashescategories")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
