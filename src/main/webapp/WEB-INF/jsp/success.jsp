<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Response Page</title>
	
	<style type="text/css">
		table {
		  border-collapse: collapse;
		  width: 50%;
		}
		
		th, td {
		  text-align: left;
		  padding: 8px;
		  font-family:"Lucida Console";
		}
		
		tr:nth-child(even){background-color: #f2f2f2}
		
		th {
		  background-color: #04AA6D;
		  color: white;
		}
	</style>
</head>
	<body>
			<h1 align="center" style="background-color: #04AA6D;">Transaction Response</h1>
			<table border="1" align="center">
				<tr>
					<td>Transaction Status </td>
					<td>${response.result}</td>
				</tr>
				<tr>
					<td>Transaction ID</td>
					<td>${response.transId}</td>
				</tr>
				<tr>
					<td>Merchant Track ID</td>
					<td>${response.trackId}</td>
				</tr>
				<tr>
					<td>Payment ID</td>
					<td>${response.paymentId}</td>
				</tr>
				<tr>
					<td>Authorization Code</td>
					<td>${response.authCode}</td>
				</tr>
				<tr>
					<td>Response Code</td>
					<td>${response.authRespCode}</td>
				</tr>
				<tr>
					<td>Card Number</td>
					<td>${response.card}</td>
				</tr>
				<tr>
					<td>Expiry Month</td>
					<td>${response.expMonth}</td>
				</tr>
				<tr>
					<td>Expiry Year</td>
					<td>${response.expYear}</td>
				</tr>
				<tr>
					<td>Action Code</td>
					<td>${response.actionCode}</td>
				</tr>
				<tr>
					<td>Original Transaction ID</td>
					<td>${response.origTransactionID}</td>
				</tr>
				<tr>
					<td>Payment Time Stamp</td>
					<td>${response.paymentTimestamp}</td>
				</tr>
				<tr>
					<td>Post Date</td>
					<td>${response.date}</td>
				</tr>
				<tr>
					<td>Customer ID</td>
					<td>${response.custid}</td>
				</tr>
				<tr>
					<td>Card On File Token</td>
					<td>${response.cardonFileToken}</td>
				</tr>
				<tr>
					<td>Masked card Number</td>
					<td>${response.maskedCardNo}</td>
				</tr>
				<tr>
					<td>UDF 1</td>
					<td>${response.udf1}</td>
				</tr>
				<tr>
					<td>UDF 2</td>
					<td>${response.udf2}</td>
				</tr>
				<tr>
					<td>UDF 3</td>
					<td>${response.udf3}</td>
				</tr>
				<tr>
					<td>UDF 4</td>
					<td>${response.udf4}</td>
				</tr>
				<tr>
					<td>UDF 5</td>
					<td>${response.udf5}</td>
				</tr>
				<tr>
					<td>UDF 6</td>
					<td>${response.udf6}</td>
				</tr>
				<tr>
					<td>UDF 7</td>
					<td>${response.udf7}</td>
				</tr>
				<tr>
					<td>UDF 8</td>
					<td>${response.udf8}</td>
				</tr>
				<tr>
					<td>UDF 9</td>
					<td>${response.udf9}</td>
				</tr>
				<tr>
					<td>UDF 10</td>
					<td>${response.udf10}</td>
				</tr>
			</table>
			<div align="center">
				<br/>
				<a href="<%=request.getContextPath()%>/Home">Click Here to Go to the Home Page</a>
			</div>
	</body>
</html>