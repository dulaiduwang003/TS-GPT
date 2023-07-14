package com.cn.app.superbot.utils;


import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


/**
 * The type Upload util.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
@SuppressWarnings("all")
public class UploadUtil {

    /**
     * The Path.
     */
    @Value(value = "${file.path}")
    private String path;

    /**
     * The Domain.
     */
    @Value(value = "${file.domain}")
    private String domain;


    /**
     * Upload string.
     *
     * @param file the file
     * @return the string
     */
    public String upload(MultipartFile file) {
        String fileDownloadUri = "";
        UUID uuid = UUID.randomUUID();
        String originalFileName = file.getOriginalFilename();
        String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
        File dest = new File(path + uuid + fileSuffix);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            fileDownloadUri = domain + "/upload/" + uuid + fileSuffix;
        } catch (Exception e) {
            throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
        }

        return fileDownloadUri;
    }

}
