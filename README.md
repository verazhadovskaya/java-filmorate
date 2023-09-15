# java-filmorate

**База данных (промежуточное задание спринта 11)**

![image](https://github.com/verazhadovskaya/java-filmorate/assets/130570412/26702bbe-795f-412a-89eb-a7de7156d587)


**Примеры запросов**
1. для получения всех пользователей
  SELECT * 
  FROM users

2. Для получения всех друзей пользователя c ID = 1
   SELECT u.friend_id
   FROM users u
   INNER JOIN friend_request fr ON u.id=fr.user_id
   INNER JOIN request_status rf ON fr.request_status_id=rf.id AND rf.request_status= 'подтверждённая'
   WHERE user_id = 1
   UNION
   SELECT u.user_id
   FROM users u
   INNER JOIN friend_request fr ON u.id=fr.friend_id
   INNER JOIN request_status rf ON fr.request_status_id=rf.id AND rf.request_status= 'подтверждённая'
   WHERE friend_id = 1 

  -Предполагаю, что в таблицу friend_request будет поступать инсерт в случае когда пользователь A отправил запрос на добавления в друзья пользователю B-
  -После одобрения заявки пользовталем B, пользователь A и B становятся друзьями, но в таблице не дублируем обратную связь (B - A и A - B)-
  -Поэтому делаем union-

3. Для получения фильма по ИД
   SELECT *
   FROM films
   WHERE id = 1

4. Для получения списка пользователей, которые поставили лайк фильму с ИД = 1
   SELECT *
   FROM films f
   INNER JOIN  film_likes fl ON f.id= fl.film_id
   INNER JOIN users u ON fl.user_id = u.id
   WHERE f.id= 1

