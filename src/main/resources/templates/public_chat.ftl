<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>История чата</title>
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
            border-left: 2px solid #999;
            padding-left: 10px;
            font-size: 13px;
        }
        .message-author {
            font-weight: bold;
            font-size: 12px;
        }
        .message-content {
            word-wrap: break-word;
            margin: 3px 0;
        }
        .message-time {
            font-size: 11px;
            color: #666;
        }
        .empty {
            text-align: center;
            color: #999;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            История чата
            <div class="nav-links">
                <a href="/chat">Чат</a>
                <a href="/">Главная</a>
            </div>
        </div>

        <#if messages?size == 0>
            <div class="empty">Сообщений нет</div>
        <#else>
            <div class="messages">
                <#list messages as msg>
                    <div class="message message-item" data-id="${msg.id}" data-content="${msg.content}" data-author="${(msg.author.login)!''}" data-author-id="${msg.author.id}" data-sent-at="${msg.sentAt}">
                        <div class="message-author">${(msg.author.login)!''}</div>
                        <div class="message-content">${msg.content}</div>
                        <div class="message-time">${msg.sentAt}</div>
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</body>
</html>
