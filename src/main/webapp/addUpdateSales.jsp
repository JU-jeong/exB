<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, exC.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%
String quantity = request.getParameter("quantity");
String product_code = request.getParameter("product_code");
String product_name = request.getParameter("product_name");
String price = request.getParameter("price");
UserVO listProduct = new UserVO(product_code, product_name, price);
UserDAO userDAO = new UserDAO();
List<UserVO> productsList = userDAO.listProducts();
List<SalesVO> salesList = userDAO.listSales();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
String currentDate = sdf.format(new Date());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>売上登録</title>
</head>
<body>
	<h1>売上登録</h1>
	<form action="UserController?action=addUpdateSales" method="post">
		<label>売上日: <%=currentDate%><br> <label>商品名</label><br>
			<select name="productList" id="productList">
				<option>----- 商品名 -----</option>
				<%
				for (int i = 0; i < productsList.size(); i++) {
					UserVO userVO = (UserVO) productsList.get(i);
				%>
				<option value="<%=userVO.getProduct_name()%>"><%=userVO.getProduct_name()%></option>
				<%
				}
				%>
		</select> <br> <label>数量:</label><br> <input type="text"
			name="salesQuantity"> <br> <input type="submit"
			value="追加" align="right">
	</form>
	<br>
	<br>
	---------------------------------------------------------------------------------
	<br>
	<br>
	<table border="1" cellpadding="5">
		<%
		for (int i = 0; i < salesList.size(); i++) {
			SalesVO salesVO = (SalesVO) salesList.get(i);
		%>
		<tr>
			<th>商品名</th>
			<th>数量</th>
		</tr>
		<tr border-collapse="collapse">
			<td align="center"><%=salesVO.getProduct_name()%></td>
			<td align="left"><%=salesVO.getQuantity()%></td>
		</tr>
		<%
		}
		%>
	</table>
	<br>
	<br>
	<h2></h2>
	<a href="index.jsp">初期画面</a>
	</h2>
</body>
</html>
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
