package com.test.bus.route.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    private Logger logger = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    private DataService dataService;

    public boolean getDirectBusRoute(String depSid, String arrSid) {

        long start = System.currentTimeMillis();
        String cacheKey = depSid + arrSid;

        Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = dataService.getBusRoutesData(cacheKey);

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
        String routeSearchTime = String.format("Searching direct route time, s: %s", (end - start) / 1000.0);
        logger.info(routeSearchTime);

        return directBusRouteExist;
    }

    private List<HashMap<String, Integer>> getInfoListByStaionId(
            Map<String, List<HashMap<String, Integer>>> stationIdStationInfoMap, String sid) {
        return Optional.ofNullable(stationIdStationInfoMap.get(sid)).orElse(new ArrayList<>());
    }


}
