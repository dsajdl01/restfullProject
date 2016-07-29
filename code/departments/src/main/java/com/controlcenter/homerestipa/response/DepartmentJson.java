package com.controlcenter.homerestipa.response;

/**
 * Created by david on 07/07/16.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class DepartmentJson {

    @JsonProperty("depId")
    private final Integer depId;

    @JsonProperty("delName")
    private final String depName;

    @JsonProperty("createdBy")
    private final String createdBy;

/*    @JsonCreator*/
    public DepartmentJson(@JsonProperty("depId") final Integer depId,
                          @JsonProperty("depName")final String depName,
                          @JsonProperty("createdBy")final String createdBy)
    {
        this.depId = depId;
        this.depName = depName;
        this.createdBy = createdBy;
    }

}
