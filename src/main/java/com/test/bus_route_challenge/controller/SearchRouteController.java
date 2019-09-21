package com.test.bus_route_challenge.controller;

import com.test.bus_route_challenge.Service.DataService;
import com.test.bus_route_challenge.model.DirectBusRouteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SearchRouteController {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    volatile boolean direct_bus_route_exist;

    @Autowired
    private DataService dataService;

    /**
     * Is direct bus route present for requested parameters.
     *
     * @return the DirectBusRouteResponse json object
     */
    @GetMapping("/direct")
    public DirectBusRouteResponse isDirectBusRouteExist(@RequestParam("dep_sid") Long dep_sid,
                                                        @RequestParam("arr_sid") Long arr_sid) {

        logger.info("getBusRoutesData method started");
        String cacheKey = dep_sid.toString() + arr_sid.toString();
        long start = System.currentTimeMillis();
        HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap = dataService.getBusRoutesData(cacheKey);
        long end = System.currentTimeMillis();
        logger.info("Preparing data for search time: " + (end - start) / 1000.0);
        logger.info("getBusRoutesData method finished");

        logger.info("isDirectBusRouteExist method started");
        direct_bus_route_exist = isDirectBusRouteExist(stationIdStationInfoMap, dep_sid, arr_sid);
        logger.info("isDirectBusRouteExist method finished");

        return new

                DirectBusRouteResponse(dep_sid, arr_sid, direct_bus_route_exist);

    }

    private boolean isDirectBusRouteExist(HashMap<String, List<HashMap<String, Integer>>> stationIdStationInfoMap,
                                          Long dep_sid, Long arr_sid) {
        long start = System.currentTimeMillis();
        List<HashMap<String, Integer>> depStationRouteInfoList = stationIdStationInfoMap.get(dep_sid.toString());
        List<HashMap<String, Integer>> arrStationRouteInfoList = stationIdStationInfoMap.get(arr_sid.toString());

        String routeID = "not found";
        outerloop:
        for (HashMap<String, Integer> depStation : depStationRouteInfoList) {
            for (Map.Entry<String, Integer> e : depStation.entrySet()) {
                String depRouteId = e.getKey();
                Integer depStationIndexValue = e.getValue();
                for (HashMap<String, Integer> arrStation : arrStationRouteInfoList) {
                    for (Map.Entry<String, Integer> entry : arrStation.entrySet()) {
                        String arrRouteId = entry.getKey();
                        Integer arrStationIndexValue = entry.getValue();
                        if (arrRouteId.equals(depRouteId) && depStationIndexValue < arrStationIndexValue) {
                            direct_bus_route_exist = true;
                            routeID = arrRouteId;
                            break outerloop;
                        }
                    }
                }
            }
        }


        long end = System.currentTimeMillis();
        logger.info("depStationRouteInfoList size: " + depStationRouteInfoList.size() +
                ", arrStationRouteInfoList size:" + arrStationRouteInfoList.size());
        logger.info("route id for direct route is " + routeID);
        logger.info("Search route time: " + (end - start) / 1000.0);

        return direct_bus_route_exist;
    }



    @GetMapping("/wirteFile")
    public String writeToFile() {
        try {
            FileWriter fw = new FileWriter("C:\\Sviat/testout50000.txt");
            fw.write("100 000");
            Random rand = new Random();
            fw.write(System.lineSeparator());
            for (int i = 1; i <= 50000; i++) {
                fw.write(i + " ");
                for (int j = 1; j <= 1000; j++) {
                    fw.write(j + rand.nextInt(500) + " ");
                }
                fw.write(System.lineSeparator());
                fw.write(System.lineSeparator());
            }

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Success...");

        return "done";
    }

}
