# Dating Route (ENG/RUS)

**ENG**

DatingRoute is a flexible web application for planning routes using an AI assistant.
The application allows users to communicate with the DeepSeek-V3.2 neural network, which helps plan routes for dates, walks with friends, or leisure activities. Based on a dialog with the user, the AI generates a route and provides a ready-to-use link to Yandex Maps with all selected locations.

**Local setup instructions:**
1) Clone the repository.

2) Create a `.env` file in the root directory of the project using `env.example` as a reference.

3) Fill in the .env file with your own values:
```env
DB_DATABASE - the name of your database
DB_USERNAME - the database username
DB_PASSWORD - the password for your database

AI_BACKEND_KEY - any string, this key is used to get access to the methods implemented in Python/AI/ai.py
JWT_SECRET_KEY - any long string

REDIS_PASSWORD - the password for Redis, a long string

AI_SECRET_KEY - a secret API key from DeepSeek in the developer dashboard. It can be obtained by registering on the DeepSeek platform at https://platform.deepseek.com/sign_in
GEOCODER_KEY - a secret API key for the Yandex Maps geocoder. It can be obtained by registering in the Yandex developer dashboard at https://developer.tech.yandex.ru/services
GEOSUGGEST_KEY - a secret API key for the Yandex Maps geosuggest service. It can also be obtained in the Yandex developer dashboard at https://developer.tech.yandex.ru/services
```

The .env file must not be committed to the repository.

4) Start Docker Engine.

5) Open a terminal in the project root directory and run:
```cmd
docker compose up -d --build
```

6) After successful startup, open the application in your browser: http://localhost:8080

---

**RUS**

DatingRoute - это гибкое веб-приложение для планирования маршрутов с использованием ИИ-ассистента.
Приложение позволяет общаться с нейросетью DeepSeek-V3.2, которая помогает спланировать маршрут для свидания, прогулки с друзьями или отдыха. На основе диалога с пользователем модель формирует маршрут и предоставляет готовую ссылку на Яндекс Карты со всеми выбранными точками.

**Для запуска приложения на локальном устройстве:**

1) Клонируйте репозиторий.

2) Создайте файл `.env` в корневой папке проекта, используя `env.example` в качестве шаблона.

3) Заполните .env информацией:
```env
DB_DATABASE - название вашей базы данных
DB_USERNAME - имя пользователя базы данных
DB_PASSWORD - пароль от вашей базы данных

AI_BACKEND_KEY - любая строка, данный ключ используется для получения доступа к методам, реализованным в Python/AI/ai.py
JWT_SECRET_KEY - любая длинная строка

REDIS_PASSWORD - пароль от Redis, длинная строка

AI_SECRET_KEY - секретный API-ключ от DeepSeek в личном кабинете для разработчиков. Получить его можно, зарегистрировавшись на платформе DeepSeek по ссылке https://platform.deepseek.com/sign_in
GEOCODER_KEY - секретный API-ключ геокодера Яндекс Карт. Получить его можно, зарегистрировавшись в кабинете разработчика Яндекса по ссылке https://developer.tech.yandex.ru/services
GEOSUGGEST_KEY - секретный API-ключ геосаджеста Яндекс Карт. Его также можно получить в кабинете разработчика Яндекса по ссылке https://developer.tech.yandex.ru/services
```

Помните, что .env не должен добавляться в ваш репозиторий.

4) Запустите Docker Engine.

5) Откройте консоль в корневой папке проекта и напишите команду:
```cmd
docker compose up -d --build
```

6) После запуска приложение будет доступно по адресу: http://localhost:8080
