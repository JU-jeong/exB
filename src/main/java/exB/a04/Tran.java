package exB.a04;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * mySQLクエリでデータを操作するクラス
 * @author ju_jeongseok
 * @param url データベースのURL
 * @param user データベースへの接続に使用するユーザー名
 * @param password データベースへの接続に使用するパスワード
 */
public class Tran {
	public static void main(String[] args) throws IOException {
		/**
		 * 1. データベース接続パラメータ
		 */
		String url = "jdbc:mysql://localhost:3306/exB_01?characterEncoding=UTF-8&serverTimezone=UTC";
		String user = "root";
		String password = "tiger";
		
		// 2. データ接続、そしてコミットを無効にして、トランザクションを手動で処理
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			con.setAutoCommit(false);
			try (Statement stmt = con.createStatement()) {
				
				// 3. 販売テーブルそして商品マスターテーブルからすべてのレコードを削除
				stmt.executeUpdate("DELETE FROM t_sales");
				stmt.executeUpdate("DELETE FROM m_product");

				// 4. クエリ実行の確認メッセージの出力
				System.out.println("商品マスタのデータを削除しました。");
				System.out.println();
				System.out.println("売上テーブルのデータを削除しました。");
				System.out.println();
				
				// 5. 商品テーブルに新しいレコードを挿入
				stmt.executeUpdate(
						"INSERT INTO m_product (product_code, product_name, price, register_datetime, update_datetime, delete_datetime) "
								+ "VALUES (1, '商品A', 10, now(), now(), null),\n"
								+ "    (2, '商品B', 100, now(), now(), null),\n"
								+ "    (3, '商品C', 1000, now(), now(), null),\n"
								+ "    (4, '商品D', 10000, now(), now(), null),\n"
								+ "    (5, '商品E', 100000, now(), now(), null);");

				// 6. クエリ実行の確認メッセージの出力
				System.out.println("商品マスタにデータを登録しました。");
				System.out.println();

				// 7. 販売テーブルに新しいレコードを挿入
				stmt.executeUpdate("INSERT INTO t_sales (sales_date, product_code, quantity) "
						+ "VALUES ('2024-01-01', 1, 1),\n" + "    ('2024-01-02', 2, 2),\n"
						+ "    ('2024-01-03', 3, 3),\n" + "    ('2024-01-04', 4, 4),\n" + "    ('2024-01-05', 5, 5),\n"
						+ "    ('2024-01-06', 1, 6),\n" + "    ('2024-01-07', 2, 7),\n" + "    ('2024-01-08', 3, 8),\n"
						+ "    ('2024-01-09', 4, 9),\n" + "    ('2024-01-10', 5, 10);");
				
				// 8. クエリ実行の確認メッセージの出力
				System.out.println("売上テーブルにデータを登録しました。");
				System.out.println();

				// 9. 商品マスタテーブルの内容を表示
				// ------------------------------------------------------------------------------------------------------

				// 10. クエリ実行の予告のメッセージの出力
				System.out.println("商品テーブルのデータを表示します");
				System.out.println();

				// 11. テーブルのカラム名とデータの間隔を合わせるために、各カラムの該当値から最も長い値を返換
				ResultSet rs = stmt.executeQuery("SELECT MAX(LENGTH(COALESCE(product_code, ''))) as ph1, \n"
						+ "       MAX(LENGTH(COALESCE(product_name, ''))) as ph2, \n"
						+ "       MAX(LENGTH(COALESCE(price, ''))) as ph3,\n"
						+ "       MAX(LENGTH(COALESCE(register_datetime, ''))) as ph4, \n"
						+ "       MAX(LENGTH(COALESCE(update_datetime, ''))) as ph5, \n"
						+ "       MAX(LENGTH(COALESCE(delete_datetime, ''))) as ph6\n" + "FROM m_product;");

				// 12. 各カラムデータ値の最大長を含めるための変数の初期化
				int ph1 = 0, ph2 = 0, ph3 = 0, ph4 = 0, ph5 = 0, ph6 = 0;

				// 13. 各カラムデータ値の最大長を変数に入れるプロセス
				if (rs.next()) {
					ph1 = rs.getInt("ph1");
					ph2 = rs.getInt("ph2");
					ph3 = rs.getInt("ph3");
					ph4 = rs.getInt("ph4");
					ph5 = rs.getInt("ph5");
					ph6 = rs.getInt("ph6");
				}
				
				// 14. delete_datetimeの場合、初期値がnull値であるため、更新日との適当な間隔調整のための数を代入する
				if (ph6 == 0)
					ph6 = 10;
				
				// 15. 商品データから全資料を出力（セミコロンは使わない）
				rs = stmt.executeQuery("SELECT product_code, product_name, price, register_datetime, update_datetime, delete_datetime FROM m_product");

				// 16. 各カラムごとに11-14の過程で設定した間隔を置いて出力
				System.out.printf(
						"%-" + ph1 + "s %-" + ph2 + "s %-" + ph3 + "s %-" + ph4 + "s %-" + ph5 + "s %-" + ph6 + "s \n",
						"商品コード", "商品名", "単価", "登録日", "更新日", "削除日");
				
				// 17. その後、間隔に合わせてデータを改行して出力
				while (rs.next()) {
					System.out.printf(
							"%-" + ph1 + "s %-" + ph2 + "s %-" + ph3 + "s %-" + ph4 + "s %-" + ph5 + "s %-" + ph6
									+ "s\n",
							rs.getString("product_code"), rs.getString("product_name"), rs.getString("price"),
							rs.getString("register_datetime"), rs.getString("update_datetime"),
							rs.getString("delete_datetime"));
				}
				System.out.println();
				
				// 18. 売上テーブルの内容を表示
				// ------------------------------------------------------------------------------------------------------

				// 19. クエリ実行の予告のメッセージの出力
				System.out.println("売上テーブルのデータを表示します");
				
				// 20. テーブルのカラム名とデータの間隔を合わせるために、各カラムの該当値から最も長い値を求め、その間隔だけカラム同士の間隔を設定
				rs = stmt.executeQuery("SELECT MAX(LENGTH(COALESCE(sales_date, ''))) as th1, \n"
						+ "       MAX(LENGTH(COALESCE(product_code, ''))) as th2, \n"
						+ "       MAX(LENGTH(COALESCE(quantity, ''))) as th3\n" + "FROM t_sales;");
				
				// 21. 各カラムデータ値の最大長を含めるための変数の初期化
				int th1 = 0, th2 = 0, th3 = 0;

				// 22. 各カラムデータ値の最大長を変数に入れるプロセス
				if (rs.next()) {
					th1 = rs.getInt("th1");
					th2 = rs.getInt("th2");
					th3 = rs.getInt("th3");
				}

				// 23. 商品データから全資料を出力（セミコロンは使わない）
				rs = stmt.executeQuery("SELECT sales_date, product_code, quantity, register_datetime, update_datetime FROM t_sales");
				
				// 24. 各カラムごとに20-23の過程で設定した間隔を置いて出力
				System.out.printf("%-" + th1 + "s %-" + th2 + "s %-" + th3 + "s \n", "売上日", "商品コード", "数量");

				// 25. その後、間隔に合わせてデータを改行して出力
				while (rs.next()) {
					System.out.printf("%-" + th1 + "s %-" + th2 + "s %-" + th3 + "s\n", rs.getString("sales_date"),
							rs.getString("product_code"), rs.getString("quantity"));
				}
				
				// 26. トランザクションをコミットし、結果セットと接続を閉じた後、例外を処理
				con.commit();
				rs.close();
				con.close();

			// 27. クエリー実行の例外発生処理構文
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		// 28. データベース接続例外発生処理構文
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
}