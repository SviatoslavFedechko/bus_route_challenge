package com.test.bus.route.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class DataService {

    private Logger logger = LoggerFactory.getLogger(DataService.class);

    public boolean getDirectBusRoute(String depSid, String arrSid) {

        long start = System.currentTimeMillis();
        String cacheKey = depSid + arrSid;

        Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = getBusRoutesData(cacheKey);

        long end = System.currentTimeMillis();
        String searchTime = String.format("Preparing data for search time, s: %s", String.valueOf((end - start) / 1000.0));
        logger.info(searchTime);
        return isDirectBusRouteExist(stationIdStationInfoMap, depSid, arrSid);
    }

    private boolean isDirectBusRouteExist(Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap,
                                          String depSid, String arrSid) {
        long start = System.currentTimeMillis();
        List<HashMap<String, Integer>> depStationRouteInfoList = getInfoListByStaionId(stationIdStationInfoMap, depSid);
        List<HashMap<String, Integer>> arrStationRouteInfoList = getInfoListByStaionId(stationIdStationInfoMap, arrSid);

        boolean directBusRouteExist = depStationRouteInfoList.stream().flatMap(depInfo -> depInfo.entrySet().stream())
                .anyMatch(depE ->
                        arrStationRouteInfoList.stream().flatMap(arrInfo -> arrInfo.entrySet().stream())
                                .anyMatch(arrE -> depE.getKey().equals(arrE.getKey()) && depE.getValue() < arrE.getValue())
                );

        long end = System.currentTimeMillis();
        String dedArrArraySize = String.format("depStationRouteInfoList size: %s, arrStationRouteInfoList size: %s",
                depStationRouteInfoList.size(), arrStationRouteInfoList.size());
        logger.info(dedArrArraySize);
        String routeSearchTime = String.format("Search route time, s: %s", (end - start) / 1000.0);
        logger.info(routeSearchTime);

        return directBusRouteExist;
    }

    private List<HashMap<String, Integer>> getInfoListByStaionId(
            Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap, String sid) {
        return Optional.ofNullable(stationIdStationInfoMap.get(sid)).orElse(new ArrayList<>());
    }

    @Cacheable(value = "routeCache", key = "#cacheKey")
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
