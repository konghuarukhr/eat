<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.SortedSet,java.util.TreeSet,com.yourmall.service.eat.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每周预定</title>
</head>
<body>
<p align="right"><a href="/eat/today">今日订餐</a></p>
<%
String today = (String) request.getAttribute("today");
OrderManager om = (OrderManager) request.getAttribute("om");
String[] week = FutureDate.getWeek(today);
SortedSet<String> names = new TreeSet<String>();
for (String day : week) {
	SortedSet<Order> dayOrders = om.getDateOrders(day);
	for(Order ord : dayOrders) {
		names.add(ord.getName());
	}
}
out.println("<br />");
out.println("<br />");
out.println("<br />");
out.println("<br />");
for (String name : names) {
	out.println("<label>" + name + "</label>");
	//out.println("<hr />");
	
	out.println("<form action=\"/eat/future?name=" + name + "\" method=\"post\">");
	
	out.println("<table border=\"1\">");
	
	out.println("<tr>");
	for (String day : week) {
		out.print("<td>" + day + "</td>");
	}
	out.println();
    out.println("</tr>");
	
	out.println("<tr>");
	int i = 0;
	for (String day : week) {
		out.print("<td>");
        out.print("<input type=\"checkbox\" name=\"date\" value=\"" + day + "\"");
        if (om.contains(new Order(name, day))) {
            out.print(" checked=\"checked\"");
        }
        if (day.compareTo(today) <= 0) {
            out.print(" disabled=\"disabled\"");
        }
        out.print(" />" + "(" + FutureDate.getDayName(i) + ")");
        out.print("</td>");
        i++;
    }
	out.println();
	out.println("</tr>");
	
	out.println("</table>");
	
	out.println("<input type=\"submit\" value=\"提交\">");
	out.println("</form>");
	
	out.println("<br />");
	out.println("<br />");
}
%>
</body>
</html>