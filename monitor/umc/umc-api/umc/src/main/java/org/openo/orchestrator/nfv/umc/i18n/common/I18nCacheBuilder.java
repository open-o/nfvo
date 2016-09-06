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
package org.openo.orchestrator.nfv.umc.i18n.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.util.filescaner.FastFileSystem;
import org.yaml.snakeyaml.Yaml;

/**
 * @author wangjiangping
 *
 */
public class I18nCacheBuilder {
    private static DebugPrn dMsg = new DebugPrn(I18nCacheBuilder.class.getName());

    public static final String EN_US = "en-us";
    public static final String ZH_CN = "zh-cn";

    public final static int I18N_EN_US_INDEX = 0;
    public final static int I18N_ZH_CN_INDEX = 1;

    Map<String, String> enUsDict = new HashMap<String, String>();
    Map<String, String> zhCnDict = new HashMap<String, String>();

    private final static String I18N_FILE_PATTERN = "*-i18n.yml";

    public List<Map<String, String>> buildI18nCache() {
        List<Map<String, String>> cacheList =
                new ArrayList<Map<String, String>>();

        BuildCache();

        cacheList.add(I18N_EN_US_INDEX, enUsDict);
        cacheList.add(I18N_ZH_CN_INDEX, zhCnDict);
        return cacheList;
    }


    private void BuildCache() {
        File[] files = FastFileSystem.getFiles(I18N_FILE_PATTERN);
        dMsg.info("load i18n yml files, file count:" + files.length);
        for (File file : files) {
            dMsg.info("load i18n yml file:" + file.getName());

            try {
                if (file.getName().contains(EN_US)) {
                    loadToENUS(file);
                    continue;
                }
                if (file.getName().contains(ZH_CN)) {
                    loadToZHCN(file);
                    continue;
                }
            } catch (UnsupportedEncodingException ex) {
                dMsg.warn("load file " + file.getName() + "failed! ", ex);
            } catch (FileNotFoundException ex) {
                dMsg.warn("File " + file.getName() + " not found! ", ex);
            }
        }
    }

    /**
     * @author wangjiangping
     * @date 2016/4/20 10:07:33
     * @description load to english dict cache.
     */
    private void loadToENUS(File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        TranslateDict dict = yaml.loadAs(new FileInputStream(file), TranslateDict.class);
        DictContent[] contents = dict.getContent();
        for (DictContent content : contents) {
            enUsDict.put(content.getKey(), content.getValue());
        }
    }

    /**
     * @author wangjiangping
     * @date 2016/4/20 10:07:55
     * @description load to chinese dict cache.
     */
    private void loadToZHCN(File file) throws UnsupportedEncodingException, FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GB2312");
        TranslateDict dict = yaml.loadAs(isr, TranslateDict.class);
        DictContent[] contents = dict.getContent();
        for (DictContent content : contents) {
            zhCnDict.put(content.getKey(), content.getValue());
        }
    }

    public static void main(String[] args) {
        FastFileSystem.setInitDir(
                "D:\\GitRoot\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf");
        new I18nCacheBuilder().buildI18nCache();
    }

}
