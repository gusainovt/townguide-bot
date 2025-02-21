CREATE TABLE contract
(
    id     INTEGER PRIMARY KEY,
    client VARCHAR(100),
    data   DATE
);

INSERT INTO contract (id, client, data)
VALUES (1, 'Jon', '2000-01-01'),
       (2, 'Jon', '2000-01-02'),
       (3, 'Jon', '2000-01-03'),
       (4, 'Alice', '2000-01-04'),
       (5, 'Alice', '2000-01-05'),
       (6, 'Alice', '2000-01-06'),
       (7, 'Bob', '2000-01-07'),
       (8, 'Bob', '2000-01-08'),
       (9, 'Bob', '2000-01-09');

SELECT client, MAX(data)
FROM contract
GROUP BY client;
