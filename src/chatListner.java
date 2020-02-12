import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class chatListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/chatapp");
        bds.setUsername("root");
        bds.setPassword("ijse");
        bds.setMaxTotal(2);
        bds.setInitialSize(2);
        sce.getServletContext().setAttribute("db", bds);

    }
}
