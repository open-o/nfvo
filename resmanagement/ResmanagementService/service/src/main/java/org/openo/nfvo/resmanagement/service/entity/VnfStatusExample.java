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

public class VnfStatusExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public VnfStatusExample() {
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

        public Criteria andResponseDescriptorIsNull() {
            addCriterion("response_descriptor is null");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorIsNotNull() {
            addCriterion("response_descriptor is not null");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorEqualTo(String value) {
            addCriterion("response_descriptor =", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorNotEqualTo(String value) {
            addCriterion("response_descriptor <>", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorGreaterThan(String value) {
            addCriterion("response_descriptor >", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorGreaterThanOrEqualTo(String value) {
            addCriterion("response_descriptor >=", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorLessThan(String value) {
            addCriterion("response_descriptor <", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorLessThanOrEqualTo(String value) {
            addCriterion("response_descriptor <=", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorLike(String value) {
            addCriterion("response_descriptor like", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorNotLike(String value) {
            addCriterion("response_descriptor not like", value, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorIn(List<String> values) {
            addCriterion("response_descriptor in", values, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorNotIn(List<String> values) {
            addCriterion("response_descriptor not in", values, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorBetween(String value1, String value2) {
            addCriterion("response_descriptor between", value1, value2, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andResponseDescriptorNotBetween(String value1, String value2) {
            addCriterion("response_descriptor not between", value1, value2, "responseDescriptor");
            return (Criteria)this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria)this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria)this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andProgressIsNull() {
            addCriterion("progress is null");
            return (Criteria)this;
        }

        public Criteria andProgressIsNotNull() {
            addCriterion("progress is not null");
            return (Criteria)this;
        }

        public Criteria andProgressEqualTo(String value) {
            addCriterion("progress =", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressNotEqualTo(String value) {
            addCriterion("progress <>", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressGreaterThan(String value) {
            addCriterion("progress >", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressGreaterThanOrEqualTo(String value) {
            addCriterion("progress >=", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressLessThan(String value) {
            addCriterion("progress <", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressLessThanOrEqualTo(String value) {
            addCriterion("progress <=", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressLike(String value) {
            addCriterion("progress like", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressNotLike(String value) {
            addCriterion("progress not like", value, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressIn(List<String> values) {
            addCriterion("progress in", values, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressNotIn(List<String> values) {
            addCriterion("progress not in", values, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressBetween(String value1, String value2) {
            addCriterion("progress between", value1, value2, "progress");
            return (Criteria)this;
        }

        public Criteria andProgressNotBetween(String value1, String value2) {
            addCriterion("progress not between", value1, value2, "progress");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionIsNull() {
            addCriterion("status_description is null");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionIsNotNull() {
            addCriterion("status_description is not null");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionEqualTo(String value) {
            addCriterion("status_description =", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionNotEqualTo(String value) {
            addCriterion("status_description <>", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionGreaterThan(String value) {
            addCriterion("status_description >", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("status_description >=", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionLessThan(String value) {
            addCriterion("status_description <", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionLessThanOrEqualTo(String value) {
            addCriterion("status_description <=", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionLike(String value) {
            addCriterion("status_description like", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionNotLike(String value) {
            addCriterion("status_description not like", value, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionIn(List<String> values) {
            addCriterion("status_description in", values, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionNotIn(List<String> values) {
            addCriterion("status_description not in", values, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionBetween(String value1, String value2) {
            addCriterion("status_description between", value1, value2, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andStatusDescriptionNotBetween(String value1, String value2) {
            addCriterion("status_description not between", value1, value2, "statusDescription");
            return (Criteria)this;
        }

        public Criteria andErrorCodeIsNull() {
            addCriterion("error_code is null");
            return (Criteria)this;
        }

        public Criteria andErrorCodeIsNotNull() {
            addCriterion("error_code is not null");
            return (Criteria)this;
        }

        public Criteria andErrorCodeEqualTo(String value) {
            addCriterion("error_code =", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeNotEqualTo(String value) {
            addCriterion("error_code <>", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeGreaterThan(String value) {
            addCriterion("error_code >", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeGreaterThanOrEqualTo(String value) {
            addCriterion("error_code >=", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeLessThan(String value) {
            addCriterion("error_code <", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeLessThanOrEqualTo(String value) {
            addCriterion("error_code <=", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeLike(String value) {
            addCriterion("error_code like", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeNotLike(String value) {
            addCriterion("error_code not like", value, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeIn(List<String> values) {
            addCriterion("error_code in", values, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeNotIn(List<String> values) {
            addCriterion("error_code not in", values, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeBetween(String value1, String value2) {
            addCriterion("error_code between", value1, value2, "errorCode");
            return (Criteria)this;
        }

        public Criteria andErrorCodeNotBetween(String value1, String value2) {
            addCriterion("error_code not between", value1, value2, "errorCode");
            return (Criteria)this;
        }

        public Criteria andResponseIdIsNull() {
            addCriterion("response_id is null");
            return (Criteria)this;
        }

        public Criteria andResponseIdIsNotNull() {
            addCriterion("response_id is not null");
            return (Criteria)this;
        }

        public Criteria andResponseIdEqualTo(String value) {
            addCriterion("response_id =", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdNotEqualTo(String value) {
            addCriterion("response_id <>", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdGreaterThan(String value) {
            addCriterion("response_id >", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdGreaterThanOrEqualTo(String value) {
            addCriterion("response_id >=", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdLessThan(String value) {
            addCriterion("response_id <", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdLessThanOrEqualTo(String value) {
            addCriterion("response_id <=", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdLike(String value) {
            addCriterion("response_id like", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdNotLike(String value) {
            addCriterion("response_id not like", value, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdIn(List<String> values) {
            addCriterion("response_id in", values, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdNotIn(List<String> values) {
            addCriterion("response_id not in", values, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdBetween(String value1, String value2) {
            addCriterion("response_id between", value1, value2, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseIdNotBetween(String value1, String value2) {
            addCriterion("response_id not between", value1, value2, "responseId");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListIsNull() {
            addCriterion("response_history_list is null");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListIsNotNull() {
            addCriterion("response_history_list is not null");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListEqualTo(String value) {
            addCriterion("response_history_list =", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListNotEqualTo(String value) {
            addCriterion("response_history_list <>", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListGreaterThan(String value) {
            addCriterion("response_history_list >", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListGreaterThanOrEqualTo(String value) {
            addCriterion("response_history_list >=", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListLessThan(String value) {
            addCriterion("response_history_list <", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListLessThanOrEqualTo(String value) {
            addCriterion("response_history_list <=", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListLike(String value) {
            addCriterion("response_history_list like", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListNotLike(String value) {
            addCriterion("response_history_list not like", value, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListIn(List<String> values) {
            addCriterion("response_history_list in", values, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListNotIn(List<String> values) {
            addCriterion("response_history_list not in", values, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListBetween(String value1, String value2) {
            addCriterion("response_history_list between", value1, value2, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andResponseHistoryListNotBetween(String value1, String value2) {
            addCriterion("response_history_list not between", value1, value2, "responseHistoryList");
            return (Criteria)this;
        }

        public Criteria andAddVmIsNull() {
            addCriterion("add_vm is null");
            return (Criteria)this;
        }

        public Criteria andAddVmIsNotNull() {
            addCriterion("add_vm is not null");
            return (Criteria)this;
        }

        public Criteria andAddVmEqualTo(String value) {
            addCriterion("add_vm =", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmNotEqualTo(String value) {
            addCriterion("add_vm <>", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmGreaterThan(String value) {
            addCriterion("add_vm >", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmGreaterThanOrEqualTo(String value) {
            addCriterion("add_vm >=", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmLessThan(String value) {
            addCriterion("add_vm <", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmLessThanOrEqualTo(String value) {
            addCriterion("add_vm <=", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmLike(String value) {
            addCriterion("add_vm like", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmNotLike(String value) {
            addCriterion("add_vm not like", value, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmIn(List<String> values) {
            addCriterion("add_vm in", values, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmNotIn(List<String> values) {
            addCriterion("add_vm not in", values, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmBetween(String value1, String value2) {
            addCriterion("add_vm between", value1, value2, "addVm");
            return (Criteria)this;
        }

        public Criteria andAddVmNotBetween(String value1, String value2) {
            addCriterion("add_vm not between", value1, value2, "addVm");
            return (Criteria)this;
        }

        public Criteria andDelVmIsNull() {
            addCriterion("del_vm is null");
            return (Criteria)this;
        }

        public Criteria andDelVmIsNotNull() {
            addCriterion("del_vm is not null");
            return (Criteria)this;
        }

        public Criteria andDelVmEqualTo(String value) {
            addCriterion("del_vm =", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmNotEqualTo(String value) {
            addCriterion("del_vm <>", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmGreaterThan(String value) {
            addCriterion("del_vm >", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmGreaterThanOrEqualTo(String value) {
            addCriterion("del_vm >=", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmLessThan(String value) {
            addCriterion("del_vm <", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmLessThanOrEqualTo(String value) {
            addCriterion("del_vm <=", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmLike(String value) {
            addCriterion("del_vm like", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmNotLike(String value) {
            addCriterion("del_vm not like", value, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmIn(List<String> values) {
            addCriterion("del_vm in", values, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmNotIn(List<String> values) {
            addCriterion("del_vm not in", values, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmBetween(String value1, String value2) {
            addCriterion("del_vm between", value1, value2, "delVm");
            return (Criteria)this;
        }

        public Criteria andDelVmNotBetween(String value1, String value2) {
            addCriterion("del_vm not between", value1, value2, "delVm");
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
