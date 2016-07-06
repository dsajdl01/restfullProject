package dep.data.core_dep;


public class Department {

    private Integer id;
    private String name;
    private Integer createdBy;

    public Department(Integer id, String name, Integer createdBy){
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