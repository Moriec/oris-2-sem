<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои заметки</title>
</head>
<body>
<h2>Мои заметки</h2>
<p><a href="/notes/create">Создать заметку</a> | <a href="/notes/public">Публичные заметки</a></p>

<#if notes?size == 0>
    <p>Заметок нет</p>
<#else>
    <ul>
        <#list notes as n>
            <li>
                <strong>${n.title}</strong>
                <span> — ${n.createdAt}</span>
                <#if n.public>
                    <span>(публичная)</span>
                <#else>
                    <span>(приватная)</span>
                </#if>
                <div>
                    <a href="/notes/${n.id}/edit">Редактировать</a>
                    <form action="/notes/${n.id}/delete" method="post" style="display:inline;">
                        <#if _csrf??>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </#if>
                        <button type="submit">Удалить</button>
                    </form>
                </div>
                <div>${n.content}</div>
            </li>
        </#list>
    </ul>
</#if>
</body>
</html>
