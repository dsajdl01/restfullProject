package com.departments.ipa.data;

/**
 * Created by david on 26/08/16.
 */
public class DepartmentTable {

    private Integer id;
    private String name;
    private Integer createdBy;
    public DepartmentTable(Integer id, String name, Integer createdBy){
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
