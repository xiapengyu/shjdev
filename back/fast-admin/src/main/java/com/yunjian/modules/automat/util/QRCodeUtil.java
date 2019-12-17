package com.yunjian.modules.automat.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;

/**
 * 二维码生成解析类
 * @author Admin
 *
 */
public class QRCodeUtil {
	/** 字符编码UTF-8 */
    private static final String CHARSET_UTF8 = "utf-8";
    
    /***
     * 生成二维码图片
     * @param content 二维码携带文本内容
     * @return BufferedImage
     * @throws Exception
     */
    public static BufferedImage createImage(String content, int qrSize) throws Exception {
        HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF8);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
    
    /**
     * 生成二维码图片（不包含图片）
     * @param content 二维码携带文本内容
     * @param qcSize 二维码大小
     * @param qrPath 生成的二维码文件全路径名
     * @throws Exception
     */
    public static InputStream createQRCode(String content, int qcSize ,String qrPath) throws Exception{
        BufferedImage image = QRCodeUtil.createImage(content, qcSize);
        File file = new File(qrPath);
        ImageIO.write(image, "png", file);
        file.delete();
        return bufferedImageToInputStream(image);
    }
    
    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return 二维码携带内容
     * @throws Exception
     */
    public static String parseQRCode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        HashMap<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET_UTF8);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     *
     * @param path 二维码所在路径
     * @return 二维码携带文本内容
     * @throws Exception
     */
    public static String parseQRCode(String path) throws Exception {
        return QRCodeUtil.parseQRCode(new File(path));
    }
    
    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return null;
    }
}
