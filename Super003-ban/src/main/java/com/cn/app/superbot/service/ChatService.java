package com.cn.app.superbot.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.model.FaceModel;
import com.cn.app.superbot.model.GptFourModel;
import org.springframework.web.multipart.MultipartFile;

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
     * Image recognition json object.
     *
     * @param file the file
     * @return the json object
     */
    JSONObject imageRecognition(final MultipartFile file);

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

    /**
     * Bing json object.
     *
     * @param parameter the parameter
     * @return the json object
     */
    String bing(final String parameter);

}
