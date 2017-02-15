/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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
package org.openo.nfvo.jujuvnfmadapter.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils.ExeRes;

import net.sf.json.JSONObject;

public class FileUtilsTest {

    FileUtils fileUtils;



    @Before
    public void setUp(){
        //fileUtils = new FileUtils();
        fileUtils.getAppAbsoluteUrl();
    }

    @Test
    public void testWriteFile() throws Exception {
        String filePath = "D:\\test.txt";
        byte[] bytes =  new byte[] {40,50};
        int b = fileUtils.writeFile(bytes, filePath);
        assertNotNull(b);

    }

    @Test
    public void testReadFile() throws Exception {
        String file = "D:\\test.txt";
        File f = new File(file);
        System.out.println(f.isAbsolute());
        String charsetName = "";
        byte[] b = fileUtils.readFile(f, charsetName);
        assertNotNull(b);
    }


    @Test
    public void testListFiles() throws Exception {
        String file = ".";
        File f = new File(file);
        List<File> files = fileUtils.listFiles(f);
        assertNotNull(files);

    }

    @Test
    public void testMkDirs() throws Exception {
        String path ="D:\\temp\\Test";
        fileUtils.mkDirs(path);
    }

    @Test
    public void testDelFiles() throws Exception {
        String path ="D:\\Test\\test.txt";
        assertTrue(fileUtils.delFiles(path));
    }

    @Test
    public void testgetFiles() throws Exception {
        String path ="D:\\temp";
        List<File> files = fileUtils.getFiles(path);
        assertNotNull(files);
    }

    @Test
    public void testCopy() throws Exception {
        String oldfile ="C:\\temp\\test.txt";
        String newfile ="D:\\temp\\test.txt";
        fileUtils.copy(oldfile, newfile, true);

    }








}
