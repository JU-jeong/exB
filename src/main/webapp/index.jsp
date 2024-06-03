<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*,exC.*" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<%
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index</title>
</head>
<body>
	<h1>課題</h1>
	<br>
	<h2><a href="listProduct.jsp">3-1 & 3-3</a></h2>
	<h2><a href="addProduct.jsp">3-2</a></h2>
	<h2><a href="addUpdateSales.jsp">3-4</a></h2>
	<h2><a href="CSVgenerator.jsp">3-5</a></h2>
	
	<br>
	<br>
	ウェブ ブラウザのリクエストを 1 つのサブレットに受け入れました。 サーブレットはウェブブラウザの要請をそのロジックに合わせて処理した後、その結果に該当するデータを表示するJSPページに転送します
	サーブレットはUserController.java、ロジック及びdbのアクセス操作はUserDAO.javaで行われるようにしました。
	また、dbとデータをやり取りするための値を入力するための二つのvoファイルが存在します。(UserVO、SalesVO)
	dbの場合、Bの2番に該当する2つのテーブルm_productとt_salesを使って課題を解決、3-4と3-5番の問題の場合、
	2つのテーブルのカラムを同時に必要とする要求条件の問題があったため、joinを使って第3テーブル（CSV_table）を作って解決しました。
	<br>
	問題のリンクをクリックすると、該当問題を具現した画面に移動し、inputのname値をformのactionを通じてコントローラの該当命令を作動させ、
	daoのロジックを通じてdbで値をやり取りするのが基本的な構造になっています。
</body>

<!-- 
	ウェブ ブラウザのリクエストを 1つのサブレットに受け入れました。 サーブレットはウェブブラウザの要請をそのロジックに合わせて処理した後、その結果に該当するデータを表示するJSPページに転送します
	サーブレットはUserController.java、ロジック及びdbのアクセス操作はUserDAO.javaで行われるようにしました。
	また、dbとデータをやり取りするための値を入力するための二つのvoファイルが存在します。(UserVO、SalesVO)
	dbの場合、Bの2番に該当する2つのテーブルm_productとt_salesを使って課題を解決、3-4と3-5番の問題の場合、
	2つのテーブルのカラムを同時に必要とする要求条件の問題があったため、joinを使って第3テーブル（CSV_table）を作って解決しました。	
	
	問題のリンクをクリックすると、該当問題を具現した画面に移動し、inputのname値をformのactionを通じてコントローラの該当命令を作動させ、
	daoのロジックを通じてdbで値をやり取りするのが基本的な構造になっています。
 -->