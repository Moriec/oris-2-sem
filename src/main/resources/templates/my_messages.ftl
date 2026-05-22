<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мои сообщения</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: monospace;
            background: #fff;
            padding: 10px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            border: 1px solid #ccc;
            padding: 10px;
        }
        .header {
            padding: 10px 0;
            border-bottom: 1px solid #ccc;
            margin-bottom: 10px;
            font-weight: bold;
        }
        .nav-links {
            display: flex;
            gap: 10px;
            margin-top: 5px;
        }
        .nav-links a {
            color: #0066cc;
            text-decoration: none;
            font-size: 12px;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
        .messages {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        .message {
            padding: 8px;
            border-left: 2px solid #0066cc;
            padding-left: 10px;
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            font-size: 13px;
        }
        .message-content {
            flex: 1;
        }
        .message-text {
            word-wrap: break-word;
            margin-bottom: 3px;
        }
        .message-time {
            font-size: 11px;
            color: #666;
        }
        .delete-btn {
            padding: 3px 8px;
            background: #f5f5f5;
            border: 1px solid #ccc;
            cursor: pointer;
            font-size: 11px;
            margin-left: 10px;
            white-space: nowrap;
            font-family: monospace;
        }
        .delete-btn:hover {
            background: #e0e0e0;
        }
        .empty {
            text-align: center;
            color: #999;
            padding: 20px;
        }
        .error {
            background: #ffe0e0;
            color: #c62828;
            padding: 8px;
            border: 1px solid #ff9999;
            margin-bottom: 10px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            Мои сообщения
            <div class="nav-links">
                <a href="/chat">Чат</a>
                <a href="/chat/public">История</a>
                <a href="/">Главная</a>
            </div>
        </div>

        <#if error??>
            <div class="error">
                <#if error == "forbidden">
                    Ошибка: нельзя удалить чужое сообщение
                <#elseif error == "not_found">
                    Ошибка: сообщение не найдено
                <#else>
                    Ошибка: ${error}
                </#if>
            </div>
        </#if>

        <#if messages?size == 0>
            <div class="empty">Нет сообщений</div>
        <#else>
            <div class="messages">
                <#list messages as msg>
                    <div class="message">
                        <div class="message-content">
                            <div class="message-text">${msg.content}</div>
                            <div class="message-time">${msg.sentAt}</div>
                        </div>
                        <form action="/chat/${msg.id}/delete" method="post" style="display: inline;">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <button type="submit" class="delete-btn">Удалить</button>
                        </form>
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</body>
</html>
