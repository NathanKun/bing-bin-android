package io.bingbin.bingbinandroid.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
                    .add("pseudo", firstname)
                    .add("email", email)
                    .add("password", password)
                    .add("name", firstname)
                    .add("firstname", firstname)
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
                    .add("email", email)
                    .add("password", password)
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
    public void googleLogin(Callback callback, String googleToken) {
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + "Google/authorizeLogin/" + googleToken)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // synchronous google login
    public Response googleLogin(String googleToken) throws IOException {
        Request build = new Request.Builder()
                .url(baseUrl + "Google/authorizeLogin/" + googleToken)
                .build();
        return client.newCall(build)
                .execute();
    }

    // Facebook/authorizeLogin
    public void facebookLogin(Callback callback, String facebookToken) {
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + "Facebook/authorizeLogin/" + facebookToken)
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // synchronous facebook login
    public Response facebookLogin(String facebookToken) throws IOException {
        Request build = new Request.Builder()
                .url(baseUrl + "Facebook/authorizeLogin/" + facebookToken)
                .build();
        return client.newCall(build)
                .execute();
    }

    // Rankinkg/getLadder
    public void getLadder(Callback callback, String BingBinToken, String Duration) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("BingBinToken", BingBinToken)
                    .add("Duration", Duration)
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
                    .add("BingBinToken", BingBinToken)
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
    public void uploadscan(Callback callback, String BingBinToken, String trashName, String trashCategory, File image) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("BingBinToken", BingBinToken)
                    .addFormDataPart("trashName", trashName)
                    .addFormDataPart("trashCategory", trashCategory)
                    .addFormDataPart("img", image.getName(), RequestBody.create(MediaType.parse("image/*"), image))
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
                    .add("BingBinToken", BingBinToken)
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

    public static void main(String[] args) {
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        };
        BingBinHttp bbh = new BingBinHttp();

        //bbh.register(cb, "email@em.ail", "first", "pw");
        // {"valid":true,"token":"151820015184385505a818896df6f4679778383"}

        //bbh.login(cb, "email@em.ail", "pw");
        // {"valid":true,"data":{"id":"15184385505a818896de99a1.06775327","name":"first","firstname":"first","email":"email@em.ail","img_url":null,"date_nais":null,"eco_point":null,"fb_id":null,"google_id":null,"pseudo":"first"},"token":"444710015184386195a8188db2345d499310863"}

        //bbh.getMyInfo(cb, "444710015184386195a8188db2345d499310863");
        // {"valid":true,"data":{"name":"first","firstname":"first","email":"email@em.ail","img_url":null,"date_nais":null,"fb_id":null,"pseudo":"first","eco_point":null}}

        //bbh.facebookLogin(cb, "EAAB3bAAZBH2EBAD5G5IITOa1hz0Lr7zxXcup6LrXToEINXXLHxMukIB6fiJY0TvOJ7q7RPi5T6S1Ldv0RFVrb1YZAyNJkZA03boMt509flxYQr6x6GUHyI4uZCtPT1DeOZBwiKP4JqLyhYHBncUdhhyXnQqcqI81rJZCsTm014FYouu6kZCKmXZCTsl28aL0ZBeKBkdQZACVAUWM5PNjrZCO7iOeYRsmyx3cyKx42Hi1pQ8ngZDZD");
        // {"token":"984500015184422845a81972c48dd9634891465"}

        //System.out.println(System.getProperty("user.dir"));
        File img = new File("jotun.png");
        System.out.println(img.getName());
        System.out.println(img.getAbsolutePath());
        System.out.println(img.getPath());
        System.out.println(img.exists());
        System.out.println(img.canRead());
        bbh.uploadscan(cb, "984500015184422845a81972c48dd9634891465", "test", "1", img);
    }
}
