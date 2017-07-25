package com.allen;

import javax.servlet.http.*;
import java.io.*;

public class Welcome extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            PrintWriter pw = res.getWriter();
            pw.println("Welcome!");

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            pw.println(username + " " + password);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        this.doGet(req, res);
    }
}