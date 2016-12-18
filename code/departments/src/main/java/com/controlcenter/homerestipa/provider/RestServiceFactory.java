package com.controlcenter.homerestipa.provider;

/**
 * Created by david on 17/12/16.
 */
public class RestServiceFactory {

    private static class LazyHolder {
        static RestServices INSTANCE;
        static {
            try {
                INSTANCE =  new RestCenterServices();
            }
            catch (Exception e){
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static RestServices getInstance() {
        return LazyHolder.INSTANCE;
    }
}
