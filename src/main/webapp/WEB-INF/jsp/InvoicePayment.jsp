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
		<form action="<%=request.getContextPath()%>/InvoicePaymentTransaction" method="post">
			<h1 align="center" style="background-color: #04AA6D;">Merchant Hosted</h1>
			<table border="1" align="center">
				<tr>
					<td>Invoice Type</td>
					<td>
						<select name="invoiceType" id="invoiceType">
							<option value="O">Open Invoice</option>
							<option value="D">Dedicated Invoice</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Currency Code</td>
					<td><input type="text" name="currency" id="currency" value="682"></td>
				</tr>
				<tr>
					<td>Invoice ID</td>
					<td><input type="text" name="invoiceId" id="invoiceId" value="" placeholder="Invoice Payment ID"></td>
				</tr>
				<tr>
					<td>Invoice Description</td>
					<td><input type="text" name="itemDesc" id="itemDesc" value="" placeholder="Invoice Description"></td>
				</tr>
				<tr>
					<td>Buyer Name</td>
					<td><input type="text" name="buyerName" id="buyerName" value="" min="1" max="50"></td>
				</tr>
				<tr>
					<td>Amount</td>
					<td><input type="text" name="amount" id="amount" value="1.00"></td>
				</tr>
				<tr>
					<td>Email ID</td>
					<td><input type="email" name="email" id="email" value=""></td>
				</tr>
				<tr>
					<td>Mobile Number</td>
					<td><input type="text" name="mobile" id="mobile" value=""></td>
				</tr>
				<tr>
					<td>Expiry Date</td>
					<td><input type="text" name="expiryDate" id="expiryDate" value="" placeholder="dd-MM-yyyy hh:mm:ss"></td>
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