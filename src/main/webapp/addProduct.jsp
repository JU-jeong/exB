<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, exC.*" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<%
String product_code = request.getParameter("product_code");
String product_name = request.getParameter("product_name");
String price = request.getParameter("price");
UserVO listProduct = new UserVO(product_code, product_name, price);
UserDAO userDAO = new UserDAO();
List<UserVO> productsList = userDAO.listProducts();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品登録</title>
</head>
<body>
	<h1>商品登録</h1>
	<br>
	<form action="UserController?action=addProduct" method="post">
		<input type="hidden" name="addProduct" value="addProduct"><label
			style="font-size: 20pt;">商品名:</label> <input type="text"
			name="addProductName"><br> <br> <label
			style="font-size: 20pt;">単価: <input type="text"
			name="addProductPrice"></label> <br> <br> <input
			type="hidden" name="addProduct" value="addProduct"> <input
			type="submit" value="登録" align="right"> <br>
	</form>
	<br>
	<br>
	<h2></h2>
	<a href="index.jsp">初期画面</a>
	</h2>
</body>
</html>
<%
String duplicateError = (String) session.getAttribute("duplicateError");
if (duplicateError != null) {
%>
<script type="text/javascript">
            alert('<%=duplicateError%>');
	</script>
<%
System.out.println(duplicateError);
session.removeAttribute("duplicateError"); 
}
%>