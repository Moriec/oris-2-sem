<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Заметка</title>
</head>
<body>
<h2>Заметка</h2>
<form action="${action}" method="post">
    <div>
        <label for="title">Заголовок</label>
        <input type="text" id="title" name="title" value="${note.title!}" required/>
    </div>
    <div>
        <label for="content">Содержимое</label>
        <textarea id="content" name="content" rows="8" cols="60" required>${note.content!}</textarea>
    </div>
    <div>
        <label><input type="checkbox" name="isPublic" <#if note.public?has_content && note.public>checked</#if>/> Публичная</label>
    </div>
    <#if _csrf??>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </#if>
    <button type="submit">Сохранить</button>
</form>
<p><a href="/notes">Вернуться к списку</a></p>
</body>
</html>
