package com.allen;

import javax.servlet.http.*;
import java.io.*;

public class Login extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            PrintWriter pw = res.getWriter();
            pw.println("<html>");
            pw.println("<body>");
            pw.println("<h1>Login</h1>");
            pw.println("<form action=check method=post>");

            pw.println("<p>Username</p><br>");
            pw.println("<input type=text name=username>");
            pw.println("<p>Password</p><br>");
            pw.println("<input type=password name=password>");

            pw.println("<br><br>");
            pw.println("<input type=submit value=Submit>");
            pw.println("</form>");
            pw.println("</body>");
            pw.println("</html>");
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