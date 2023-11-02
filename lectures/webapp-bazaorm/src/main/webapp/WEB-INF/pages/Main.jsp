<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <body>

  <c:if test="${errorMessage!=null}">
	<h1><c:out value="${errorMessage}"/></h1>
	<p>Try Again.</p>
  </c:if>
  	<form  action="?" method="POST">
  		<label>Nickname: </label>
  		<input type="text" name="nick" value="${errorNick}">
  		<br>
  		<label> Password: </label>
  		<input type="password" name="psswd">
  		<br>
  		<button type="submit">Login</button>
  		<button type="reset">Reset</button>
  	</form>
	<a href="./register">Register now!</a>
	<a href="./author">See authors.</a>
  </body>
</html>