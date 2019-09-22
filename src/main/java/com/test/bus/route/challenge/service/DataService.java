package com.test.bus.route.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    private Logger logger = LoggerFactory.getLogger(DataService.class);

    @Cacheable(cacheNames = "routeCache", key = "#cacheKey")
    public Map<String, List<HashMap<String, Integer>>> getBusRoutesData(String cacheKey) {
        Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("./data/example.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] routeInfoArray = line.split(" ");
                // routeInfoArray description: first value in array = route id, all next values are buss station ids
                String routeId = routeInfoArray[0];
                for (int i = 1; i < routeInfoArray.length; i++) {
                    String stationId = routeInfoArray[i];
                    boolean stationInfoListIsNull = stationIdStationInfoMap.get(routeInfoArray[i]) == null;
                    List stationInfoList = stationInfoListIsNull ? new ArrayList() : stationIdStationInfoMap.get(stationId);

                    // routeIdStationIdIndexMap: consist route Id as a key and position index of station id in route info array as a value.
                    HashMap<String, Integer> routeIdStationIdIndexMap = new HashMap<>();
                    routeIdStationIdIndexMap.put(routeId, i);

                    stationInfoList.add(routeIdStationIdIndexMap);
                    if (stationInfoListIsNull) {
                        stationIdStationInfoMap.put(stationId, stationInfoList);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("could not get Bus Routes Data", e);
        }
        logger.warn("no cache used");
        return stationIdStationInfoMap;
    }
}
