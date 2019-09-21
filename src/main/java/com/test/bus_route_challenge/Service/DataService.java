package com.test.bus_route_challenge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
public class DataService {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    public boolean getDirectBusRoute(String dep_sid, String arr_sid) {

        long start = System.currentTimeMillis();
        String cacheKey = dep_sid + arr_sid;

        HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = getBusRoutesData(cacheKey);

        long end = System.currentTimeMillis();
        logger.info("Preparing data for search time, s: " + (end - start) / 1000.0);

        return isDirectBusRouteExist(stationIdStationInfoMap, dep_sid, arr_sid);
    }

    private boolean isDirectBusRouteExist(HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap,
                                          String dep_sid, String arr_sid) {
        long start = System.currentTimeMillis();
        List<HashMap<String, Integer>> depStationRouteInfoList = getInfoListByStaionId(stationIdStationInfoMap, dep_sid);
        List<HashMap<String, Integer>> arrStationRouteInfoList = getInfoListByStaionId(stationIdStationInfoMap, arr_sid);

        boolean direct_bus_route_exist = depStationRouteInfoList.stream().flatMap(depInfo -> depInfo.entrySet().stream())
                .anyMatch(depE ->
                        arrStationRouteInfoList.stream().flatMap(arrInfo -> arrInfo.entrySet().stream())
                                .anyMatch(arrE -> depE.getKey().equals(arrE.getKey()) && depE.getValue() < arrE.getValue())
                );

        long end = System.currentTimeMillis();
        logger.info("depStationRouteInfoList size: " + depStationRouteInfoList.size() +
                ", arrStationRouteInfoList size:" + arrStationRouteInfoList.size());
        logger.info("Search route time, s: " + (end - start) / 1000.0);

        return direct_bus_route_exist;
    }

    private List<HashMap<String, Integer>> getInfoListByStaionId(
            HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap, String sid) {
        return Optional.ofNullable(stationIdStationInfoMap.get(sid)).orElse(new ArrayList<>());
    }

    @Cacheable(value = "routeCache", key = "#cacheKey")
    public HashMap<String, List<HashMap<String, Integer>>> getBusRoutesData(String cacheKey) {
        HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Sviat/testout10000.txt")))) {
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
            logger.error(e.getMessage());
        }
        logger.warn("no cache used");
        return stationIdStationInfoMap;
    }
}
