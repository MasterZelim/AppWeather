<%@ page import="com.example.appweather.util.ConfigurationUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Авторизация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        h1 {
            margin-top: 20px;
            color: #333;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 300px;
            width: 100%;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: none;
            border-radius: 4px;
            background-color: #007BFF;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .messageSuccess {
            color: #30be1f;
            text-align: center;
            margin-top: 20px;
        }

        .messageError {
            color: #e00f0f;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h1>Авторизация</h1>
<form action="/login" method="post">
    <label for="userLogin">Имя пользователя:</label>
    <input type="text" id="userLogin" name="userLogin" required>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Войти</button>
</form>
<a href="<%= ConfigurationUtil.get("register_jsp")%>" class=" button">Регистрация</a>
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
        if (message.equals("Регистрация прошла успешно!")) {
%>
<div class="messageSuccess">
    <p><%= message %></p>
</div>
<%
} else if (message.equals("Неверный пароль!")) {
%>
<div class="messageError">
    <p><%= message %>
    </p>
</div>
<%
} else if (message.equals("Неверный логин!")) {
%>
<div class="messageError">
    <p><%= message %>
    </p>
</div>
<%
        }
    }
%>
</body>
</html>