package com.test.bus_route_challenge.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DirectBusRouteResponse {

    private Long dep_sid;
    private Long arr_sid;
    private boolean direct_bus_route;

}
