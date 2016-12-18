package com.controlcenter.homerestipa.provider;

import com.controlcenter.homerestipa.utils.ValidationStaffHepler;

/**
 * Created by david on 17/12/16.
 */
public class RestCenterServices implements RestServices {

    private ValidationStaffHepler validationStaffHepler;

    public RestCenterServices(){

        validationStaffHepler = new ValidationStaffHepler();

    }

    public ValidationStaffHepler getValidationStaffHepler() {
        return validationStaffHepler;
    }
}
