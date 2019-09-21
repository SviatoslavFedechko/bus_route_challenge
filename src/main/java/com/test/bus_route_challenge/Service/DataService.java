package com.test.bus_route_challenge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DataService {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    @Cacheable(value = "routeCache", key = "#cacheKey" )
    public HashMap<String, List<HashMap<String, Integer>>> getBusRoutesData(String cacheKey) {
        logger.warn("no cache used");
        HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Sviat/testout50000.txt")))) {
            String routeCount = br.readLine();
            String st;
            while ((st = br.readLine()) != null) {
                String[] routeInfoArray = st.split(" ");
                // routeInfoArray description: first value in array = route id, all next values are buss station ids
                String routeId = routeInfoArray[0];
                for (int i = 1; i < routeInfoArray.length; i++) {
                    String stationId = routeInfoArray[i];
                    boolean StationInfoListIsNull = stationIdStationInfoMap.get(routeInfoArray[i]) == null;
                    List StationInfoList = StationInfoListIsNull ? new ArrayList() : stationIdStationInfoMap.get(stationId);

                    // routeIdStationIdIndexMap: consist route Id as a key and position index of station id in route info array as a value.
                    HashMap<String, Integer> routeIdStationIdIndexMap = new HashMap<>();
                    routeIdStationIdIndexMap.put(routeId, i);

                    StationInfoList.add(routeIdStationIdIndexMap);
                    if (StationInfoListIsNull) {
                        stationIdStationInfoMap.put(stationId, StationInfoList);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return stationIdStationInfoMap;
    }
}
