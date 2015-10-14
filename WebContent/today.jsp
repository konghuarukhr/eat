<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.SortedSet,com.yourmall.service.eat.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>今日订餐</title>
</head>
<body>
<p align="right"><a href="/eat/history">历史订餐</a> <a href="/eat/future">每周预定</a></p>
<br />
<br />
<br />
<form action="/eat/today?action=insert" method="post">
       <p align="center"><label>订餐人： </label><input type="text" name="name" /> <input type="submit" value="提交" /></p>
</form>
<br />
<br />
<br />
<br />
<hr />
<%
String ret = (String) request.getAttribute("ret");
if (ret != null) {
	out.println("<script>");
	out.println("alert(\"" + ret + "\")");
	out.println("</script>");
}
SortedSet<Order> orders = (SortedSet<Order>) request.getAttribute("orders");
if (orders != null && !orders.isEmpty()) {
	out.println("<table>");
	for (Order order : orders) {
		out.println("<tr>");
		
		out.print("<td>");
		out.print("<label>");
		out.print(order.getName());
		out.print("</label>");
		out.println("</td>");
		
		out.print("<td>");
		out.print("<form action=\"/eat/today?action=delete&name=" + order.getName() + "\" method=\"post\">");
		out.print("<input type=\"submit\" value=\"取消订餐\" />");
		out.print("</form>");
		out.println("</td>");
		
		out.println("</tr>");
	}
	out.println("</table>");
}
%>
</body>
</html>