<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <body>

  	<form  action="?" method="POST">
  		<label>Firstname: </label>
  		<input type="text" name="fn">
  		<br>
  		<label> Lastname: </label>
  		<input type="text" name="ln">
  		<br>
  		<label> E-Mail: </label>
  		<input type="text" name="email">
  		<br>
  		<label> Nickname: </label>
  		<input type="text" name="nick">
  		<br>
  		<label> Password: </label>
  		<input type="password" name="psswd">
  		<br>
  		<button type="submit">Register</button>
  		<button type="reset">Reset</button>
  	</form>
  	<c:if test="${errAtt != null}">
  		<p>${errAtt}</p>
  		<span>TRY AGAIN.</span>
  	</c:if>
  	<c:if test="${errUsr != null}">
  		<p>${errUsr}</p>
  		<span>TRY AGAIN.</span>
  	</c:if>
  </body>
</html>