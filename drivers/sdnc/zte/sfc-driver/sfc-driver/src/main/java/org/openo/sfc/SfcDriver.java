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
package org.openo.sfc;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.openo.sfc.health.ConsoleHealthCheck;
import org.openo.sfc.resources.DriverResource;
import org.openo.sfc.service.ConfigInfo;
import org.openo.sfc.resources.MsbServiceRegister;
import org.openo.sfc.utils.SfcConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SfcDriver extends Application<SfcDriverConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SfcDriver.class);

    public static void main(String[] args) throws Exception {
        new SfcDriver().run(args);
    }

    @Override
    public String getName() {
        return SfcConst.SERVICE_NAME;
    }

    @Override
    public void initialize(Bootstrap<SfcDriverConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/iui", "/", "index.html", "iui"));
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
    }

    @Override
    public void run(SfcDriverConfig configuration,
                    Environment environment) {
        final DriverResource driverResource = new DriverResource();
        final ConsoleHealthCheck healthCheck =
                new ConsoleHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(driverResource);
        ConfigInfo.setConfig(configuration);

        registerService();
        initSwaggerConfig(environment, configuration);
    }


    private void initSwaggerConfig(Environment environment, SfcDriverConfig configuration) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle(" Console Service rest API");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.openo.sfc.resources");
        //swagger rest api basepath
        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();

        basePath = basePath.endsWith("/") ? basePath : (new StringBuilder()).append(basePath).append('/').toString();
        basePath = basePath + "service";
        LOGGER.info("getApplicationContextPath： " + basePath);
        config.setBasePath(basePath);
        config.setScan(true);
    }

    private void registerService()
    {
        Thread msbRegisterThread = new Thread(new MsbServiceRegister());
        msbRegisterThread.setName("Register Service 2 MSB");
        msbRegisterThread.start();
    }

}
