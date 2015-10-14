<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.SortedSet,com.yourmall.service.eat.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史订餐</title>
</head>
<body>
<p align="right"><a href="/eat/today">今日订餐</a></p>
<%
SortedSet<Order> orders = (SortedSet<Order>) request.getAttribute("orders");
String lastDate = "";
if (orders != null && !orders.isEmpty()) {
    for (Order order : orders) {
    	if (!order.getDate().equals(lastDate)) {
    		lastDate = order.getDate();
    		out.println("<br />");
    		out.println("<br />");
    		out.println("<br />");
    		out.println("<br />");
    		out.println(order.getDate());
    		out.println("<hr />");
    	} else {
    	    out.println("<br />");
    	}
        out.println(order.getName());
        
    }
}
%>
</body>
</html>