package io.bingbin.bingbinandroid.utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Interface for BingBinCallback
 *
 * Created by Junyang HE on 05/03/2018.
 */

public interface BingBinCallbackAction {
    void onFailure();
    void onResponseNotSuccess();
    void onJsonParseError();
    void onNotValid(String errorStr);
    void onTokenNotValid(String errorStr);
    void onValid(JSONObject json) throws JSONException;
    void onAnyError();
}
