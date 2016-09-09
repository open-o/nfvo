/*
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
package org.openo.nfvo.monitor.dac;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.openo.nfvo.monitor.dac.common.util.DacUtil;
import org.openo.nfvo.monitor.dac.common.util.filescan.FastFileSystem;
import org.openo.nfvo.monitor.dac.datarp.DataMsgQueue;
import org.openo.nfvo.monitor.dac.msb.MSBRestServiceProxy;
import org.openo.nfvo.monitor.dac.msb.MsbConfiguration;
import org.openo.nfvo.monitor.dac.msb.bean.MsbRegisterBean;
import org.openo.nfvo.monitor.dac.msb.bean.ServiceNodeBean;
import org.openo.nfvo.monitor.dac.snmptrap.TrapInitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jetty.HttpConnectorFactory;
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
        MsbConfiguration.setMsbAddress(dacAppConfig.getMsbAddress());
        environment.jersey().packages("org.openo.nfvo.monitor.dac.resources");// register rest-api interface

        initCometd(environment);
        initSwaggerConfig(environment, dacAppConfig);
        FastFileSystem.init();
        initDac();
        registerDacToMSB(dacAppConfig);
        LOGGER.info("Initialize DAC finished.");
    }

    private void registerDacToMSB(DacAppConfig dacAppConfig) {
		// TODO Auto-generated method stub
    	SimpleServerFactory simpleServerFactory = (SimpleServerFactory)dacAppConfig.getServerFactory();
    	HttpConnectorFactory connector = (HttpConnectorFactory)simpleServerFactory.getConnector();
		MsbRegisterBean registerBean = new MsbRegisterBean();
		ServiceNodeBean serviceNode = new ServiceNodeBean();
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Unable to get host ip: " + e.getMessage());
		}
		if(ip.equals("")){
			ip = connector.getBindHost();
		}
		serviceNode.setIp(ip);
		serviceNode.setPort(String.valueOf(connector.getPort()));
		serviceNode.setTtl(0);
		List<ServiceNodeBean> nodeList =  new ArrayList<ServiceNodeBean>();
		nodeList.add(serviceNode);
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		LOGGER.info("register monitor-dac service to msb finished.");
	}

	/**
     * initialize cometD server
     *
     * @param environment environment information
     */
    private void initCometd(Environment environment) {
        environment.getApplicationContext().addFilter(CrossOriginFilter.class,
                "/openoapi/dacnotification/v1/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));// add filter
        environment.getApplicationContext()
                .addServlet("org.cometd.server.CometDServlet", "/openoapi/dacnotification/v1/*")
                .setInitOrder(1);// add servlet
        environment.getApplicationContext()
                .addServlet("org.openo.nfvo.monitor.dac.cometd.CometdServlet",
                        "/openoapi/dacnotification/v1")
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
        config.setResourcePackage("org.openo.nfvo.monitor.dac.resources");

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
