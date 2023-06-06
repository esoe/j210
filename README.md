# j210
## J210 : разработка веб-сервисов
Лабораторная работа No1 по курсу «DEV-J210. JavaEE.Разработка web сервисов»


## Содержание работы
Доработать приложение из курса DEV-J200:
1. Написать класс Transformer, который будет преобразовывать entity-класс Client (с учетом вложенных объектов типа Address), полученные из БД, в XML-документ “Clients.xml”.

2. Написать класс DemoSAX, который на основе SAX разбирает файл Clients.xml

3. Написать сервлет CheckSAX, который получает от пользователя запрос с параметром поиска, в соответствии со значением которого, в XML-документе производится поиск объектовпо содержимому поля «Наименование клиента». Сведения о клиентах и их адресах, удовлетворяющие условиям поиска, извлекаются из XML-документа с использованием класса DemoSAX. Предварительно, перед произведением поиска, XML-документ должен обновляться с использованием класса Transformer. В случае отсутствия объектов, удовлетворяющих условиям поиска, сервлет должен отправить соответствующее сообщение пользователю.

4. Написать класс DemoDOM, который на основе DOM разбирает файл Clients.xml

5. Написать сервлет CheckDOM, который получает от пользователя запрос с параметром поиска, в соответствии со значением которого, в XML-документе производится поиск объектов по содержимому поля «Наименование клиента».

Сведения о клиентах и их адресах, удовлетворяющие условиям поиска, извлекаются из XML-документа с использованием класса DemoDOM. Предварительно, перед произведением поиска, XML-документдолжен обновляться с использованием класса Transformer. В случае отсутствия объектов, удовлетворяющих условиям поиска, сервлет должен отправить соответствующее сообщение пользователю.
