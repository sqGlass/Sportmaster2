# Sportmaster2

1. Сценарий заказа спорт. товаров

Пользователь может увидеть типы товаров ("shoes", "jackets"), может посмотреть конкретные товары каждого типа ("shoes" -> "Zoom freak 3"), также может зайти внутрь каждого товара.

Авторизованный пользователь также может добавлять товар в корзину (это действие уберет товар из ассортимента магазина), удалять товар из корзины (товар вернется в магазин), купить товары из корзины (если позволяет баланс пользователя).
При этом можно авторизироваться под другим пользователем, не потеряв данные первого.
Также при авторизации пользователя генерируется "dailyDiscount" от 1% до 30%. При повторной авторизации в течение одной сессии новая генерация не произойдет.

Команды:
0) Список доступных команд:
	curl -X GET "http://localhost:8080/"

1) Авторизация:
	curl -X POST "http://localhost:8080/login?login={urLogin}&pass={urPassword}"
доступные аккауны:
	curl -X POST "http://localhost:8080/login?login=sasha1337&pass=1234"
	curl -X POST "http://localhost:8080/login?login=vasyaKill&pass=777"

2) Просмотр типов товаров:
	curl -X GET "http://localhost:8080/items"

3) Просмотр товаров типа "shoes":
	curl -X GET "http://localhost:8080/items/shoes"

4) Просмотр товаров типа "jackets":
	curl -X GET "http://localhost:8080/items/jackets"

5) Посмотреть конкретный товар по ID:
	curl -X GET "http://localhost:8080/items/id/{id}"
	curl -X GET "http://localhost:8080/items/id/3"

6) Добавить товар в корзину:
	curl -X POST "http://localhost:8080/items/id/{id}"
	curl -X POST "http://localhost:8080/items/id/3"

7) Удалить товар из корзины:
	curl -X DELETE "http://localhost:8080/myshopper/delete/{id}"
	curl -X DELETE "http://localhost:8080/myshopper/delete/3"

8) Просмотр корзины авторизованного пользователя:
	curl -X GET "http://localhost:8080/myshopper"

9) Просмотр аккаунта пользователя:
	curl -X GET "http://localhost:8080/account"

10) Совершить покупку:
	curl -X POST "http://localhost:8080/myshopper/buy"


