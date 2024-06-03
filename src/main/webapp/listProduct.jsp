<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, exC.*" pageEncoding="UTF-8"%>
<%@ page session="false"%>
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
<title>商品検索</title>
</head>
<body>
	<h1>商品検索</h1>
	<br>
	<form action="UserController?action=searchProduct" method="post">
		<label font-size : 20pt>商品名</label> <input type="text"
			name="searchText"> <input type="submit" value="検索">
	</form>
	<br>
	<br>
	<table border="1";  border-collapse="collapse"; align="center"
		; width="100%">
		<tr align="center">
			<th>商品コード</th>
			<th>商品名</th>
			<th>単価</th>
			<th>操作</th>
		</tr>
		<%
		for (int i = 0; i < productsList.size(); i++) {
			UserVO userVO = (UserVO) productsList.get(i);
		%>
		<tr border-collapse="collapse">
			<td align="center"><%=userVO.getProduct_code()%></td>
			<td align="left"><%=userVO.getProduct_name()%></td>
			<td align="right"><%=userVO.getPrice()%></td>
			<td><A
				href="updateDeleteProduct.jsp?product_code=<%=userVO.getProduct_code()%>"
				target="blank"> 編集 </A></td>
		</tr>
		<%
		}
		%>
	</table>
	<br>
	<br>
	<h2></h2><a href="index.jsp">初期画面</a></h2>
</body>
</html>