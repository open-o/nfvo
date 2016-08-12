/**
 *       Copyright (C) 2015 ZTE, Inc. and others. All rights reserved. (ZTE)
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
package com.zte.ums.sfc.console;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zte.ums.sfc.console.health.ConsoleHealthCheck;
import com.zte.ums.sfc.console.resources.SfcDriverResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SfcApp  extends Application<SfcAppConfig> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SfcApp.class);
    public static void main(String[] args) throws Exception {
        new SfcApp().run(args);
    }

    @Override
    public String getName() {
        return "SfcApp service";
    }

    @Override
    public void initialize(Bootstrap<SfcAppConfig> bootstrap) {
    	bootstrap.addBundle(new AssetsBundle("/iui","/","index.html","iui"));
    	bootstrap.addBundle(new AssetsBundle("/api-doc","/api-doc","index.html","api-doc"));
    }

    @Override
    public void run(SfcAppConfig configuration,
                    Environment environment) {
        final SfcDriverResource resource = new SfcDriverResource();
        final ConsoleHealthCheck healthCheck =
                new ConsoleHealthCheck(configuration.getTemplate());
            environment.healthChecks().register("template", healthCheck);
            environment.jersey().register(resource);
            initSwaggerConfig(environment,configuration);
            
    }

    
    private void initSwaggerConfig(Environment environment,SfcAppConfig configuration) {
        environment.jersey().register(new ApiListingResource());      
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle("ZTE Conductor Console Service rest API");
        config.setVersion("1.0.0");
        config.setResourcePackage("com.zte.ums.nfv.eco.console.resources");
        //swagger rest api basepath
        SimpleServerFactory simpleServerFactory =  (SimpleServerFactory)configuration.getServerFactory();
        String basePath =  simpleServerFactory.getApplicationContextPath();
        
        basePath = basePath.endsWith("/") ? basePath : (new StringBuilder()).append(basePath).append('/').toString();
        basePath = basePath  + "service";        		
        LOGGER.info("getApplicationContextPath： " + basePath);
        config.setBasePath(basePath);
        config.setScan(true);
    }

}
