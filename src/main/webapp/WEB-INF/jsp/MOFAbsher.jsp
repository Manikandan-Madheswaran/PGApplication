<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>MOF Transaction Data</title>
	
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
		  color: green;
		}
	</style>
	
	<script type="text/javascript">
	function serviceCodemethod(code)
	{
		if(code=='violation')
		{
			document.getElementById("violation").style.display='block';
			document.getElementById("sentence").style.display='none';
		}
		else if(code='sentence')
		{
			document.getElementById("sentence").style.display='block';
			document.getElementById("violation").style.display='none';
		}
		else if(code='normal')
		{
			document.getElementById("violation").style.display='none';
			document.getElementById("sentence").style.display='none';
		}
	}
	
	function ready()
	{
		document.getElementById("violation").style.display='none';
		document.getElementById("sentence").style.display='none';
	}
	</script>
	
</head>
	<body onload="javascript:ready();">
		<form action="<%=request.getContextPath()%>/MOFHostedTransaction" method="post">
			<h1 align="center" style="background-color: #04AA6D;">MOF Hosted Transaction</h1>
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
				<tr>
					<td>Sector ID</td>
					<td><input type="text" name="sectorID" id="sectorID" value=""></td>
				</tr>
				<tr>
					<td>Service Code</td>
					<td><input type="text" name="serviceCode" id="serviceCode" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary ID Type</td>
					<td><input type="text" name="beneficiaryIDType" id="beneficiaryIDType" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary ID</td>
					<td><input type="text" name="beneficiaryID" id="beneficiaryID" value=""></td>
				</tr>
				<tr>
					<td>Beneficiary Name</td>
					<td><input type="text" name="beneficiayName" id="beneficiayName" value=""></td>
				</tr>
				<tr>
					<td>Branch Code</td>
					<td><input type="text" name="branchCode" id="branchCode" value=""></td>
				</tr>
				<tr>
					<td>Service Code Type</td>
					<td>
						<select name="serviceCodeType" id="serviceCodeType" onchange="javascript:serviceCodemethod(this.value);">
							<option value="normal">Normal</option>
							<option value="violation">Violation</option>
							<option value="sentence">Sentence</option>
						</select>
					</td>
				</tr>
			</table>
			<div id="violation">
				<table border="1" align="center">
				<th style="color:green;" align="center" colspan="2">Violation Details</th>
					<tr>
						<td>Violation Count</td>
						<td><input type="text" name="violationCount" id="violationCount" value=""></td>
					</tr>
					<tr>
						<td>Violation ID</td>
						<td><input type="text" name="violationID" id="violationID" value=""></td>
					</tr>
					<tr>
						<td>Violation Amount</td>
						<td><input type="text" name="violationAmt" id="violationAmt" value=""></td>
					</tr>
				</table>
			</div>
			<div id="sentence">
				<table border="1" align="center">
				<th style="color:green" align="center" colspan="2">Sentence Details</th>
					<tr>
						<td>Number of Sentences</td>
						<td><input type="text" name="numberOfSentences" id="numberOfSentences" value=""></td>
					</tr>
					<tr>
						<td>Payment Inquiry Type</td>
						<td><input type="text" name="paymentInquiryType" id="paymentInquiryType" value=""></td>
					</tr>
					<tr>
						<td>Sentence Number</td>
						<td><input type="text" name="sentenceNumber" id="sentenceNumber" value=""></td>
					</tr>
					<tr>
						<td>Sentence Amount</td>
						<td><input type="text" name="sentenceAmt" id="sentenceAmt" value=""></td>
					</tr>
					<tr>
						<td>Installment Number</td>
						<td><input type="text" name="installmentNumber" id="installmentNumber" value=""></td>
					</tr>
				</table>
			</div>
			<div>
				<table>
				</table>
			</div>
			<br/>
			<div style="text-align:center">  
    				<input type="submit" value="submit" style="text-align:center;background-color: #f2f2f2">
					<input type="submit" value="cancel" style="text-align:center;background-color: #f2f2f2">  
			</div>
		</form>
	</body>
</html>