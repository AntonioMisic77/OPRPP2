<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
	<head>
		<title>OS Report</title>
	</head>
	
	<body style="background-color:${pickedBgCol}">
			<h1>OS usage</h1>
			
			<p>
				Here are the results of OS usage in survey that we completed.
			</p>
			
			<img alt="chart" src="<%=request.getContextPath()%>/reportImage"> </img>
			
			<a href="./index.jsp">Back to Home Page</a>
	</body>

</html>