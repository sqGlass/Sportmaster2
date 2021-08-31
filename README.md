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


2) Просмотр всех товаров:
	curl -X GET "http://localhost:8080/items"

3) Посмотреть конкретный товар по ID:
	curl -X GET "http://localhost:8080/items/{id}"
	curl -X GET "http://localhost:8080/items/3"

4) Добавить товар в корзину:
	curl -X POST "http://localhost:8080/items/{id}"
	curl -X POST "http://localhost:8080/items/3"



5) Просмотр категорий товаров:
	curl -X GET "http://localhost:8080/items/categories"

6) Просмотр товаров типа "shoes":
	curl -X GET "http://localhost:8080/items/categories/shoes"

7) Просмотр товаров типа "jackets":
	curl -X GET "http://localhost:8080/items/categories/jackets"


8) Удалить товар из корзины:
	curl -X DELETE "http://localhost:8080/orders/{id}"
	curl -X DELETE "http://localhost:8080/orders/3"

9) Просмотр корзины авторизованного пользователя:
	curl -X GET "http://localhost:8080/orders"

10) Купить все товары из корзины
	curl -X POST "http://localhost:8080/orders"


11) Просмотр аккаунта пользователя:
	curl -X GET "http://localhost:8080/account"

12) Совершить покупку:
	curl -X POST "http://localhost:8080/myshopper/buy"


