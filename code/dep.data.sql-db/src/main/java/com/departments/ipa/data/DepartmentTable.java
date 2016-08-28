package com.departments.ipa.data;
/**
 * Created by david on 26/08/16.
 */
public class DepartmentTable {

    private final Integer id;
    private final String name;
    private final Integer createdBy;
    public DepartmentTable(final Integer id, final String name, final Integer createdBy){
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        DepartmentTable other = (DepartmentTable) obj;
        return id  == other.id
                && name == other.name
                && createdBy == other.createdBy;
    }

    @Override
    public String toString() {
        return "DepartmentTable [id = " + id + ", name = " + name + ", createdBy = " + createdBy + "]";
    }


}
