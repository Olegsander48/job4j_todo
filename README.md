## job4j_cinema
[![Java CI with Maven](https://github.com/Olegsander48/job4j_todo/actions/workflows/maven.yml/badge.svg)](https://github.com/Olegsander48/job4j_todo/actions/workflows/maven.yml)
## Описание проекта:
Job4j_todo является todo-листом (планировщик задач). Он предоставляет функциональность для создания и управления задачами, позволяя контролировать процесс выполнения работы, отслеживать прогресс и освободить себя от груза ответсвенности "помнить все". Вы можете создавать, редактировать, удалять задачи, а так же изменять статус. 

## Стек технологий:
- Java 21
- PostgreSQL 17
- Maven 3.9.9
- Liquibase 4.15.0
- Spring boot-web 3.5.4
- Spring boot-thymeleaf 3.5.4
- Spring boot-test 3.5.4
- Hibernate 5.6.11.Final
- H2 db 2.2.220
- Lombok 1.18.38
- Jacoco 0.8.10

## Требования к окружению:
- Java 21
- PostgreSQL 17
- Maven 3.9.9

## Запуск проекта:
1. Необходимо создать базу данных **todo**
```SQL
create database todo
```
2.  Теперь небходимо создать таблицы про помощи скриптов Liquibase. Необходимо выполнить фазу **через Maven**:
```
liquibase:update
```
Теперь у нас БД готова к использованию

3. Перейдите на [страницу](https://github.com/Olegsander48/job4j_todo/actions/workflows/jar.yml) и выберите последний выполненный worflow, скачайте my-app-jar
4. Из архива достаньте файл todo-0.0.1-SNAPSHOT.jar
5. Перейдите в директорию с jar-файлом, выполните команду:
```CMD
java -jar todo-0.0.1-SNAPSHOT.jar
```
6. Приложение запущено, перейдите по [адресу, указанному в командной строке,](http://localhost:8080/) и пользуйтесь приложением
   
## Интерфейс приложения:
1. Страница создания задачи
![Страница создания задачи](https://github.com/Olegsander48/job4j_todo/blob/master/images/create_task.png)
2. Страница списка всех задач
![Страница списка всех задач](https://github.com/Olegsander48/job4j_todo/blob/master/images/tasks.png)
Имеется возможность отфильтровать задачи по статусу
![Фильтр](https://github.com/Olegsander48/job4j_todo/blob/master/images/filter_tasks.png)
3. Страница просмотра задачи. Тут содержится базовый функционал управления задачей 
![Страница задачи](https://github.com/Olegsander48/job4j_todo/blob/master/images/task.png)
4. При нажатии на копку "редактировать" открывается окно редактирования задачи
![Страница сессий в кинотеатрах](https://github.com/Olegsander48/job4j_todo/blob/master/images/edit_task.png)

## Мои контакты:
Обращайтесь по любым вопросам и обратной связи, буду рад ответить :blush::blush:
<p align="center">
<a href="https://t.me/Olegsander48" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/telegram.svg" alt="olegsander48" height="30" width="30" /></a>&nbsp;
<a href="https://linkedin.com/in/aleksandr-prigodich-b7028a1b3" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/linkedin.svg" alt="aleksandr-prigodich" height="30" width="30" /></a>&nbsp;
<a href="http://discord.com/users/olegsander48" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/discord.svg" alt="olegsander48" height="40" width="30" /></a>&nbsp;
<a href="mailto:prigodichaleks@gmail.com?subject=Hi%20Aleks.%20I%20saw%20your%20GitHub%20profile%20&body=I'm%20writing%20to%20you%20because%20...%0A"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/gmail.svg" alt="prigodichaleks@gmail.com" height="40" width="30" /></a>&nbsp;
</p>
