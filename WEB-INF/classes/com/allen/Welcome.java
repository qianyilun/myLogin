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
            HttpSession session = req.getSession(true);
            String pass = (String) session.getAttribute("PASS");

            // redirect to login web, notice: null is a specific value that needs to be considered
            if (pass == null) {
                res.sendRedirect("login");
                // notice must return, otherwise has NullPointerException
                return;                
            }
            

            System.out.println("????");
            
            if (pass.equals("OK")) {
                pw.println(username + " " + password);
            } else {
                res.sendRedirect("login");
            }

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