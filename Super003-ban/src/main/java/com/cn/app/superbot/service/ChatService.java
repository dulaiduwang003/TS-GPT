package com.cn.app.superbot.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.model.FaceModel;
import com.cn.app.superbot.model.GptFourModel;


/**
 * The interface Gpt service.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public interface ChatService {


    /**
     * Chat gpt 4 json object.
     *
     * @param model the model
     * @return the json object
     */
    JSONObject chatGpt4(final GptFourModel model);




    /**
     * Http string.
     *
     * @param body the body
     * @return the string
     */
    JSONObject build(final Object body, final String url);


    /**
     * Face string.
     *
     * @param model the model
     * @return the string
     */
    JSONObject face(final FaceModel model);



}
