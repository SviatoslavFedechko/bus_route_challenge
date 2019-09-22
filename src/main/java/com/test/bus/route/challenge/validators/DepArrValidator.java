package com.test.bus.route.challenge.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DepArrValidator {

    public boolean isDepArrSidValid(String dep_sid, String arr_sid) {
        return StringUtils.isNumeric(dep_sid) && StringUtils.isNumeric(arr_sid);
    }
}
