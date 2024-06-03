<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, exC.*" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>	
<%
request.setCharacterEncoding("UTF-8");
String product_code = request.getParameter("product_code");
int productCodeInt = Integer.parseInt(product_code);
String product_code_list = String.format("%03d", productCodeInt);
String product_name = request.getParameter("product_name");
String price = request.getParameter("price");
UserDAO userDAO = new UserDAO();
UserVO listProduct = new UserVO(product_code, product_name, price);
List<UserVO> productsList = userDAO.listProducts();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品変更・削除</title>
</head>
<body>
	<h1>商品変更・削除</h1>
	<br>
	<br>
	<label style="font-size: 20pt;">商品コード: <%=product_code_list%></label>
	<br>
	<br>
	<form action="UserController?action=updateProduct" method="post">
		<input type="hidden" name="product_code" value="<%=product_code%>">
		<label style="font-size: 20pt;">商品名:</label> <input type="text"
			name="UpdateProductName"> <br> <br> <label
			style="font-size: 20pt;">単価: <input type="text"
			name="UpdateProductPrice"></label> <br> <br> <input
			type="submit" value="変更" align="left">
	</form>

	<form action="UserController?action=deleteProduct" method="post">
		<input type="hidden" name="product_code" value="<%=product_code%>">
		<input type="hidden" name="deleteProduct" value="deleteProduct">
		<input type="submit" value="削除" align="right"> <br>
	</form>
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
	<br>
	<br>
	<h2></h2>
	<a href="index.jsp">初期画面</a>
	</h2>
</body>
</html>