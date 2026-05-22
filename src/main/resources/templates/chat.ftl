<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Чат</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
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
            display: flex;
            flex-direction: column;
            height: 80vh;
        }
        .header {
            padding: 10px;
            border-bottom: 1px solid #ccc;
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
            flex: 1;
            overflow-y: auto;
            padding: 10px;
            border-bottom: 1px solid #ccc;
            font-size: 13px;
        }
        .message {
            margin-bottom: 10px;
            padding: 5px;
            border-left: 2px solid #999;
            padding-left: 8px;
        }
        .message.own {
            border-left-color: #0066cc;
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
        .status {
            padding: 5px 10px;
            font-size: 11px;
            border-bottom: 1px solid #ccc;
        }
        .status.connected {
            background: #f0f0f0;
        }
        .status.disconnected {
            background: #ffe0e0;
        }
        .input-area {
            padding: 10px;
            display: flex;
            gap: 5px;
        }
        .input-area input {
            flex: 1;
            padding: 5px;
            border: 1px solid #ccc;
            font-family: monospace;
            font-size: 13px;
        }
        .input-area button {
            padding: 5px 15px;
            border: 1px solid #ccc;
            background: #f5f5f5;
            cursor: pointer;
            font-family: monospace;
            font-size: 13px;
        }
        .input-area button:hover {
            background: #e0e0e0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            Чат
            <div class="nav-links">
                <a href="/chat/public">История</a>
                <a href="/chat/my">Мои</a>
                <a href="/logout">Выход</a>
            </div>
        </div>
        <div class="status disconnected" id="status">Подключение...</div>
        <div class="messages" id="messages"></div>
        <div class="input-area">
            <input type="text" id="messageInput" placeholder="Сообщение..." autocomplete="off">
            <button onclick="sendMessage()">Отправить</button>
        </div>
    </div>

    <script>
        let stompClient = null;
        const currentUserId = ${currentUserId};

        function connect() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                updateStatus(true);
                stompClient.subscribe('/topic/messages', function(message) {
                    const msg = JSON.parse(message.body);
                    displayMessage(msg);
                });
                loadInitialMessages();
            }, function(error) {
                updateStatus(false);
                setTimeout(connect, 5000);
            });
        }

        function updateStatus(connected) {
            const status = document.getElementById('status');
            if (connected) {
                status.textContent = 'Подключено';
                status.className = 'status connected';
            } else {
                status.textContent = 'Отключено';
                status.className = 'status disconnected';
            }
        }

        function loadInitialMessages() {
            fetch('/chat/public')
                .then(response => response.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const messagesDiv = document.getElementById('messages');
                    messagesDiv.innerHTML = '';
                    const messages = doc.querySelectorAll('.message-item');
                    messages.forEach(msg => {
                        const id = msg.getAttribute('data-id');
                        const content = msg.getAttribute('data-content');
                        const author = msg.getAttribute('data-author');
                        const authorId = parseInt(msg.getAttribute('data-author-id'));
                        const sentAt = msg.getAttribute('data-sent-at');
                        displayMessage({
                            id: id,
                            content: content,
                            author: author,
                            authorId: authorId,
                            sentAt: sentAt
                        });
                    });
                    scrollToBottom();
                });
        }

        function displayMessage(msg) {
            const messagesDiv = document.getElementById('messages');
            const messageEl = document.createElement('div');
            messageEl.className = 'message' + (msg.authorId === currentUserId ? ' own' : '');
            messageEl.id = 'msg-' + msg.id;

            const date = new Date(msg.sentAt);
            const timeStr = date.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' });

            messageEl.innerHTML = '<div class="message-author">' + msg.author + '</div>' +
                                  '<div class="message-content">' + escapeHtml(msg.content) + '</div>' +
                                  '<div class="message-time">' + timeStr + '</div>';

            messagesDiv.appendChild(messageEl);
            scrollToBottom();
        }

        function sendMessage() {
            const input = document.getElementById('messageInput');
            const content = input.value.trim();

            if (!content) return;

            if (stompClient && stompClient.connected) {
                stompClient.send('/app/send', {}, JSON.stringify({ content: content }));
                input.value = '';
            } else {
                alert('Нет соединения');
            }
        }

        function scrollToBottom() {
            const messagesDiv = document.getElementById('messages');
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }

        function escapeHtml(text) {
            const map = {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#039;'
            };
            return text.replace(/[&<>"']/g, m => map[m]);
        }

        document.getElementById('messageInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });

        connect();
    </script>
</body>
</html>

