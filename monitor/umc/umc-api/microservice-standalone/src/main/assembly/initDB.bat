@REM
@REM
@REM Copyright 2016, CMCC Technologies Co., Ltd.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@echo off
set HOME=%~dp0
set user=%1
set password=%2
set port=%3
set host=%4
echo start create umc db
echo HOME=%HOME%
set sql_path=%HOME%\dbscripts\mysql
mysql -u%user% -p%password% -P%port% -h%host% < %sql_path%\openo-umc-createdb.sql
set "dberr=%errorlevel%"
if not "%dberr%"=="0" (  
    goto error
  )
  
:success
echo init umc database success!
pause & exit
:error
echo init umc database faild!
pause & exit


