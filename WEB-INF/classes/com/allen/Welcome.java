package com.allen;

import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.lang.StringBuilder;

public class Welcome extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            PrintWriter pw = res.getWriter();
            pw.println("<html>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<img src=imgs/loginPic.png style=width:80px;height:80px>" + "</br>");
            pw.println("Welcome!" + "</br>");

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            HttpSession session = req.getSession(true);
            String uname = (String) session.getAttribute("uname");
            
            // redirect to login web, notice: null is a specific value that needs to be considered
            if (uname == null) { // illegal login
                res.sendRedirect("login");
                // notice must return, otherwise has NullPointerException
                return;                
            } else {
                String id = session.getId();
                pw.println("sessionID: " + id + "</br> Session Name: " + uname + "</br>");
                splitView(pw, req);
            }

            pw.println("</center>");
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

    private void splitView(PrintWriter pw, HttpServletRequest req) {
        int pageSize = 3; 
        int pageNow = 1;
        int recordCount = 0; // 查表
        int pageCount = 0; // 计算

        String expectPage = req.getParameter("expectPage");

        if (expectPage != null) { // NOT the first access
            pageNow = Integer.parseInt(expectPage);
        }

        Connection ct = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            String databaseName = "student_info";
            String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";
            ct = DriverManager.getConnection(url, "root", "   ");

            String counterQuery = "SELECT COUNT(*) FROM account;";
            ps = ct.prepareStatement(counterQuery);
            rs = ps.executeQuery();

            if (rs.next()) {
                recordCount = rs.getInt(1);

                // get the page numbers
                if (recordCount % pageSize == 0) {
                    pageCount = recordCount / pageSize;
                } else {
                    pageCount = recordCount / pageSize + 1;
                } 

                // get the records from SQL
                String recordsQuery_helper;
                String notInSet;
                String query = "SELECT * FROM account" + 
                                " WHERE id NOT IN ";

                if (pageNow == 1) {
                    // ?????
                    ps = ct.prepareStatement("SELECT * FROM account " + "LIMIT ?;");
                    ps.setInt(1, pageSize);
                    rs = ps.executeQuery();
                } else {
                    // due to MySQL not support "LIMIT & IN/ALL/ANY/SOME subquery"
                    // e.g. SELECT * FROM account WHERE id NOT IN (SELECT * FROM account LIMIT 3) LIMIT 3;
                    recordsQuery_helper = "SELECT id FROM account " + 
                                    " LIMIT " + pageSize*(pageNow-1) + ";";
                    
                    ps = ct.prepareStatement(recordsQuery_helper);
                    rs = ps.executeQuery();
                    
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        sb.append(rs.getInt(1) + ",");
                    }
                    sb.setLength(sb.length()-1);
                    notInSet = sb.toString();
                    System.out.println(notInSet);

                    // implement query
                    ps = ct.prepareStatement("SELECT * FROM account " + 
                                    "WHERE id NOT IN (" + notInSet + ") LIMIT ?;");
                    ps.setInt(1, pageSize);
                    rs = ps.executeQuery();
                } 
                
                // ============ draw the table =============
                pw.println("<table border=1>");
                pw.println("<tr>");
                pw.println("<th>" + "Username" + "</th>");
                pw.println("<th>" + "Password" + "</th>");
                pw.println("<th>" + "ID" + "</th>");                          
                pw.println("</tr>");
                

                while (rs.next()) {
                    pw.println("<tr>");
                    pw.println("<td>" + rs.getString(1) + "</td>");
                    pw.println("<td>" + rs.getString(1) + "</td>");
                    pw.println("<td>" + rs.getInt(3) + "</td>" + "</br>");
                    pw.println("</td>");                    
                    pw.println("</tr>"); 
                }
            
                pw.println("</table>");

                // previous page
                if (pageNow != 1) {
                    pw.println("<a href=welcome?expectPage=" + (pageNow-1) + "> " + "Previous" + "</a>");
                }
                // print out the hyper links
                for (int i = pageNow; i <= pageNow+4; i++) {
                    pw.print("<a href=welcome?expectPage=" + i + "> " + i + "</a>");
                }
                // next page
                if (pageNow != pageCount) {
                    pw.println("<a href=welcome?expectPage=" + (pageNow+1) + "> " + "Next" + "</a>");
                }
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}