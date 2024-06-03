INSERT INTO m_product (product_code, product_name, price) VALUES (100, 'dml02', 5000);

INSERT INTO t_sales (sales_date, product_code, quantity) VALUES ('2024-01-19', 100, 500);

/*
ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails 
(`exb_01`.`t_sales`, CONSTRAINT `t_sales_ibfk_1` FOREIGN KEY (`product_code`) REFERENCES `m_product` (`product_code`))

t_salesテーブルを設計する際、product_codeカラムはm_productテーブルでFK参照するように設計しておきましたが、
参照するテーブルであるm_productにそのデータを先に追加した後、参照されるテーブルt_salesにデータを追加するとエラーが解決されました。

*/