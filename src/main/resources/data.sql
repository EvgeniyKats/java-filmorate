MERGE INTO genre AS t 
USING (SELECT 1 AS id, 'Комедия' AS name
       UNION ALL
	   SELECT 2 AS id, 'Драма' AS name
       UNION ALL
	   SELECT 3 AS id, 'Мультфильм' AS name
       UNION ALL
	   SELECT 4 AS id, 'Триллер' AS name
       UNION ALL
	   SELECT 5 AS id, 'Документальный' AS name
       UNION ALL
	   SELECT 6 AS id, 'Боевик' AS name) AS s ON t.genre_id = s.id
WHEN NOT MATCHED THEN
INSERT VALUES(DEFAULT, s.name);

MERGE INTO rating_mpa AS t 
USING (SELECT 1 AS id, 'G' AS name
       UNION ALL
	   SELECT 2 AS id, 'PG' AS name
       UNION ALL
	   SELECT 3 AS id, 'PG-13' AS name
       UNION ALL
	   SELECT 4 AS id, 'R' AS name
       UNION ALL
	   SELECT 5 AS id, 'NC-17' AS name) AS s ON t.rating_mpa_id = s.id
WHEN NOT MATCHED THEN
INSERT VALUES(DEFAULT, s.name);