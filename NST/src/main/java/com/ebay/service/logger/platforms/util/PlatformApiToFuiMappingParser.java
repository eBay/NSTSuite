package com.ebay.service.logger.platforms.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ebay.nst.resourcefile.reader.ResourceFileReader;
import com.ebay.runtime.RuntimeConfigManager;

public class PlatformApiToFuiMappingParser {

  private static final String resourceFileNamePattern = "nstToFuiMappings%s.csv";
  private Map<String, Map<String, PlatformApiToFuiMapping>> mappingTable;

  public PlatformApiToFuiMappingParser() throws URISyntaxException, IOException {
    init();
  }

  public PlatformApiToFuiMapping getMappingsForApiName(String apiName) {

    Set<String> keys = mappingTable.keySet();
    for (String key : keys) {
      PlatformApiToFuiMapping mappings = mappingTable.get(key).get(apiName);
      if (mappings != null) {
        return mappings;
      }
    }
    return null;
  }

  public PlatformApiToFuiMapping getMappingsForServiceAndApiName(String serviceName, String apiName) {

    Map<String, PlatformApiToFuiMapping> map = mappingTable.get(serviceName);

    if (map == null) {
      return null;
    }

    return map.get(apiName);
  }

  private void init() throws URISyntaxException, IOException {

    String platformName = RuntimeConfigManager.getInstance().getPlatform().name().toLowerCase();
    platformName = platformName.substring(0, 1).toUpperCase() + platformName.substring(1);
    String fileName = String.format(resourceFileNamePattern, platformName);

    // Read in the resource file for the service, endpoint and host mappings.
    ResourceFileReader resourceFileReader = new ResourceFileReader();
    List<Map<String, String>> hostTableData = resourceFileReader.readFixedColumnDataFromCsv(fileName);

    mappingTable = new HashMap<>();

    // Expected keys: SERVICE, API, NAVIGATION, ENTRY, IMPORTS and MEMBER_FIELDS
    // Check row data length (must have correct number of columns).
    for (int i = 0; i < hostTableData.size(); i++) {

      Map<String, String> rowData = hostTableData.get(i);

      PlatformApiToFuiMapping mappings = new PlatformApiToFuiMapping();
      mappings.setPlatformRequestTypeStatements(rowData.get(PlatformApiToFuiMappingKeys.PLATFORM_REQUEST_TYPE.name()));
      mappings.setNavigationStatements(rowData.get(PlatformApiToFuiMappingKeys.NAVIGATION.name()));
      mappings.setEntryStatements(rowData.get(PlatformApiToFuiMappingKeys.ENTRY.name()));
      mappings.setImportStatements(rowData.get(PlatformApiToFuiMappingKeys.IMPORTS.name()));
      mappings.setMemberFieldStatements(rowData.get(PlatformApiToFuiMappingKeys.MEMBER_FIELDS.name()));

      Map<String, PlatformApiToFuiMapping> apiMapping = new HashMap<>();
      apiMapping.put(rowData.get(PlatformApiToFuiMappingKeys.API.name()), mappings);

      String service = rowData.get(PlatformApiToFuiMappingKeys.SERVICE.name());
      Map<String, PlatformApiToFuiMapping> serviceMapping = mappingTable.get(service);
      if (serviceMapping == null) {
        mappingTable.put(service, apiMapping);
      } else {
        serviceMapping.putAll(apiMapping);
        mappingTable.put(service, serviceMapping);
      }
    }
  }
}
