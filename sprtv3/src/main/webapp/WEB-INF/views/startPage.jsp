<%--
  Created by IntelliJ IDEA.
  User: sglass
  Date: 03.09.2021
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello!</title>
</head>
<body>
 URLs                                                               : commands
<br>
 <br>
 GET http://localhost:8080/items                                    : Show items
<br>
 <br>
 GET http://localhost:8080/items/{id}                               : Show item by ID
 <br>
 <br>
 POST http://localhost:8080/items/{id}                              : Add item to order
 <br>
 <br>
 http://localhost:8080/items/categories                             : Show categories
 <br>
 <br>
 http://localhost:8080/items/categories/shoes                       : Show shoes
 <br>
 <br>
 http://localhost:8080/items/categories/jackets                     : Show jackets
 <br>
 <br>
 GET http://localhost:8080/orders                                   : Show ur orders
 <br>
 <br>
 DELETE http://localhost:8080/orders/{id}                           : Delete item from order by ID
 <br>
 <br>
 POST http://localhost:8080/orders                                  : Buy all items from order
 <br>
 <br>
 POST http://localhost:8080/login?login={urLogin}&pass={urPassword} : Autorization
 <br>
 <br>
 GET http://localhost:8080/account                                  : Users account
 <br>
 <br>
</body>
</html>
