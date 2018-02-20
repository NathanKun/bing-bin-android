package io.bingbin.bingbinandroid.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jhe on 20/02/2018.
 *
 * @author Junyang HE
 */

public class BingBinCallBack implements Callback {

    private Runnable onFailure;
    private Runnable onResponseNotSuccess;
    private Runnable onJsonParseError;
    private Runnable onNotValid;
    private Runnable onValid;

    public BingBinCallBack(Runnable onFailure, Runnable onResponseNotSuccess,
                           Runnable onJsonParseError, Runnable onNotValid, Runnable onValid) {
        this.onFailure = onFailure;
        this.onResponseNotSuccess = onResponseNotSuccess;
        this.onJsonParseError = onJsonParseError;
        this.onNotValid = onNotValid;
        this.onValid = onValid;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        onFailure.run();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(!response.isSuccessful()) {
            onResponseNotSuccess.run();
            return;
        }

        String res = response.body().string();
        try {
            JSONObject json = new JSONObject(res);
            if(!json.getBoolean("valid")) {
                onNotValid.run();
                return;
            }
            onValid.run();
        } catch (JSONException e) {
            e.printStackTrace();
            onJsonParseError.run();
        }
    }
}
