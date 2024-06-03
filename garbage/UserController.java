package exC;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* Servlet implementation class ProductController
*/
@WebServlet("UserController")
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

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		System.out.println("action:" + action);
		String nextPage = null;
		nextPage = "listSearchProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		/*
		switch (action) {
		case "listProduct":
			listProduct(request, response);
			break;
		case "searchProduct":
			searchProduct(request, response);
			break;
		case "addProduct":
			addProduct(request, response);
			break;
		case "updateProduct":
			updateProduct(request, response);
			break;
		case "deleteProduct":
			deleteProduct(request, response);
			break;
		case "addSales":
			addSales(request, response);
			break;
		case "addUpdateSales":
			addUpdateSales(request, response);
			break;
		case "generateCSVdate":
			generateCSVdate(request, response);
			break;
		case "generateCSVall":
			generateCSVall(request, response);
			break;
		default:
			throw new IllegalArgumentException("Invalid action: " + action);
			*/
		}
	}
/*
	private void listProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		// Handle /erController action

		List<UserVO> ProductsList = userDAO.listProducts();
		request.setAttribute("userVO", ProductsList);
		// System.out.println("됫다2");
		// System.out.println("searchedProducts");

	}

	private void searchProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		// Handle /serController action
		// 검색어를 이용하여 데이터베이스에서 검색
		String searchProduct = request.getParameter("searchText");
		System.out.println(request.getParameter("searchText"));
		List<UserVO> searchedProducts = userDAO.searchProducts(searchProduct);
		// 검색 결과를 request 속성에 저장
		request.setAttribute("searchedProducts", searchedProducts);
		nextPage = "listSearchProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	private void addProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		// Handle /rController action
		System.out.println("asdlfjasldkfja;dslkfj");
		String id = request.getParameter("addProductName");
		String pwd = request.getParameter("addProductPrice");
		// UserVO userVO = new UserVO(id, pwd);
		userDAO.addProduct(id, pwd);
		// System.out.println("됫다3");
		nextPage = "listProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		System.out.println("됫다12");

	}

	private void updateProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		String product_code = request.getParameter("productCode");
		String product_name = request.getParameter("UpdateProductName");
		String product_price = request.getParameter("UpdateProductPrice");
		userDAO.updateProduct(product_code, product_name, product_price);
		System.out.println("됫다11");
		System.out.println("uhun5");
		nextPage = "listProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);

	}

	private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		String deleteProduct = request.getParameter("product_code");
		userDAO.deleteProduct(deleteProduct);
		nextPage = "listProduct.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	private void addUpdateSales(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String selectedProduct = request.getParameter("productList");
		System.out.println(selectedProduct);
		String salesQuantity = request.getParameter("salesQuantity");
		System.out.println(salesQuantity);
		userDAO.addSales(selectedProduct, salesQuantity);
		nextPage = "addEditSales.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	private void addSales(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		System.out.println("3-4");
		String selectedProduct = request.getParameter("telecom");
		String salesQuantity = request.getParameter("salesQuantity");
		userDAO.addSales(selectedProduct, salesQuantity);
		nextPage = "addEditSales.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	private void generateCSVdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
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
	}

	private void generateCSVall(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		userDAO.generateCSVall();
		nextPage = "CSVgenerator.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
	*/
}