<html lang='en'>
<head>
    <title>checkin form</title>
    <style>
        body {
            font-family: monospace, Calibri, Arial, Helvetica, sans-serif, serif, EmojiFont;
        }

        p {
            margin: 0;
        }

        .red {
            color: red;
        }

        #content {
            margin-bottom: 16px;
        }

        #footer {
            line-height: 90%;
        }
    </style>
</head>
<body>
<div id="content">
    <p>Dear Karen and Librarian team,</p><br/>
    <p>請協助Checkout <span class="red">[${projectName!}]</span>，相關檔案如附件。</p><br/>
    <p>LacrNo: <span class="red">${lacrNo!}</span></p><br/>
</div>

<div id="footer">
    <p>Best regards</p><br/>
    <p>${senderName!}</p><br/>
</div>
</body>
</html>