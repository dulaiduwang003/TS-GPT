package com.cn.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * gpt相关工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
public class ChatGptUtil {

    /**
     * 解析CHATGPT数据体 flux
     *
     * @param data the data
     * @return the boolean
     */
    public String dataParsing(final String data) {
        if (JSON.isValidObject(data)) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            JSONArray choices = jsonObject.getJSONArray("choices");
            if (choices != null && !choices.isEmpty()) {
                final JSONObject dataSet = choices.getJSONObject(0);
                if (dataSet.containsKey("delta")) {
                    JSONObject delta = dataSet.getJSONObject("delta");
                    if (delta.containsKey("content")) {
                        return delta.getString("content");
                    }
                } else {
                    final JSONObject message = dataSet.getJSONObject("message");
                    return message.getString("content");
                }
            }
        }
        return "";
    }

    /**
     * 文本敏感词检查
     *
     * @param data the data
     * @return boolean
     */
    public boolean isSusceptible(final String data) {

        String processedInput = data.toUpperCase().replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("chat");
        return pattern.matcher(processedInput).find();
    }

}
