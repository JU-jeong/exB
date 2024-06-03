CREATE TABLE t_sales (
    sales_date DATE NOT NULL,
    product_code smallint(3)zerofill NOT NULL,
    quantity INT NOT NULL,
    register_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_datetime DATETIME,
    FOREIGN KEY (product_code) REFERENCES m_product(product_code)
);

ALTER TABLE t_sales MODIFY update_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
alter table t_sales modify quantity int unsigned not null check (quantity > 0);

+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+
| Field             | Type                          | Null | Key | Default           | Extra                                         |
+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+
| sales_date        | date                          | NO   |     | NULL              |                                               |
| product_code      | smallint(3) unsigned zerofill | NO   | MUL | NULL              |                                               |
| quantity          | int unsigned                  | NO   |     | NULL              |                                               |
| register_datetime | datetime                      | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_datetime   | datetime                      | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+

/*
 m_productと連動するようにproduct_codeにFOREIGN KEYの設定を付与しました。
 
 update_datetimeは、DEFAULT_GENERATED on update CURRENT_TIMESTAMPを通じて
明示的なタイムスタンプなしのアップデートによって、現在のタイムスタンプ値が更新されることにしました。

unsignedとcheckで陽数だけ入れるようにしました。
 */
