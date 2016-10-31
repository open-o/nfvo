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
package org.openo.nfvo.monitor.umc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.openo.nfvo.monitor.umc.cache.CacheService;
import org.openo.nfvo.monitor.umc.dac.DacResource;
import org.openo.nfvo.monitor.umc.db.UmcDbSession;
import org.openo.nfvo.monitor.umc.drill.resources.LayerMonitorResource;
import org.openo.nfvo.monitor.umc.drill.resources.TopologyManagerResource;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.config.ClientConfigFactory;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.RocResourceConfig;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.fm.resources.AlarmTypeResource;
import org.openo.nfvo.monitor.umc.fm.resources.CurrentAlarmResource;
import org.openo.nfvo.monitor.umc.fm.resources.ProbableCauseResource;
import org.openo.nfvo.monitor.umc.fm.resources.SystemTypeMocRelationResource;
import org.openo.nfvo.monitor.umc.fm.resources.SystemTypeResource;
import org.openo.nfvo.monitor.umc.i18n.I18n;
import org.openo.nfvo.monitor.umc.monitor.resources.MonitorResource;
import org.openo.nfvo.monitor.umc.monitor.wrapper.DACServiceWrapper;
import org.openo.nfvo.monitor.umc.msb.MSBRestServiceProxy;
import org.openo.nfvo.monitor.umc.msb.MsbConfiguration;
import org.openo.nfvo.monitor.umc.msb.bean.MsbRegisterBean;
import org.openo.nfvo.monitor.umc.msb.bean.ServiceNodeBean;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.DacConfiguration;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocConfiguration;
import org.openo.nfvo.monitor.umc.pm.resources.HistoryPmDataResource;
import org.openo.nfvo.monitor.umc.pm.resources.MeasureTaskResource;
import org.openo.nfvo.monitor.umc.pm.resources.MoTypeResource;
import org.openo.nfvo.monitor.umc.pm.resources.ResourceResource;
import org.openo.nfvo.monitor.umc.res.resource.ChangeResource;
import org.openo.nfvo.monitor.umc.sm.resources.UiframeResource;
import org.openo.nfvo.monitor.umc.sm.wrapper.SmcServiceWrapper;
import org.openo.nfvo.monitor.umc.sm.wrapper.UiFrameServiceWrapper;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;
import org.openo.nfvo.monitor.umc.util.ExtensionUtil;
import org.openo.nfvo.monitor.umc.util.UMCUtil;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UMCApp extends Application<UMCAppConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UMCApp.class);
    private static final String EXTEND_RESOURCE = "umc.resource";
    public static void main(String[] args) throws Exception {
        new UMCApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<UMCAppConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
        bootstrap.addBundle(new AssetsBundle("/uiframe", "/iui/uiframe", "index.html", "iui-uiframe"));
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

/*        bootstrap.addBundle(new H2DbServerBundle<UMCAppConfig>() {
        });*/
    }

    @Override
    public void run(UMCAppConfig umcAppConfig, Environment environment) {
    	FastFileSystem.init();
    	UMCUtil.setLanguage(umcAppConfig.getLanguage());
        ClientConfigFactory.setRocServerAddr(umcAppConfig.getRocServerAddr());
        RocConfiguration.setRocServerAddr(umcAppConfig.getRocServerAddr());
        RocResourceConfig.setRocResourceAddr(umcAppConfig.getRocServerAddr());
        MsbConfiguration.setMsbAddress(umcAppConfig.getMsbAddress());
        DacConfiguration.getInstance().setDacServerPort(umcAppConfig.getDacServerPort());
        final LayerMonitorResource layerMonitor = new LayerMonitorResource();
        final TopologyManagerResource topologyManager = new TopologyManagerResource();
        environment.jersey().register(layerMonitor);
        environment.jersey().register(topologyManager);

        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(new UiframeResource());
        environment.jersey().register(new MonitorResource());

        environment.jersey().register(new MoTypeResource());
        environment.jersey().register(new ResourceResource());
        environment.jersey().register(new MeasureTaskResource());
        environment.jersey().register(new HistoryPmDataResource());

        environment.jersey().register(new CurrentAlarmResource());
        environment.jersey().register(new SystemTypeResource());
        environment.jersey().register(new ProbableCauseResource());
        environment.jersey().register(new SystemTypeMocRelationResource());
        environment.jersey().register(new AlarmTypeResource());
        environment.jersey().register(new ChangeResource());
        environment.jersey().register(new DacResource());

		Object[] extendResources = ExtensionAccess.getExtensions(IRecource.class.getName(), EXTEND_RESOURCE);

		if (extendResources != null && extendResources.length > 0)
		{
			for (Object extendResource : extendResources) {
				environment.jersey().register(extendResource);
			}
		}

        UiFrameServiceWrapper.getInstance().setFrameconfig(umcAppConfig.getFrameCommInfo());
        UiFrameServiceWrapper.getInstance().setVersioninfo(umcAppConfig.getVersionInfo());
        SmcServiceWrapper.getInstance().setLoginInfo(umcAppConfig.getLoginInfo());

        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        String[] packageUrls = new String[] {UMCApp.class.getPackage().getName()};
        ExtensionUtil.init(packageUrls);
        initCometd(environment, umcAppConfig);
        //set swagger
        setSwagger(umcAppConfig);


        //set umc hibernate session
        UmcDbSession.init(umcAppConfig);

        CacheService.init();
        FmCacheUtil.init();
        //set DAC cometdClient
        DACServiceWrapper.getInstance().initDACcometdClient();

/*        if(umcAppConfig.getDacIp() != null){
        	DACServiceWrapper.getInstance().initLocalDac(umcAppConfig.getDacIp());
        }*/
        //init dac 
        DACServiceWrapper.getInstance().initDac();

        //RocCometdClient.getInstance().subscribe(umcAppConfig.getRocServerAddr());

        I18n.init();
        registerUmcService(umcAppConfig);
    }

    private void registerUmcService(UMCAppConfig umcAppConfig) {
		// TODO Auto-generated method stub
       	SimpleServerFactory simpleServerFactory = (SimpleServerFactory)umcAppConfig.getServerFactory();
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
		registerBean.setServiceName("umc");
		registerBean.setUrl("/openoapi/umc/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		
/*		registerBean = new MsbRegisterBean();
		registerBean.setServiceName("monitor-umcdrill");
		registerBean.setUrl("/openoapi/umcdrill/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);*/
		LOGGER.info("register monitor-umc service to msb finished.");
	}

    private void registerUmcService_bak(UMCAppConfig umcAppConfig) {
		// TODO Auto-generated method stub
       	SimpleServerFactory simpleServerFactory = (SimpleServerFactory)umcAppConfig.getServerFactory();
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
		registerBean.setServiceName("monitor-umcfm");
		registerBean.setUrl("/openoapi/umcfm/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		
		registerBean = new MsbRegisterBean();
		registerBean.setServiceName("monitor-umcmonitor");
		registerBean.setUrl("/openoapi/umcmonitor/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		
		registerBean = new MsbRegisterBean();
		registerBean.setServiceName("monitor-umcpm");
		registerBean.setUrl("/openoapi/umcpm/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		
		registerBean = new MsbRegisterBean();
		registerBean.setServiceName("monitor-umcsm");
		registerBean.setUrl("/openoapi/umcsm/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);
		
/*		registerBean = new MsbRegisterBean();
		registerBean.setServiceName("monitor-umcdrill");
		registerBean.setUrl("/openoapi/umcdrill/v1");
		registerBean.setNodes(nodeList);
		MSBRestServiceProxy.registerService(registerBean);*/
		LOGGER.info("register monitor-umc service to msb finished.");
	}
	/**
     * initialize cometD server
     *
     * @param environment environment information
     */
    private void initCometd(Environment environment, UMCAppConfig umcAppConfig) {
    	String allPath = umcAppConfig.getCometdServletInfo().getServletPath() + "/*";
        environment.getApplicationContext().addFilter(CrossOriginFilter.class, allPath,
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));// add filter
        environment.getApplicationContext()
                .addServlet("org.cometd.server.CometDServlet", allPath)
                .setInitOrder(1);// add servlet
        environment.getApplicationContext()
                .addServlet(umcAppConfig.getCometdServletInfo().getServletClass(),
                		umcAppConfig.getCometdServletInfo().getServletPath())
                .setInitOrder(2);// add servlet
    }
    @Override
    public String getName() {
        return " UMC APP ";
    }


    public void setSwagger(UMCAppConfig umcAppConfig){
        BeanConfig config = new BeanConfig();
        config.setTitle("UMC Services");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.openo.nfvo.monitor.umc");
        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) umcAppConfig.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();
        String rootPath = simpleServerFactory.getJerseyRootPath();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
        basePath = basePath.equals("/") ? rootPath : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

}
