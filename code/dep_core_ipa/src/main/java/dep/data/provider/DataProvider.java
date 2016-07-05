package dep.data.provider;

import java.util.Date;
import com.departments.ipa.dep_dbo.DepartmentDBO;

public class DataProvider {

	public String getMessage(){
		DepartmentDBO dbo = new DepartmentDBO();
		String sqlLayer = "";
		try {
			sqlLayer = dbo.getData();
		} catch (Exception e) {
			sqlLayer = e.toString() + ",";
		}
		return sqlLayer + "Hello World from rest layer!, Hello from dep_core_API," +
				"RESTful Service is running ..., Ping @ " + new Date().toString();
	}
}
