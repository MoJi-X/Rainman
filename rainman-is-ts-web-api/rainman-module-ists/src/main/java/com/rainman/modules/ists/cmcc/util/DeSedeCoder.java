package com.rainman.modules.ists.cmcc.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Title: DESedeCoder
 * Description: OA UserInfo加解密算法
 *
 * @author 李京泽
 * @date 2021年12月7日
 */
public class DeSedeCoder {
    /**
     * 加密算法
     */
    private static final String ALGORITHM = "DESede";
    /**
     * 生产环境密钥
     */
    private static final String KEY = "ibmblueefinance63618888d";

    private static final String AT = "@";

    private static final int TWO = 2;

    /**
     * 3DES加密算法，按照ecb等model加密
     * 将加密字节，通过byte2hex转换成字符串
     *
     * @param userInfo，传入的用户信息，规范userid+“$”+time
     * @return
     */
    public static String encryptMode(String userInfo) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);

            /** 加密  Cipher.getInstance(Algorithm);**/
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return byte2hex(c1.doFinal(userInfo.getBytes()));
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    // src为加密后的缓冲区

    /**
     * Title: decryptMode
     * Description: 解密userinfo
     *
     * @param userInfo
     * @return
     */
    public static String decryptMode(String userInfo) {
        try {
            byte[] srcB = hex2byte(userInfo.getBytes());

            // 生成密钥
            SecretKey deskey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);

            // 解密
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            String url = new String(c1.doFinal(srcB));

            String[] mingwen = url.split("\\$");
            String userid = "";
            if (mingwen[0].indexOf(AT) >= 0) {
                userid = mingwen[0].substring(0, mingwen[0].indexOf(AT)).toUpperCase();
            } else {
                userid = mingwen[0].toUpperCase();
            }

            return userid;
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * Title: byte2hex
     * Description: byte to hex
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * Title: hex2byte
     * Description: hex to byte
     *
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % TWO) != 0){
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += TWO) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
