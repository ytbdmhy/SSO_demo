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
        long start = System.currentTimeMillis();
        try {
            Thumbnails.of(imagePath)
                    .size(200, 300)
                    .toFile(imagePath.substring(0, imagePath.indexOf(".jpg")) + "_200×300.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("over,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
