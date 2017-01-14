package com.controlcenter.homerestipa.provider;

import com.controlcenter.homerestipa.utils.ValidationHepler;
import com.department.core.data.PasswordAuthentication;
import com.httpSession.core.HttpSessionCoreServlet;

/**
 * Created by david on 17/12/16.
 */
public class RestCenterServices implements RestServices {

    private PasswordAuthentication passwordAuth;
    private ValidationHepler validationHepler;

    public RestCenterServices(){

        HttpSessionCoreServlet httpSessionCoreServlet = new HttpSessionCoreServlet();
        passwordAuth = new PasswordAuthentication( httpSessionCoreServlet );
        validationHepler = new ValidationHepler(passwordAuth);

    }

    public ValidationHepler getValidationHepler() {
        return validationHepler;
    }
}
