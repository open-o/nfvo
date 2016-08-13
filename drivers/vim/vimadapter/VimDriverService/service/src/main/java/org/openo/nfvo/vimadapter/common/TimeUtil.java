/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vimadapter.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.nfvo.vimadapter.service.constant.Constant;

public final class TimeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TimeUtil.class);

    private TimeUtil() {

    }

    public static String getHisIsoTime(long curtime, long interval) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long hisTime = curtime - interval;
        return formatter.format(hisTime).replace(" ", "T");
    }

    public static String getIsoTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time).replace(" ", "T");
    }

    public static long getTime() {
        return new Date().getTime();
    }

    public static long getIntTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(time.replace("T", " "));
            return date.getTime();
        } catch(ParseException e) {
            LOG.error("fun=getIntTime msg=catch exception {}.", e);
        }
        return Constant.TIME_EXCEPT_VALUE;
    }
}