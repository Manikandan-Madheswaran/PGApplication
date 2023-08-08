<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Transaction Data</title>
	
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
		<form action="<%=request.getContextPath()%>/MerchantHostedURPaymentTransaction" method="post">
			<h1 align="center" style="background-color: #04AA6D;">Merchant Hosted</h1>
			<table border="1" align="center">
				<tr>
					<td>Transaction Type </td>
					<td>
						<select name="action" id="action">
							<option value="1">Purchase</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Mobile Number</td>
					<td><input type="text" name="mobileNumber" id="mobileNumber" value=""></td>
				</tr>
				<tr>
					<td>Card Holder Name</td>
					<td><input type="text" name="cardHolderName" id="cardHolderName" value="Test" min="1" max="30"></td>
				</tr>
				<tr>
					<td>Amount</td>
					<td><input type="text" name="amount" id="amount" value="10.00"></td>
				</tr>
				<tr>
					<td>Currency Code</td>
					<td><input type="text" name="currency" id="currency" value="682"></td>
				</tr>
				<tr>
					<td>Response URL</td>
					<td><input type="text" name="responseURL" id="responseURL" value=""></td>
				</tr>
				<tr>
					<td>Error URL</td>
					<td><input type="text" name="errorURL" id="errorURL" value=""></td>
				</tr>
				<!-- <tr>
					<td>Language ID</td>
					<td><input type="text" name="langid" id="langid" value="en"></td>
				</tr> -->
				<tr>
					<td>Card Type</td>
					<td>
						<select name="cardType" id="cardType">
							<option value="C">Credit</option>
							<option value="D">Debit</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>UDF 1</td>
					<td><input type="text" name="udf1" id="udf1" value=""></td>
				</tr>
				<tr>
					<td>UDF 2</td>
					<td><input type="text" name="udf2" id="udf2" value=""></td>
				</tr>
				<tr>
					<td>UDF 3</td>
					<td><input type="text" name="udf3" id="udf3" value=""></td>
				</tr>
				<tr>
					<td>UDF 4</td>
					<td><input type="text" name="udf4" id="udf4" value=""></td>
				</tr>
				<tr>
					<td>UDF 5</td>
					<td><input type="text" name="udf5" id="udf5" value=""></td>
				</tr>
				<tr>
					<td>UDF 6</td>
					<td><input type="text" name="udf6" id="udf6" value=""></td>
				</tr>
				<tr>
					<td>UDF 7</td>
					<td><input type="text" name="udf7" id="udf7" value=""></td>
				</tr>
				<tr>
					<td>UDF 8</td>
					<td><input type="text" name="udf8" id="udf8" value=""></td>
				</tr>
				<tr>
					<td>UDF 9</td>
					<td><input type="text" name="udf9" id="udf9" value=""></td>
				</tr>
				<tr>
					<td>UDF 10</td>
					<td><input type="text" name="udf10" id="udf10" value=""></td>
				</tr>
			</table>
			<br/>
			<div style="text-align:center">  
    				<input type="submit" value="submit" style="text-align:center;background-color: #f2f2f2">
					<input type="submit" value="cancel" style="text-align:center;background-color: #f2f2f2">  
			</div>
		</form>
	</body>
</html>