package dep.data.core.provider.inter.provider;

import com.department.core.data.PasswordAuthentication;
import com.httpSession.core.HttpSessionCoreServlet;

/**
 * Created by david on 19/11/16.
 */
public interface DepartmentCoreServices {

    DepartmentInter getDepartmentImpl();

    UserInter getUserImpl();

    HttpSessionCoreServlet getHttpSessionCoreServlet();

    PasswordAuthentication getPasswordAuthentication();
}
