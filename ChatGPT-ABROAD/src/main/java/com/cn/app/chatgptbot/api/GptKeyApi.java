package com.cn.app.chatgptbot.api;

import com.cn.app.chatgptbot.msg.Result;
import com.cn.app.chatgptbot.uitls.GptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The type Gpt key api.
 *
 * @author bdth
 * @email  2074055628@qq.com
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public final class GptKeyApi {


    /**
     * Gets app status.
     *
     * @return the app status
     */
    @GetMapping(value = "/chat/status", name = "RunningStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAppStatus() {
        return Result.data(GptUtil.getCtlDataVo());
    }


}
