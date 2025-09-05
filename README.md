## Исходное приложение
Доступно на сайте разработчика https://demo.jmix.io/petclinic

#autotests
Тесты web и rest

#ссылка на тестируемый проект (форк с минимальными изменениями для тестирования rest-api)
https://github.com/isunagatov/jmix-petclinic-2.git

docker-compose для selenoid:
/selenoid/docker-compose.yml /selenoid/browsers.json

# локальный запуск тестируемого проекта
Для старта приложения необходимы 
1. Установленный PostgreSQL
2. Установленный плагин jMix в iDea
3. В application.properties указать DB pass
4. При запуске база создастся

# Запуск автотестов
Необходимо настроить test/resources/conf.xml :

- указать **selenoid = false**, для запуска без selenoid proxy
- <SubSystem> <url> указать url хоста приложения, по умолчанию localhost

- Для связи с базой указать свои креды к базе
   <code> \
   \<DBconfig> \
        <!--### connect database-->
        \<subSystem>Petclinic</subSystem> \
        \<databaseHost>**localhost:5432**</databaseHost> \
        \<databaseName>**jmixpetclinic5**</databaseName> \
        \<databaseUser>postgres</databaseUser> \
        \<databasePass>**dbpass**</databasePass> - ваш пароль \
    \</DBconfig> \
	</code>
- Для запуска через Selenoid указать его эндпоинт
   - <selenoidUrl>**http://192.168.xx.xx**:4444/wd/hub</selenoidUrl>
	 
- Запуск selenoid из каталога проекта selenoid (необходим установленный docker) 
   - **docker-compose up -d**
	
- Запуск автотестов в терминале задачей gradle
   - **gradle testNG**
- Запуск добавлением конфигурацию запуска через TestNG для выбранного метода/класса
   - для UI добавить в VM options значение: **-ea -Dtestng.dtd.http=true**