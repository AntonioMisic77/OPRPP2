<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" session="true"%>
<%@page import="java.util.concurrent.TimeUnit"%>


<html>
	<head>
		<title>Application Information</title>
	</head>
	<body style="background-color:${pickedBgCol}">
			<% long start = (Long) application.getAttribute("time"); %>
			<% long end =  System.currentTimeMillis(); %>
			
			<%
				long time = end - start;
				
				long days = TimeUnit.MILLISECONDS.toDays(time);
				time -= TimeUnit.DAYS.toMillis(days);
				long hours = TimeUnit.MILLISECONDS.toHours(time);
				time -= TimeUnit.HOURS.toMillis(hours);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
				time -= TimeUnit.MINUTES.toMillis(minutes);
				long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
				time -= TimeUnit.SECONDS.toMillis(seconds);
			
			%>
			<span> 
				Aplication is up.
				
				<%= days %> days <%= hours %> hours <%= minutes %> minutes <%= seconds %> second  <%= time %> miliseconds	
			</span>
			
			<p>
				<a href="<%= request.getContextPath()%>/pages/index.jsp">Back to Home Page  </a>	
			</p>
			
	</body>
</html>