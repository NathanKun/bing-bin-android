package io.bingbin.bingbinandroid.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.function.Consumer;

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
    private Consumer<String> onNotValid;
    private Runnable onTokenNotValid;
    private Consumer<JSONObject> onValid;
    private Runnable onAllError;

    public BingBinCallBack(Runnable onFailure, Runnable onResponseNotSuccess, Runnable onJsonParseError,
                           Consumer<String> onNotValid, Runnable onTokenNotValid, Consumer<JSONObject> onValid,
                           Runnable onAllError) {
        this.onFailure = onFailure;
        this.onResponseNotSuccess = onResponseNotSuccess;
        this.onJsonParseError = onJsonParseError;
        this.onNotValid = onNotValid;
        this.onTokenNotValid = onTokenNotValid;
        this.onValid = onValid;
        this.onAllError = onAllError;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        onFailure.run();
        onAllError.run();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(!response.isSuccessful()) {
            onResponseNotSuccess.run();
            onAllError.run();
            return;
        }

        String res = response.body().string();
        try {
            JSONObject json = new JSONObject(res);
            if(!json.getBoolean("valid")) {
                String errorStr = json.getString("error");
                if(StringUtils.contains(errorStr, "token")) {
                    onTokenNotValid.run();
                } else {
                    onNotValid.accept(errorStr);
                }
                onAllError.run();
                return;
            }
            onValid.accept(json.getJSONObject("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            onJsonParseError.run();
            onAllError.run();
        }
    }
}
