<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@page import="java.util.Random"%>

<html>
	<head>
		<title>Funny Story</title>
	</head>
	<%
		String[] colors = {"red","blue","white","black","green","brown"};
		Random random = new Random();
		
		Integer i = random.nextInt(5); 
	%>
	<body style="background-color:${pickedBgCol}">
			
			<p style="color:${colors[i]}"> 
				Once upon a time in the land of Codington, there was a peculiar programming language named Java. 
				Java was known for its strict rules and love for coffee. It had a reputation for being reliable, but sometimes it could be a little too serious.
				One day, Java decided it was time to have some fun and break free from its monotonous routine.
				It gathered all the other programming languages, including Python, JavaScript, and C++, for a hilarious coding competition.
				The competition started with a simple task: printing "Hello, World!" on the screen.
			    Python, being the mischievous language it was, quickly finished the task and added a friendly emoji to the end. 
			    The crowd burst into laughter, applauding Python's creative twist. Next, it was JavaScript's turn. 
			    JavaScript, always up for a good joke, decided to animate the text and make it bounce around the screen.
			    Everyone was mesmerized by the bouncing "Hello, World!" and couldn't help but chuckle.
                Then came C++, known for its complexity and precision. It started writing a long and elaborate code, defining classes, objects, and methods just to print those famous words. 
                The audience began to get impatient, wondering when C++ would finally finish. 
                But suddenly, a small glitch caused the program to crash, and the crowd erupted in laughter at the irony.
				Finally, it was Java's turn. Java took a deep breath, determined to show its playful side. 
				It started writing a simple code, but then it paused, thinking for a moment. With a sly smile, Java decided to add a surprise twist.
				As the code executed, a cup of steaming hot coffee appeared on the screen next to the "Hello, World!" message. 
				The crowd burst into laughter, knowing Java's obsession with coffee. 
				But that wasn't all. With each refresh, the cup of coffee magically refilled itself, creating an endless stream of caffeine-induced laughter.
				Java had successfully lightened the mood and brought joy to the coding competition.
				Everyone applauded and cheered for Java's witty sense of humor. From that day on, Java became known not only for its reliability but also for its ability to surprise and entertain.
				And so, in the land of Codington, the tale of Java's funny coding competition became legendary, reminding programmers that even in the world of strict rules and syntax, a little humor and creativity can go a long way.
			</p>
			
			<a href="../pages/index.jsp">Back to Home page.</a>
	</body>
</html>