package com.cn.app.superbot.interfaces;

import com.google.gson.JsonObject;

/**
 * The interface Callback.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public interface Callback {

    /**
     * Called when the asynchronous operation completes successfully.
     *
     * @param rawData the JSON data returned by the operation
     */
    void onSuccess(JsonObject rawData);

    /**
     * Called when the asynchronous operation fails.
     *
     * @param rawData the JSON data returned by the operation
     * @param cause   the cause of the failure
     */
    void onFailure(JsonObject rawData, String cause);


    /**
     * On update.
     *
     * @param rawData the raw data
     */
    void onUpdate(JsonObject rawData);

}

