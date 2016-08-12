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
package org.openo.orchestrator.nfv.umc.pm.osf.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author wangjiangping
 * @date 2016/2/16 10:12:46
 *
 */
public class HibernateSession {
    private static File cfgfile = null;

    private static ServiceRegistry serviceRegistry = null;
    private static Configuration configuration = null;
    private static SessionFactory sessionFactory = null;

    /**
     * Get a hibernate sessionFactory.
     */
    public static SessionFactory init() {
        createCfgFile();

        configuration = new Configuration().configure(cfgfile);
        serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    /**
     * Destory a hibernate sessionFactory.
     */
    public static void destory() {
        sessionFactory.close();
    }

    private static void createCfgFile(){
        final String filename = "Hibernate.cfg.xml";

        String folder = System.getProperty("java.io.tmpdir");
        cfgfile = new File(folder + filename);
        if (!cfgfile.exists()) {
            try {
                cfgfile.createNewFile();

                FileWriter fw = new FileWriter(cfgfile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(data);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /* Maybe you don't need it. */
    private static void removeCfgFile() {
        if (cfgfile.exists()) {
            cfgfile.deleteOnExit();
        }
    }

    private static final String data =""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE hibernate-configuration PUBLIC \n\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" \n\"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\n"
            + "<hibernate-configuration>\n"
            + "<session-factory>\n"
            + "    <property name=\"show_sql\">true</property>\n"
            + "    <property name=\"dialect\">org.hibernate.dialect.H2Dialect</property>\n"
            + "    <property name=\"hibernate.connection.driver_class\">org.h2.Driver</property>\n"
            + "    <property name=\"hibernate.connection.url\">jdbc:h2:tcp://localhost:8405/./db/umcdb</property>\n"
            + "    <property name=\"hibernate.connection.username\">openo</property>\n"
            + "    <property name=\"hibernate.connection.password\">openo</property>\n"
            + "    <property name=\"hbm2ddl\">update</property>\n"
            + "    <property name=\"current_session_context_class\">thread</property>\n"

            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.ItPmTask\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.ItThresholdTable\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.PmIndexDefTable\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.PmNepoAttributeDefTable\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.PmNepoDefTable\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.PmTaskInfoTable\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvHostLinuxFs\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvHostLinuxRam\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvHostLinuxCpu\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvHostLinuxNic\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvVduLinuxFs\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvVduLinuxNic\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvVduLinuxCpu\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.pm.osf.db.entity.NfvVduLinuxRam\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.fm.bean.CurrentAlarm\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.fm.bean.ProbableCause\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.fm.bean.SystemType\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.fm.bean.AlarmType\"/>\n"
            + "    <mapping class=\"org.openo.orchestrator.nfv.umc.fm.bean.SystemTypeMocRelation\"/>\n"
            + "</session-factory>\n"
            + "</hibernate-configuration>"
            + "";

    public static void main(String[] args){
        //createCfgFile();
        removeCfgFile();
    }
}
