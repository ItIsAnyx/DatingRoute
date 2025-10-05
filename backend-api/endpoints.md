# Auth (/api/auth)

## 1. Register
```http request
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
    "email": "email@gmail.com",
    "login": "login",
    "password": "password"
}
```
Response:
```json
{
    "token": "token"
}
```

## 2. Login
```http request
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
    "email": "email@gmail.com",
    "password": "password"
}
```
Response:
```json
{
    "token": "token"
}
```

## 3. Refresh Token

```http request
GET http://localhost:8081/api/auth/refresh-token
```

# User (/api/users)

## 1. Get all users

```http request
GET http://localhost:8081/api/users
Authorization: Bearer {{token}}
```
Response:
```json
[
    {
        "id": 1,
        "email": "email@gmail.com",
        "login": "login",
        "password_hash": "kfjsdfjsdhu",
        "role": "USER"
    },
    {
        "id": 2,
        "email": "email@gmail.com",
        "login": "login",
        "password_hash": "kfjsdfjsdhu",
        "role": "ADMIN"
    }
]
```

## 2. Get current user

```http request
GET http://localhost:8081/api/users/me
Authorization: Bearer {{token}}
```
Response:
```json
{
    "id": 1,
    "email": "email@gmail.com",
    "login": "login",
    "password_hash": "kfjsdfjsdhu",
    "role": "USER"
}
```

# Chat

## 1. Get all chats which are related to user

```http request
GET http://localhost:8080/api/chats
Authorization: Bearer {{token}}
```
Response: 
```json
[
    {
        "id": 1,
        "title": "title"
    },
    {
        "id": 2,
        "title": "title"
    }
]
```

## 2. Create new chat
Пользователь отправляет первое сообщение к ИИ. ИИ генерирует заголовок для чата.

Данный endpoint используется только для первичного создания чата. Дальнейшие взаимодействие с чатом происходят в пункте 5. 

```http request
POST http://localhost:8081/api/chats/
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "content": "content"
}
```
Response:
```json
{
    "id": 1,
    "title": "title"
}
```

## 3. Rename chat 
```http request
PUT http://localhost:8081/api/chats/{id}
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "title": "title"
}
```
Response:
```json
{
    "id": 1, 
    "title": "title"
}
```

## 4. Delete chat
```http request
DELETE http://localhost:8081/api/chats/{id}
Authorization: Bearer {{token}}
```
Response:
```json
{
    "id": 1
}
```

## 5. Get message in the chat
```http request
GET http://localhost:8081/api/chats/{id}/messages?size={size}&page={page}
Authorization: Bearer {{token}}
```
Response:

message_type: "USER_MESSAGE", "AI_MESSAGE", "CONFIRMATION", "ERROR"

```json
{
  "page": 1,
  "size": 2,
  "messages": [
    {
      "sender": "AI",
      "content": "content",
      "message_type": "AI_MESSAGE",
      "time": "2012-04-23T18:25:43.511Z"
    },
    {
      "sender": "me",
      "content": "content",
      "message_type": "USER_MESSAGE",
      "time": "2012-04-23T18:25:43.511Z"
    }
  ]
}
```
## 6. Send messages
Отправка сообщений для уточнения или создания маршрута будут происходит в WebSocket

Пока что делаю

# AI

## 1. Send message to AI

```http request
POST http://localhost:8082/send-message
Content-Type: application/json
AI_SECRET_KEY: {{secret_key}}

{
    "content": "content"
}
```
Response:
```json
{
    "content": "content"
}
```

## 2. Create chat
```http request
POST http://localhost:8082/create-chat
AI_SECRET_KEY: "secret_key"

{
    "content": "content"
}
```
Response: 
```json
{
  "title": "title",
  "content": "content"
}
```

## 3. Confirmation route by user
При данном запросе:
1. Пользователь подтверждает генерацию маршрута
2. ИИ присылает json с точками на маршруте согласно формату API выбранной карты
3. Делается запрос к API картам с полученным json
4. Получаем ответ от API карт
5. Вывод полученный ответ на карту на фронте

TO_DO:

- [ ] Узнать у Сани какой формат должен приходит на API
- [ ] Научить ИИ выводит данный формат через запросы пользователя
- [ ] Выводит ответ от API на фронте

```http request
GET http://localhost:8082/confirmation
AI_SECRET_KEY: {{secret_key}}
```
Response:

TOD