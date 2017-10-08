package com.erola.btsearch.spider.dht.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Erola on 2017/9/12.
 */
public class ByteUtil {
    /**
     * 生成随机的nodeId
     * @return
     */
    public static byte[] createRandomNodeId() {
        Random random = new Random();
        byte[] ret = new byte[20];
        random.nextBytes(ret);
        return ret;
    }

    /**
     * int 转换为 byte[]
     * @param value
     * @return
     */
    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    /**
     * byte[] 转换为 int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int ret = -1;
        try (ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(bytes);
             DataInputStream dataInputStream = new DataInputStream(bytesInputStream)) {
            ret = dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * byte[] 转换为16进制字符串
     * @param bytes
     * @return
     */
    public static String byteArrayToHex(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            StringBuffer sb = new StringBuffer(bytes.length * 2);
            for (int i = 0; i < bytes.length; i++) {
                String hexNumber = "0" + Integer.toHexString(0xff & bytes[i]);
                sb.append(hexNumber.substring(hexNumber.length() - 2));
            }
            return sb.toString();
        }
        return null;
    }
}