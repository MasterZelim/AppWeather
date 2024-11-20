<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список локаций</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 30px;
            background-color: #007bff;
            color: white;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .header .userLogin {
            font-weight: bold;
            font-size: 1.2em;
        }

        .logout-button {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .logout-button:hover {
            background-color: #d32f2f;
        }

        h2 {
            text-align: center;
            margin-top: 40px;
            font-size: 2em;
            color: #333;
        }

        form {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        form input[type="text"] {
            padding: 10px;
            font-size: 1em;
            width: 300px;
            border: 2px solid #007bff;
            border-radius: 4px;
            margin-right: 10px;
        }
        ::placeholder {
            font-size: 0.7em;
            color: #555;
        }

        form button {
            padding: 10px 20px;
            font-size: 1em;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        form button:hover {
            background-color: #0056b3;
        }

        .location-list {
            margin: 40px auto;
            max-width: 600px;
            padding: 0 20px;
        }

        .location-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            margin-bottom: 10px;
            background-color: white;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: box-shadow 0.3s;
        }

        .location-item:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .location-item span {
            font-size: 1.1em;
            color: #333;
        }

        .remove-button {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .remove-button:hover {
            background-color: #d32f2f;
        }

        * {
            box-sizing: border-box;
        }

    </style>
</head>
<body>

<div class="header">
    <span class="userLogin">Пользователь: ${sessionScope.userLogin}</span>

    <form action="/logout" method="post" style="display: inline;">
        <button type="submit" class="logout-button">Logout</button>
    </form>
</div>

<h2>Найти локацию</h2>

<form action="/home" method="post">
    <input type="text" id="location" name="location" placeholder="Введите название локации на английском языке" required>
    <button type="submit">Найти</button>
</form>

<div class="location-list">
    <h3>Список добавленных локаций:</h3>
    <c:forEach var="weatherDTO" items="${requestScope.weatherDTOList}">
        <div class="location-item">
            <span>${weatherDTO.nameDTO().nameLocation()}</span>
            <span>${weatherDTO.mainDTO().temp()} °C</span>
            <form action="/removeLocation" method="post" style="display: inline;">
                <input type="hidden" name="nameLocation" value="${weatherDTO.nameDTO().nameLocation()}">
                <input type="hidden" name="userLogin" value=${sessionScope.userLogin}>
                <button type="submit" class="remove-button">Удалить</button>
            </form>
        </div>
    </c:forEach>
</div>

</body>
</html>