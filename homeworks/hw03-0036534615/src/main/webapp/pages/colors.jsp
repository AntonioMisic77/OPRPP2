<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  session="true" %>

<html>
  <head>
  	<title>Background Color chooser </title>
  </head>
  <body style="background-color:${pickedBgCol}">
  		<h1>Choose Background color<h1>
  	
  		<p>
  			<li><a href="../setcolor?color=white">WHITE</a></li>
  			<li><a href="../setcolor?color=red">RED</a></li>
  			<li><a href="../setcolor?color=green">GREEN</a></li>
  			<li><a href="../setcolor?color=cyan">CYAN</a></li>
  		<p>
  		
  		<span><a href="./index.jsp"> Return to Home Page</a></span>
  </body>
</html>