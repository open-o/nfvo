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

public class VnfInfoExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public VnfInfoExample() {
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
