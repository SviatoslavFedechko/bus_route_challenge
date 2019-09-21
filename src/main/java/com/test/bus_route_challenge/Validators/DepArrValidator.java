package com.test.bus_route_challenge.Validators;

import com.test.bus_route_challenge.Service.DataService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepArrValidator {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    public boolean isDepArrSidValid(String dep_sid, String arr_sid) {
        return StringUtils.isNumeric(dep_sid) && StringUtils.isNumeric(arr_sid);
    }
}
