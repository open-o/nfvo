package org.openo.orchestrator.nfv.dac.common.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <p>
 * 文件名称: 加解密算法
 * </p>
 * <p>
 * 文件描述: 加解密算法类，提供加密和解密两种算法
 * </p>
 * <p>
 * 版权所有: 版权所有(C)2001-2004
 * </p>
 * <p>
 * 公 司: 深圳市中兴通讯股份有限公司
 * </p>
 * <p>
 * 内容摘要:
 * </p>
 * <p>
 * 其他说明:
 * </p>
 * <p>
 * 完成日期：2002年10月20日
 * </p>
 * <p>
 * 修改记录1:
 * </p>
 * 
 * @version 1.0
 * @author 孙旭东
 * @JTIUSN
 */

public class CipherAlgorithm {
    private static final DebugPrn dMsg = new DebugPrn(CipherAlgorithm.class.getName());

    /* 加密口令的密钥数据 */
    private final static byte[] RAW_KEY_DATA = new byte[] { (byte) 0xF1, (byte) 0xF7, (byte) 0xDF, (byte) 0x9D, (byte) 0x91, (byte) 0xF7, (byte) 0x3D, (byte) 0xA7 };

    /* 定义 加密算法,可用 DES,DESede,Blowfish */
    private final static String ALGORITHM = "DES";

    /* 密钥，生成密钥的时间比较长 */
    private static SecretKey key = null;

    /**
     *
     */
    private static void getKey() {
        try {
            // 添加新安全算法,如果用JCE就要把它添加进去
            // Security.addProvider(new com.sun.crypto.provider.SunJCE());
            // 获取密匙数据
            // DES算法要求有一个可信任的随机数源
            // SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(RAW_KEY_DATA);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            key = keyFactory.generateSecret(dks);
        } catch (NoSuchAlgorithmException e1) {
            dMsg.info(e1.getMessage(), e1);
        } catch (Exception e3) {
            dMsg.info(e3.getMessage(), e3);
        }
    }

    /**
     * 加密算法
     * 
     * @param clearText
     *            明文
     * @return String 密文
     */
    public static String encrypt(String clearText) {
        String result = null;
        if (key == null) {
            getKey();
        }

        try {
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherByte = c1.doFinal(clearText.getBytes());
            result = byte2hex(cipherByte);
        } catch (Exception e1) {
            dMsg.info(e1.getMessage());
        } 
        return result;
    }

    /**
     * 解密算法
     * 
     * @param cipherText
     *            密文
     * @return String 明文
     */
    public static String decrypt(String cipherText) {
        String result = cipherText;
        if (key == null) {
            getKey();
        }

        try {
            /* 先转化字符串成为字节流 */
            byte[] cipherByte = string2byte(cipherText);
            // 解密
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, key);
            byte[] clearByte = c1.doFinal(cipherByte);
            result = new String(clearByte);
            return result;
        } catch (Exception e1) {
            dMsg.info(e1.getMessage());
        }
        return result;
    }

    /**
     * 二进制转字符串
     * 
     * @param textByte
     * @return
     */
    private static String byte2hex(byte[] textByte) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < textByte.length; n++) {
            stmp = (java.lang.Integer.toHexString(textByte[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            // if (n<b.length-1) hs=hs+":";
        }
        return hs.toUpperCase();
    }

    /**
     * 字符串转二进制
     * 
     * @param hexText
     * @return
     */
    private static byte[] string2byte(String hexText) {
        int len = hexText.length();
        /* 一个字节是由两个字符组成的 */
        byte[] hexByte = new byte[len / 2];
        String stmp = "";
        for (int i = 0; i < len; i += 2) {
            stmp = hexText.substring(i, i + 2);
            // hexByte[i/2]=(byte)(Integer.parseInt(stmp,16)&((int)0xFF));
            hexByte[i / 2] = (byte) Integer.parseInt(stmp, 16);
        }
        return hexByte;
    }
}
