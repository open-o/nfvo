/**
 * Copyright 2016 [ZTE] and others.
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

package com.zte.ums.sfc.console.resources;

import com.codahale.metrics.annotation.Timed;
import com.zte.ums.sfc.console.entity.*;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4N;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;
import com.zte.ums.sfc.console.service.SdnServiceConsumer;
import com.zte.ums.sfc.console.utils.SfcDriverUtil;
import com.zte.ums.sfc.console.wrapper.N2sReqWrapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//
//@Path("api/sdncdriver/v1/")
//@Produces(MediaType.APPLICATION_JSON)
//public class SfcDriverResource extends DriverResource{
//}
