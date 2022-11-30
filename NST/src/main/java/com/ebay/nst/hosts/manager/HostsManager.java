package com.ebay.nst.hosts.manager;

import com.ebay.nst.resourcefile.reader.ResourceFileReader;
import org.testng.Reporter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that handles changes between hosts. Allows for command line
 * switching of hosts sourced from file or the command line.
 * <p>
 * To start with, make sure you have a serviceHosts.csv file with three column
 * headers (serviceName, environment, host) defined and in each column add the
 * name of the service, the environment (prod, qa, feature) and then the host.
 * <p>
 * By default, QA is returned as the environment. To override this include the
 * VM arg -DtestExeEnv. You can override or specify a new host by including the
 * following VM args in your runtime configuration. -DserviceNameOverride,
 * -DenvironmentOverride, -DhostOverride. By including all three you can specify
 * a brand new host that doesn't exist on file or override a file entry.
 */
public class HostsManager {

    private static HostsManager manager;
    private PoolType poolType = PoolType.QA;
    private Map<String, String> serverAndEnvironmentToHostMap;

    private HostsManager() {
        Reporter.log("Be sure to include a serviceHosts.csv file in the root of your src/test/resources folder.", true);
    }

    /**
     * Get the instance for this run.
     *
     * @return Instance configured for this run.
     * @throws URISyntaxException Pass through.
     * @throws IOException Pass through.
     * @throws IllegalStateException Pass through.
     */
    public static HostsManager getInstance() throws URISyntaxException, IOException, IllegalStateException {

        if (manager == null) {
            synchronized (HostsManager.class) {
                manager = new HostsManager();
                manager.init();
            }
        }

        return manager;
    }
    
    /**
     * Reinitialize the host settings.
     * 
     * @throws IllegalStateException Pass through.
     * @throws URISyntaxException Pass through.
     * @throws IOException Pass through.
     */
    public void reinitialize() throws IllegalStateException, URISyntaxException, IOException {
    	manager.init();
    }

    /**
     * Get the pool/environment that we are targeting with this configuration.
     *
     * @return PoolType set for this run.
     */
    public PoolType getPoolType() {
        return poolType;
    }

    /**
     * Get the host for the service specified. Considers the pool type set for the
     * current run when doing this lookup.
     *
     * @param serviceName Service name to lookup.
     * @return Host to target for that service.
     */
    public String getHostForService(String serviceName) {

        String key = String.format("%s:%s", serviceName.toLowerCase(), poolType.name());
        if (!serverAndEnvironmentToHostMap.containsKey(key)) {
            throw new IllegalArgumentException(String.format("No mapping exists for %s in the %s pool lookup table.", serviceName, poolType.name()));
        }

        return serverAndEnvironmentToHostMap.get(key);
    }

    private void init() throws URISyntaxException, IOException, IllegalStateException {

        String testExecutionEnvironment = System.getProperty("testExeEnv");
        if (testExecutionEnvironment != null) {
            poolType = PoolType.getType(testExecutionEnvironment);
        } else {
            poolType = PoolType.QA;
        }

        // TODO: if production - make sure we have all of the input data we need to
        // collect. This may belong in another class.

        // Read in the resource file for the service, endpoint and host mappings.
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        String[][] hostTableData = resourceFileReader.readParametricDataSetFromCsv("serviceHosts.csv", true);

        serverAndEnvironmentToHostMap = new HashMap<>();
        for (int i = 0; i < hostTableData.length; i++) {
            if (hostTableData[i].length < 3) {
                throw new IllegalStateException("Host mapping file should have three columns (serviceName, environment and host).");
            }

            String serviceName = hostTableData[i][0];
            PoolType environment = PoolType.getType(hostTableData[i][1]);
            String host = hostTableData[i][2];
            String key = String.format("%s:%s", serviceName.toLowerCase(), environment.name());
            serverAndEnvironmentToHostMap.put(key, host);
        }
    }
}
