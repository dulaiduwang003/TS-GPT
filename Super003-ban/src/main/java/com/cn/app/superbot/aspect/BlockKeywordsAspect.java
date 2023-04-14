package com.cn.app.superbot.aspect;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.annotations.BlockKeywords;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.dto.GptFourDto;
import com.cn.app.superbot.dto.GptTurboDto;
import com.cn.app.superbot.entity.MiniProgramUser;
import com.cn.app.superbot.exception.LimitException;
import com.cn.app.superbot.service.UserService;
import com.cn.app.superbot.utils.WeChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;

/**
 * The type Block keywords aspect.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Aspect
public class BlockKeywordsAspect {

    /**
     * The Sensitive words.
     */
    private final static HashSet<String> sensitiveWords = new HashSet<>(Arrays.asList("openai", "gpt"));

    /**
     * The chat utils.
     */
    private final WeChatUtils weChatUtils;

    /**
     * The User service.
     */
    private final UserService userService;

    /**
     * Usage restrictions object.
     *
     * @param joinPoint     the join point
     * @param blockKeywords the block keywords
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("@annotation(blockKeywords)")
    public Object blockKeywordsFilter(ProceedingJoinPoint joinPoint, BlockKeywords blockKeywords) throws Throwable {
        final MiniProgramUser _m = userService.usesFrequencyInfo();
        if (_m.getFrequency() < blockKeywords.limit()) {
            throw new LimitException("当前功能需要消耗" + blockKeywords.limit() + MsgConstants.LIMIT_NOT_ERR, 400);
        }
        final Object arg = joinPoint.getArgs()[0];
        try {
            //filter prohibited words
            weChatUtils.filtration(arg.toString());
        } catch (Exception e) {
            log.error("调取微信文本内容检测失败");
        }
        final Object proceed = joinPoint.proceed();
        final JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(proceed));
        final Integer code = result.getInteger("code");
        if (code == 200) {
            userService.decreaseFrequency(_m.getMiniProgramUserId(), _m.getFrequency() - blockKeywords.limit());
            if (blockKeywords.check()) {
                String s;
                if (arg instanceof GptTurboDto || arg instanceof GptFourDto || arg instanceof MultipartFile) {
                    s = result.getJSONObject("data")
                            .getJSONArray("choices")
                            .getJSONObject(0).getJSONObject("message").getString("content");
                } else {
                    s = result.toString();
                }
                if (containsSensitiveWords(s)) {
                    throw new LimitException(MsgConstants.GPT_SUSCEPTIBLE_ERR, 400);
                }
            }
        }

        return proceed;
    }

    /**
     * Contains sensitive words boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public boolean containsSensitiveWords(final String text) {
        String cleanedText = text.replaceAll("[^a-zA-Z]", "").toLowerCase();
        return sensitiveWords.stream().anyMatch(cleanedText::contains);
    }
}
