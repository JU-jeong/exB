package exC;

import java.sql.Date;
/**
 * 商品と関連した値の入力を受けるための生成者と伝達確認のためのtoStringをオーバーライディングして実装したVOファイルです。 
 * DTOとの差別化のためにsetterを無効にしました。
 * 
 * @author ju_jeongseok
 * @param product_name 製品名
 * @param product_name 製品名
 * @param price 製品の価格
 * @param product_code 製品の固有識別子
 * @param product_name 製品名
 * @param price 製品の価格
 * @return getterメソッドによるprivateのフィールド値を返します
 */

public class UserVO {
	private String product_code;
	private String product_name;
	private String price;
	private Date register_datetime;
	private Date update_datetime;
	private Date delete_datetime;

	public UserVO(String product_name) {
		super();
		this.product_name = product_name;
	}
	
	public UserVO(String product_name, String price) {
		super();
		this.product_name = product_name;
		this.price = price;
	}
	
	public UserVO(String product_code, String product_name, String price) {
		super();
		this.product_code = product_code;
		this.product_name = product_name;
		this.price = price;
	}

	/*
	public UserVO(int product_code, String product_name, int price, Date register_datetime, Date update_datetime,
			Date delete_datetime) {
		super();
		this.product_code = product_code;
		this.product_name = product_name;
		this.price = price;
		this.register_datetime = register_datetime;
		this.update_datetime = update_datetime;
		this.delete_datetime = delete_datetime;
	}
	*/
	
	public String getProduct_code() {
		return product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	
	public String getPrice() {
		return price;
	}
	
	public Date getRegister_datetime() {
		return register_datetime;
	}
	
	public Date getUpdate_datetime() {
		return update_datetime;
	}
	
	public Date getDelete_datetime() {
		return delete_datetime;
	}

	/*
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setRegister_datetime(Date register_datetime) {
		this.register_datetime = register_datetime;
	}
	public void setUpdate_datetime(Date update_datetime) {
		this.update_datetime = update_datetime;
	}
	public void setDelete_datetime(Date delete_datetime) {
		this.delete_datetime = delete_datetime;
	}
	*/
	/**
	 *  UserVO の文字列表現
	 */
	@Override
	public String toString() {
		return "UserVO [product_code=" + product_code + ", product_name=" + product_name + ", price=" + price
				+ ", register_datetime=" + register_datetime + ", update_datetime=" + update_datetime
				+ ", delete_datetime=" + delete_datetime + ", getProduct_code()=" + getProduct_code()
				+ ", getProduct_name()=" + getProduct_name() + ", getPrice()=" + getPrice()
				+ ", getRegister_datetime()=" + getRegister_datetime() + ", getUpdate_datetime()="
				+ getUpdate_datetime() + ", getDelete_datetime()=" + getDelete_datetime() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}