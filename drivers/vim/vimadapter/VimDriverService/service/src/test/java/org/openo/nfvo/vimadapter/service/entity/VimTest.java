/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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
 
package org.openo.nfvo.vimadapter.service.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class VimTest {

    @Test
    public void testGetId() {
        Vim vim = new Vim();
        String result = vim.getId();
        assertNull("result", result);
    }

    @Test
    public void testSetId() {
        Vim vim = new Vim();
        vim.setId("testId");
        String result = vim.getId();
        assertEquals("result", "testId", result);
    }

    @Test
    public void testGetName() {
        Vim vim = new Vim();
        String result = vim.getName();
        assertNull("result", result);
    }

    @Test
    public void testSetName() {
        Vim vim = new Vim();
        vim.setName("testName");
        String result = vim.getName();
        assertEquals("result", "testName", result);
    }

    @Test
    public void testGetType() {
        Vim vim = new Vim();
        String result = vim.getType();
        assertNull("result", result);
    }

    @Test
    public void testGetUserName() {
        Vim vim = new Vim();
        String result = vim.getUserName();
        assertNull("result", result);
    }

    @Test
    public void testSetUserName() {
        Vim vim = new Vim();
        vim.setUserName("testUserName");
        String result = vim.getUserName();
        assertEquals("result", "testUserName", result);
    }

    @Test
    public void testGetPwd() {
        Vim vim = new Vim();
        String result = vim.getPwd();
        assertNull("result", result);
    }

    @Test
    public void testSetPwd() {
        Vim vim = new Vim();
        vim.setPwd("testpwd");
        String result = vim.getPwd();
        assertEquals("result", "testpwd", result);
    }

    @Test
    public void testGetUrl() {
        Vim vim = new Vim();
        String result = vim.getUrl();
        assertNull("result", result);
    }

    @Test
    public void testEquals() {
        boolean result = new Vim().equals(new Vim());
        assertTrue("result", result);
    }

    @Test
    public void testEquals1() {
        boolean result = new Vim().equals("");
        assertFalse("result", result);
    }

    @Test
    public void testEquals2() {
        Vim obj = new Vim();
        obj.setId("testVimId");
        boolean result = new Vim().equals(obj);
        assertFalse("result", result);
    }

    @Test
    public void testEquals3() {
        Vim vim = new Vim();
        vim.setId("testVimId");
        Vim obj = PowerMockito.mock(Vim.class);
        boolean result = vim.equals(obj);
        assertFalse("result", result);
    }

    @Test
    public void testEquals4() {
        Vim obj = new Vim();
        boolean result = obj.equals(obj);
        assertTrue("result", result);
    }

    @Test
    public void testEquals5() {
        boolean result = new Vim().equals(null);
        assertFalse("result", result);
    }

    @Test
    public void testEquals6() {
        Vim vim = new Vim();
        Vim vim2 = new Vim();
        vim.setId("");
        vim2.setId("");
        boolean result = vim.equals(vim2);
        assertTrue("result", result);
    }

    @Test
    public void testEquals7() {
        Vim vim = new Vim();
        Vim vim2 = new Vim();
        vim.setId("vimId");
        vim2.setId("vim2Id");
        boolean result = vim.equals(vim2);
        assertFalse("result", result);
    }
}
