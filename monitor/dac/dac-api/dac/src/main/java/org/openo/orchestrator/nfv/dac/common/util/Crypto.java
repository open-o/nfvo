/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.dac.common.util;


public class Crypto
{
    public static String encrypt(String inpasswd)
    {
        return CipherAlgorithm.encrypt(inpasswd);
    }

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
