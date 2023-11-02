<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
 <head>
 	<style type="text/css">
 		table.rez td {text-align: center;}
 	</style>
 </head>
 <body>

 	<h1>Rezultati glasanja</h1>
 	<p>Ovo su rezultati glasanja.</p>
 	<table border="1" cellspacing="0" class="rez">
 	<thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
 	<tbody>
 		<c:forEach var="result" items="${results}">
 			<tr>
 				<td>${result.getName()}</td>
 				<td>${result.getVotes()}</td>
 			</tr>
 		</c:forEach>
 	
 	</tbody>
 	</table>
 	
 	<h2>Grafički prikaz rezultata</h2>
 	<img alt="Pie-chart" src="/glasanje-grafika" width="400" height="400" />
 	
 </body>
</html>