# geodata-table-app

Три экрана:
1. При запуске. Показывает текстовые поля логин/пароль и кнопку "Войти". При нажатии идет к серверу GET (параметры запроса: username=XXX, password=XXX), он возвращает JSON. Если "status" == "ok", то пропускаем, нет - показываем красиво, что логин/пароль неправильные. 
Сервер выдаст "ok" на "test"/"123" и тогда идем на следующий экран, запоминая "code".
2. Таблица с данными. Данные получаем по GET (параметры запроса: code=XXX из предыдущего шага, p=N - страница с 1), 
выдает по 10 элементов. В приложении - отображается как бесконечная пагинация. Доходим до "низа" списка - подгружаем данные. Каждый элемент таблицы должен содержать картинку 
(выберите любой внешний URL), подгружаемую асинхронно и имя (name из полученных данных). При нажатии на элемент, переходим на третий экран.
3. По нажатию на элемент на втором экране переходим сюда и показываем все поля и карту с точкой по координатам в полях "lat"/"lon" из JSON. Даем возможность вернуться к списку.
