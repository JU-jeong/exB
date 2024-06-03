package exC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *  データベース内のユーザ関連データを管理するための Data Access Object（DAO）。
 *	製品の一覧表示、製品の検索、製品の追加、更新、および削除方法を提供します、
 *	売上を登録または更新し、売上データのCSVファイルを生成
 * @author ju_jeongseok
 */

public class UserDAO {
	/**
	 * データベース接続パラメータ
	 */
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	String url = "jdbc:mysql://localhost:3306/exB_01?characterEncoding=UTF-8&serverTimezone=UTC";
	String user = "root";
	String password = "tiger";

	/**
	 * UserDAO クラスのコンストラクタ。
	 * JNDIから取得したデータ ソースを使用して、データベースへの接続を設定
	 * これにより、アプリケーションは手動で接続を作成するのではなく、コンテナが管理する接続プールを使用
	 * セットアップ中に例外が発生した場合、それらはキャプチャされ、標準出力に出力
	 */
	public UserDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/mysql");
			//con.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println("例外");
		}
	}
	/**
	 * データベースからすべての製品のリストを取得
	 */
	public List listProducts() {
		List productList = new ArrayList();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			String query = "select LPAD(product_code, 3, '0') AS product_code, product_name, FORMAT(price, 0) AS price from m_product order by product_code asc;";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String product_code = rs.getString("product_code");
				String product_name = rs.getString("product_name");
				String price = rs.getString("price");
				UserVO userVO = new UserVO(product_code, product_name, price);
				productList.add(userVO);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productList;
	}
	
	/**
	 * 指定した検索用語に基づいて、データベース内の製品を検索
	 * @param searchProduct 製品名と照合する検索用語を生成する
	 * @return 一致する製品を表す UserVO オブジェクトのリストを返す
	 */
	public List searchProducts(String searchProduct) {
		List<UserVO> searchedProducts = new ArrayList<>();
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			String query = "select LPAD(product_code, 3, '0') AS product_code, product_name, FORMAT(price, 0) AS price FROM m_product WHERE product_name LIKE ? order by product_code asc;";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + searchProduct + "%");
			if(searchProduct.equals("")) {
			pstmt.setString(1, searchProduct);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String product_code = rs.getString("product_code");
				String product_name = rs.getString("product_name");
				String price = rs.getString("price");
				UserVO searchedProduct = new UserVO(product_code, product_name, price);
				searchedProducts.add(searchedProduct);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return searchedProducts;
	}

	/**
	 * 指定した名前と価格で新しい製品をデータベースに追加
	 * @param product_name 追加する製品の名前
	 * @param price 追加する製品の価格 
	 * @return 製品が正常に追加された場合は true、そうでない場合はfalseを返す
	 */
	public boolean addProduct(String product_name, String price) {
		ResultSet rs = null;
		int maxProductCode = 0;
		try {
			if (product_name == null || price == null) {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			Timestamp currentTime = new Timestamp(new Date().getTime());
			String maxCodeNumQuery = "SELECT max(product_code) FROM m_product";
			pstmt = con.prepareStatement(maxCodeNumQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxProductCode = rs.getInt(1);
			}
			String selectQuery = "SELECT product_code FROM m_product WHERE product_name = ?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, product_name);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				String insertQuery = "INSERT INTO m_product (product_code, product_name, price, register_datetime, update_datetime, delete_datetime) VALUES (?, ?, ?, ?, ?, null)";
				pstmt = con.prepareStatement(insertQuery);
				pstmt.setInt(1, maxProductCode + 1);
				pstmt.setString(2, product_name);
				pstmt.setString(3, price);
				pstmt.setTimestamp(4, currentTime);
				pstmt.setTimestamp(5, currentTime);
				pstmt.executeUpdate();
				rs.close();
				pstmt.close();
				con.close();
				return true;
			} else {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 指定された詳細情報を使用して、データベース内の既存の製品を更新
	 * @param product_code 更新する製品のコード
	 * @param product_name 製品の新しい名前
	 * @param product_price 製品の新しい価格
	 * @return 製品が正常に更新された場合は true、そうでない場合はfalseを返す
	 * @throws ClassNotFoundException JDBCドライバクラスが見つからない場合
	 */
	public boolean updateProduct(String product_code, String product_name, String product_price)
			throws ClassNotFoundException {
		ResultSet rs = null;
		try {
			if (product_name == null || product_price == null) {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			}
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			String selectQuery = "SELECT product_code FROM t_sales WHERE product_code = ?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, product_code);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			} else {
				Timestamp update_date = new Timestamp(new Date().getTime());
				String updateQuery = "UPDATE m_product SET product_name = ?, price = ?, update_datetime = ? WHERE product_code = ?";
				pstmt = con.prepareStatement(updateQuery);
				pstmt.setString(1, product_name);
				pstmt.setString(2, product_price);
				pstmt.setTimestamp(3, update_date);
				pstmt.setString(4, product_code);
				pstmt.executeUpdate();
				rs.close();
				pstmt.close();
				con.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 製品コードを使用してデータベースから製品を削除
	 * @param product_code 削除する製品のコード
	 * @return 製品が正常に削除された場合は true、そうでない場合はfalseを返す
	 * @throws ClassNotFoundException :JDBCドライバクラスが見つからない場合
	 */
	public boolean deleteProduct(String product_code) throws ClassNotFoundException {
	    ResultSet rs = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        con = DriverManager.getConnection(url, user, password);
	        String updateQuery = "UPDATE m_product SET delete_datetime = NOW() WHERE product_code = ?";
	        pstmt = con.prepareStatement(updateQuery);
	        pstmt.setString(1, product_code);
	        int updateResult = pstmt.executeUpdate();
	        String selectQuery = "SELECT product_code FROM t_sales WHERE product_code = ?";
	        pstmt = con.prepareStatement(selectQuery);
	        pstmt.setString(1, product_code);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            rs.close();
	            pstmt.close();
	            con.close();
	            return false;
	        } else {
	            String deleteQuery = "DELETE FROM m_product WHERE product_code = ?";
	            pstmt = con.prepareStatement(deleteQuery);
	            pstmt.setString(1, product_code);
	            int deleteResult = pstmt.executeUpdate();
	            rs.close();
	            pstmt.close();
	            con.close();
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	/**
	 * 製品の販売データをデータベースに登録または更新
	 * @param salesDate 販売日の日付
	 * @param productCode 販売する製品のコード
	 * @param quantity 商品の販売数量のパラメーター
	 */
	public void registerOrUpdateSales(String salesDate, String productCode, int quantity) {
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			Timestamp currentTime = new Timestamp(new Date().getTime());
			String selectQuery = "SELECT * FROM t_sales WHERE sales_date = ? AND product_code = ? order by product_code asc";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(2, Integer.parseInt(productCode));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String updateQuery = "UPDATE t_sales SET quantity = ?, update_datetime = ? WHERE sales_date = ? AND product_code = ?";
				pstmt = con.prepareStatement(updateQuery);
				pstmt.setInt(1, quantity);
				pstmt.setTimestamp(2, currentTime);
				pstmt.setInt(4, Integer.parseInt(productCode));
				pstmt.executeUpdate();
			} else {
				String insertQuery = "INSERT INTO t_sales (sales_date, product_code, quantity, register_datetime, update_datetime) VALUES (?, ?, ?, ?, ?)";
				pstmt = con.prepareStatement(insertQuery);
				pstmt.setInt(2, Integer.parseInt(productCode));
				pstmt.setInt(3, quantity);
				pstmt.setTimestamp(4, currentTime);
				pstmt.setTimestamp(5, currentTime);
				pstmt.executeUpdate();
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * データベース内の製品の販売データを追加または更新
	 * @param product_name 販売された製品の名前
	 * @param product_quantity 販売された製品の数量を表示
	 * @return 販売データが正常に追加または更新された場合はtrueを返し、そうでない場合はfalseを返す
	 * @throws SQLException データベースへのアクセスにエラーがある場合にスロー
	 */
	public boolean addUpdateSales(String product_name, String product_quantity) throws SQLException {
		ResultSet rs = null;
		Timestamp currentTime = new Timestamp(new Date().getTime());
		String findProductPrice = null;
		double findProductPriceDouble = 0.00;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			String findProductCodeQuery = "select product_code from m_product where product_name = ?";
			pstmt = con.prepareStatement(findProductCodeQuery);
			pstmt.setString(1, product_name);
			rs = pstmt.executeQuery();
			int findProductCodeQueryInt = 0;
			if (rs.next()) {
				findProductCodeQueryInt = Integer.parseInt(rs.getString(1));
			}
			findProductPrice = "select price from m_product where product_name = ?";
			pstmt = con.prepareStatement(findProductPrice);
			pstmt.setString(1, product_name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				findProductPriceDouble = Double.parseDouble(rs.getString(1));
			}
			if (!rs.next()) {
				String insertSalesQuery = "INSERT INTO t_sales(sales_date, product_code, quantity, register_datetime) VALUES (?, ?, ?, ?)";
				pstmt = con.prepareStatement(insertSalesQuery);
				pstmt.setTimestamp(1, currentTime);
				pstmt.setInt(2, findProductCodeQueryInt);
				pstmt.setString(3, product_quantity);
				pstmt.setTimestamp(4, currentTime);
				pstmt.executeUpdate();
				String insertListQuery = "INSERT INTO CSV_table (sales_date, product_code, product_name, price, quantity, amount) VALUES (?, ?, ?, ?, ?, price*quantity)";
				pstmt = con.prepareStatement(insertListQuery);
				pstmt.setTimestamp(1, currentTime);
				pstmt.setInt(2, findProductCodeQueryInt);
				pstmt.setString(3, product_name);
				pstmt.setDouble(4, findProductPriceDouble);
				pstmt.setString(5, product_quantity);
				pstmt.executeUpdate();
				rs.close();
				pstmt.close();
				con.close();
				return true;
			} else {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * データベースからすべての販売データのリストを取得
	 * @return 販売リストを返送する売上データを表すVOオブジェクト
	 */
	public List listSales() {
		List salesList = new ArrayList();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			String query = "select * from CSV_table order by sales_date asc;";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String sales_date = rs.getString("sales_date");
				String product_code = rs.getString("product_code");
				String product_name = rs.getString("product_name");
				String price = rs.getString("price");
				String quantity = rs.getString("quantity");
				String amount = rs.getString("amount");
				SalesVO salesVO = new SalesVO(sales_date, product_code, product_name, price, quantity, amount);
				salesList.add(salesVO);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesList;
	}
	
	/*
	public boolean generateCSVdate(String sales_date) {
		try {
			ResultSet rs = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			try {
				String[] salesDateSplit = sales_date.split("-");
				if (sales_date == null || salesDateSplit.length != 2) {
					return false;
				}
				int salesYear = Integer.parseInt(salesDateSplit[0]);
				int salesMonth = Integer.parseInt(salesDateSplit[1]);
				String filePath = "C:\\Users\\ju_jeongseok\\Desktop\\csv_" + sales_date + ".csv";
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
				String csvQueryhasDate = "select LPAD(product_code, 3, '0') AS product_code, product_name, price, quantity, amount from CSV_table where YEAR(sales_date) = ? AND MONTH(sales_date) = ? order by product_code asc;";
				pstmt = con.prepareStatement(csvQueryhasDate);
				pstmt.setInt(1, salesYear);
				pstmt.setInt(2, salesMonth);
				rs = pstmt.executeQuery();			
				/*
				File file = new File(filePath);
				writer.write("商品コード,商品名,単価,数量,金額");
				writer.write("\n");
				while (rs.next()) {
					String productCode = rs.getString("product_code");
					String productName = rs.getString("product_name");
					String price = rs.getString("price");
					String quantity = rs.getString("quantity");
					String amount = rs.getString("amount");
					writer.write(productCode + "," + productName + "," + price + "," + quantity + "," + amount + "\n");
				}
				writer.close();
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "MS949"));
				bw.write("商品コード,商品名,単価,数量,金額");
				bw.write("\n");
				while (rs.next()) {
					String productCode = rs.getString("product_code");
					String productName = rs.getString("product_name");
					String price = rs.getString("price");
					String quantity = rs.getString("quantity");
					String amount = rs.getString("amount");
					bw.write(productCode + "," + productName + "," + price + "," + quantity + "," + amount + "\n");
				}
				bw.close();
				rs.close();
				pstmt.close();
				con.close();
				return true;
			} catch (ArrayIndexOutOfBoundsException e) {
				rs.close();
				pstmt.close();
				con.close();
				return false;
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void generateCSVall() {
		try {
			ResultSet rs = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			String filePath = "C:\\Users\\ju_jeongseok\\Desktop\\csv_all.csv";
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			File file = new File(filePath);
			writer.write("商品コード,商品名,単価,数量,金額");
			writer.write("\n");
			String csvQuerySales = "select LPAD(product_code, 3, '0') AS product_code, product_name, price, quantity, amount from CSV_table order by product_code asc;";
			pstmt = con.prepareStatement(csvQuerySales);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String productCode = rs.getString("product_code");
				String productName = rs.getString("product_name");
				String price = rs.getString("price");
				String quantity = rs.getString("quantity");
				String amount = rs.getString("amount");
				writer.write(productCode + "," + productName + "," + price + "," + quantity + "," + amount + "\n");
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
				writer.write(productCode + "," + productName + "," + price + "\n");
			}
			writer.close();
			rs.close();
			pstmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
