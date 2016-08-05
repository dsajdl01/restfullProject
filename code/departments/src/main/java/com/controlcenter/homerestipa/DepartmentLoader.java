package com.controlcenter.homerestipa;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.Application;

/**
 * Created by david on 30/07/16.
 */

public class DepartmentLoader extends ResourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentLoader.class);

    @SuppressWarnings({ "serial" })
    private static final Map<String,Object> PROPERTIES = new HashMap<String,Object>() {{
        put("com.sun.jersey.api.json.POJOMappingFeature",Boolean.TRUE);
        put("jersey.config.server.tracing","ALL");
        put("jersey.config.server.tracing.threshold","VERBOSE");
    }};

    public DepartmentLoader(){

        LOGGER.info("DepartmentLoader constractor {}", DepartmentLoader.class.toString());
        register(new DepartmentBinder());
        register(JacksonJsonProvider.class);
        packages(true, "com.controlcenter.homerestipa");
        addProperties(PROPERTIES);
        LOGGER.debug("Application components registered successfully");
    }

    class DepartmentBinder extends AbstractBinder {
      @Override
        protected void configure(){
          LOGGER.info("DepartmentBinder ===> DepartmentLoader {}", DepartmentLoader.class.toString());
      }
    }
}