# Афиша

## Эндпоинты для рейтингов
Путь в проекте: `./afisha-server/src/main/java/com/practice/afisha/rating`

` POST /ratings/{userId}/{eventId}?liked={liked} ` - cоздание рейтинга (оценки) для события по указанному `{eventId}` от пользователя по переданному `{userId}`. Параметр `liked` принимает значения `true` или `false` в зависимости от того понравилось ли пользователю событие. Оставить рейтинг может только пользователь, который принимал участие в событии.  
Метод: `RatingController.create()`
  
` PATCH /ratings/{ratingId}/{userId}?liked={liked} ` - изменение уже оставленной оценки событию по уникальному идентификатору `{ratingId}`.  
Метод: `RatingController.update()`

` DELETE /ratings/{ratingId}/{userId} ` - удаление рейтинга по уникальному идентификатору `{ratingId}`.  
Метод: `RatingController.delete()`

` GET ratings/events/{eventId}?from={from}&size={size} ` - получение списка всех рейтингов оставленных событию по указанному идентификатору `{eventId}`.  
Метод: `RatingController.findByEventId()`

` GET ratings/events/likes?from={from}&size={size} ` - получение списка всех событий, отсортированных по значению разницы между отметками `LIKED` и `DISLIKED` в убывающем порядке.  
Метод: `RatingController.findAllByLikesRatio()`

` GET ratings/events/top ` - получение информации о событии, имеющего наибольшее значение разницы между отметками `LIKED` и `DISLIKED`.  
Метод: `RatingController.findTheMostLikedEvent()`

` GET ratings/users/{userId}?from={from}&size={size} ` - получение всех оценок, оставленных пользователем по идентификатору `userId`.  
Метод: `RatingController.findByUserId()`

` GET ratings/initiators/{userId}?from={from}&size={size} ` - получение всех оценок, оставленных всем событиям, организованным пользователем по идентификатору `userId`.  
Метод: `RatingController.findRatingsForAllUsersEvents()`

` GET ratings/initiators/likes?from={from}&size={size} ` - получение списка пользователей, отсортированного по значению разницы между отметками `LIKED` и `DISLIKED` в убывающем порядке, оставленных их событиям.  
Метод: `RatingController.findAllInitiatorsSortedByLikesRatio()`

` GET ratings/initiators/top ` - получение информации о пользователе, имеющего наибольшее значение разницы между отметками `LIKED` и `DISLIKED`, оставленных его событиям.  
Метод: `RatingController.findMostLikedInitiator()`
