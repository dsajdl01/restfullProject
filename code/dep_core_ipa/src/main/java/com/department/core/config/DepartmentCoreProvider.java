package com.department.core.config;

import dep.data.core.provider.inter.provider.DepartmentCoreServices;
import org.glassfish.hk2.api.Factory;

/**
 * Created by david on 19/11/16.
 */
public class DepartmentCoreProvider implements Factory<DepartmentCoreServices> {
    public DepartmentCoreServices provide() {
        return DepCoreFactory.getInstance();
    }

    public void dispose(DepartmentCoreServices dcs0) {

    }
}
