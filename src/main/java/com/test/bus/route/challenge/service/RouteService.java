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

    public boolean getDirectBusRoute(Integer depSid, Integer arrSid) {
        Map<Integer, Set<Integer>> stationIdStationInfoMap = dataService.getBusRoutesData();
        return isDirectBusRouteExist(stationIdStationInfoMap, depSid, arrSid);
    }

    public boolean isDirectBusRouteExist(Map<Integer, Set<Integer>> stationIdRouteIdMap,
                                          Integer depSid, Integer arrSid) {
        long start = System.currentTimeMillis();
        Set<Integer> depRouteIdSet = getInfoListByStaionId(stationIdRouteIdMap, depSid);
        Set<Integer> arrRouteIdSet = getInfoListByStaionId(stationIdRouteIdMap, arrSid);

        Integer routeId = depRouteIdSet.stream().filter(depRouteId ->
                        arrRouteIdSet.stream().anyMatch(arrRouteId -> depRouteId.equals(arrRouteId)))
                .findFirst().orElse(-1);

        long end = System.currentTimeMillis();
        String dedArrArraySize = String.format("depStationRouteInfoList size: %s, arrStationRouteInfoList size: %s",
                depRouteIdSet.size(), arrRouteIdSet.size());
        logger.info(dedArrArraySize);
        String routeSearchTime = String.format("Direct route id is %s. Searching direct route time, s: %s",
                routeId, (end - start) / 1000.0);
        logger.info(routeSearchTime);

        return routeId >= 0;
    }

    private Set<Integer> getInfoListByStaionId(Map<Integer, Set<Integer>> stationIdStationInfoMap, Integer sid) {
        return Optional.ofNullable(stationIdStationInfoMap.get(sid)).orElse(new HashSet<>());
    }


}
