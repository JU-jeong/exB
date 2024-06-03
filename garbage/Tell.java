package section3;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Servlet implementation class ProductController
 */
@WebServlet("/rController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			try {
				doHandle(request, response);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		System.out.println("action:" + action);

		if (action == null || action.equals("/serController")) {
			String searchProduct = request.getParameter("searchText");
			if (searchProduct != null && !searchProduct.isEmpty()) {
				// 검색어를 이용하여 데이터베이스에서 검색
				System.out.println(request.getParameter("searchText"));
				List<UserVO> searchedProducts = userDAO.searchProducts(searchProduct);
				// 검색 결과를 request 속성에 저장
				request.setAttribute("searchedProducts", searchedProducts);
				System.out.println("됫다14");

			} else {
				List<UserVO> ProductsList = userDAO.listProducts();
				request.setAttribute("userVO", ProductsList);
				// System.out.println("됫다2");
				// System.out.println("searchedProducts");
				nextPage = "listSearchProduct.jsp";
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);

			}
			System.out.println("됫다13");
			// System.out.println("됫다2");
		}

		else if (action.equals("/serController")) {
			String id = request.getParameter("addProductName");
			String pwd = request.getParameter("addProductPrice");
			// UserVO userVO = new UserVO(id, pwd);
			userDAO.addProduct(id, pwd);
			// System.out.println("됫다3");
			nextPage = "listProduct.jsp";
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			System.out.println("됫다12");

		} else if (action.equals("/erController")) {
			String product_code = request.getParameter("productCode");
			String product_name = request.getParameter("UpdateProductName");
			String product_price = request.getParameter("UpdateProductPrice");
			userDAO.updateProduct(product_code, product_name, product_price);
			System.out.println("됫다11");

			if (product_name != null && product_price != null) {
				System.out.println("uhun5");
				nextPage = "listProduct.jsp";
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
				System.out.println("됫다10");

			} else {
				// 상품이 존재하지 않는 경우 또는 에러 처리
				System.out.println("uhun6");
				nextPage = "listProduct.jsp";
				System.out.println("됫다9");
			}

			String product_code2 = request.getParameter("productCode");
			String product_name2 = request.getParameter("UpdateProductName");
			String product_price2 = request.getParameter("UpdateProductPrice");
			userDAO.updateProduct(product_code, product_name, product_price);
			System.out.println("됫다8");

			if (product_name != null && product_price != null) {
				System.out.println("uhun3");
				nextPage = "listProduct.jsp";
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
				System.out.println("됫다7");

			} else {
				// 상품이 존재하지 않는 경우 또는 에러 처리
				System.out.println("uhun4");
				nextPage = "listProduct.jsp";
				System.out.println("됫다6");
			}
			System.out.println("됫다5");
		} else if (action.equals("/erController")) {
			System.out.println("됫다4");

			String product_code = request.getParameter("product_code");
			System.out.println(product_code);
			String product_name = request.getParameter("UpdateProductName");
			String product_price = request.getParameter("UpdateProductPrice");
			userDAO.updateProduct(product_name, product_price, product_code);
			System.out.println("됫다0");
			if (product_name != null && product_price != null) {
				System.out.println("uhun1");
				nextPage = "listProduct.jsp";
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
				System.out.println("됫다3");
			} else {
				// 상품이 존재하지 않는 경우 또는 에러 처리
				System.out.println("uhun2");
				nextPage = "listProduct.jsp";
				System.out.println("됫다2");
			}
			System.out.println("됫다1");
		}
		else if (action.equals("/addSales")) {
			System.out.println("3-4");
			String selectedProduct = request.getParameter("telecom");
			String salesQuantity = request.getParameter("salesQuantity");
			userDAO.addSales(selectedProduct, salesQuantity);
			nextPage = "addEditSales.jsp";
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		}
		else {
			System.out.println("3-6");
			//action equals 指定年月商品別売上集計CSV;
			String generateCSVdate = request.getParameter("salesDate");
			if(generateCSVdate != null) {
				userDAO.generateCSVdate(generateCSVdate);
				nextPage = "CSVgenerator.jsp";
				RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}else{
				return;
			}

		}if(action.equals("/deleteProduct")) {
		String deleteProduct = request.getParameter("product_code");
		userDAO.deleteProduct(deleteProduct);
		nextPage = "listProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		}
		else {
			System.out.println("3-5");
			nextPage = "CSVgenerator.jsp";
			String selectedProduct = request.getParameter("telecom");
			System.out.println(selectedProduct);
			String salesQuantity = request.getParameter("salesQuantity");
			System.out.println(salesQuantity);
			userDAO.addSales(selectedProduct, salesQuantity);
			nextPage = "addEditSales.jsp";
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			System.out.println("됫다-1");
		}
		System.out.println("3-5");
		userDAO.generateCSVall();
		nextPage = "CSVgenerator.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
}

/*
 * } else if (action.equals("/listSales.do")) { // 매상 목록 조회 List<SalesVO>
 * salesList = userDAO.listSales(); request.setAttribute("SalesList",
 * salesList); nextPage = "/test02/listSales.jsp"; } else if
 * (action.equals("/addSales.do")) { // 매상 등록 String salesDate =
 * request.getParameter("salesDate"); String productCode =
 * request.getParameter("productCode"); String quantity =
 * request.getParameter("quantity");
 *
 * if (salesDate != null && productCode != null && quantity != null &&
 * !salesDate.isEmpty() && !productCode.isEmpty() && !quantity.isEmpty()) { //
 * 입력값이 유효한 경우 매상 등록 userDAO.registerOrUpdateSales(salesDate, productCode,
 * quantity); // 등록 후 목록 페이지로 이동 nextPage = "/User/listSales.do"; } else { //
 * 입력값이 부족한 경우 에러 처리 또는 다시 입력폼으로 이동 nextPage = "/error.jsp"; } }
 *
 *
 * else { List<UserVO> ProductsList = userDAO.listProducts();
 * request.setAttribute("ProductsList", ProductsList); nextPage =
 * "/test02/listProducts.jsp"; } RequestDispatcher dispatch =
 * request.getRequestDispatcher(nextPage); dispatch.forward(request, response);
 * }
 *
 * }
 */