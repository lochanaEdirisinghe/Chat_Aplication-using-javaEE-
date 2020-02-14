import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/chat")
public class chatApp extends HttpServlet {
    String userid="";
    String username="";

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
        String option = req.getParameter("option");

        BasicDataSource db = (BasicDataSource) getServletContext().getAttribute("db");
        switch (option) {
            case  "getAll":

            try (Connection connection = db.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement("Select * from user");
                ResultSet rst = pstm.executeQuery();
                System.out.println(rst);
                JsonArrayBuilder customerArray = Json.createArrayBuilder();
                JsonArrayBuilder customerArray2 = Json.createArrayBuilder();

                while (rst.next()) {
                    JsonObjectBuilder customer = Json.createObjectBuilder();
                    String id = rst.getString(1);
                    String name = rst.getString(2);
                    if(!id.equals(userid)) {
                        customer.add("id", id);
                        customer.add("name", name);
                        customerArray.add(customer.build());
                    }

                }

                PrintWriter writer = resp.getWriter();
                writer.print(customerArray.build());
                /*JsonObjectBuilder customer2 = Json.createObjectBuilder();
                customer2.add("id", userid);
                customer2.add("name", username);
                customerArray2.add(customer2.build());*/
                //writer.print(customerArray2.build());


            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;

            case  "getUser":

            try (Connection connection = db.getConnection()) {
                String name = req.getParameter("name");
                String password = req.getParameter("password");
                PreparedStatement pst = connection.prepareStatement("select * from user where name=? and password=?");
                pst.setObject(1, name);
                pst.setObject(2, password);
                ResultSet rst = pst.executeQuery();
                if (rst.next()) {
                    userid=rst.getString(1);
                    username=rst.getString(2);
                    resp.getWriter().print("ok");
                } else {
                    resp.getWriter().print("wrong username and password");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
        }
    }

}
