package com.ytbdmhy.SSO_demo.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

/**
 * @Copyright: weface
 * @Description:
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:36 2019-06-21 created by miaohaoyun
 * @Remarks: thumbnailator 0.48
 */
public class ImageUtil {



    public static void main(String[] args) {
        String imagePath = "C:/Users/Administrator/Desktop/荷花.jpg";
        String pre = imagePath.substring(0, imagePath.indexOf(".jpg"));
        long start = System.currentTimeMillis();
        try {
            Thumbnails.of(imagePath)

                    // 图片尺寸不变，压缩图片文件大小
                    .scale(1f)
                    .outputQuality(0.25f)
                    .outputFormat("jpg")
                    .toFile(pre + "_0.25Quality");

                    // 裁剪
//                    .sourceRegion(Positions.CENTER, 400, 400)
//                    .size(200, 200)
//                    .keepAspectRatio(false)
//                    .toFile(pre + "_region_center.jpg");

                    // 水印
//                    .size(1280, 1024)
//                    .watermark(Positions.BOTTOM_RIGHT, )
//                    .outputQuality(0.8f)
//                    .toFile(pre + "_water_bottom_right.jpg");

                    // 旋转
//                    .size(1280, 1024)
//                    .rotate(90)
//                    .toFile(pre + "_rotate+90.jpg");

                    // 不按照比例，指定大小进行缩放
//                    .size(200, 200)
//                    .keepAspectRatio(false)
//                    .toFile(pre + "_200×200keepAR.jpg");

                    // 按照比例进行缩放
//                    .scale(1.2f)
//                    .toFile(pre + "_120%.jpg");

                    // 按照比例进行缩放
//                    .scale(0.25f)
//                    .toFile(pre + "_25%.jpg");

                    // 指定大小进行缩放
//                    .size(2560, 2048)
//                    .toFile(pre + "_2560×2048.jpg");

                    // 指定大小进行缩放
//                    .size(200, 300)
//                    .toFile(imagePath.substring(0, imagePath.indexOf(".jpg")) + "_200×300.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("over,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
