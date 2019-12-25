# MpeiX - приложение МЭИ здорового человека

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/tonykolomeytsev/mpeiapp?label=version) 
![](https://github.com/tonykolomeytsev/mpeiapp/workflows/Android%20build/badge.svg?branch=master)

Расписание пар, карта корпусов и личный кабинет БАРС для студентов НИУ МЭИ. Самое правильное приложения с расписанием МЭИ. 

+ Бесконечная лента с расписанием
+ Табличка с недельным расписанием
+ Карта корпусов, общежитий, мест общепита
+ Личный кабинет БАРС с просмотром оценок
+ Приємний дэсiгн 
+ Малый вес приложения 
+ Открiтый исходнiй код 
+ Меми.🤗💪😸😃

Пулл реквесты приветствуются.

[Скачать](https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp) приложение с Google Play.

![screenshots](https://github.com/tonykolomeytsev/mpeiapp/blob/master/screenshots/1.png)

### Требования приложения

+ Api version 21+ (Android 5 Lollipop)
+ Доступ к интернету
+ Доступ к геолокации

### Стек
 
+ Многомодульная, упрощенная **Clean Architecture** с активным использованием **Koin**.
+ Kotlin Coroutines
+ Android Arch 
  - Navigation Component
  - LiveData
  - Room
+ Firebase 
  - Firestore
  - Crashlytics
  - Analytics
  - Remote Config
+ Для скрапинга веб-сайтов используется Jsoup

### Архитектура

Приложение многомодульное, с жестким следованием Dependency Rule. Схема может быть не очень актуальной на момент прочтения (могут появится новые модули, или быть удалены старые), но суть должна быть понятна).
Все фрагменты используют паттерн MVP.

![screenshots](https://github.com/tonykolomeytsev/mpeiapp/blob/master/screenshots/2.jpg)

**Все модули зависят от `core`**

+ **app** - главный модуль, в котором происходит инициализация компонентов Dagger.
+ **mainscreen** - модуль, в котором находится Activity приложения, провайдится Router для навигации.
+ **feed** - модуль, в котором находится Fragment экрана с лентой расписания.
+ **timetable** - модуль, в котором находится Fragment экрана с недельным расписанием с ViewPager.
+ **map** - модуль, в котором находится Fragment экрана карты.
+ **bars** - модуль, в котором находится фрагменты личного кабинета БАРС.
+ **update** - модуль, в котором находится Force Update Dialog Fragment.
+ **settings** - модуль, в котором находится фрагменты раздела настроек.
+ **coreui** - модуль, в котором находятся общие для всех видимых фич Views, графические ресурсы.
+ **core** - модуль, в котором находятся интерфейсы всех Repository, UseCase и ВООБЩЕ все интерфейсы, от которых зависят другие модули.
+ **repository** - модуль, работающий с БД, интернетом и Firebase. Предоставляет всем зависимости репозиториев приложения.
+ **domain** - модуль, хранящий в себе все usecase'ы приложения и предоставляющий их всем, кому надо.
