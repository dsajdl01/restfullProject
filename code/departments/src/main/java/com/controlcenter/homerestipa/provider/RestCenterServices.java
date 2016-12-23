package com.controlcenter.homerestipa.provider;

import com.controlcenter.homerestipa.utils.ValidationStaffHepler;
import com.department.core.data.PasswordAuthentication;

/**
 * Created by david on 17/12/16.
 */
public class RestCenterServices implements RestServices {

    private PasswordAuthentication passwordAuth;
    private ValidationStaffHepler validationStaffHepler;

    public RestCenterServices(){

        passwordAuth = new PasswordAuthentication();
        validationStaffHepler = new ValidationStaffHepler(passwordAuth);

    }

    public ValidationStaffHepler getValidationStaffHepler() {
        return validationStaffHepler;
    }
}
