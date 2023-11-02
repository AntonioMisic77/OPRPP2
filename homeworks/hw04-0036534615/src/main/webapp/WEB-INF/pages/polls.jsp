<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
	<head>
		<title>Choose Pool</title>
	</head>
	
	<body>
		<h1>Voting app</h1>
 		
 		<ol>
 			<c:forEach var="poll" items="${polls}">
 				<li><a href="glasanje?pollID=${poll.getId()}">${poll.getTitle()}</a></li>
 			</c:forEach>	
	    </ol>
	<body>
</html>