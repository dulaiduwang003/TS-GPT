package com.cn.utils;

import com.alipay.api.internal.util.codec.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 二维码生成工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public class QRCodeGenerator {

    /**
     * 根据 地址创建 二维码
     *
     * @param url the url
     * @return the string
     * @throws WriterException the writer exception
     * @throws IOException     the io exception
     */
    public static String generateQRCode(final String url) throws WriterException, IOException {
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0); // 设置二维码边框宽度（单位：像素）

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 设置背景色为透明
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, width, height);

        // 绘制二维码
        graphics.setColor(Color.BLACK); // 设置线条颜色为白色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return "data:image/png;base64," + Base64.encodeBase64String(bytes);
    }


}
