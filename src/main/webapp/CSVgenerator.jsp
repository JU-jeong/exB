<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CSVmaker</title>
</head>
<body>
	<h1>CSVダウンロード</h1>
	<br>
	<br>
	<form action="CSVController?action=generateCSVall" method="post">
		<input type="hidden" name="csvGenerateall" value="csvGenerateall">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" value="商品別売上集計CSV" align="right">
		</form>
	<br>
	<br>
	<form action="CSVController?action=generateCSVdate" method="post">
		<label style="font-size: 12pt;">年月:</label> 
		<input type="month" name="salesDate">
		<input type="hidden" name="csvGeneratedate" value="csvGeneratedate">
		<input type="submit" value="指定年月商品別売上集計CSV" align="right">
	</form>
	<br>
	<br>	
	<%
String nullError = (String) session.getAttribute("nullError");
if (nullError != null) {
%>
<script type="text/javascript">
            alert('<%=nullError%>');
	</script>
<%
System.out.println(nullError);
session.removeAttribute("nullError"); 
}
%>
	<br>
	<br>
	<h2></h2>
	<a href="index.jsp">初期画面</a>
	</h2>
</body>
</html>
