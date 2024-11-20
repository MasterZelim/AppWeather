<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h1 {
            color: #007BFF;
            margin-top: 20px;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
            width: 300px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            background-color: #007BFF;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            background-color: #0056b3;
        }

        .messageSuccess {
            margin-top: 20px;
            color: #13cc2b;
        }
        .messageError {
            margin-top: 20px;
            color: #ff0000;
        }
    </style>
</head>
<body>
<h1>Регистрация</h1>
<form action="/register" method="post">
    <label for="userLogin">Имя пользователя:</label>
    <input type="text" id="userLogin" name="userLogin" th:value="${user.username}" required>
    <br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" th:value="${user.password}" required>
    <br>

    <label for="confirmPassword">Подтвердите пароль:</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required>
    <br>

    <button type="submit">Зарегистрироваться</button>
</form>
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
        if (message.equals("Пароли не совпадают.")) {
%>
<div class="messageError">
    <p><%= message %></p>
</div>
<%}}%>
</body>
</html>