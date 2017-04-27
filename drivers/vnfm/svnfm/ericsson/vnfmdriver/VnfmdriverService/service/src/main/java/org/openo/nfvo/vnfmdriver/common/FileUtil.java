/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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

package org.openo.nfvo.vnfmdriver.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.FilerException;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version     NFVO 0.5  Feb 15, 2017
 */

public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * <br>
     *
     * @param path
     * @return
     * @throws IOException
     * @since NFVO 0.5
     */
    public static String read(String path) throws IOException {
        LOG.info("File path={}", path);

        BufferedReader reader = null;
        String restString = "";
        try {
            String readingString = null;
            reader = new BufferedReader(new FileReader(new File(path)));

            while((readingString = reader.readLine()) != null) {
                restString = restString + readingString;
            }
            reader.close();
        } catch(IOException e) {
            LOG.warn("IOException!, {}", e.getMessage());
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch(IOException e1) {
                    LOG.warn("IOException!" + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
        return restString;
    }

    /**
     * Constructor<br>
     * <p>
     * </p>
     *
     * @since NFVO 0.5
     */
    private FileUtil() {
        // private constructor
    }
}
