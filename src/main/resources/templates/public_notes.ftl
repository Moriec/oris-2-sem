<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Публичные заметки</title>
</head>
<body>
<h2>Публичные заметки</h2>
<p><a href="/login">Войти</a> | <a href="/notes">Мои заметки</a></p>

<#if notes?size == 0>
    <p>Публичных заметок нет</p>
<#else>
    <ul>
        <#list notes as n>
            <li>
                <strong>${n.title}</strong>
                <span> — ${n.createdAt}</span>
                <div>${n.content}</div>
            </li>
        </#list>
    </ul>
</#if>
</body>
</html>
