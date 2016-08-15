package dep.data.core_dep;


public class Department {

    private Integer id;
    private String name;
    private String creater;

    public Department(Integer id, String name, String creater){
        this.id = id;
        this.name = name;
        this.creater = creater;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreater() {
        return creater;
    }
}