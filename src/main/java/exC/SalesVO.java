package exC;

/**
 * 商品の販売と関連した値を入力してもらうための生成者と伝達確認のためのtoStringをオーバーライディングして具現したVOファイルです。 
 * DTOとの差別化のためにsetterを無効にしました。
 * 
 * @author ju_jeongseok
 * @param sales_date 販売日
 * @param product_code 販売された製品の一意の識別子
 * @param product_name 販売された製品の名前
 * @param price 販売された製品の単価
 * @param quantity 販売台数
 * @param amount 売上高の合計
 * @return getterメソッドによるprivateのフィールド値を返します
 */
public class SalesVO {
	private String sales_date;
	private String product_code;
	private String product_name;
	private String price;
	private String quantity;
	private String amount;

	public SalesVO(String sales_date, String product_code, String product_name, String price, String quantity,
			String amount) {
		super();
		this.sales_date = sales_date;
		this.product_code = product_code;
		this.product_name = product_name;
		this.price = price;
		this.quantity = quantity;
		this.amount = amount;
	}
	
	public String getSales_date() {
		return sales_date;
	}
	
	public String getProduct_code() {
		return product_code;
	}
	
	public String getProduct_name() {
		return product_name;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public String getAmount() {
		return amount;
	}

	/*
	public void setSales_date(String sales_date) {
		this.sales_date = sales_date;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	*/
	
	/**
	 * 販売の文字列表示VOオブジェクト
	 */
	@Override
	public String toString() {
		return "SalesVO [sales_date=" + sales_date + ", product_code=" + product_code + ", product_name=" + product_name
				+ ", price=" + price + ", quantity=" + quantity + ", amount=" + amount + "]";
	}
}

