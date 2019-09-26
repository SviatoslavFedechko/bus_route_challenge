package com.test.bus.route.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class DataService {

    private Logger logger = LoggerFactory.getLogger(DataService.class);

    @Cacheable(value = "routeCache")
    public Map<Integer, Set<Integer>> getBusRoutesData() {
        logger.info("inside getBusRoutesData method");
        long start = System.currentTimeMillis();

        Map<Integer, Set<Integer>> stationIdRouteIdMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("./data/example.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] routeInfoArray = line.split(" ");
                // routeInfoArray description: first value in array = route id, all next values are buss station ids
                Integer routeId = StringUtils.isEmpty(routeInfoArray[0]) ? null : Integer.valueOf(routeInfoArray[0]);
                for (int i = 1; i < routeInfoArray.length; i++) {
                    Integer stationId = Integer.valueOf(routeInfoArray[i]);
                    boolean stationInfoListIsNull = stationIdRouteIdMap.get(Integer.valueOf(routeInfoArray[i])) == null;
                    Set<Integer> routeIdSet = stationInfoListIsNull ? new HashSet() : stationIdRouteIdMap.get(stationId);

                    routeIdSet.add(routeId);
                    if (stationInfoListIsNull) {
                        stationIdRouteIdMap.put(stationId, routeIdSet);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("could not get Bus Routes Data", e);
        }
        long end = System.currentTimeMillis();
        String searchTimeMsg = String.format("getBusRoutesData method finished in %s seconds: ",
                String.valueOf((end - start) / 1000.0));
        logger.info(searchTimeMsg);
        return stationIdRouteIdMap;
    }
}
