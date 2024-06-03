package exC;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 画面とdbの動作処理をつなぐコントローラの作成
 * 
 * @author ju_jeongseok
 * @throws ServletException 初期化に失敗した場合、ServletExceptionをスロー
 * @param クライアントの要求を含む HttpServletRequest オブジェクト
 * @param 応答を送信するための   HttpServletResponse オブジェクト
 * @throws ServletException サーブレット例外が発生した場合スロー
 * @throws IOException      エラーが発生した場合にスロー
 * @param クライアントの要求を含む HttpServletRequest オブジェクト
 * @param 応答を送信するための   HttpServletResponse オブジェクト
 * @throws ServletException サーブレット例外が発生した場合スロー
 * @throws IOException      エラーが発生した場合にスロー
 * @param クライアントの要求を含む HttpServletRequest オブジェクト
 * @param 応答を送信するための   HttpServletResponse オブジェクト
 * @throws ServletException       サーブレット例外が発生した場合スロー
 * @throws IOException            エラーが発生した場合にスロー
 * @throws ClassNotFoundException UserDAO クラスが見つからない場合にスロー
 * @throws SQLException           データベース アクセス エラーが発生した場合にスロー
 */
@WebServlet("/UserController")
public class UserController extends HttpServlet {
	/**
	 * Serializationのための設定
	 */
	private static final long serialVersionUID = 1L;
	UserDAO userDAO;

	/**
	 * doGet()、doPost()サービスメソッド呼び出しのためのinitメソッド
	 */
	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	/**
	 * doHandle使用のためのdoGetメソッド
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * doHandle使用のためのdoPostメソッド
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			try {
				doHandle(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get post方式を両方とも処理できるdoHandleの中で要請を受けられるように処理しました。
	 */
	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		RequestDispatcher dispatch = null;
		String action = request.getParameter("action");
		// System.out.println("action:" + action);
		/**
		 * 基本的な仕組みは、各jspのformの該当するactionコマンドをスイッチ文の値に設定し、これを操作して実行するように処理し、
		 * その中で値が必ず必要な場合の処理は値が正しい形で伝達された場合、命令を処理後再び指定ページに移動するように、
		 * 値が正しい形で伝達されなかった場合、指定ページに移動してエラーメッセージを表示するように処理しました。
		 */
		/**
		 *	jspから因子を受けて空白を除去した後、残る値を含む商品名をdaoにsearch Productsメソッド実行するよう要請して受け取るアクション
		 */
		switch (action) {
		case "searchProduct":
			String searchProduct = request.getParameter("searchText");
			searchProduct = searchProduct.strip();
			searchProduct = searchProduct.replaceAll("\\p{Z}", "");
			System.out.println(searchProduct);
			List<UserVO> searchedProducts = userDAO.searchProducts(searchProduct);
			request.setAttribute("searchedProducts", searchedProducts);
			nextPage = "listSearchProduct.jsp";
			dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			break;

		/**
		 *	jspから印字を受け、新しい商品名と価格を持つ商品をdbに登録するadd Productメソッドをdaoで実行するよう要請するアクション
		 *	dbに同名の商品が存在すると、エラーメッセージを呼び出す
		 *	値が正しく入力されていない場合、エラーメッセージを呼び出す。
		 */
		case "addProduct":
			String productName = request.getParameter("addProductName");
			String productPrice = request.getParameter("addProductPrice");
			boolean addProduct = userDAO.addProduct(productName, productPrice);
			if (addProduct) {
				nextPage = "listProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			} else {
				request.getSession().setAttribute("duplicateError", "既に存在する商品又は正しい形の値を入力されておりません。");
				HttpSession session = request.getSession(false);
				nextPage = "addProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}
			break;

		/**
		* jsp から印字を受け、当該商品コードを持つ商品の名前と情報を変更するupdateProductメソッドをdao で実行するよう要請するアクション、
		* 販売db に商品の販売履歴が存在すると、修正できない。
		* 値が正しく入力されていない場合、エラーメッセージを呼び出す。
		*/
		case "updateProduct":
			String product_code = request.getParameter("product_code");
			String product_name = request.getParameter("UpdateProductName");
			String product_price = request.getParameter("UpdateProductPrice");
			boolean updateProduct = userDAO.updateProduct(product_code, product_name, product_price);
			if (updateProduct) {
				nextPage = "listProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			} else {
				request.getSession().setAttribute("duplicateError", "既に販売中の商品ですので修正できない又は正しい形の値を入力されておりません。");
				HttpSession session = request.getSession(false);
				nextPage = "updateDeleteProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}
			break;

		/**
		* jsp から印字を受け、当該商品コードを持つ商品を削除するdelete Product メソッドをdao で実行するよう要請するアクション、
		* 販売db に商品の販売履歴が存在すると、削除できない。
		*/
		case "deleteProduct":
			String delete_code = request.getParameter("product_code");
			boolean deleteProduct = userDAO.deleteProduct(delete_code);
			if (deleteProduct) {
				nextPage = "listProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			} else {
				request.getSession().setAttribute("duplicateError", "既に販売中の商品ですので解除できません。");
				HttpSession session = request.getSession(false);
				nextPage = "updateDeleteProduct.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}
			break;

		/**
		* jspから印字を受け、当該商品と販売量に応じた販売履歴を生成してdbに保存するaddUpdateSalesメソッドをdaoで実行するよう要請するアクション
		*  値が正しく入力されていない場合、エラーメッセージを呼び出す。
		*/
		case "addUpdateSales":
			String selectedProduct = request.getParameter("productList");
			String salesQuantity = request.getParameter("salesQuantity");
			boolean addUpdateSales = userDAO.addUpdateSales(selectedProduct, salesQuantity);
			if (addUpdateSales) {
				nextPage = "addUpdateSales.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			} else {
				request.getSession().setAttribute("nullError", "正しい形の値を入力されておりません。");
				HttpSession session = request.getSession(false);
				nextPage = "addUpdateSales.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}
			break;

		/*
		 * case "generateCSVdate": 
		 * String generateCSVdate1 = request.getParameter("salesDate"); 
		 * boolean generateCSVdate = userDAO.generateCSVdate(generateCSVdate1); 
		 * 	if (generateCSVdate) {
		 * response.setHeader("Cache-Control", "no-cache");
		 * response.addHeader("Content-disposition", "attachment; fileName=" +
		 * generateCSVdate); nextPage = "CSVgenerator.jsp"; dispatch =
		 * request.getRequestDispatcher(nextPage); dispatch.forward(request, response);
		 * } else { 
		 * request.getSession().setAttribute("nullError","正しい形の値を入力されておりません。"); 
		 * HttpSession session = request.getSession(false);
		 * nextPage = "CSVgenerator.jsp"; 
		 * dispatch = request.getRequestDispatcher(nextPage); 
		 * dispatch.forward(request, response);
		 * } break;
		 
		case "generateCSVall":
			userDAO.generateCSVall();
			nextPage = "CSVgenerator.jsp";
			dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			break;
		}
		*/
		}
	}
}