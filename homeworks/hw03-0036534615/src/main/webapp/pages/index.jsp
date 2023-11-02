<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>

<html>
  <head>
  	<title>HOME PAGE</title>
  </head>
  <body style="background-color:${pickedBgCol}">
  		<h1>HOME PAGE<h1>
  		
  		<p>
  			<a href="colors.jsp"> Background color chooser </a>
  		<p>
  		
  		<p>
  			<a href="../trigonometric?a=0&b=90">Calculate Trygonometric functions</a>
  			
  			<form action="../trigonometric" method="GET">
 				Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 				Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 				<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
			</form>
  		<p>
  		
  		<p>
   			<a href="../stories/funny.jsp">Read funny story here.</a>
  		</p>
  		
  		<a href="report.jsp">See Survey Results</a>
  		
  		<p>
  			<a href="appinfo.jsp">Application information.</a>
  		</p>
  		
  		
  </body>
</html>