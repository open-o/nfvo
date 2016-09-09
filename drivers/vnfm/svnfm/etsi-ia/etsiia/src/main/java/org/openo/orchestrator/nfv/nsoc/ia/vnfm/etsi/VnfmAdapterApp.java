/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common.Config;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.health.ConsoleHealthCheck;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.resources.VnfmAdapterResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

public class VnfmAdapterApp  extends Application<VnfmAdapterConfig> {

	private static final Logger LOGGER = LoggerFactory.getLogger(VnfmAdapterApp.class);
    public static void main(String[] args) throws Exception {
        new VnfmAdapterApp().run(args);
    }

    @Override
    public String getName() {
        return "etsi-vnfm-adapter";
    }

    @Override
    public void initialize(Bootstrap<VnfmAdapterConfig> bootstrap) {
    	bootstrap.addBundle(new AssetsBundle("/api-doc","/api-doc","index.html","api-doc"));
    }

    @Override
    public void run(VnfmAdapterConfig configuration,
                    Environment environment) {
        Config.setConfigration(configuration);
        final VnfmAdapterResource resource = new VnfmAdapterResource();
        final ConsoleHealthCheck healthCheck =
                new ConsoleHealthCheck(configuration.getTemplate());
            environment.healthChecks().register("template", healthCheck);
            environment.jersey().register(resource);
            initSwaggerConfig(environment,configuration);
    }



    private void initSwaggerConfig(Environment environment,VnfmAdapterConfig configuration) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle("CMCC Conductor Console Service rest API");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.resources");
      //设置swagger里面访问rest api时的basepath
        /*SimpleServerFactory simpleServerFactory =
                (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();

        basePath =
                basePath.endsWith("/") ? basePath : (new StringBuilder()).append(basePath)
                        .append('/').toString();
        LOGGER.info("getApplicationContextPath： " + basePath);
        config.setBasePath(basePath);
        config.setScan(true);*/

        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();
        String rootPath = simpleServerFactory.getJerseyRootPath();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
        basePath = basePath.equals("/") ? rootPath : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

}
