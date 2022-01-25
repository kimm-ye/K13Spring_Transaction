<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>buyTicketMain.jsp</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
	<form action="buyTicketAction.do" method="post" name="ticketFrm">
	<!-- table>tr*3>td*2 -->
	<table class="table table-bordered" style="width:500px;">
		<tr>
			<td>고객아이디</td>
			<td>
				<input type="text" name="customerId" />
			</td>
		</tr>
		<tr>
			<td>티켓구매수</td>
			<td>
				<select name="amount">
				<%
				for(int i=1 ; i<=10 ; i++){
				%>
					<option value="<%=i%>"><%=i%>장</option>
				<%} %>
				</select>				
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<button type="submit" class="btn 
					btn-warning">구매하기</button>
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>