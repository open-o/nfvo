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

package org.openo.nfvo.resmanagement.service.entity;

import java.util.ArrayList;
import java.util.List;

public class VnfExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public VnfExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if(oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if(condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if(value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if(value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria)this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria)this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdIsNull() {
            addCriterion("vnf_instance_id is null");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdIsNotNull() {
            addCriterion("vnf_instance_id is not null");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdEqualTo(String value) {
            addCriterion("vnf_instance_id =", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdNotEqualTo(String value) {
            addCriterion("vnf_instance_id <>", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdGreaterThan(String value) {
            addCriterion("vnf_instance_id >", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_instance_id >=", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdLessThan(String value) {
            addCriterion("vnf_instance_id <", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("vnf_instance_id <=", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdLike(String value) {
            addCriterion("vnf_instance_id like", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdNotLike(String value) {
            addCriterion("vnf_instance_id not like", value, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdIn(List<String> values) {
            addCriterion("vnf_instance_id in", values, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdNotIn(List<String> values) {
            addCriterion("vnf_instance_id not in", values, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdBetween(String value1, String value2) {
            addCriterion("vnf_instance_id between", value1, value2, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceIdNotBetween(String value1, String value2) {
            addCriterion("vnf_instance_id not between", value1, value2, "vnfInstanceId");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameIsNull() {
            addCriterion("vnf_instance_name is null");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameIsNotNull() {
            addCriterion("vnf_instance_name is not null");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameEqualTo(String value) {
            addCriterion("vnf_instance_name =", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameNotEqualTo(String value) {
            addCriterion("vnf_instance_name <>", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameGreaterThan(String value) {
            addCriterion("vnf_instance_name >", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_instance_name >=", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameLessThan(String value) {
            addCriterion("vnf_instance_name <", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameLessThanOrEqualTo(String value) {
            addCriterion("vnf_instance_name <=", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameLike(String value) {
            addCriterion("vnf_instance_name like", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameNotLike(String value) {
            addCriterion("vnf_instance_name not like", value, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameIn(List<String> values) {
            addCriterion("vnf_instance_name in", values, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameNotIn(List<String> values) {
            addCriterion("vnf_instance_name not in", values, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameBetween(String value1, String value2) {
            addCriterion("vnf_instance_name between", value1, value2, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andVnfInstanceNameNotBetween(String value1, String value2) {
            addCriterion("vnf_instance_name not between", value1, value2, "vnfInstanceName");
            return (Criteria)this;
        }

        public Criteria andNsIdIsNull() {
            addCriterion("ns_id is null");
            return (Criteria)this;
        }

        public Criteria andNsIdIsNotNull() {
            addCriterion("ns_id is not null");
            return (Criteria)this;
        }

        public Criteria andNsIdEqualTo(String value) {
            addCriterion("ns_id =", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdNotEqualTo(String value) {
            addCriterion("ns_id <>", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdGreaterThan(String value) {
            addCriterion("ns_id >", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdGreaterThanOrEqualTo(String value) {
            addCriterion("ns_id >=", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdLessThan(String value) {
            addCriterion("ns_id <", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdLessThanOrEqualTo(String value) {
            addCriterion("ns_id <=", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdLike(String value) {
            addCriterion("ns_id like", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdNotLike(String value) {
            addCriterion("ns_id not like", value, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdIn(List<String> values) {
            addCriterion("ns_id in", values, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdNotIn(List<String> values) {
            addCriterion("ns_id not in", values, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdBetween(String value1, String value2) {
            addCriterion("ns_id between", value1, value2, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsIdNotBetween(String value1, String value2) {
            addCriterion("ns_id not between", value1, value2, "nsId");
            return (Criteria)this;
        }

        public Criteria andNsNameIsNull() {
            addCriterion("ns_name is null");
            return (Criteria)this;
        }

        public Criteria andNsNameIsNotNull() {
            addCriterion("ns_name is not null");
            return (Criteria)this;
        }

        public Criteria andNsNameEqualTo(String value) {
            addCriterion("ns_name =", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameNotEqualTo(String value) {
            addCriterion("ns_name <>", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameGreaterThan(String value) {
            addCriterion("ns_name >", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameGreaterThanOrEqualTo(String value) {
            addCriterion("ns_name >=", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameLessThan(String value) {
            addCriterion("ns_name <", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameLessThanOrEqualTo(String value) {
            addCriterion("ns_name <=", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameLike(String value) {
            addCriterion("ns_name like", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameNotLike(String value) {
            addCriterion("ns_name not like", value, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameIn(List<String> values) {
            addCriterion("ns_name in", values, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameNotIn(List<String> values) {
            addCriterion("ns_name not in", values, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameBetween(String value1, String value2) {
            addCriterion("ns_name between", value1, value2, "nsName");
            return (Criteria)this;
        }

        public Criteria andNsNameNotBetween(String value1, String value2) {
            addCriterion("ns_name not between", value1, value2, "nsName");
            return (Criteria)this;
        }

        public Criteria andVnfmIdIsNull() {
            addCriterion("vnfm_id is null");
            return (Criteria)this;
        }

        public Criteria andVnfmIdIsNotNull() {
            addCriterion("vnfm_id is not null");
            return (Criteria)this;
        }

        public Criteria andVnfmIdEqualTo(String value) {
            addCriterion("vnfm_id =", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdNotEqualTo(String value) {
            addCriterion("vnfm_id <>", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdGreaterThan(String value) {
            addCriterion("vnfm_id >", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdGreaterThanOrEqualTo(String value) {
            addCriterion("vnfm_id >=", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdLessThan(String value) {
            addCriterion("vnfm_id <", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdLessThanOrEqualTo(String value) {
            addCriterion("vnfm_id <=", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdLike(String value) {
            addCriterion("vnfm_id like", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdNotLike(String value) {
            addCriterion("vnfm_id not like", value, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdIn(List<String> values) {
            addCriterion("vnfm_id in", values, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdNotIn(List<String> values) {
            addCriterion("vnfm_id not in", values, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdBetween(String value1, String value2) {
            addCriterion("vnfm_id between", value1, value2, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmIdNotBetween(String value1, String value2) {
            addCriterion("vnfm_id not between", value1, value2, "vnfmId");
            return (Criteria)this;
        }

        public Criteria andVnfmNameIsNull() {
            addCriterion("vnfm_name is null");
            return (Criteria)this;
        }

        public Criteria andVnfmNameIsNotNull() {
            addCriterion("vnfm_name is not null");
            return (Criteria)this;
        }

        public Criteria andVnfmNameEqualTo(String value) {
            addCriterion("vnfm_name =", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameNotEqualTo(String value) {
            addCriterion("vnfm_name <>", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameGreaterThan(String value) {
            addCriterion("vnfm_name >", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameGreaterThanOrEqualTo(String value) {
            addCriterion("vnfm_name >=", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameLessThan(String value) {
            addCriterion("vnfm_name <", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameLessThanOrEqualTo(String value) {
            addCriterion("vnfm_name <=", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameLike(String value) {
            addCriterion("vnfm_name like", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameNotLike(String value) {
            addCriterion("vnfm_name not like", value, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameIn(List<String> values) {
            addCriterion("vnfm_name in", values, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameNotIn(List<String> values) {
            addCriterion("vnfm_name not in", values, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameBetween(String value1, String value2) {
            addCriterion("vnfm_name between", value1, value2, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfmNameNotBetween(String value1, String value2) {
            addCriterion("vnfm_name not between", value1, value2, "vnfmName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameIsNull() {
            addCriterion("vnf_package_name is null");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameIsNotNull() {
            addCriterion("vnf_package_name is not null");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameEqualTo(String value) {
            addCriterion("vnf_package_name =", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameNotEqualTo(String value) {
            addCriterion("vnf_package_name <>", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameGreaterThan(String value) {
            addCriterion("vnf_package_name >", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_package_name >=", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameLessThan(String value) {
            addCriterion("vnf_package_name <", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameLessThanOrEqualTo(String value) {
            addCriterion("vnf_package_name <=", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameLike(String value) {
            addCriterion("vnf_package_name like", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameNotLike(String value) {
            addCriterion("vnf_package_name not like", value, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameIn(List<String> values) {
            addCriterion("vnf_package_name in", values, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameNotIn(List<String> values) {
            addCriterion("vnf_package_name not in", values, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameBetween(String value1, String value2) {
            addCriterion("vnf_package_name between", value1, value2, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfPackageNameNotBetween(String value1, String value2) {
            addCriterion("vnf_package_name not between", value1, value2, "vnfPackageName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameIsNull() {
            addCriterion("vnf_descriptor_name is null");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameIsNotNull() {
            addCriterion("vnf_descriptor_name is not null");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameEqualTo(String value) {
            addCriterion("vnf_descriptor_name =", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameNotEqualTo(String value) {
            addCriterion("vnf_descriptor_name <>", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameGreaterThan(String value) {
            addCriterion("vnf_descriptor_name >", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_descriptor_name >=", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameLessThan(String value) {
            addCriterion("vnf_descriptor_name <", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameLessThanOrEqualTo(String value) {
            addCriterion("vnf_descriptor_name <=", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameLike(String value) {
            addCriterion("vnf_descriptor_name like", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameNotLike(String value) {
            addCriterion("vnf_descriptor_name not like", value, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameIn(List<String> values) {
            addCriterion("vnf_descriptor_name in", values, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameNotIn(List<String> values) {
            addCriterion("vnf_descriptor_name not in", values, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameBetween(String value1, String value2) {
            addCriterion("vnf_descriptor_name between", value1, value2, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVnfDescriptorNameNotBetween(String value1, String value2) {
            addCriterion("vnf_descriptor_name not between", value1, value2, "vnfDescriptorName");
            return (Criteria)this;
        }

        public Criteria andVimIdIsNull() {
            addCriterion("vim_id is null");
            return (Criteria)this;
        }

        public Criteria andVimIdIsNotNull() {
            addCriterion("vim_id is not null");
            return (Criteria)this;
        }

        public Criteria andVimIdEqualTo(String value) {
            addCriterion("vim_id =", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdNotEqualTo(String value) {
            addCriterion("vim_id <>", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdGreaterThan(String value) {
            addCriterion("vim_id >", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdGreaterThanOrEqualTo(String value) {
            addCriterion("vim_id >=", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdLessThan(String value) {
            addCriterion("vim_id <", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdLessThanOrEqualTo(String value) {
            addCriterion("vim_id <=", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdLike(String value) {
            addCriterion("vim_id like", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdNotLike(String value) {
            addCriterion("vim_id not like", value, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdIn(List<String> values) {
            addCriterion("vim_id in", values, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdNotIn(List<String> values) {
            addCriterion("vim_id not in", values, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdBetween(String value1, String value2) {
            addCriterion("vim_id between", value1, value2, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimIdNotBetween(String value1, String value2) {
            addCriterion("vim_id not between", value1, value2, "vimId");
            return (Criteria)this;
        }

        public Criteria andVimNameIsNull() {
            addCriterion("vim_name is null");
            return (Criteria)this;
        }

        public Criteria andVimNameIsNotNull() {
            addCriterion("vim_name is not null");
            return (Criteria)this;
        }

        public Criteria andVimNameEqualTo(String value) {
            addCriterion("vim_name =", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameNotEqualTo(String value) {
            addCriterion("vim_name <>", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameGreaterThan(String value) {
            addCriterion("vim_name >", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameGreaterThanOrEqualTo(String value) {
            addCriterion("vim_name >=", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameLessThan(String value) {
            addCriterion("vim_name <", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameLessThanOrEqualTo(String value) {
            addCriterion("vim_name <=", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameLike(String value) {
            addCriterion("vim_name like", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameNotLike(String value) {
            addCriterion("vim_name not like", value, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameIn(List<String> values) {
            addCriterion("vim_name in", values, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameNotIn(List<String> values) {
            addCriterion("vim_name not in", values, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameBetween(String value1, String value2) {
            addCriterion("vim_name between", value1, value2, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimNameNotBetween(String value1, String value2) {
            addCriterion("vim_name not between", value1, value2, "vimName");
            return (Criteria)this;
        }

        public Criteria andVimTenantIsNull() {
            addCriterion("vim_tenant is null");
            return (Criteria)this;
        }

        public Criteria andVimTenantIsNotNull() {
            addCriterion("vim_tenant is not null");
            return (Criteria)this;
        }

        public Criteria andVimTenantEqualTo(String value) {
            addCriterion("vim_tenant =", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantNotEqualTo(String value) {
            addCriterion("vim_tenant <>", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantGreaterThan(String value) {
            addCriterion("vim_tenant >", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantGreaterThanOrEqualTo(String value) {
            addCriterion("vim_tenant >=", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantLessThan(String value) {
            addCriterion("vim_tenant <", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantLessThanOrEqualTo(String value) {
            addCriterion("vim_tenant <=", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantLike(String value) {
            addCriterion("vim_tenant like", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantNotLike(String value) {
            addCriterion("vim_tenant not like", value, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantIn(List<String> values) {
            addCriterion("vim_tenant in", values, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantNotIn(List<String> values) {
            addCriterion("vim_tenant not in", values, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantBetween(String value1, String value2) {
            addCriterion("vim_tenant between", value1, value2, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andVimTenantNotBetween(String value1, String value2) {
            addCriterion("vim_tenant not between", value1, value2, "vimTenant");
            return (Criteria)this;
        }

        public Criteria andJobIdIsNull() {
            addCriterion("job_id is null");
            return (Criteria)this;
        }

        public Criteria andJobIdIsNotNull() {
            addCriterion("job_id is not null");
            return (Criteria)this;
        }

        public Criteria andJobIdEqualTo(String value) {
            addCriterion("job_id =", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdNotEqualTo(String value) {
            addCriterion("job_id <>", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdGreaterThan(String value) {
            addCriterion("job_id >", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdGreaterThanOrEqualTo(String value) {
            addCriterion("job_id >=", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdLessThan(String value) {
            addCriterion("job_id <", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdLessThanOrEqualTo(String value) {
            addCriterion("job_id <=", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdLike(String value) {
            addCriterion("job_id like", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdNotLike(String value) {
            addCriterion("job_id not like", value, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdIn(List<String> values) {
            addCriterion("job_id in", values, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdNotIn(List<String> values) {
            addCriterion("job_id not in", values, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdBetween(String value1, String value2) {
            addCriterion("job_id between", value1, value2, "jobId");
            return (Criteria)this;
        }

        public Criteria andJobIdNotBetween(String value1, String value2) {
            addCriterion("job_id not between", value1, value2, "jobId");
            return (Criteria)this;
        }

        public Criteria andVnfStatusIsNull() {
            addCriterion("vnf_status is null");
            return (Criteria)this;
        }

        public Criteria andVnfStatusIsNotNull() {
            addCriterion("vnf_status is not null");
            return (Criteria)this;
        }

        public Criteria andVnfStatusEqualTo(String value) {
            addCriterion("vnf_status =", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusNotEqualTo(String value) {
            addCriterion("vnf_status <>", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusGreaterThan(String value) {
            addCriterion("vnf_status >", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_status >=", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusLessThan(String value) {
            addCriterion("vnf_status <", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusLessThanOrEqualTo(String value) {
            addCriterion("vnf_status <=", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusLike(String value) {
            addCriterion("vnf_status like", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusNotLike(String value) {
            addCriterion("vnf_status not like", value, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusIn(List<String> values) {
            addCriterion("vnf_status in", values, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusNotIn(List<String> values) {
            addCriterion("vnf_status not in", values, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusBetween(String value1, String value2) {
            addCriterion("vnf_status between", value1, value2, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfStatusNotBetween(String value1, String value2) {
            addCriterion("vnf_status not between", value1, value2, "vnfStatus");
            return (Criteria)this;
        }

        public Criteria andVnfTypeIsNull() {
            addCriterion("vnf_type is null");
            return (Criteria)this;
        }

        public Criteria andVnfTypeIsNotNull() {
            addCriterion("vnf_type is not null");
            return (Criteria)this;
        }

        public Criteria andVnfTypeEqualTo(String value) {
            addCriterion("vnf_type =", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeNotEqualTo(String value) {
            addCriterion("vnf_type <>", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeGreaterThan(String value) {
            addCriterion("vnf_type >", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeGreaterThanOrEqualTo(String value) {
            addCriterion("vnf_type >=", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeLessThan(String value) {
            addCriterion("vnf_type <", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeLessThanOrEqualTo(String value) {
            addCriterion("vnf_type <=", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeLike(String value) {
            addCriterion("vnf_type like", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeNotLike(String value) {
            addCriterion("vnf_type not like", value, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeIn(List<String> values) {
            addCriterion("vnf_type in", values, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeNotIn(List<String> values) {
            addCriterion("vnf_type not in", values, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeBetween(String value1, String value2) {
            addCriterion("vnf_type between", value1, value2, "vnfType");
            return (Criteria)this;
        }

        public Criteria andVnfTypeNotBetween(String value1, String value2) {
            addCriterion("vnf_type not between", value1, value2, "vnfType");
            return (Criteria)this;
        }

        public Criteria andMaxVmIsNull() {
            addCriterion("max_vm is null");
            return (Criteria)this;
        }

        public Criteria andMaxVmIsNotNull() {
            addCriterion("max_vm is not null");
            return (Criteria)this;
        }

        public Criteria andMaxVmEqualTo(Integer value) {
            addCriterion("max_vm =", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmNotEqualTo(Integer value) {
            addCriterion("max_vm <>", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmGreaterThan(Integer value) {
            addCriterion("max_vm >", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_vm >=", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmLessThan(Integer value) {
            addCriterion("max_vm <", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmLessThanOrEqualTo(Integer value) {
            addCriterion("max_vm <=", value, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmIn(List<Integer> values) {
            addCriterion("max_vm in", values, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmNotIn(List<Integer> values) {
            addCriterion("max_vm not in", values, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmBetween(Integer value1, Integer value2) {
            addCriterion("max_vm between", value1, value2, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxVmNotBetween(Integer value1, Integer value2) {
            addCriterion("max_vm not between", value1, value2, "maxVm");
            return (Criteria)this;
        }

        public Criteria andMaxCpuIsNull() {
            addCriterion("max_cpu is null");
            return (Criteria)this;
        }

        public Criteria andMaxCpuIsNotNull() {
            addCriterion("max_cpu is not null");
            return (Criteria)this;
        }

        public Criteria andMaxCpuEqualTo(Integer value) {
            addCriterion("max_cpu =", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuNotEqualTo(Integer value) {
            addCriterion("max_cpu <>", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuGreaterThan(Integer value) {
            addCriterion("max_cpu >", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_cpu >=", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuLessThan(Integer value) {
            addCriterion("max_cpu <", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuLessThanOrEqualTo(Integer value) {
            addCriterion("max_cpu <=", value, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuIn(List<Integer> values) {
            addCriterion("max_cpu in", values, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuNotIn(List<Integer> values) {
            addCriterion("max_cpu not in", values, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuBetween(Integer value1, Integer value2) {
            addCriterion("max_cpu between", value1, value2, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxCpuNotBetween(Integer value1, Integer value2) {
            addCriterion("max_cpu not between", value1, value2, "maxCpu");
            return (Criteria)this;
        }

        public Criteria andMaxDiskIsNull() {
            addCriterion("max_disk is null");
            return (Criteria)this;
        }

        public Criteria andMaxDiskIsNotNull() {
            addCriterion("max_disk is not null");
            return (Criteria)this;
        }

        public Criteria andMaxDiskEqualTo(Integer value) {
            addCriterion("max_disk =", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskNotEqualTo(Integer value) {
            addCriterion("max_disk <>", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskGreaterThan(Integer value) {
            addCriterion("max_disk >", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_disk >=", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskLessThan(Integer value) {
            addCriterion("max_disk <", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskLessThanOrEqualTo(Integer value) {
            addCriterion("max_disk <=", value, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskIn(List<Integer> values) {
            addCriterion("max_disk in", values, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskNotIn(List<Integer> values) {
            addCriterion("max_disk not in", values, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskBetween(Integer value1, Integer value2) {
            addCriterion("max_disk between", value1, value2, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxDiskNotBetween(Integer value1, Integer value2) {
            addCriterion("max_disk not between", value1, value2, "maxDisk");
            return (Criteria)this;
        }

        public Criteria andMaxRamIsNull() {
            addCriterion("max_ram is null");
            return (Criteria)this;
        }

        public Criteria andMaxRamIsNotNull() {
            addCriterion("max_ram is not null");
            return (Criteria)this;
        }

        public Criteria andMaxRamEqualTo(Integer value) {
            addCriterion("max_ram =", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamNotEqualTo(Integer value) {
            addCriterion("max_ram <>", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamGreaterThan(Integer value) {
            addCriterion("max_ram >", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_ram >=", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamLessThan(Integer value) {
            addCriterion("max_ram <", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamLessThanOrEqualTo(Integer value) {
            addCriterion("max_ram <=", value, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamIn(List<Integer> values) {
            addCriterion("max_ram in", values, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamNotIn(List<Integer> values) {
            addCriterion("max_ram not in", values, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamBetween(Integer value1, Integer value2) {
            addCriterion("max_ram between", value1, value2, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxRamNotBetween(Integer value1, Integer value2) {
            addCriterion("max_ram not between", value1, value2, "maxRam");
            return (Criteria)this;
        }

        public Criteria andMaxShdIsNull() {
            addCriterion("max_shd is null");
            return (Criteria)this;
        }

        public Criteria andMaxShdIsNotNull() {
            addCriterion("max_shd is not null");
            return (Criteria)this;
        }

        public Criteria andMaxShdEqualTo(Integer value) {
            addCriterion("max_shd =", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdNotEqualTo(Integer value) {
            addCriterion("max_shd <>", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdGreaterThan(Integer value) {
            addCriterion("max_shd >", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_shd >=", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdLessThan(Integer value) {
            addCriterion("max_shd <", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdLessThanOrEqualTo(Integer value) {
            addCriterion("max_shd <=", value, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdIn(List<Integer> values) {
            addCriterion("max_shd in", values, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdNotIn(List<Integer> values) {
            addCriterion("max_shd not in", values, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdBetween(Integer value1, Integer value2) {
            addCriterion("max_shd between", value1, value2, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxShdNotBetween(Integer value1, Integer value2) {
            addCriterion("max_shd not between", value1, value2, "maxShd");
            return (Criteria)this;
        }

        public Criteria andMaxNetIsNull() {
            addCriterion("max_net is null");
            return (Criteria)this;
        }

        public Criteria andMaxNetIsNotNull() {
            addCriterion("max_net is not null");
            return (Criteria)this;
        }

        public Criteria andMaxNetEqualTo(Integer value) {
            addCriterion("max_net =", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetNotEqualTo(Integer value) {
            addCriterion("max_net <>", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetGreaterThan(Integer value) {
            addCriterion("max_net >", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_net >=", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetLessThan(Integer value) {
            addCriterion("max_net <", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetLessThanOrEqualTo(Integer value) {
            addCriterion("max_net <=", value, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetIn(List<Integer> values) {
            addCriterion("max_net in", values, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetNotIn(List<Integer> values) {
            addCriterion("max_net not in", values, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetBetween(Integer value1, Integer value2) {
            addCriterion("max_net between", value1, value2, "maxNet");
            return (Criteria)this;
        }

        public Criteria andMaxNetNotBetween(Integer value1, Integer value2) {
            addCriterion("max_net not between", value1, value2, "maxNet");
            return (Criteria)this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria)this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria)this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria)this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria)this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if(value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
