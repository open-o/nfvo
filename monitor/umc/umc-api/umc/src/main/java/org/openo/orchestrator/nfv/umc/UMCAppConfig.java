package org.openo.orchestrator.nfv.umc;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;
import org.openo.orchestrator.nfv.umc.cometdserver.CometdServletInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.FrameCommInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.LoginInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.VersionInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Data
@NoArgsConstructor
public class UMCAppConfig extends Configuration {
    @NotEmpty
    private String language;

    @NotEmpty
    private String defaultName = "UMC";


    private String rocServerAddr;

    private String dacIp;

    @NotEmpty
    private String dacServerPort;

    @Valid
    private LoginInfo loginInfo;

    @Valid
    private FrameCommInfo frameCommInfo;

    @Valid
    private VersionInfo versionInfo;

    @Valid
    private CometdServletInfo cometdServletInfo;

    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

}
