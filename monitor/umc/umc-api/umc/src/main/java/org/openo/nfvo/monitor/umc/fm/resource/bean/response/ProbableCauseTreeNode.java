/**
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
package org.openo.nfvo.monitor.umc.fm.resource.bean.response;

import org.openo.nfvo.monitor.umc.i18n.I18n;;

/**
 * definition of probable cause tree node
 *
 */
public class ProbableCauseTreeNode {

    public static final int MOC = 0;

    public static final int SYSTEMTYPE = 1;

    public static final int PROBABLECAUSE = 2;

    private String id = "";

    private String parentId = "";

    private int type = MOC;

    private String value = "";

    private String desc = "";

    private String iconPath = "";

    public boolean isChildOf(ProbableCauseTreeNode node) {
        if (!isRoot() && this.parentId.equals(node.getId())) {
            return true;
        }
        return false;
    }

    private boolean isRoot() {
        return this.id.equals("root");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }


    @Override
    public String toString() {
        return this.parentId + this.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProbableCauseTreeNode other = (ProbableCauseTreeNode) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (parentId == null) {
            if (other.parentId != null) return false;
        } else if (!parentId.equals(other.parentId)) return false;
        return true;
    }

    public void translate(String language) {
        I18n i18n = I18n.getInstance(language);

        if(i18n==null){
            return;
        }

        this.desc = i18n.translate(this.desc);
    }
}
