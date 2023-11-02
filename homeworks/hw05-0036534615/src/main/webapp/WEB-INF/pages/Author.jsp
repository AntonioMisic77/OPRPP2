<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <body>
  	<c:if test="${bla != null}">
  		<span>${bla}</span>
  		<a href="./logout">Logout</a>
  	</c:if>
  	<c:if test="${bla == null}">
  		<span>Not loged in</span>
  	</c:if>
  	
  	<br>
  	<br>
  	<br>
  	<p> Hey checkout my page </p>
	
	<c:forEach var="e" items="${user.getEntries()}">
		<p>${e.title}</p>
		<span>${e.text}</span>
		<br>
	</c:forEach>
	
	<p>Leave a message to me if you want <p>
	
	<c:forEach var="el" items="${user.getMessages()}">
	    <p>Some user said:  ${el.content}</p>
	</c:forEach>
	
	<form method="POST" action="?">
	   <input type="text" name="a">
	   <button type="submit">Send</button>
	</form>
	
  </body>
</html>