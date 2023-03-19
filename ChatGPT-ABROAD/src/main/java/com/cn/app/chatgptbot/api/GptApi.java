package com.cn.app.chatgptbot.api;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.chatgptbot.dto.GptAlphaDto;
import com.cn.app.chatgptbot.dto.GptBingDto;
import com.cn.app.chatgptbot.dto.GptTurboDto;
import com.cn.app.chatgptbot.model.GptAlphaModel;
import com.cn.app.chatgptbot.model.GptTurboModel;
import com.cn.app.chatgptbot.msg.Result;
import com.cn.app.chatgptbot.uitls.GptUtil;
//import com.cn.app.chatgptbot.uitls.ProxyUtil;
import com.cn.app.chatgptbot.uitls.WeChatDetectUtils;
import com.cn.app.chatgptbot.uitls.WebClientUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The type Gpt api controller.
 *
 * @author bdth
 * @email  2074055628@qq.com
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public final class GptApi {

//    /**
//     * broker tool
//     */
//    private final ProxyUtil proxyUtil;

    /**
     * weChat tool
     */
    private final WeChatDetectUtils weChatDetectUtils;


    /**
     * Gets open id.
     *
     * @param code the code
     * @return the open id
     */
    @GetMapping(value = "/auth/{code}", name = "WeChat-OpenId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOpenId(@PathVariable final String code) {
        // Returns  WeChat user OpenId
        log.info("code" + code);
        Result result = Result.data(weChatDetectUtils.getUserOpenId(code));
        Gson gson = new Gson();

        log.info("result: "+ gson.toJson(result));
        return result;
    }

    /**
     * Gpt turbo result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/chat/turbo", name = "GPT-Turbo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result gptTurbo(@Validated @RequestBody final GptTurboDto dto) {
        final List<GptTurboModel.Messages> messages = dto.getMessages();
        /*
         * Obtain user-sent data for word interception
         * last one is always the data sent by  user
         */
        weChatDetectUtils.filtration(messages.get(messages.size() - 1).getContent(), dto.getOpenId());
        // switch to the OpenAPI model
        final GptTurboModel gptTurboModel = GptTurboDto.convertToGptTurboModel(dto);
        // select the key randomly
        final String mainKey = GptUtil.getMainKey();
        // execute the request
        return Result.data(WebClientUtil.build("chat/completions", gptTurboModel, mainKey));

    }


    /**
     * Gpt alpha result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/chat/official", name = "GPT-official", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result gptAlpha(@Validated @RequestBody final GptAlphaDto dto) {
        //filter sensitive words ( WeChat )
        weChatDetectUtils.filtration(dto.getPrompt(), dto.getOpenId());
        // to DTO
        final GptAlphaModel gptAlphaModel = GptAlphaDto.convertToGptAlphaModel(dto);
        // get  key at random
        final String mainKey = GptUtil.getMainKey();
        return Result.data(
                WebClientUtil.build( "images/generations", gptAlphaModel, mainKey)
        );
    }

    /**
     * Gpt bing result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/chat/bing", name = "微软Bing机器人", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result gptBing(@Validated @RequestBody final GptBingDto dto) {
        //filter sensitive words ( WeChat )
        log.info("必应传参：" + JSONObject.toJSONString(dto));
        weChatDetectUtils.filtration(dto.getParam(), dto.getOpenId());
        Result result = Result.data(WebClientUtil.intranet(dto.getParam()));
        log.info("必应返回结果：" + JSONObject.toJSONString(result));
        // send  request
        return result;

    }


}
