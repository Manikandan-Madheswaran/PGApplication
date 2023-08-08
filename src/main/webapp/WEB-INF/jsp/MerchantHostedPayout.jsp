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
		<form action="<%=request.getContextPath()%>/MerchantHostedPayoutTransaction" method="post">
			<h1 align="center" style="background-color: #04AA6D;">Merchant Hosted</h1>
			<table border="1" align="center">
				<tr>
					<td>Transaction Type </td>
					<td>
						<select name="action" id="action">
							<option value="1">Purchase</option>
							<option value="4">Authorization</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Card Number</td>
					<td><input type="text" name="cardNumber" id="cardNumber" value="" min="13" max="19"></td>
				</tr>
				<tr>
					<td>Card Expiry </td>
					<td>
						<select name="expMonth" id="expMonth">
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
						</select>
						<select name="expYear" id="expYear">
							<option value="2021">2021</option>
							<option value="2022">2022</option>
							<option value="2023">2023</option>
							<option value="2024">2024</option>
							<option value="2025">2025</option>
							<option value="2026">2026</option>
							<option value="2027">2027</option>
							<option value="2028">2028</option>
							<option value="2029">2029</option>
							<option value="2030">2030</option>
							<option value="2031">2031</option>
							<option value="2032">2032</option>
							<option value="2033">2033</option>
							<option value="2034">2034</option>
							<option value="2035">2035</option>
							<option value="2036">2036</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>CVV</td>
					<td><input type="text" name="cvv" id="cvv" value="" onkeyup="if(value>999) value=999;"></td>
				</tr>
				<tr>
					<td>Card Holder Name</td>
					<td><input type="text" name="cardHolderName" id="cardHolderName" value="Merchant Demo" min="1" max="30"></td>
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
				<tr><td align="center" style="background-color: #04AA6D;" colspan="2">Payout List 1</td></tr>
				<tr>
					<td>Bank ID Code 1</td>
					<td><input type="text" name="bankIdCode1" id="bankIdCode1" value=""></td>
				</tr>
				<tr>
					<td>IBAN Number 1</td>
					<td><input type="text" name="iBanNum1" id="iBanNum1" value=""></td>
				</tr>
				<tr>
					<td>Service Amount 1</td>
					<td><input type="text" name="serviceAmount1" id="serviceAmount1" value=""></td>
				</tr>
				<tr>
					<td>Value Date 1</td>
					<td><input type="text" name="valueDate1" id="valueDate1" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary Name 1</td>
					<td><input type="text" name="benificiaryName1" id="benificiaryName1" value=""></td>
				</tr>
				<tr><td align="center" style="background-color: #04AA6D;" colspan="2">Payout List 2</td></tr>
				<tr>
					<td>Bank ID Code 2</td>
					<td><input type="text" name="bankIdCode2" id="bankIdCode2" value=""></td>
				</tr>
				<tr>
					<td>IBAN Number 2</td>
					<td><input type="text" name="iBanNum2" id="iBanNum2" value=""></td>
				</tr>
				<tr>
					<td>Service Amount 2</td>
					<td><input type="text" name="serviceAmount2" id="serviceAmount2" value=""></td>
				</tr>
				<tr>
					<td>Value Date 2</td>
					<td><input type="text" name="valueDate2" id="valueDate2" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary Name 2</td>
					<td><input type="text" name="benificiaryName2" id="benificiaryName2" value=""></td>
				</tr>
				<tr><td align="center" style="background-color: #04AA6D;" colspan="2">Payout List 3</td></tr>
				<tr>
					<td>Bank ID Code 3</td>
					<td><input type="text" name="bankIdCode3" id="bankIdCode3" value=""></td>
				</tr>
				<tr>
					<td>IBAN Number 3</td>
					<td><input type="text" name="iBanNum3" id="iBanNum3" value=""></td>
				</tr>
				<tr>
					<td>Service Amount 3</td>
					<td><input type="text" name="serviceAmount3" id="serviceAmount3" value=""></td>
				</tr>
				<tr>
					<td>Value Date 3</td>
					<td><input type="text" name="valueDate3" id="valueDate3" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary Name 3</td>
					<td><input type="text" name="benificiaryName3" id="benificiaryName3" value=""></td>
				</tr>
			</table>
			<div style="text-align:center">  
    				<input type="submit" value="submit" style="text-align:center;background-color: #f2f2f2">
					<input type="submit" value="cancel" style="text-align:center;background-color: #f2f2f2">  
			</div>
		</form>
	</body>
</html>