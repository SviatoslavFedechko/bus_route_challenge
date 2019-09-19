package com.test.bus_route_challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DirectBusRouteResponse {

    private Long dep_sid;
    private Long arr_sid;
    private boolean direct_bus_route;

}
