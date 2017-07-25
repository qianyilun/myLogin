package com.allen;

import javax.servlet.http.*;
import java.io.*;

public class Check extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if (username.equals("allen") && password.equals("allen")) {
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