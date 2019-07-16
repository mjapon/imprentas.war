package org.imprentas.sys.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/DescargaReportServlet")
public class DescargaReportServlet extends HttpServlet {

/*
 String filePath = "E:/Test/Download/MYPIC.JPG";
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
 */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String filePath = "/opt/jobsgenerados/";
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);

    }
}
