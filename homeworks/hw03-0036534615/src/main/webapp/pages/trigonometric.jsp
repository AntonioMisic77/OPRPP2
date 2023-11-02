<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
	<head>
		<title>Trigonometic Results</title>
	</head>
	
	<body style="background-color:${pickedBgCol}">
		<table>
			<tr>
				<th>Angle</th>
				<th>Sin(x)</th>
				<th>Cos(x)</th>
			</tr>
			<c:forEach var="result" items="${results}">
				<tr>
				  <th> ${result.getAngle()} </th>
				  <th> ${result.getSinus()} </th>
				  <th> ${result.getCosinus()} </th>
				</tr>
			</c:forEach>
		</table>
		
		<a href="./pages/index.jsp">Back to Home page.</a>
	</body>

</html>