package com.cn.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;


/**
 * 文件上传
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
@Slf4j
public class UploadUtil {

    /**
     * The Endpoint.
     */
    @Value("${oss.ali-oss.endpoint}")
    private String endpoint;

    /**
     * The Access key.
     */
    @Value("${oss.ali-oss.accessKey}")
    private String accessKey;

    /**
     * The Secret key.
     */
    @Value("${oss.ali-oss.secretKey}")
    private String secretKey;

    /**
     * The Bucket name.
     */
    @Value("${oss.ali-oss.bucketName}")
    private String bucketName;

    /**
     * Upload file string.
     *
     * @param file the file
     * @param path the path
     * @return the string
     */
    public String uploadFile(final MultipartFile file, final String path) {

        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKey, secretKey);
        try (InputStream inputStream = file.getInputStream()) {
            String originalFileName = file.getOriginalFilename();

            assert originalFileName != null;
            String fileName;
            fileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'));

            String filePath = path + "/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/png");
            ossClient.putObject(bucketName, filePath, inputStream, objectMetadata);
            return "/" + filePath;

        } catch (IOException e) {
            throw new OSSException();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * Upload image from url string.
     *
     * @param imageUrl the image url
     * @param path     the path
     * @return the string
     */
    public String uploadImageFromUrl(String imageUrl, String path) {
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKey, secretKey);
        try {
            // 生成随机的图片名称
            String fileName = path + "/" + UUID.randomUUID().toString() + ".png";


            // 通过URL下载网络图片到本地
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            // 上传图片到OSS
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);
            return "/" + fileName;
        } catch (IOException e) {
            throw new OSSException();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * Upload base 64 image string.
     *
     * @param base64Image the base 64 image
     * @param path        the path
     * @return the string
     */
    public String uploadBase64Image(final String base64Image, String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        try {
            // 使用UUID生成新的文件名
            String fileName = path + "/" + UUID.randomUUID().toString() + ".jpg";
            // 将Base64图片转换为字节数组
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            // 创建PutObjectRequest对象并上传图片
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(imageBytes), objectMetadata);
            ossClient.putObject(request);
            return "/" + fileName;
        } catch (Exception e) {
            throw new OSSException();
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }

    }

    /**
     * Deleted file.
     *
     * @param path the path
     */
    public void deletedFile(final String path) {
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKey, secretKey);
        try {

            if (path.startsWith("/")) {
                ossClient.deleteObject(bucketName, path.substring(1));
            } else {
                ossClient.deleteObject(bucketName, path);
            }
        } catch (OSSException | ClientException e) {
            throw new OSSException();
        } finally {
            ossClient.shutdown();
        }
    }

}
