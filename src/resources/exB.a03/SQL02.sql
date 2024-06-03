SELECT product_code, product_name, price, register_datetime, update_datetime, delete_datetime
FROM m_product WHERE product_code IN
(SELECT product_code FROM t_sales GROUP BY product_code HAVING COUNT(product_code) >  1
);
/*
まず、t_salesの販売数量が1を超えるproduct_codeを重複なく収集し、そのproduct_codeに該当する商品の情報をm_productで収集するように設計しました。
SELECT DISTINCT product_code FROM t_sales WHERE quantity > 1
*/