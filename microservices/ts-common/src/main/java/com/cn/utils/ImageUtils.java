package com.cn.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * 图像工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")

@Slf4j
public class ImageUtils {

    /**
     * Convert to base 64 string.
     *
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    public static String convertToBase64(MultipartFile file) {
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64Image = Base64.getEncoder().encodeToString(bytes);
        String prefix = "data:image/" + file.getContentType() + ";base64,";
        return prefix + base64Image;
    }

}
