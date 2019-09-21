package com.test.bus_route_challenge.controller;

import com.test.bus_route_challenge.Exceptions.ValidationException;
import com.test.bus_route_challenge.Service.DataService;
import com.test.bus_route_challenge.Validators.DepArrValidator;
import com.test.bus_route_challenge.model.DirectBusRouteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SearchRouteController {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private DataService dataService;

    @Autowired
    private DepArrValidator depArrValidator;

    /**
     * Is direct bus route present for requested parameters.
     *
     * @return the DirectBusRouteResponse json object
     */
    @GetMapping("/direct")
    public DirectBusRouteResponse getDirectBusRoute(@RequestParam("dep_sid") String dep_sid,
                                                    @RequestParam("arr_sid") String arr_sid) {

        if (!depArrValidator.isDepArrSidValid(dep_sid, arr_sid)) {
            logger.error("departure or arrival sid is invalid");
            throw new ValidationException("departure or arrival sid is invalid");
        }

        boolean direct_bus_route_exist = dataService.getDirectBusRoute(dep_sid, arr_sid);
        return new DirectBusRouteResponse(Integer.valueOf(dep_sid), Integer.valueOf(arr_sid), direct_bus_route_exist);
    }

    @GetMapping("/writeFile")
    public String writeToFile() {
        try (FileWriter fw = new FileWriter("C:\\Sviat/testout50000.txt")) {
            fw.write("50 000");
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

        } catch (Exception e) {
            logger.error(e.toString());
        }
        logger.info("Success...");

        return "done";
    }

}
