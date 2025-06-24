-- Демонстрация запросов к БД из книги Бена Форта

---------
-- Урок 2
---------
-- Извлечение данных из таблиц

SELECT
    prod_name
FROM
    products;

SELECT
    prod_id,
    prod_name,
    prod_price
FROM
    products;

SELECT *
FROM products;

-- Извлечение уникальных строк

SELECT DISTINCT
    vend_id
FROM
    products;

SELECT DISTINCT
     vend_id,
     prod_price
FROM
    products;

-- Ограничение выводимых строк

SELECT
    prod_name
FROM
    products
LIMIT 5;

SELECT
    prod_name
FROM
    products
LIMIT 5 OFFSET 5;

-- Упражнения

SELECT
    cust_id
FROM
    customers;

SELECT DISTINCT
    prod_id
FROM
    orderitems;

SELECT *
FROM customers;

SELECT cust_id
FROM customers;

---------
-- Урок 3
---------
-- Сортировка полученных данных

SELECT
    prod_name
FROM
     products
ORDER BY
     prod_name;

-- Сортировка по несколькими столбцам

SELECT
    prod_id,
    prod_price,
    prod_name
FROM
     products
ORDER BY
    prod_price,
    prod_name;

-- Сортировка по номеру столбца

SELECT
    prod_id,
    prod_price,
    prod_name
FROM
    products
ORDER BY 2, 3;

-- Указание направления сортировки

SELECT prod_id, prod_price, prod_name
FROM products
ORDER BY prod_price DESC;

SELECT prod_id, prod_price, prod_name
FROM products
ORDER BY prod_price DESC, prod_name;

-- Упражнения

SELECT cust_name
FROM customers
ORDER BY cust_name DESC;

SELECT cust_id, order_num
FROM orders
ORDER BY cust_id, order_date DESC;

SELECT quantity, item_price
FROM orderitems
ORDER BY quantity DESC, item_price DESC;

---------
-- Урок 4
---------
-- Фильтрация данных
-- Предложение WHERE

SELECT prod_name, prod_price
FROM products
WHERE prod_price = 3.49;

-- Сравнение с одним значением

SELECT prod_name, prod_price
FROM products
WHERE prod_price < 10;

-- Проверка на равенство

SELECT vend_id, prod_name
FROM products
WHERE vend_id != 'DLL01';

-- Сравнение с диапозоном значений

SELECT prod_name, prod_price
FROM products
WHERE prod_price BETWEEN 5 AND 10;

-- Сравнение с NULL

SELECT prod_name, prod_price
FROM products
WHERE prod_price IS NULL;

SELECT cust_name
FROM customers
WHERE cust_email IS NULL;

-- Упражнения

SELECT prod_id, prod_name
FROM products
WHERE prod_price = 9.49;

SELECT prod_id, prod_name
FROM products
WHERE prod_price > 9;

SELECT order_num
FROM orderitems
WHERE quantity > 100;

SELECT prod_name, prod_price
FROM products
WHERE prod_price BETWEEN 3 AND 6;

---------
-- Урок 5
---------
-- Расширенная фильтрация данных
-- Комбинирование предложений WHERE

SELECT prod_id, prod_price, prod_name
FROM products
WHERE vend_id = 'DLL01'
  AND prod_price <= 4;

SELECT prod_name, prod_price
FROM products
WHERE vend_id = 'DLL01'
   OR vend_id = 'BRS01';


-- Оператор IN

SELECT prod_name, prod_price
FROM products
WHERE vend_id IN ('DLL01', 'BRS01')
ORDER BY prod_name;

-- Оператор NOT

SELECT prod_name
FROM products
WHERE NOT vend_id = 'DLL01'
ORDER BY prod_name;

-- Упражнения

SELECT vend_name
FROM vendors
WHERE vend_country = 'USA'
  AND vend_state = 'CA';

SELECT order_num, prod_id, quantity
FROM orderitems
WHERE prod_id IN ('BR01','BR02','BR03')
AND quantity >= 100;

SELECT prod_name, prod_price
FROM products
WHERE prod_price BETWEEN 3 AND 6
ORDER BY prod_price;

---------
-- Урок 6
---------
-- Фильтрация с использованием метасимволов
-- Метасивол "знак процента" (%)

SELECT prod_id, prod_name
FROM products
WHERE prod_name LIKE 'Fish%';

SELECT prod_id, prod_name
FROM products
WHERE prod_name LIKE '%bean bag%';

SELECT prod_name
FROM products
WHERE prod_name LIKE 'F%y%';

-- Мета символ "знак подчеркивания" (_)

SELECT prod_id, prod_name
FROM products
WHERE prod_name LIKE '__ inch teddy bear%';

-- Упражнения

SELECT prod_name, prod_desc
FROM products
WHERE prod_desc LIKE '%toy%';

SELECT prod_name, prod_desc
FROM products
WHERE prod_desc NOT LIKE '%toy%'
ORDER BY prod_name;

SELECT prod_name, prod_desc
FROM products
WHERE prod_desc LIKE '%toy%'
  AND prod_desc LIKE '%carrots%';

SELECT prod_name, prod_desc
FROM products
WHERE prod_desc LIKE '%toy%carrots%';

---------
-- Урок 7
---------
-- Создание вычесляемых полей

SELECT
    RTRIM(vend_name) || ' (' ||
    RTRIM(vend_country) || ')' AS name_and_country
FROM vendors
ORDER BY vend_name;

-- Выполнение арифметических вычислений

SELECT
    prod_id,
    quantity,
    item_price,
    quantity * item_price AS expanded_price
FROM orderitems
WHERE order_num = 20008;

-- Упражнения

SELECT vend_name    AS vname,
       vend_address AS vaddress,
       vend_city    AS vcity
FROM vendors
ORDER BY vname;

SELECT prod_id     AS id,
       prod_price  AS price,
       prod_price * 0.9 AS sale_price
FROM products;

---------
-- Урок 8
---------
-- Функции обработки данных

SELECT CURRENT_DATE AS date;

SELECT vend_name, UPPER(vend_name) AS name_uppercase
FROM vendors
ORDER BY vend_name;

SELECT order_num
FROM orders
WHERE DATE_PART('year', order_date) = 2020;

SELECT order_num
FROM orders
WHERE EXTRACT(year FROM order_date) = 2020;

SELECT order_num
FROM orders
WHERE order_date BETWEEN to_date('2020-01-01', 'yyyy-mm-dd')
          AND to_date('2020-12-31', 'yyyy-mm-dd');

-- Упражнения

SELECT cust_id,
       cust_name,
       UPPER(LEFT(cust_contact, 2) || LEFT(cust_city, 3)) AS user_login
FROM customers;

SELECT order_num,
       order_date
FROM orders
WHERE DATE_PART('year', order_date) = 2020
  AND DATE_PART('month', order_date) = 01
ORDER BY order_date;

---------
-- Урок 9
---------
-- Итоговые вычесления

SELECT AVG(prod_price) AS avg_price
FROM products;

SELECT AVG(prod_price) AS avg_price
FROM products
WHERE vend_id = 'DLL01';

SELECT COUNT(*) AS num_cust
FROM customers;

SELECT COUNT(cust_email) AS num_cust
FROM customers;

SELECT MAX(prod_price) AS max_price
FROM products;

SELECT MIN(prod_price) AS min_price
FROM products;

SELECT SUM(quantity) AS item_ordered
FROM orderitems
WHERE order_num = 20005;

SELECT SUM(item_price * quantity) AS total_price
FROM orderitems
WHERE order_num = 20005;

SELECT AVG(DISTINCT prod_price) AS avg_price
FROM products
WHERE vend_id = 'DLL01';

SELECT COUNT(*)        AS num_items,
       MIN(prod_price) AS price_min,
       MAX(prod_price) AS price_max,
       AVG(prod_price) AS price_avg
FROM products;

-- Упражнения

SELECT SUM(quantity) AS total_items
FROM orderitems;

SELECT SUM(quantity) AS total_items
FROM orderitems
WHERE prod_id = 'BR01';

SELECT MAX(prod_price) AS max_price
FROM products
WHERE prod_price < 10;

----------
-- Урок 10
----------
-- Группирование данных

SELECT vend_id, COUNT(*) AS num_prods
FROM products
GROUP BY vend_id;

SELECT cust_id, COUNT(*) AS orders
FROM orders
GROUP BY cust_id
HAVING COUNT(*) >= 2;

SELECT vend_id, COUNT(*) AS num_prods
FROM products
WHERE prod_price >= 4
GROUP BY vend_id
HAVING COUNT(*) >= 2;

SELECT order_num, count(*) AS items
FROM orderitems
GROUP BY order_num
HAVING count(*) >= 3
ORDER BY items, order_num;

-- Упражнения

SELECT order_num, count(*) AS order_lines
FROM orderitems
GROUP BY order_num
ORDER BY order_lines;

SELECT vend_id, min(prod_price) AS cheapest_item
FROM products
GROUP BY vend_id
ORDER BY cheapest_item;

SELECT order_num
FROM orderitems
GROUP BY order_num
HAVING sum(quantity) >= 100;

SELECT order_num, sum(quantity * item_price) AS total_sum
FROM orderitems
GROUP BY order_num
HAVING sum(quantity * item_price) >= 1000;

----------
-- Урок 11
----------
-- Подзапросы

SELECT order_num
FROM orderitems
WHERE prod_id = 'RGAN01';

SELECT cust_id
FROM orders
WHERE order_num IN (20007, 20008);

SELECT cust_id
FROM orders
WHERE order_num IN (SELECT order_num
                    FROM orderitems
                    WHERE prod_id = 'RGAN01');

SELECT cust_name, cust_contact
FROM customers
WHERE cust_id IN (1000000004, 1000000005);


SELECT cust_name, cust_contact
FROM customers
WHERE cust_id IN (SELECT cust_id
                  FROM orders
                  WHERE order_num IN (SELECT order_num
                                      FROM orderitems
                                      WHERE prod_id = 'RGAN01'));

SELECT count(*) AS orders
FROM orders
WHERE cust_id = '1000000001';

SELECT cust_name,
       cust_state,
       (SELECT COUNT(*)
        FROM orders o
        WHERE o.cust_id = c.cust_id) AS orders
FROM customers c
ORDER BY cust_name;

-- Упражнения

SELECT cust_id
FROM orders
WHERE order_num IN (SELECT order_num
                    FROM orderitems
                    WHERE item_price >= 10);

SELECT cust_id,
       order_date
FROM orders
WHERE order_num IN (SELECT order_num
                    FROM orderitems
                    WHERE prod_id = 'BR01')
ORDER BY order_date;

SELECT cust_id,
       order_date,
       (SELECT cust_email
        FROM customers c
        WHERE o.cust_id = c.cust_id)
FROM orders o
WHERE order_num IN (SELECT order_num
                    FROM orderitems
                    WHERE prod_id = 'BR01')
ORDER BY order_date;

SELECT cust_email
FROM customers
WHERE cust_id IN (SELECT cust_id
                  FROM orders
                  WHERE order_num IN (SELECT order_num
                                      FROM orderitems
                                      WHERE prod_id = 'BR01'));

SELECT cust_id,
       (SELECT sum(quantity * item_price)
        FROM orderitems oi
        WHERE oi.order_num = o.order_num) AS total_ordered
FROM orders o
ORDER BY total_ordered desc;

SELECT prod_name,
       (SELECT sum(quantity)
        FROM orderitems o
        WHERE o.prod_id = p.prod_id) AS quant_sold
FROM products p;

----------
-- Урок 12
----------
-- Соединение таблиц

SELECT vend_name, prod_name, prod_price
FROM vendors v,
     products p
WHERE v.vend_id = p.vend_id;

SELECT vend_name, prod_name, prod_price
FROM vendors v, products p;

SELECT vend_name, prod_name, prod_price
FROM vendors v INNER JOIN products p
ON v.vend_id = p.vend_id;

SELECT vend_name, prod_name, prod_price, quantity
FROM products p,
     vendors v,
     orderitems o
WHERE p.prod_id = o.prod_id
  AND p.vend_id = v.vend_id
  AND order_num = 20007;

SELECT cust_name, cust_contact
FROM customers c,
     orders o,
     orderitems oi
WHERE c.cust_id = o.cust_id
  AND oi.order_num = o.order_num
  AND prod_id = 'RGAN01';

-- Упражнения

SELECT c.cust_name, o.order_num
FROM customers c,
     orders o
WHERE c.cust_id = o.cust_id
ORDER BY c.cust_name, o.order_num;

SELECT c.cust_name, o.order_num
FROM customers c INNER JOIN orders o
ON c.cust_id = o.cust_id
ORDER BY c.cust_name, o.order_num;

SELECT c.cust_name, o.order_num, sum(oi.item_price) AS OrderTotal
FROM customers c,
     orders o,
     orderitems oi
WHERE c.cust_id = o.cust_id
  AND o.order_num = oi.order_num
GROUP BY c.cust_name, o.order_num
ORDER BY c.cust_name, o.order_num;

SELECT o.cust_id,
       o.order_date
FROM orders o, orderitems oi
WHERE oi.order_num = o.order_num
  AND oi.prod_id = 'BR01'
ORDER BY order_date;

SELECT o.cust_id,
       o.order_date,
       c.cust_email
FROM orders o INNER JOIN customers c
ON o.cust_id = c.cust_id
INNER JOIN orderitems oi
ON o.order_num = oi.order_num
WHERE oi.prod_id = 'BR01'
ORDER BY order_date;

SELECT c.cust_name,
       sum(oi.quantity * oi.item_price) AS total_sum
FROM orderitems oi
INNER JOIN orders o
ON oi.order_num = o.order_num
INNER JOIN customers c
ON o.cust_id = c.cust_id
GROUP BY c.cust_name
HAVING sum(quantity * item_price) >= 1000;

----------
-- Урок 13
----------
-- Создание расширенных соединений
-- Самосоединения
SELECT cust_id, cust_name, cust_contact
FROM customers
WHERE cust_name = (SELECT cust_name
                   FROM customers
                   WHERE cust_contact = 'Jim Jones');

SELECT c1.cust_id, c1.cust_name, c1.cust_contact
FROM customers c1,
     customers c2
WHERE c1.cust_name = c2.cust_name
  AND c2.cust_contact = 'Jim Jones';

-- Естественные соединения

SELECT c.*,
       o.order_num,
       o.order_date,
       oi.prod_id,
       oi.quantity,
       oi.item_price
FROM customers c,
     orders o,
     orderitems oi
WHERE c.cust_id = o.cust_id
  AND oi.order_num = o.order_num
  AND prod_id = 'RGAN01';

-- Внешние соединения
SELECT c.cust_id, o.order_num
FROM customers c
         INNER JOIN orders o
                    ON c.cust_id = o.cust_id;

SELECT c.cust_id, o.order_num
FROM customers c
         LEFT OUTER JOIN orders o
                    ON c.cust_id = o.cust_id;

SELECT c.cust_id, o.order_num
FROM customers c
         RIGHT OUTER JOIN orders o
                    ON c.cust_id = o.cust_id;

SELECT c.cust_id, o.order_num
FROM customers c
         FULL OUTER JOIN orders o
                    ON c.cust_id = o.cust_id;

-- Соединения с итоговыми функциями

SELECT c.cust_id,
       count(o.order_num) AS num_ord
FROM customers c
         INNER JOIN orders o
                    ON c.cust_id = o.cust_id
GROUP BY c.cust_id;

SELECT c.cust_id,
       count(o.order_num) AS num_ord
FROM customers c
         LEFT OUTER JOIN orders o
                    ON c.cust_id = o.cust_id
GROUP BY c.cust_id;

-- Упражнения

SELECT c.cust_name,
       o.order_num
FROM customers c
         INNER JOIN orders o
                    ON c.cust_id = o.cust_id;

SELECT c.cust_name,
       o.order_num
FROM customers c
         LEFT JOIN orders o
                    ON c.cust_id = o.cust_id;

SELECT p.prod_name,
       oi.order_num
FROM products p
         LEFT JOIN orderitems oi
                   ON p.prod_id = oi.prod_id
ORDER BY p.prod_name;

SELECT p.prod_name,
       sum(oi.quantity) AS total_ord
FROM products p
         LEFT JOIN orderitems oi
                   ON p.prod_id = oi.prod_id
GROUP BY p.prod_name
ORDER BY p.prod_name;

SELECT v.vend_id,
       count(p.prod_id)
FROM vendors v
         LEFT JOIN products p
                   ON v.vend_id = p.vend_id
GROUP BY v.vend_id;

----------
-- Урок 14
----------
-- Комбинированные запросы

SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_state IN ('IL', 'IN', 'MI')
UNION
SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_name = 'Fun4All';

EXPLAIN SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_state IN ('IL', 'IN', 'MI')
UNION ALL
SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_name = 'Fun4All';

SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_state IN ('IL', 'IN', 'MI')
UNION
SELECT cust_name, cust_contact, cust_email
FROM customers
WHERE cust_name = 'Fun4All'
ORDER BY cust_name,cust_contact;

-- Упражнения

SELECT prod_id, quantity
FROM orderitems
WHERE quantity = 100
UNION
SELECT prod_id, quantity
FROM orderitems
WHERE prod_id LIKE 'BNBG%'
ORDER BY prod_id;

SELECT prod_id, quantity
FROM orderitems
WHERE quantity = 100
AND prod_id LIKE 'BNBG%'
ORDER BY prod_id;

SELECT prod_name AS name
FROM products
UNION
SELECT cust_name AS name
FROM customers
ORDER BY name;

----------
-- Урок 15
----------
-- Добавление данных

INSERT INTO customers (cust_id,
                       cust_name,
                       cust_address,
                       cust_city,
                       cust_state,
                       cust_zip,
                       cust_country)
VALUES (1000000006,
        'Toy Land',
        '123 Any Street',
        'New York',
        'NY',
        '11111',
        'USA');

CREATE TABLE cust_copy AS
SELECT *
FROM customers;

-- Упражнения

INSERT INTO customers(cust_id,
                      cust_name,
                      cust_address,
                      cust_city,
                      cust_state,
                      cust_zip,
                      cust_country)
VALUES (1000000007,
        'Timur',
        '33 Kommunisticheskaya',
       'Borovsk',
        'KLG',
        '410010',
        'Russia');

CREATE TABLE orders_copy AS
SELECT *
FROM orders;

CREATE TABLE orderitems_copy AS
SELECT *
FROM orderitems;

----------
-- Урок 16
----------
-- Обнавление и удаление данных

UPDATE customers
SET cust_email = 'kim@thetoystore.com'
WHERE cust_id = '1000000005';

UPDATE customers
SET cust_contact = 'Sam Roberts',
    cust_email   = 'sam@toyland.com'
WHERE cust_id = '1000000006';

DELETE
FROM customers
WHERE cust_id = '1000000006';

-- Упражнения

UPDATE customers
SET cust_state = upper(cust_state)
WHERE cust_country = 'USA';

UPDATE vendors
SET vend_state = upper(vend_state)
WHERE vend_country = 'USA';

DELETE
FROM customers
WHERE cust_id = '1000000007';

----------
-- Урок 17
----------
-- Создание таблиц и работа с ними

ALTER TABLE vendors
    ADD vend_phone char(20);

ALTER TABLE vendors
DROP COLUMN vend_phone;

DROP TABLE cust_copy;

-- Упражнения

ALTER TABLE vendors
    ADD vend_web char(100);

UPDATE vendors
SET vend_web = 'https://translate.yandex.ru/?source_lang=en&target_lang=ru'
WHERE vend_id = 'FRB01';

----------
-- Урок 18
----------
-- Представления

CREATE VIEW ProductCustomers AS
SELECT cust_name, cust_contact, prod_id
FROM customers c,
     orders o,
     orderitems oi
WHERE c.cust_id = o.cust_id
  AND oi.order_num = o.order_num;

SELECT cust_name, cust_contact
FROM productcustomers
WHERE prod_id = 'RGAN01';

CREATE VIEW vendors_locations AS
SELECT rtrim(vend_name) || ' (' ||
       rtrim(vend_country) || ')' AS vend_title
FROM vendors;

CREATE OR REPLACE VIEW customers_email_list AS
SELECT cust_id, cust_name, cust_email
FROM customers
WHERE cust_email IS NOT NULL;

CREATE VIEW orderitems_expanded AS
SELECT order_num,
       prod_id,
       quantity,
       item_price,
       quantity * item_price AS expanded_price
FROM orderitems;

SELECT *
FROM orderitems_expanded
WHERE order_num = 20008;

-- Упражнения

CREATE VIEW customers_with_orders AS
SELECT c.*
FROM customers c INNER JOIN orders o
ON c.cust_id = o.cust_id;

----------
-- Урок 19
----------
-- Хранимые процедуры

CREATE OR REPLACE PROCEDURE MailingListCount (
    OUT ListCount INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
SELECT count(*)
INTO ListCount
FROM customers
WHERE cust_email IS NOT NULL;
END;
$$
;

DO
$$
    DECLARE
    ReturnValue INTEGER;
    BEGIN
        CALL MailingListCount(ReturnValue);
        RAISE NOTICE 'ReturnValue: %', ReturnValue;
    END
$$;

----------
-- Урок 21
----------
-- Курсоры

DO $$
    DECLARE
        CustCursor CURSOR FOR
            SELECT *
            FROM customers
            WHERE cust_email IS NULL;
        cust_record RECORD;  -- Переменная для хранения строки из курсора
    BEGIN
        -- Открытие курсора
        OPEN CustCursor;

        -- Цикл для извлечения данных из курсора
        LOOP
            FETCH CustCursor INTO cust_record;
            EXIT WHEN NOT FOUND;  -- Выход из цикла, если больше нет строк

            -- Здесь можно обработать каждую строку, например, вывести данные
            RAISE NOTICE 'Customer ID: %, Email: %', cust_record.id, cust_record.cust_email;
        END LOOP;

        -- Закрытие курсора
        CLOSE CustCursor;
    END $$;

----------
-- Урок 22
----------
-- Индексы

CREATE INDEX prod_name_ind
    ON products (prod_name);

-- Триггеры

CREATE TRIGGER customer_state
AFTER INSERT OR UPDATE
FOR EACH ROW
BEGIN
UPDATE customers c
SET cust_state = upper(cust_state)
WHERE c.cust_id = :OLD.cust_id
END;

