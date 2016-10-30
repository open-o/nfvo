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

public class VirtualLinkExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public VirtualLinkExample() {
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

        public Criteria andBackendIdIsNull() {
            addCriterion("backend_id is null");
            return (Criteria)this;
        }

        public Criteria andBackendIdIsNotNull() {
            addCriterion("backend_id is not null");
            return (Criteria)this;
        }

        public Criteria andBackendIdEqualTo(String value) {
            addCriterion("backend_id =", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdNotEqualTo(String value) {
            addCriterion("backend_id <>", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdGreaterThan(String value) {
            addCriterion("backend_id >", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdGreaterThanOrEqualTo(String value) {
            addCriterion("backend_id >=", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdLessThan(String value) {
            addCriterion("backend_id <", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdLessThanOrEqualTo(String value) {
            addCriterion("backend_id <=", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdLike(String value) {
            addCriterion("backend_id like", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdNotLike(String value) {
            addCriterion("backend_id not like", value, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdIn(List<String> values) {
            addCriterion("backend_id in", values, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdNotIn(List<String> values) {
            addCriterion("backend_id not in", values, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdBetween(String value1, String value2) {
            addCriterion("backend_id between", value1, value2, "backendId");
            return (Criteria)this;
        }

        public Criteria andBackendIdNotBetween(String value1, String value2) {
            addCriterion("backend_id not between", value1, value2, "backendId");
            return (Criteria)this;
        }

        public Criteria andIsPublicIsNull() {
            addCriterion("is_public is null");
            return (Criteria)this;
        }

        public Criteria andIsPublicIsNotNull() {
            addCriterion("is_public is not null");
            return (Criteria)this;
        }

        public Criteria andIsPublicEqualTo(String value) {
            addCriterion("is_public =", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicNotEqualTo(String value) {
            addCriterion("is_public <>", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicGreaterThan(String value) {
            addCriterion("is_public >", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicGreaterThanOrEqualTo(String value) {
            addCriterion("is_public >=", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicLessThan(String value) {
            addCriterion("is_public <", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicLessThanOrEqualTo(String value) {
            addCriterion("is_public <=", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicLike(String value) {
            addCriterion("is_public like", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicNotLike(String value) {
            addCriterion("is_public not like", value, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicIn(List<String> values) {
            addCriterion("is_public in", values, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicNotIn(List<String> values) {
            addCriterion("is_public not in", values, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicBetween(String value1, String value2) {
            addCriterion("is_public between", value1, value2, "isPublic");
            return (Criteria)this;
        }

        public Criteria andIsPublicNotBetween(String value1, String value2) {
            addCriterion("is_public not between", value1, value2, "isPublic");
            return (Criteria)this;
        }

        public Criteria andDcNameIsNull() {
            addCriterion("dc_name is null");
            return (Criteria)this;
        }

        public Criteria andDcNameIsNotNull() {
            addCriterion("dc_name is not null");
            return (Criteria)this;
        }

        public Criteria andDcNameEqualTo(String value) {
            addCriterion("dc_name =", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameNotEqualTo(String value) {
            addCriterion("dc_name <>", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameGreaterThan(String value) {
            addCriterion("dc_name >", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameGreaterThanOrEqualTo(String value) {
            addCriterion("dc_name >=", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameLessThan(String value) {
            addCriterion("dc_name <", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameLessThanOrEqualTo(String value) {
            addCriterion("dc_name <=", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameLike(String value) {
            addCriterion("dc_name like", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameNotLike(String value) {
            addCriterion("dc_name not like", value, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameIn(List<String> values) {
            addCriterion("dc_name in", values, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameNotIn(List<String> values) {
            addCriterion("dc_name not in", values, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameBetween(String value1, String value2) {
            addCriterion("dc_name between", value1, value2, "dcName");
            return (Criteria)this;
        }

        public Criteria andDcNameNotBetween(String value1, String value2) {
            addCriterion("dc_name not between", value1, value2, "dcName");
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

        public Criteria andPhysicialNetIsNull() {
            addCriterion("physicial_net is null");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetIsNotNull() {
            addCriterion("physicial_net is not null");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetEqualTo(String value) {
            addCriterion("physicial_net =", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetNotEqualTo(String value) {
            addCriterion("physicial_net <>", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetGreaterThan(String value) {
            addCriterion("physicial_net >", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetGreaterThanOrEqualTo(String value) {
            addCriterion("physicial_net >=", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetLessThan(String value) {
            addCriterion("physicial_net <", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetLessThanOrEqualTo(String value) {
            addCriterion("physicial_net <=", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetLike(String value) {
            addCriterion("physicial_net like", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetNotLike(String value) {
            addCriterion("physicial_net not like", value, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetIn(List<String> values) {
            addCriterion("physicial_net in", values, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetNotIn(List<String> values) {
            addCriterion("physicial_net not in", values, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetBetween(String value1, String value2) {
            addCriterion("physicial_net between", value1, value2, "physicialNet");
            return (Criteria)this;
        }

        public Criteria andPhysicialNetNotBetween(String value1, String value2) {
            addCriterion("physicial_net not between", value1, value2, "physicialNet");
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

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria)this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria)this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria)this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeIsNull() {
            addCriterion("network_type is null");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeIsNotNull() {
            addCriterion("network_type is not null");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeEqualTo(String value) {
            addCriterion("network_type =", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeNotEqualTo(String value) {
            addCriterion("network_type <>", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeGreaterThan(String value) {
            addCriterion("network_type >", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeGreaterThanOrEqualTo(String value) {
            addCriterion("network_type >=", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeLessThan(String value) {
            addCriterion("network_type <", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeLessThanOrEqualTo(String value) {
            addCriterion("network_type <=", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeLike(String value) {
            addCriterion("network_type like", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeNotLike(String value) {
            addCriterion("network_type not like", value, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeIn(List<String> values) {
            addCriterion("network_type in", values, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeNotIn(List<String> values) {
            addCriterion("network_type not in", values, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeBetween(String value1, String value2) {
            addCriterion("network_type between", value1, value2, "networkType");
            return (Criteria)this;
        }

        public Criteria andNetworkTypeNotBetween(String value1, String value2) {
            addCriterion("network_type not between", value1, value2, "networkType");
            return (Criteria)this;
        }

        public Criteria andSegmentationIsNull() {
            addCriterion("segmentation is null");
            return (Criteria)this;
        }

        public Criteria andSegmentationIsNotNull() {
            addCriterion("segmentation is not null");
            return (Criteria)this;
        }

        public Criteria andSegmentationEqualTo(String value) {
            addCriterion("segmentation =", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationNotEqualTo(String value) {
            addCriterion("segmentation <>", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationGreaterThan(String value) {
            addCriterion("segmentation >", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationGreaterThanOrEqualTo(String value) {
            addCriterion("segmentation >=", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationLessThan(String value) {
            addCriterion("segmentation <", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationLessThanOrEqualTo(String value) {
            addCriterion("segmentation <=", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationLike(String value) {
            addCriterion("segmentation like", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationNotLike(String value) {
            addCriterion("segmentation not like", value, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationIn(List<String> values) {
            addCriterion("segmentation in", values, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationNotIn(List<String> values) {
            addCriterion("segmentation not in", values, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationBetween(String value1, String value2) {
            addCriterion("segmentation between", value1, value2, "segmentation");
            return (Criteria)this;
        }

        public Criteria andSegmentationNotBetween(String value1, String value2) {
            addCriterion("segmentation not between", value1, value2, "segmentation");
            return (Criteria)this;
        }

        public Criteria andMtuIsNull() {
            addCriterion("mtu is null");
            return (Criteria)this;
        }

        public Criteria andMtuIsNotNull() {
            addCriterion("mtu is not null");
            return (Criteria)this;
        }

        public Criteria andMtuEqualTo(String value) {
            addCriterion("mtu =", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuNotEqualTo(String value) {
            addCriterion("mtu <>", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuGreaterThan(String value) {
            addCriterion("mtu >", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuGreaterThanOrEqualTo(String value) {
            addCriterion("mtu >=", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuLessThan(String value) {
            addCriterion("mtu <", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuLessThanOrEqualTo(String value) {
            addCriterion("mtu <=", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuLike(String value) {
            addCriterion("mtu like", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuNotLike(String value) {
            addCriterion("mtu not like", value, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuIn(List<String> values) {
            addCriterion("mtu in", values, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuNotIn(List<String> values) {
            addCriterion("mtu not in", values, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuBetween(String value1, String value2) {
            addCriterion("mtu between", value1, value2, "mtu");
            return (Criteria)this;
        }

        public Criteria andMtuNotBetween(String value1, String value2) {
            addCriterion("mtu not between", value1, value2, "mtu");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentIsNull() {
            addCriterion("vlan_transparent is null");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentIsNotNull() {
            addCriterion("vlan_transparent is not null");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentEqualTo(String value) {
            addCriterion("vlan_transparent =", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentNotEqualTo(String value) {
            addCriterion("vlan_transparent <>", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentGreaterThan(String value) {
            addCriterion("vlan_transparent >", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentGreaterThanOrEqualTo(String value) {
            addCriterion("vlan_transparent >=", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentLessThan(String value) {
            addCriterion("vlan_transparent <", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentLessThanOrEqualTo(String value) {
            addCriterion("vlan_transparent <=", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentLike(String value) {
            addCriterion("vlan_transparent like", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentNotLike(String value) {
            addCriterion("vlan_transparent not like", value, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentIn(List<String> values) {
            addCriterion("vlan_transparent in", values, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentNotIn(List<String> values) {
            addCriterion("vlan_transparent not in", values, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentBetween(String value1, String value2) {
            addCriterion("vlan_transparent between", value1, value2, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andVlanTransparentNotBetween(String value1, String value2) {
            addCriterion("vlan_transparent not between", value1, value2, "vlanTransparent");
            return (Criteria)this;
        }

        public Criteria andRouterExternalIsNull() {
            addCriterion("router_external is null");
            return (Criteria)this;
        }

        public Criteria andRouterExternalIsNotNull() {
            addCriterion("router_external is not null");
            return (Criteria)this;
        }

        public Criteria andRouterExternalEqualTo(String value) {
            addCriterion("router_external =", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalNotEqualTo(String value) {
            addCriterion("router_external <>", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalGreaterThan(String value) {
            addCriterion("router_external >", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalGreaterThanOrEqualTo(String value) {
            addCriterion("router_external >=", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalLessThan(String value) {
            addCriterion("router_external <", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalLessThanOrEqualTo(String value) {
            addCriterion("router_external <=", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalLike(String value) {
            addCriterion("router_external like", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalNotLike(String value) {
            addCriterion("router_external not like", value, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalIn(List<String> values) {
            addCriterion("router_external in", values, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalNotIn(List<String> values) {
            addCriterion("router_external not in", values, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalBetween(String value1, String value2) {
            addCriterion("router_external between", value1, value2, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andRouterExternalNotBetween(String value1, String value2) {
            addCriterion("router_external not between", value1, value2, "routerExternal");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeIsNull() {
            addCriterion("resource_provider_type is null");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeIsNotNull() {
            addCriterion("resource_provider_type is not null");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeEqualTo(String value) {
            addCriterion("resource_provider_type =", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeNotEqualTo(String value) {
            addCriterion("resource_provider_type <>", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeGreaterThan(String value) {
            addCriterion("resource_provider_type >", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeGreaterThanOrEqualTo(String value) {
            addCriterion("resource_provider_type >=", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeLessThan(String value) {
            addCriterion("resource_provider_type <", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeLessThanOrEqualTo(String value) {
            addCriterion("resource_provider_type <=", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeLike(String value) {
            addCriterion("resource_provider_type like", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeNotLike(String value) {
            addCriterion("resource_provider_type not like", value, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeIn(List<String> values) {
            addCriterion("resource_provider_type in", values, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeNotIn(List<String> values) {
            addCriterion("resource_provider_type not in", values, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeBetween(String value1, String value2) {
            addCriterion("resource_provider_type between", value1, value2, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderTypeNotBetween(String value1, String value2) {
            addCriterion("resource_provider_type not between", value1, value2, "resourceProviderType");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdIsNull() {
            addCriterion("resource_provider_id is null");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdIsNotNull() {
            addCriterion("resource_provider_id is not null");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdEqualTo(String value) {
            addCriterion("resource_provider_id =", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdNotEqualTo(String value) {
            addCriterion("resource_provider_id <>", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdGreaterThan(String value) {
            addCriterion("resource_provider_id >", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdGreaterThanOrEqualTo(String value) {
            addCriterion("resource_provider_id >=", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdLessThan(String value) {
            addCriterion("resource_provider_id <", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdLessThanOrEqualTo(String value) {
            addCriterion("resource_provider_id <=", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdLike(String value) {
            addCriterion("resource_provider_id like", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdNotLike(String value) {
            addCriterion("resource_provider_id not like", value, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdIn(List<String> values) {
            addCriterion("resource_provider_id in", values, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdNotIn(List<String> values) {
            addCriterion("resource_provider_id not in", values, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdBetween(String value1, String value2) {
            addCriterion("resource_provider_id between", value1, value2, "resourceProviderId");
            return (Criteria)this;
        }

        public Criteria andResourceProviderIdNotBetween(String value1, String value2) {
            addCriterion("resource_provider_id not between", value1, value2, "resourceProviderId");
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
