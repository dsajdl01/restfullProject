package com.controlcenter.homerestipa.provider;

import com.controlcenter.homerestipa.utils.ValidationHelper;
import com.department.core.data.PasswordAuthentication;
import com.httpSession.core.HttpSessionCoreServlet;

/**
 * Created by david on 17/12/16.
 */
public class RestCenterServices implements RestServices {

    private PasswordAuthentication passwordAuth;
    private ValidationHelper validationHelper;

    public RestCenterServices(){

        HttpSessionCoreServlet httpSessionCoreServlet = new HttpSessionCoreServlet();
        passwordAuth = new PasswordAuthentication( httpSessionCoreServlet );
        validationHelper = new ValidationHelper(passwordAuth);

    }

    public ValidationHelper getValidationHelper() {
        return validationHelper;
    }
}
