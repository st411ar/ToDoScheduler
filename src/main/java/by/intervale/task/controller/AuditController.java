package by.intervale.task.controller;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public final class AuditController extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		doGet(req, res);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
        res.setContentType("text/xml");
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
		ServletContext context = this.getServletContext();
		String todoXmlPath = context.getRealPath("/res/audit.xml");
        Scanner sc = new Scanner(new File(todoXmlPath), "UTF-8");
		PrintWriter answer = res.getWriter();
        while (sc.hasNext()) {
        	answer.println(sc.nextLine());
        }
        sc.close();
	}
}