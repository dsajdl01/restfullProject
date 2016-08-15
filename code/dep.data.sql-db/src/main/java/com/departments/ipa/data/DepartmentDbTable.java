package com.departments.ipa.data;

/**
 * Created by david on 15/08/16.
 */
public class DepartmentDbTable {
    private Integer id;
    private String name;
    private Integer createdBy;

    public DepartmentDbTable(Integer id, String name, Integer createdBy){
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }
}
