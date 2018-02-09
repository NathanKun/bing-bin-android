package io.bingbin.bingbinandroid.utils;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http connection class for BingBin
 * Created by Junyang HE on 2018/1/31.
 *
 * @author Junyang HE
 */

public class BingBinHttp {
    private final OkHttpClient client = new OkHttpClient();

    public void test(Handler handler) {

        (new Thread(){
            @Override
            public void run(){
                try {
                    Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(response.body().string());

                    handler.obtainMessage().sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
