<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home Page</title>
</head>
<body>
<div align="center">
	<h1 align="center" style="background-color: #04AA6D;">Transaction Types</h1>
	<br/>
	<a href="<%=request.getContextPath()%>/BankHosted">Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MOFAgencies">MOF Phase One</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MOFAbsher">MOF Phase Two</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MerchantHosted">Merchant Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/Supporting">Supporting Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/BankHostedBillPayment">Sadad BillPay Bank Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/BankHostedPayout">Payout Bank Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MerchantHostedBillPayment">Sadad BillPay Merchant Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MerchantHostedPayout">Payout Merchant Hosted Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/MerchantHostedURPayment">Merchant Hosted URPayment Transaction</a><br/><br/>
	<a href="<%=request.getContextPath()%>/InvoicePayment">Invoice Payment Transaction</a><br/><br/>
</div>
</body>
</html>