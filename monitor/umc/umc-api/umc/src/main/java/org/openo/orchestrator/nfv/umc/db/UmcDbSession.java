/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.openo.orchestrator.nfv.umc.UMCAppConfig;
import org.openo.orchestrator.nfv.umc.pm.common.GeneralFileLocaterImpl;

import io.dropwizard.db.DataSourceFactory;

/**
 * @author wangjiangping
 * @date 2016/3/29 16:38:58
 *
 */
public class UmcDbSession {
    private static Boolean show_sql = false;
    private static String dialect = "org.hibernate.dialect.H2Dialect";

    private static String driverclass;
    private static String url;
    private static String user;
    private static String password;

    private static String hbm2ddl = "update";
    private static String current_session_context_class="thread";

    private static File cfgfile = null;
    private static ServiceRegistry serviceRegistry = null;
    private static Configuration configuration = null;
    private static SessionFactory sessionFactory = null;

    /**
     * init umc hibernate sessionFactory.
     */
    public static void init(UMCAppConfig appConfig) {
        DataSourceFactory ds= appConfig.getDatabase();
        setDbVars(ds);
        createCfgFile();
        sessionFactory = createSession();
        UmcDbUtil.setSessionFactory(sessionFactory);
    }

    private static void setDbVars(DataSourceFactory ds) {
        driverclass = ds.getDriverClass();
        url = ds.getUrl();
        user = ds.getUser();
        password = ds.getPassword();
    }

    private static SessionFactory createSession() {
        configuration = new Configuration().configure(cfgfile);
        serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    /**
     * @return the sessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public static void setSessionFactory(SessionFactory sessionFactory) {
        UmcDbSession.sessionFactory = sessionFactory;
    }

    /**
     * Destory a hibernate sessionFactory.
     */
    public static void destory() {
        sessionFactory.close();
    }


    private static void createCfgFile(){
        final String filename = "Hibernate.cfg.xml";
        //String folder = System.getProperty("java.io.tmpdir");
        String folder = GeneralFileLocaterImpl.getGeneralFileLocater().getConfigPath();

        cfgfile = new File(folder + File.separator + filename);

        if(cfgfile.exists()){
            //cfgfile.delete();
            return ;
        }

        try {
            cfgfile.createNewFile();

            FileWriter fw = new FileWriter(cfgfile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(genData());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * genarate Hibernate.cfg.xml file content.
     */
    private static String genData(){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
          .append("<!DOCTYPE hibernate-configuration PUBLIC \n\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" \n\"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\n")
          .append("<hibernate-configuration>\n")
          .append("<session-factory>\n")
          .append("    <property name=\"show_sql\">"+ String.valueOf(show_sql) +"</property>\n")
          .append("    <property name=\"dialect\">"+ dialect +"</property>\n")
          .append("    <property name=\"hibernate.connection.driver_class\">"+ driverclass +"</property>\n")
          .append("    <property name=\"hibernate.connection.url\">"+ url +"</property>\n")
          .append("    <property name=\"hibernate.connection.username\">"+ user +"</property>\n")
          .append("    <property name=\"hibernate.connection.password\">"+ password +"</property>\n")
          .append("    <property name=\"hbm2ddl.auto\">"+ hbm2ddl +"</property>\n")
          .append("    <property name=\"current_session_context_class\">"+ current_session_context_class +"</property>\n")
          .append("        <!-- system table entity class-->\n")
          .append(getSystemPoMapping())
          .append("\n")
          .append("        <!-- extend device table entity class-->\n")
          .append(getExtendPoMapping())
          .append("\n")
          .append("</session-factory>\n")
          .append("</hibernate-configuration>\n");

        return sb.toString();
    }

    private static String getSystemPoMapping(){
        StringBuilder sb = new StringBuilder();
        List<String> systemPoName = UmcDbUtil.getSystemPo();
        for(String poname : systemPoName){
            sb.append("        <mapping class=\""+ poname +"\"/>\n");
        }

        return sb.toString();
    }

    private static String getExtendPoMapping(){
        StringBuilder sb = new StringBuilder();
        List<String> systemPoName = UmcDbUtil.getExtendPo();
        for(String poname : systemPoName){
            sb.append("        <mapping class=\""+ poname +"\"/>\n");
        }

        return sb.toString();
    }
}
