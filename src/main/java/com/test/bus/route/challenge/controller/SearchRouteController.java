package com.test.bus.route.challenge.controller;

import com.test.bus.route.challenge.exceptions.ValidationException;
import com.test.bus.route.challenge.service.RouteService;
import com.test.bus.route.challenge.validators.DepArrValidator;
import com.test.bus.route.challenge.model.DirectBusRouteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SearchRouteController {

    private Logger logger = LoggerFactory.getLogger(SearchRouteController.class);

    @Autowired private RouteService dataService;
    @Autowired private DepArrValidator depArrValidator;

    /**
     * Is direct bus route present for requested parameters.
     *
     * @return the DirectBusRouteResponse json object
     */
    @GetMapping("/direct")
    public DirectBusRouteResponse getDirectBusRoute(@RequestParam("dep_sid") Integer dep_sid,
                                                    @RequestParam("arr_sid") Integer arr_sid) {

        if (!depArrValidator.isDepArrSidValid(dep_sid, arr_sid)) {
            logger.error("departure or arrival sid is invalid");
            throw new ValidationException("departure or arrival sid is invalid");
        }

        boolean direct_bus_route_exist = dataService.getDirectBusRoute(dep_sid, arr_sid);
        return new DirectBusRouteResponse(dep_sid, arr_sid, direct_bus_route_exist);
    }

}
