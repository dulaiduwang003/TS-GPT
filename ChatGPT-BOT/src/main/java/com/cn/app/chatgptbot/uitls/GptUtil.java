
package com.cn.app.chatgptbot.uitls;

import com.cn.app.chatgptbot.config.OpenConfig;
import com.cn.app.chatgptbot.exception.CustomException;
import com.cn.app.chatgptbot.vo.CtlDataVo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type GPT util.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class GptUtil {

    /**
     * disposition
     */
    private final OpenConfig openConfig;

    /**
     * effective
     */
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * lapse
     */
    private static final ConcurrentHashMap<String, String> lapse = new ConcurrentHashMap<>();

    /**
     * choose
     */
    public static boolean choose;

    /**
     * use key
     */
    public static String mainKey = "";

    /**
     * bing url
     */
    @Value(value = "${bingUrl}")
    public String temp;

    /**
     * bingUrl
     */
    public static String bingUrl;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        // traverse the insert
        openConfig.getKeys().forEach(GptUtil::add);
        final Collection<String> allKey = getAllKey();
        final List<String> list = allKey.stream().toList();
        // get the first one
        mainKey = list.get(0);
        bingUrl = temp;
    }

    /**
     * Gets main key.
     *
     * @return the main key
     */
    public static String getMainKey() {
        return mainKey;
    }


    /**
     * Add.
     *
     * @param str the str
     */
    public static void add(String str) {
        cache.putIfAbsent(str, str);
    }


    /**
     * Gets random key.
     */
    public static synchronized void getRandomKey(final String openKey) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final Collection<String> allKey = getAllKey();
        if (CollectionUtils.isEmpty(allKey)) {
            throw new CustomException("缓存池中已无可用的Key 请点击输入框的控制面板打开小汽车联系作者补充");
        }
        int index = new Random().nextInt(allKey.size());
        final List<String> list = allKey.stream().toList();
        final String str = list.get(index);
        if (getMainKey().equals(openKey)) {
            mainKey = cache.get(str);
            lapse.put(openKey, openKey);
        }
    }

    /**
     * Remove key.
     *
     * @param list the list
     */
    public static void removeKey(final List<String> list) {
        list.forEach(cache::remove);
    }

    /**
     * Get all key collection.
     *
     * @return the collection
     */
    public static Collection<String> getAllKey() {
        return cache.values();
    }


    public static CtlDataVo getCtlDataVo() {
        final CtlDataVo ctlDataVo = new CtlDataVo();
        ctlDataVo.setChoose(choose);
        ctlDataVo.setAvailableKeys(getAllKey());
        ctlDataVo.setLapseKeys(getLapseKey());
        ctlDataVo.setThirdPartyKey(null);
        ctlDataVo.setMainKey(mainKey);
        return ctlDataVo;
    }


    /**
     * Gets lapse key.
     *
     * @return the lapse key
     */
    public static Collection<String> getLapseKey() {
        return lapse.values();
    }
}
