/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.dac;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.openo.orchestrator.nfv.dac.common.util.DacUtil;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.openo.orchestrator.nfv.dac.datarp.DataMsgQueue;
import org.openo.orchestrator.nfv.dac.snmptrap.TrapInitService;
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

/**
 * DAC start class
 */
public class DacApp extends Application<DacAppConfig> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DacApp.class);

    public static void main(String[] args) throws Exception {
        new DacApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<DacAppConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
    }

    @Override
    public void run(DacAppConfig dacAppConfig, Environment environment) throws Exception {
        LOGGER.info("Start to initialize DAC.");
        environment.jersey().packages("org.openo.orchestrator.nfv.dac.resources");// register rest interface

        initCometd(environment);
        initSwaggerConfig(environment, dacAppConfig);
        FastFileSystem.init();
        initDac();
        LOGGER.info("Initialize DAC finished.");
    }

    /**
     * initialize cometD server
     *
     * @param environment environment information
     */
    private void initCometd(Environment environment) {
        environment.getApplicationContext().addFilter(CrossOriginFilter.class,
                "/api/dacnotification/v1/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));// add filter
        environment.getApplicationContext()
                .addServlet("org.cometd.server.CometDServlet", "/api/dacnotification/v1/*")
                .setInitOrder(1);// add servlet
        environment.getApplicationContext()
                .addServlet("org.openo.orchestrator.nfv.dac.cometd.CometdServlet",
                        "/api/dacnotification/v1")
                .setInitOrder(2);// add servlet
    }

    /**
     * initialize swagger configuration
     *
     * @param environment   environment information
     * @param configuration DAC configuration
     */
    private void initSwaggerConfig(Environment environment, DacAppConfig configuration) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle("API Description");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.openo.orchestrator.nfv.dac.resources");

        SimpleServerFactory simpleServerFactory =
                (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();
        String rootPath = simpleServerFactory.getJerseyRootPath();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));

        basePath = basePath.equals("/")
                ? rootPath
                : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

    /**
     * initialize DAC server
     */
    private void initDac() {
        DataMsgQueue dataMsgQueue = new DataMsgQueue();
        DacUtil.setDataMsgQueue(dataMsgQueue);
        dataMsgQueue.start();// init upload data thread
		TrapInitService.startService(); // init trap listen
    }
}
