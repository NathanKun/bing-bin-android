package io.bingbin.bingbinandroid.utils;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
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

public class BingBinCallback implements Callback {

    private BingBinCallbackAction action;
    public BingBinCallback(BingBinCallbackAction action) {
        this.action = action;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        action.onFailure();
        action.onAnyError();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(!response.isSuccessful()) {
            action.onResponseNotSuccess();
            action.onAnyError();
            return;
        }

        String res = response.body().string();
        try {
            Log.d("BingBinCallback", res);
            JSONObject json = new JSONObject(res);
            if(!json.getBoolean("valid")) {
                String errorStr = json.getString("error");
                if(StringUtils.contains(errorStr, "token")) {
                    action.onTokenNotValid(errorStr);
                } else {
                    action.onNotValid(errorStr);
                }
                action.onAnyError();
                return;
            }
            if(json.has("data")) {
                action.onValid(json.getJSONObject("data"));
            } else {
                action.onValid(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            action.onJsonParseError();
            action.onAnyError();
        }
    }
}
