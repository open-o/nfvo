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
package org.openo.nfvo.jujuvnfmadapter.service.entity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openo.nfvo.jujuvnfmadapter.service.entity.JujuVnfmInfoExample.Criteria;

public class JujuVnfmInfoExampleTest {
    
    JujuVnfmInfoExample jujuexample = new JujuVnfmInfoExample();
    
    @Test
    public void createCriteriaTest(){
        Criteria criteria =  jujuexample.createCriteria();
        assertNotNull(criteria);
    }
    @Test
    public void orTest(){
        Criteria criteria =  jujuexample.or();
        assertNotNull(criteria);
    }
    @Test
    public void clearTest(){
        jujuexample.clear();
        assertTrue(true);
    }
    @Test 
    public void CriteriaTest(){
        JujuVnfmInfoExample.Criteria criteria = new JujuVnfmInfoExample.Criteria();
        boolean isValid = criteria.isValid();
        assertTrue(!isValid);
    }
    @Test 
    public void generatedCriteriaTest(){
        JujuVnfmInfoExample.Criteria criteria = new JujuVnfmInfoExample.Criteria();
        boolean isValid = criteria.isValid();
        assertTrue(!isValid);
    }
    
    @Test 
    public void andIdIsNullTest(){
        JujuVnfmInfoExample.Criteria criteria = new JujuVnfmInfoExample.Criteria();
        Criteria c= criteria.andIdIsNull();
        assertNotNull(c);
    }
    @Test 
    public void getConditionCroterionTest(){
        JujuVnfmInfoExample.Criterion criterion = new JujuVnfmInfoExample.Criterion("test",new Object(),"typeHandler");
       String condition = criterion.getCondition();
        assertEquals(condition,"test");
    }

}
