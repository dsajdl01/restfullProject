package dep.data.core.provider;

import com.department.core.config.DepartmentProperties;
import com.department.core.data.PasswordAuthentication;
import com.departments.dto.dep_dbo.DepartmentDBO;
import com.departments.dto.dep_dbo.DepartmentDBOConnection;
import com.departments.dto.dep_dbo.UserDBO;
import dep.data.core.provider.inter.provider.DepartmentCoreServices;
import dep.data.core.provider.inter.provider.UserInter;
import dep.data.core.provider.inter.provider.DepartmentInter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * Created by david on 19/11/16.
 */
public class CoreServices implements DepartmentCoreServices {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CoreServices.class);

    private DepartmentInter departmentInter;

    private UserInter userInter;

    public CoreServices() {
        LOGGER.info(" Constructor of CoreServices: is loading ...");
        PasswordAuthentication passwordAuth = new PasswordAuthentication();
        final Connection dbConnection = new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig()).getDbConnection();
        this.userInter = new UserImpl(new UserDBO(dbConnection), passwordAuth);
        this.departmentInter = new DepartmentImpl(new DepartmentDBO(dbConnection));
    }

    public DepartmentInter getDepartmentImpl() {
        return this.departmentInter;
    }

    public UserInter getUserImpl() {
        return this.userInter;
    }
}
