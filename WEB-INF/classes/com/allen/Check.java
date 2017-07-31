package com.allen;

import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class Check extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        Connection connection = null;
        Statement sm = null;
        ResultSet rs = null;

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            Class.forName("com.mysql.jdbc.Driver");
            String databaseName = "student_info";
            String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection(url, "root", "   ");
            sm = connection.createStatement();
            String query = "SELECT * FROM account" + 
                            " WHERE username = '" + username + "'" + 
                            " LIMIT 1;";
            rs = sm.executeQuery(query);

            // legal login
            if (rs.next()) {
                if (password.equals(rs.getString(1))) {
                    HttpSession session = req.getSession(true); 
                    session.setAttribute("PASS", "OK");
                    session.setMaxInactiveInterval(20);
                    res.sendRedirect("welcome?username=" + username + "&password=" + password);
                }
            }
            
            PrintWriter out = res.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('User or password incorrect');");
            out.println("location='index.jsp';");
            out.println("</script>");
            res.sendRedirect("login");
   
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (sm != null) {
                    sm.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}