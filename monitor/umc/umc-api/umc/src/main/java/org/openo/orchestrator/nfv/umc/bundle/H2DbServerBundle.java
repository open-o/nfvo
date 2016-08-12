package org.openo.orchestrator.nfv.umc.bundle;

import java.util.Map;

import org.h2.tools.Server;
import org.openo.orchestrator.nfv.umc.UMCAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class H2DbServerBundle<T extends Configuration>
		implements ConfiguredBundle<T>, DatabaseConfiguration<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(H2DbServerBundle.class);

	@Override
	public void initialize(Bootstrap<?> bootstrap) {

	}

	public final void run(UMCAppConfig configuration, Environment environment) throws Exception {
		Map<String, String> dbPortMap = configuration.getDatabase().getProperties();
		initH2DbServer(dbPortMap);
		
		
	}

	public DataSourceFactory getDataSourceFactory(UMCAppConfig configuration) {
		return configuration.getDatabase();
	}

	private void initH2DbServer(Map<String, String> dbPortMap) {
		try {
			Server.createWebServer(new String[] { "-web", "-webAllowOthers", "-webPort", dbPortMap.get("webPort") })
					.start();
			Server.createTcpServer(new String[] { "-tcp", "-tcpAllowOthers", "-tcpPort", dbPortMap.get("tcpPort") })
					.start();
			LOGGER.info("H2 Database Server start success.");
		} catch (Exception e) {
			LOGGER.info("H2 Database Server start fail.");
			e.printStackTrace();
		}
	}
}
