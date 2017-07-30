package com.allen;

import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class Check extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/" + "student_information" + "?autoReconnect=true&useSSL=false";
            Connection connection = DriverManager.getConnection(url, "root", "   ");
            Statement sm = connection.createStatement();
            ResultSet rs = sm.executeQuery("SELECT * FROM student");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

            if (username.equals("allen") && password.equals("allen")) {
                HttpSession session = req.getSession(true); 
                session.setAttribute("PASS", "OK");
                session.setMaxInactiveInterval(20);
                res.sendRedirect("welcome?username=" + username + "&password=" + password);
            } else {
                res.sendRedirect("login");
            }    
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        
    }
}