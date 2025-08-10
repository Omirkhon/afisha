# Афиша

## Эндпоинты для рейтингов
Путь в проекте: `./afisha-server/src/main/java/com/practice/afisha/user`

` POST users/{userId}/ratings/{eventId}?liked={liked} ` - cоздание рейтинга (оценки) для события по указанному `{eventId}` от пользователя по переданному `{userId}`. Параметр `liked` принимает значения `true` или `false` в зависимости от того понравилось ли пользователю событие. Оставить рейтинг может только пользователь, который принимал участие в событии.  
Метод: `UserController.createRating()`
  
` PATCH users/{userId}/ratings/{ratingId}?liked={liked} ` - изменение уже оставленной оценки событию по уникальному идентификатору `{ratingId}`.  
Метод: `UserController.updateRating()`

` DELETE users/{userId}/ratings/{ratingId} ` - удаление рейтинга по уникальному идентификатору `{ratingId}`.  
Метод: `UserController.deleteRating()`

` GET users/ratings/events/{eventId}?from={from}&size={size} ` - получение списка всех рейтингов оставленных событию по указанному идентификатору `{eventId}`.  
Метод: `UserController.findRatingsByEventId()`

` GET users/ratings/events/likes?from={from}&size={size} ` - получение списка всех событий, отсортированных по значению разницы между отметками `LIKED` и `DISLIKED` в убывающем порядке.  
Метод: `UserController.findAllEventsByLikesRatio()`

` GET users/ratings/events/top ` - получение информации о событии, имеющего наибольшее значение разницы между отметками `LIKED` и `DISLIKED`.  
Метод: `UserController.findTheMostLikedEvent()`

` GET users/{userId}/ratings?from={from}&size={size} ` - получение всех оценок, оставленных пользователем по идентификатору `userId`.  
Метод: `UserController.findRatingsByUserId()`

` GET users{userId}/ratings/events?from={from}&size={size} ` - получение всех оценок, оставленных всем событиям, организованным пользователем по идентификатору `userId`.  
Метод: `UserController.findRatingsForAllUsersEvents()`

` GET users/ratings/likes?from={from}&size={size} ` - получение списка пользователей, отсортированного по значению разницы между отметками `LIKED` и `DISLIKED` в убывающем порядке, оставленных их событиям.  
Метод: `UserController.findAllInitiatorsSortedByLikesRatio()`

` GET users/ratings/top ` - получение информации о пользователе, имеющего наибольшее значение разницы между отметками `LIKED` и `DISLIKED`, оставленных его событиям.  
Метод: `UserController.findMostLikedInitiator()`
