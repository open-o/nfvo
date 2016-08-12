/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.pm.osf.db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao;
import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.H2DbServer;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.HibernateSession;

/**
 * @author wangjiangping
 * @Date 2016/2/3 8:40:44
 */
public class ItPmTaskDaoTest {

    private PmTaskDao itPmTaskDao = new PmTaskDao(HibernateSession.init());

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        H2DbServer.startUp();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        try {
            HibernateSession.destory();
            H2DbServer.shutDown();
        } catch (Exception e) {
            Assert.fail("Exception" + e.getMessage());
        }
    }

    @Before
    public void setUp(){
        PmTask pmtask = new PmTask();
        pmtask.setTaskId(10010);
        pmtask.setJobId(9999);
        pmtask.setOid("1050");
        pmtask.setProxy("127.0.0.1");
        pmtask.setNetTypeId("abcd");
        pmtask.setPOid("100");
        pmtask.setTaskStatus("0");
        pmtask.setGranularity("5");

        itPmTaskDao.save(pmtask);
    }

    @After
    public void tearDown(){
        PmTask pmtask = new PmTask();
        pmtask.setTaskId(10010);
        pmtask.setJobId(9999);
        itPmTaskDao.delete(pmtask);
    }

    /**
     * Test method for {@link org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao#findAll()}.
     */
    @Test
    public void testFindAll() {
        List<PmTask> res = itPmTaskDao.findAll();
        Assert.assertTrue(res.size() > 0);
    }

    /**
     * Test method for
     * {@link org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao#findByTaskId(java.lang.String)}
     * .
     */
    @Test
    public void testFindByTaskId() {
        List<PmTask> res = itPmTaskDao.findByTaskId("10010");
        assertTrue(res.size() > 0 && res.get(0).getTaskId() == 10010 && res.get(0).getJobId() == 9999);
    }

    /**
     * Test method for
     * {@link org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao#deleteByOid(java.lang.String)}
     * .
     */
    @Test
    public void testDeleteByOid() {
        itPmTaskDao.deleteByOid("1050");
        List<PmTask> res = itPmTaskDao.findByTaskId("10010");
        assertEquals(res.size(), 0);
    }

    /**
     * Test method for
     * {@link org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao#getMaxPmJobId()}.
     */
    @Test
    public void testGetMaxPmJobId() {
        int maxPmJobId = itPmTaskDao.getMaxPmJobId();
        assertEquals(maxPmJobId, 9999);
    }

}
