### Диаграмма
![ER](https://github.com/EvgeniyKats/java-filmorate/blob/add-er-diagram/src/main/resources/ER.png?raw=true)

[Ссылка на диаграмму](https://github.com/EvgeniyKats/java-filmorate/blob/add-er-diagram/src/main/resources/ER.png?raw=true)

### Прототип команд, написаны в блокноте, не проверены
###### [Получение всех фильмов]
```sql
SELECT f.film_id,
       f.name,
       f.description,
       CAST(f.release AS date),
       f.duration,
       g.name,
       r.name	   
FROM film AS f
INNER JOIN film_genre AS fg ON f.film_id = fg.film_id
INNER JOIN genre AS g ON fg.genre_id = g.genre_id
INNER JOIN rating AS r ON f.rating_id = rating.rating_id;
```

###### [Получение всех пользователей]
```sql
SELECT u.user_id,
       u.login,
       u.name,
       CAST(u.birthday AS date),
       e.email_address	   
FROM user AS u
INNER JOIN email AS e ON u.user_id = e.user_id;
```

###### [Топ N фильмов]
```sql
SELECT f.name,
       COUNT(fl.user_id) AS count_likes
FROM film_like AS fl
INNER JOIN film AS f ON fl.film_id = f.film_id
GROUP BY f.name
ORDER BY count_likes DESC
LIMIT [N];
```

###### [Список общих друзей пользователя A с пользователем B]
```sql
SELECT uf.friend_id
FROM user_friend AS uf
WHERE user_id = [A_ID] AND friend_id IN (SELECT friend_id 
                                         FROM user_friend 
                                         WHERE user_id = [B_ID]
					);
```
### Исходный скрипт quickdatabasediagrams.com
```
film 
-
film_id bigint PK
name varchar
description char(200)
release date
duration integer
rating_id varchar FK >- rating.rating_id


user
-
user_id bigint PK
login varchar
name varchar
birthday date


email
-
email_address varchar PK
user_id bigint UNIQUE FK - user.user_id


rating
-
rating_id integer
name varchar


genre
-
genre_id integer PK
name varchar

film_genre
-
id bigint PK
film_id bigint FK >- film.film_id
genre_id integer FK >- genre.genre_id


user_friend
-
id bigint PK
user_id bigint FK >- user.user_id
friend_id bigint FK >- user.user_id


film_like
-
id bigint PK
film_id bigint FK >- film.film_id
user_id bigint FK >- user.user_id
```

