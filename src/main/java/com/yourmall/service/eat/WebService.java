package com.yourmall.service.eat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/")
@SuppressWarnings("serial")
public class WebService extends HttpServlet {
	private OrderManager om;
	private Properties props;
	
	@Override
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		om = (OrderManager) ctx.getAttribute("om");
		props = (Properties) ctx.getAttribute("props");
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log(req.getRequestURL().toString());
		
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowStr = df.format(now.getTime());
		
		String uri = req.getRequestURI();
		if (uri.equalsIgnoreCase("/eat/") || uri.equalsIgnoreCase("/eat/today")) {
			SortedSet<Order> orders = om.getDateOrders(nowStr);
			req.setAttribute("orders", orders);
			req.getRequestDispatcher("today.jsp").forward(req, resp);
		} else if (uri.equalsIgnoreCase("/eat/history")) {
			SortedSet<Order> orders = om.getDateOrdersBeforeRD(nowStr);
			req.setAttribute("orders", orders);
			req.getRequestDispatcher("history.jsp").forward(req, resp);
		} else if (uri.equalsIgnoreCase("/eat/future")) {
			req.setAttribute("today", nowStr);
			req.setAttribute("om", om);
			req.getRequestDispatcher("future.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		log(req.getRequestURL().toString());
		
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowStr = df.format(now.getTime());
		
		String uri = req.getRequestURI();
		if (uri.equalsIgnoreCase("/eat/today")) {
			String action = req.getParameter("action");
			String eatterName = req.getParameter("name");
			if (action != null) {
				if (eatterName != null && !eatterName.equals("")) {
					if (action.equalsIgnoreCase("insert")) {
						log("/today: insert " + eatterName + " on " + nowStr);
						String ret = Ordering.add(om, eatterName, props);
						if (ret != null) {
							log("/today: not insert: " + ret);
							req.setAttribute("ret", ret);
						}
					} else if (action.equalsIgnoreCase("delete")) {
						log("/today: delete " + eatterName + " on " + nowStr);
						String ret = Ordering.remove(om, eatterName, props);
						if (ret != null) {
							log("/today: not delete: " + ret);
							req.setAttribute("ret", ret);
						}
					}
				}
			}
			SortedSet<Order> orders = om.getDateOrders(nowStr);
			req.setAttribute("orders", orders);
			req.getRequestDispatcher("today.jsp").forward(req, resp);
		} else if (uri.equalsIgnoreCase("/eat/future")) {
			String eatterName = req.getParameter("name");
			String[] eattingDates = req.getParameterValues("date");
			if (eatterName != null && !eatterName.equals("")) {
				String[] week = null;
				try {
					week = FutureDate.getWeek(nowStr);
				} catch (ParseException e) {
					log("parse date failed", e);
				}
				for (String day : week) {
					if (day.compareTo(nowStr) > 0) {
						Ordering.remove(om, eatterName, day, props);
						log("/future: delete " + eatterName + " on " + day);
					}
				}
				if (eattingDates != null) {
					for (String eattingDate : eattingDates) {
						Ordering.add(om, eatterName, eattingDate, props);
						log("/future: insert " + eatterName + " on " + eattingDate);
					}
				}
			}
			req.setAttribute("today", nowStr);
			req.setAttribute("om", om);
			req.getRequestDispatcher("future.jsp").forward(req, resp);
		}
	}
}
