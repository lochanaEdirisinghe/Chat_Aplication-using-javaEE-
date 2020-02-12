import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/chat")
public class chatApp extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicDataSource db = (BasicDataSource) getServletContext().getAttribute("db");
        try (Connection connection = db.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("insert into user values(?,?,?,?)");
            pst.setObject(1, req.getParameter("id"));
            pst.setObject(2, req.getParameter("name"));
            pst.setObject(3, req.getParameter("email"));
            pst.setObject(4, req.getParameter("password"));
            int rst = pst.executeUpdate();
            System.out.println(rst);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicDataSource db = (BasicDataSource) getServletContext().getAttribute("db");
        try (Connection connection = db.getConnection()) {
            String name=req.getParameter("name");
            String password = req.getParameter("password");
            PreparedStatement pst = connection.prepareStatement("select * from user where name=? and password=?");
            pst.setObject(1, name);
            pst.setObject(2, password);
            ResultSet rst = pst.executeQuery();
            if(rst.next()){
                resp.getWriter().print("ok");
            }else{
                resp.getWriter().print("wrong username and password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
