<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Property Status</title>
</head>
<body>
	<center>
		<table border="0">
			<tr>
				<td style="font-weight:bold">Message</td>
				<td style="color:red">${message}</td>
			</tr>
			<tr>
				<td style="font-weight:bold">Status</td>
				<td style="color:red">${status}</td>
			</tr>
		</table>
	</center>
	<div align="center">
		<br/>
		<a href="<%=request.getContextPath()%>/Home">Click Here to Go to the Home Page</a>
	</div>
</body>
</html>