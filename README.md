Spring rest API приложение.

Для запуска нужен maven.
1. Запускаем MySQL из контейнера docker командой docker-compose up -d
2. запускаем приложение команда mvn spring-boot:run
В БД будет создана база geodb user: root, password: secretpassw#

Заходим на localhost:8080 
Переходим по ссылке Register Here и регистрируем нового пользователя.
Логинимся с зарегистрированными e-mail и password
В веб форме доступны работа с geoclasses (добавление и просмотр) localhost:8080/geoclasses
и с пользователями localhost:8080/users localhost:8080/register

Кроме этого доступны API endpoints (для использования можно не логинится)

GET /sections/by-code/{code} returns a list of all Sections that have geologicalClasses with the specified code.
GET /byid/{id} - returns a geologicalClasses with the specified id.
GET /secdto returns a list of all Sections

для экспорта в файл можно настроить путь к папке в файле application.properties параметр: app-configs.outFilesLocation 
POST /api/import (file) returns ID of the Async Job and launches importing.
GET /api/import/{id} returns result of importing by Job ID ("COMPLETED", "IN PROGRESS", "ERROR")
GET /api/export returns ID of the Async Job and launches exporting.
GET /api/export/{id} returns result of parsed file by Job ID ("COMPLETED", "IN PROGRESS", "ERROR")
GET /api/export/{id}/file returns a file by Job ID (throw an exception if exporting is in process)


