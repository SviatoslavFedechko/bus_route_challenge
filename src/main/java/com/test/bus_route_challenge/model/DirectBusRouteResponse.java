package com.test.bus_route_challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DirectBusRouteResponse {

    private Integer dep_sid;
    private Integer arr_sid;
    private boolean direct_bus_route;

}
