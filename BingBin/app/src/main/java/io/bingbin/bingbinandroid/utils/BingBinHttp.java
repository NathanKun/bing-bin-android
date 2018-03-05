package io.bingbin.bingbinandroid.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

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

@SuppressWarnings("unused")
public class BingBinHttp {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseUrl = "https://bingbin.io/index.php/";

    // app/registerValidation
    public void register(BingBinCallback callback, String email, String firstname, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("pseudo", firstname);
        map.put("email",  email);
        map.put("password",  password);
        map.put("name",  firstname);
        map.put("firstname",  firstname);
        asynPost(callback, "app/registerValidation", map);
    }

    // app/loginAuthorize
    public void login(BingBinCallback callback, String email, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password",  password);
        asynPost(callback, "app/loginAuthorize", map);
    }

    // Google/authorizeLogin
    public void googleLogin(BingBinCallback callback, String googleToken) {
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
    public void facebookLogin(BingBinCallback callback, String facebookToken) {
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

    // Ranking/getLadder
    public void getLadder(BingBinCallback callback, String bingBinToken, String duration) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        map.put("Duration", duration);
        map.put("limit", String.valueOf(50));
        asynPost(callback, "Ranking/getLadder", map);
    }

    // app/getmyinfo
    public void getMyInfo(BingBinCallback callback, String bingBinToken) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        asynPost(callback, "app/getmyinfo", map);
    }

    // app/uploadscan
    public void uploadscan(BingBinCallback callback, String bingBinToken, String trashName, String trashCategory, Bitmap bmp) {
        try {

            //Compress Image
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);

            Log.d("uploadScan size", String.valueOf(bos.size()));

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("BingBinToken", bingBinToken)
                    .addFormDataPart("trashName", trashName)
                    .addFormDataPart("trashCategory", trashCategory)
                    .addFormDataPart("img", (new SimpleDateFormat("yyyyMMdd-HHmmssSSS", Locale.FRANCE).format(new Date())),
                            RequestBody.create(MediaType.parse("image/*"), bos.toByteArray()))
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
    public void getTrashesCategories(BingBinCallback callback, String bingBinToken) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        asynPost(callback, "app/gettrashescategories", map);
    }

    // app/sendSunPoint
    public void sendSunPoint(BingBinCallback callback, String bingBinToken, String targetId) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        map.put("targetId",  targetId);
        asynPost(callback, "app/sendSunPoint", map);
    }

    // app/modifyRabbit
    public void modifyRabbit(BingBinCallback callback, String bingBinToken, int rabbitId) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        map.put("rabbitId",  String.valueOf(rabbitId));
        asynPost(callback, "app/modifyRabbit", map);
    }

    // app/modifyLeaf
    public void modifyLeaf(BingBinCallback callback, String bingBinToken, int leafId) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        map.put("leafId",  String.valueOf(leafId));
        asynPost(callback, "app/modifyLeaf", map);
    }

    // app/getMySummary
    public void getMyRecycleCounts(BingBinCallback callback, String bingBinToken) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        asynPost(callback, "app/getMySummary", map);
    }

    // app/getMyScanHistory
    public void getMyRecycleHistory(BingBinCallback callback, String bingBinToken) {
        Map<String, String> map = new HashMap<>();
        map.put("BingBinToken", bingBinToken);
        map.put("limit", String.valueOf(50));
        asynPost(callback, "app/getMyScanHistory", map);
    }

    // synchronous modifyRabbit and modifyLeaf
    public Response[] modifyAvatar(String bingBinToken, int rabbitId, int leafId) {
        try {
            FormBody body = new FormBody.Builder()
                    .add("BingBinToken", bingBinToken)
                    .add("rabbitId", String.valueOf(rabbitId))
                    .build();
            Request request = new Request.Builder()
                    .url(baseUrl + "app/modifyRabbit")
                    .post(body)
                    .build();
            Response resRabbit = client.newCall(request).execute();

            body = new FormBody.Builder()
                    .add("BingBinToken", bingBinToken)
                    .add("leafId", String.valueOf(leafId))
                    .build();
            request = new Request.Builder()
                    .url(baseUrl + "app/modifyLeaf")
                    .post(body)
                    .build();
            Response resLeaf = client.newCall(request).execute();

            return new Response[] {resRabbit, resLeaf};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * general method for make a asynchronous POST
     * @param callback  callback
     * @param url       url for adding to the base url
     * @param params    parameters for POST
     */
    private void asynPost(BingBinCallback callback, String url, Map<String, String> params) {
        try {
            Iterator it = params.entrySet().iterator();

            FormBody.Builder builder = new FormBody.Builder();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                builder.add((String) entry.getKey(), (String) entry.getValue());
            }

            Request request = new Request.Builder()
                    .url(baseUrl + url)
                    .post(builder.build())
                    .build();

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
