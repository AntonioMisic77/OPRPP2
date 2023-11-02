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
	<c:choose>
		<c:when test="${authors.size() == null}">
			<p> Wrong nick selected. </p>
		</c:when>
		<c:otherwise>
		    <p>Hey check out my page </p>
		
	         
			<c:forEach var="e" items="${authors}">
				<a href="./author/${e.nick}">${e.firstName} ${e.lastName}</a>
				<br>
			</c:forEach>
		</c:otherwise>
	</c:choose>
  </body>
</html>