package com.test.bus.route.challenge.validators;

import org.springframework.stereotype.Service;

@Service
public class DepArrValidator {

    public boolean isDepArrSidValid(Integer depSid, Integer arrSid) {
        return depSid >= 0 && arrSid >= 0;
    }
}
