package com.departments.dto.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 14/08/16.
 */
public class PropertiesDataConfig {

    private static Map<String, String> depatmentProperties;

    public PropertiesDataConfig(){
        depatmentProperties = new HashMap<String, String>();
    }

    public void addPropertiesDataConfig(String key, String value){
        depatmentProperties.put(key, value);
    }

    public String getValue(String key){
        if(depatmentProperties.containsKey(key)){
          return depatmentProperties.get(key);
        }
        return null;
    }
}
