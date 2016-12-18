package com.controlcenter.homerestipa.provider;

import org.glassfish.hk2.api.Factory;

/**
 * Created by david on 17/12/16.
 */
public class RestServiceProvicer implements Factory<RestServices> {

    public RestServices provide() {
        return RestServiceFactory.getInstance();
    }

public void dispose(RestServices dcs0) {}

}

