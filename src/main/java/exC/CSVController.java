package exC;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
/**
 * CSV 生成要求を処理するサーブレット。 HTTP POST 要求を処理して、特定の月と年の販売データの CSV レポートを生成します。 生成された
 * CSV ファイルは、HTTP 応答でダウンロード可能な添付ファイルとして送信されます。
 * 
 * @author ju_jeongseok
 * @param request  クライアントの要求
 * @param response クライアントに返信する応答
 * @servletException をスロー（サーブレット例外が発生した場合）
 * @I/O エラーが発生した場合に IOException をスロー
 * @param request  クライアントの要求
 * @param response クライアントに返信する応答
 * @servletException をスロー（サーブレット例外が発生した場合）
 * @I/O エラーが発生した場合に IOException をスロー
 * @param request  sales date パラメータを含むクライアントの要求
 * @param response CSV レポートの生成後にクライアントに返信する応答
 * @throws servletException をスローする（サーブレット例外が発生した場合）
 * @throws I/OException エラーが発生した場合に IOException をスロー
 * @throws ClassNotFoundException が発生した場合 JDBCドライバクラスが見つからない時スロー
 * @throws SQLException データベース アクセス エラーが発生した場合に SQLException をスロー
 */
@WebServlet("/CSVController")
public class CSVController extends HttpServlet {
	/**
	 * データベース接続パラメータ
	 */
	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	String url = "jdbc:mysql://localhost:3306/exB_01?characterEncoding=UTF-8&serverTimezone=UTC";
	String user = "root";
	String password = "tiger";
	/**
	 * UserDAO クラスのコンストラクタ。 JNDIから取得したデータ ソースを使用して、データベースへの接続を設定
	 * これにより、アプリケーションは手動で接続を作成するのではなく、コンテナが管理する接続プールを使用
	 * セットアップ中に例外が発生した場合、それらはキャプチャされ、標準出力に出力
	 */
	public CSVController() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/mysql");
		} catch (Exception e) {
			System.out.println("例外");
		}
	}
	/**
	 * HTTP GET 要求を doHandle メソッドに転送して処理
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	/**
	 * HTTP POST 要求を doHandle メソッドに転送して処理
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doHandle(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * HTTP POST 要求を処理して、特定の月および年の販売データの CSV レポートを生成 販売データはデータベースから取得され、CSV
	 * ファイルに書き込まれ、HTTP 応答でダウンロード可能な添付ファイルとして送信 
	 */
	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		RequestDispatcher dispatch = null;
		String action = request.getParameter("action");
		switch (action) {
		case "generateCSVdate":
			String generateCSVdate1 = request.getParameter("salesDate");
			if (generateCSVdate1.equals("")) {
				request.getSession().setAttribute("nullError", "正しい形の値を入力されておりません。");
				HttpSession session = request.getSession(false);
				nextPage = "CSVgenerator.jsp";
				dispatch = request.getRequestDispatcher(nextPage);
				dispatch.forward(request, response);
			}
			try {
				ResultSet rs = null;
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection(url, user, password);
				String[] salesDateSplit = generateCSVdate1.split("-");
				try {
					int salesYear = Integer.parseInt(salesDateSplit[0]);
					int salesMonth = Integer.parseInt(salesDateSplit[1]);
					String filePath = "C:\\Users\\ju_jeongseok\\Desktop\\csv_" + generateCSVdate1 + ".csv";
					String filePath2 = generateCSVdate1 + ".csv";
					String csvQueryhasDate = "select LPAD(product_code, 3, '0') AS product_code, product_name, price, quantity, amount from CSV_table where YEAR(sales_date) = ? AND MONTH(sales_date) = ? order by product_code asc;";
					pstmt = con.prepareStatement(csvQueryhasDate);
					pstmt.setInt(1, salesYear);
					pstmt.setInt(2, salesMonth);
					rs = pstmt.executeQuery();
					response.setHeader("Content-Type", "text/csv; charset=UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath2 + "\"");
					PrintWriter out = response.getWriter();
					out.write('\ufeff');
					out.write("商品コード,商品名,単価,数量,金額");
					out.write("\n");
					while (rs.next()) {
						String productCode = rs.getString("product_code");
						String productName = rs.getString("product_name");
						String price = rs.getString("price");
						String quantity = rs.getString("quantity");
						String amount = rs.getString("amount");
						out.write(productCode + "," + productName + "," + price + "," + quantity + "," + amount + "\n");
					}
					out.close();
					rs.close();
					pstmt.close();
					con.close();
					nextPage = "CSVgenerator.jsp";
					dispatch = request.getRequestDispatcher(nextPage);
					dispatch.forward(request, response);
				} catch (NullPointerException e) {
					e.printStackTrace();
					rs.close();
					pstmt.close();
					con.close();
					request.getSession().setAttribute("nullError", "正しい形の値を入力されておりません。");
					HttpSession session = request.getSession(false);
					nextPage = "CSVgenerator.jsp";
					dispatch = request.getRequestDispatcher(nextPage);
					dispatch.forward(request, response);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rs.close();
					pstmt.close();
					con.close();
					request.getSession().setAttribute("nullError", "正しい形の値を入力されておりません。");
					HttpSession session = request.getSession(false);
					nextPage = "CSVgenerator.jsp";
					dispatch = request.getRequestDispatcher(nextPage);
					dispatch.forward(request, response);
				}
			} catch (ClassNotFoundException | SQLException | IOException | ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			break;		
			
		/*
		* jspでボタンが押された場合、
		* 全商品の販売履歴を含むcsvファイルを生成してドライブにダウンロードするアクション
		* すべての販売データのCSVファイルを生成し、レポートには製品コード、名前、価格、数量および金額が含まれる。
		*/
		case "generateCSVall":
			try {
				ResultSet rs = null;
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection(url, user, password);
				String filePath = "C:\\Users\\ju_jeongseok\\Desktop\\csv_all.csv";
				String filePath2 = "csv_all.csv";
				File file = new File(filePath);
				response.setHeader("Content-Type", "text/csv; charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath2 + "\"");
				PrintWriter out = response.getWriter();
				out.write('\ufeff');
				out.write("商品コード,商品名,単価,数量,金額");
				out.write("\n");
				String csvQuerySales = "select LPAD(product_code, 3, '0') AS product_code, product_name, price, quantity, amount from CSV_table order by product_code asc;";
				pstmt = con.prepareStatement(csvQuerySales);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String productCode = rs.getString("product_code");
					String productName = rs.getString("product_name");
					String price = rs.getString("price");
					String quantity = rs.getString("quantity");
					String amount = rs.getString("amount");
					out.write(productCode + "," + productName + "," + price + "," + quantity + "," + amount + "\n");
				}
				String csvQueryNoSales = "SELECT LPAD(m_product.product_code, 3, '0') AS product_code, m_product.product_name, m_product.price\n"
						+ "FROM exb_01.m_product \n"
						+ "LEFT JOIN exb_01.CSV_table ON m_product.product_code = CSV_table.product_code \n"
						+ "WHERE CSV_table.product_code IS NULL;";
				pstmt = con.prepareStatement(csvQueryNoSales);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String productCode = rs.getString("product_code");
					String productName = rs.getString("product_name");
					String price = rs.getString("price");
					out.write(productCode + "," + productName + "," + price + "\n");
				}
				out.close();
				rs.close();
				pstmt.close();
				con.close();
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
}