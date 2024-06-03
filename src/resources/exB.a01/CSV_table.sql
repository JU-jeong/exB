CREATE TABLE CSV_table AS
SELECT 
    t_sales.sales_date,
    m_product.product_code,
    m_product.product_name,
    m_product.price,
    t_sales.quantity,
    m_product.price * t_sales.quantity AS amount
FROM 
    m_product
JOIN 
    t_sales ON m_product.product_code = t_sales.product_code;
    
+--------------+-------------------------------+------+-----+---------+-------+
| Field        | Type                          | Null | Key | Default | Extra |
+--------------+-------------------------------+------+-----+---------+-------+
| sales_date   | date                          | NO   |     | NULL    |       |
| product_code | smallint(3) unsigned zerofill | NO   |     | NULL    |       |
| product_name | varchar(50)                   | NO   |     | NULL    |       |
| price        | int unsigned                  | NO   |     | NULL    |       |
| quantity     | int unsigned                  | NO   |     | NULL    |       |
| amount       | bigint unsigned               | NO   |     | 0       |       |
+--------------+-------------------------------+------+-----+---------+-------+

/*3-5の解決のためにm_productとt_salesのカラムを結合した第3のテーブルを作りました。*/