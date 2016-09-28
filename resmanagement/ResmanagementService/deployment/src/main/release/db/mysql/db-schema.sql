#*******************************************************************************
# Copyright 2016 Huawei Technologies Co., Ltd.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#*******************************************************************************

/******************drop old database and user***************************/
use mysql;
drop database IF  EXISTS resmanagementdb;
delete from user where User='resmanagement';
FLUSH PRIVILEGES;

/******************create new database and user***************************/
create database resmanagementdb CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON resmanagementdb.* TO 'resmanagement'@'%' IDENTIFIED BY 'resmanagement' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'resmanagement'@'%' IDENTIFIED BY 'resmanagement' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON resmanagementdb.* TO 'resmanagement'@'localhost' IDENTIFIED BY 'resmanagement' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'resmanagement'@'localhost' IDENTIFIED BY 'resmanagement' WITH GRANT OPTION;
FLUSH PRIVILEGES;

use resmanagementdb;
set Names 'utf8';

/******************drop old table and create new***************************/

DROP TABLE IF EXISTS host;
CREATE TABLE host (
    ID                        VARCHAR(128)       NOT NULL,
    NAME                    VARCHAR(256)       NULL,
    CPU                      VARCHAR(256)       NULL,
    MEMORY                  VARCHAR(256)       NULL,
    DISK                      VARCHAR(256)       NULL,
    VIM_ID                     VARCHAR(256)       NULL,
    VIM_NAME                  VARCHAR(256)       NULL,
    CONSTRAINT host PRIMARY KEY(ID)
);

DROP TABLE IF EXISTS location;
CREATE TABLE location (
    ID                        VARCHAR(128)       NOT NULL,
    COUNTRY                 VARCHAR(256)       NULL,
    LOCATION                  VARCHAR(256)       NULL,
    LATITUDE                VARCHAR(256)       NULL,
    LONGITUDE               VARCHAR(256)       NULL,
    DESCRIPTION             VARCHAR(256)       NULL,
    CONSTRAINT location PRIMARY KEY(ID)
);

DROP TABLE IF EXISTS network;
CREATE TABLE network (
    ID                        VARCHAR(128)       NOT NULL,
    NAME                    VARCHAR(256)       NULL,
    TENANT_ID                  VARCHAR(256)       NULL,
    VIM_ID                  VARCHAR(256)       NULL,
    VIM_NAME                   VARCHAR(256)       NULL,
    STATUS                     VARCHAR(256)       NULL,
    PHYSICAL_NETWORK        VARCHAR(256)       NULL,
    NETWORK_TYPE            VARCHAR(256)       NULL,
    SEGMENTATION_ID         VARCHAR(256)       NULL,
    CONSTRAINT network PRIMARY KEY(ID)
);

DROP TABLE IF EXISTS port;
CREATE TABLE port (
    ID                        VARCHAR(128)       NOT NULL,
    NAME                    VARCHAR(256)       NULL,
    NWTWORK_ID              VARCHAR(256)       NULL,
    STATUS                  VARCHAR(256)       NULL,
    TENANT_ID               VARCHAR(256)       NULL,
    VIM_ID                     VARCHAR(256)       NULL,
    VIM_NAME                  VARCHAR(256)       NULL,
    CONSTRAINT port PRIMARY KEY(ID)
);

DROP TABLE IF EXISTS site;
CREATE TABLE site (
    ID                        VARCHAR(128)       NOT NULL,
    NAME                    VARCHAR(256)       NULL,
    LOCATION                  VARCHAR(256)       NULL,
    COUNTRY                 VARCHAR(256)       NULL,
    VIM_ID                   VARCHAR(256)       NULL,
    VIM_NAME                 VARCHAR(256)       NULL,
    STATUS                  VARCHAR(256)       NULL,
    TOTAL_CPU                  VARCHAR(256)       NULL,
    TOTAL_MEMORY            VARCHAR(256)       NULL,
    TOTAL_DISK              VARCHAR(256)       NULL,
    USED_CPU                VARCHAR(256)       NULL,
    USED_MEMORY             VARCHAR(256)       NULL,
    USED_DISK                 VARCHAR(256)       NULL,
    CONSTRAINT site PRIMARY KEY(ID)
);

DROP TABLE IF EXISTS vim;
CREATE TABLE vim (
    ID                        VARCHAR(128)       NOT NULL,
    CONSTRAINT vim PRIMARY KEY(ID)
);

