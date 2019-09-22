package com.test.bus.route.challenge.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DepArrValidator {

    public boolean isDepArrSidValid(String depSid, String arrSid) {
        return StringUtils.isNumeric(depSid) && StringUtils.isNumeric(arrSid);
    }
}
