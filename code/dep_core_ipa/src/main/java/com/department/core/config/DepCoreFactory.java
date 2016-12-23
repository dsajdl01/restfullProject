package com.department.core.config;

import dep.data.core.provider.CoreServices;
import dep.data.core.provider.inter.provider.DepartmentCoreServices;

/**
 * Created by david on 19/11/16.
 */
public class DepCoreFactory {

    private static class LazyHolder {
            static DepartmentCoreServices INSTANCE;
        static {
            try {
                INSTANCE =  new CoreServices();
            }
            catch (Exception e){
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static DepartmentCoreServices getInstance() {
        return LazyHolder.INSTANCE;
    }
}
