package org.openo.orchestrator.nfv.dac.common.util;

/**
 * <p>文件名: Crypto.java </p>
 * <p>说  明： </p>
 * @version 1.0
 * @author
 */

public class Crypto
{
    /**
     * 加密
     * @param passwd 用户密码
     * @return 密文
     */
    public static String encrypt(String inpasswd)
    {
        return CipherAlgorithm.encrypt(inpasswd);
    }

    /**
     * 解密
     * @param inpasswd 密文
     * @return 用户密码
     */
    public static String unencrypt(String ipasswd)
    {
        return CipherAlgorithm.decrypt(ipasswd);
    }

    private static byte[] getKeyBytes(int len)
    {

        byte kbytes[] = new byte[len];

        byte tmpbytes[] = key.getBytes();

        dMsg.info("temp  xxxx kbytes size --" + kbytes.length + ",len--" + len);
        int keylen = tmpbytes.length;
        for (int i = 0; i < keylen / 2; i++)
        {
            tmpbytes[i] = tmpbytes[keylen - i - 1];

        }
        for (int i = 0; i < len; i++)
        {
            try
            {
                if (i < keylen)
                {
                    kbytes[i] = ' ';
                    kbytes[i] = tmpbytes[i];
                }
                else
                {
                    kbytes[i] = tmpbytes[i - keylen];
                }
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
//                System.err.println("ex le");
//                ex.printStackTrace();
                dMsg.info(ex.toString(), ex);
            }
        }

        return kbytes;
    }

    private static final String key = "1141034227079";
    private static final byte magic1 = 106;
    private static final byte magic2 = 108;
    private static final DebugPrn dMsg = new DebugPrn(CipherAlgorithm.class.getName());
}
