CREATE TABLE m_product (
    product_code smallint(3)zerofill PRIMARY KEY NOT NULL,
    product_name NVARCHAR(50) NOT NULL,
    price int NOT NULL,
    register_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    delete_datetime DATETIME
);

ALTER TABLE m_product MODIFY update_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
alter table m_product modify product_code smallint(3)zerofill unsigned not null check (product_code >= 1 and product_code <= 999);
alter table m_product modify price int unsigned not null check (price > 0);


+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+
| Field             | Type                          | Null | Key | Default           | Extra                                         |
+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+
| product_code      | smallint(3) unsigned zerofill | NO   | PRI | NULL              |                                               |
| product_name      | varchar(50)                   | NO   |     | NULL              |                                               |
| price             | int unsigned                  | NO   |     | NULL              |                                               |
| register_datetime | datetime                      | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| update_datetime   | datetime                      | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| delete_datetime   | datetime                      | YES  |     | NULL              |                                               |
+-------------------+-------------------------------+------+-----+-------------------+-----------------------------------------------+

/*
product_codeは重複しないようにPRIMARY KEYとして設計しており、
コード、商品名、価格、初期に必ず入るべき値にはNOTNULL条件を追加し、
登録日も同一条件を追加し、トランザクション適用時の時間がデフォルトで入力されるように設計しました。

また、smallint(3)zerofillを通じて1を001みたいな形にしました。

また、check (product_code >= 1 and product_code <= 999)を通じて条件を満たしました。

unsignedとcheckで陽数だけ入れるようにしました。

単価、数量はこれに対する特別な条件が付与されていないため、intに設定しました。

update_datetimeは、DEFAULT_GENERATED on update CURRENT_TIMESTAMPを通じて
明示的なタイムスタンプなしのアップデートによって、現在のタイムスタンプ値が更新されることにしました。

削除の日付はトランザクション実行時に適用されるようNULLを入力することを許可しました。
*/