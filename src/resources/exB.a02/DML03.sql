UPDATE t_sales SET quantity = 10 WHERE sales_date = '2024-01-11' AND product_code = 1;

//sales_dateを指定するべきです。 product_codeのみの指定では、意図しないデータを更新してしまう可能性があります。