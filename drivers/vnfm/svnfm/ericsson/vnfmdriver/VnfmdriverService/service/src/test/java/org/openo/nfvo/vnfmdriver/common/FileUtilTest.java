/*
 * Copyright Ericsson AB. 2017
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

package org.openo.nfvo.vnfmdriver.common;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openo.nfvo.vnfmdriver.activator.impl.ServiceControllerImpl;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
public class FileUtilTest {

    /**
     * <br>
     *
     * @throws Exception when read file failed
     * @since NFVO 0.5
     */
    @Test
    public void read() throws Exception {
        String path = Constant.VNFM_DRIVER_INFO;
        String result = FileUtil.read(ServiceControllerImpl.class
                                .getClassLoader()
                                .getResource("")
                                .getPath()
                                .replace("test-classes", "classes")
                                + path);
        assertNotNull(result);
    }

}
