package by.intervale.task.controller;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;


import org.xml.sax.SAXException;


import by.intervale.task.bean.Item;

import by.intervale.task.dao.xml.ItemsDAO;
import by.intervale.task.dao.xml.AuditDAO;


public final class MainController extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		doGet(req, res);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
		PrintWriter answer = res.getWriter();

		String action = req.getParameter("action");
		ItemsDAO itemsDAO = new ItemsDAO(getResource("/res/todo.xml"));
		AuditDAO auditDAO = new AuditDAO(getResource("/res/audit.xml"));
		Item item = buildItem(req);

		try {
			if ("create".equals(action)) {
				itemsDAO.create(item);
			} else if ("edit".equals(action)) {
				itemsDAO.edit(item);
			} else if ("delete".equals(action)) {
				itemsDAO.delete(item);
			}
			auditDAO.add(action, item);
			answer.println("{success:true,msg:'action success'}");
		} catch (IOException e) {
			handleException(e, answer);
		} catch (ParserConfigurationException e) {
			handleException(e, answer);
		} catch (SAXException e) {
			handleException(e, answer);
		} catch (TransformerException e) {
			handleException(e, answer);
		}
	}

	private Item buildItem(HttpServletRequest req) {
		String id = req.getParameter("id");
		String date = req.getParameter("date");
		String name = req.getParameter("name");
		String status = req.getParameter("status");
		String description = req.getParameter("description");
		return new Item(id, date, name, status, description);
	}

	private File getResource(String resourcePath) {
		ServletContext context = this.getServletContext();
		String todoXmlPath = context.getRealPath(resourcePath);
        return new File(todoXmlPath);
	}

	private void handleException(Exception e, PrintWriter answer) {
		System.out.println(e);
		e.printStackTrace();
		answer.println("{success:false,msg:'action error'}");
	}
}