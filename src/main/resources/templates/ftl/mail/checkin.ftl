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
            font-family: monospace, Calibri, Arial, Helvetica, sans-serif, serif, EmojiFont;
        }

        #footer {
            margin-top: 16px;
            line-height: 90%;
            font-family: monospace, Calibri, Arial, Helvetica, sans-serif, serif, EmojiFont;
        }
    </style>
</head>
<body>
<div id="content">
    <p>Dear Librarian team,</p><br/>
    <p>請協助Checkin <span class="red">[${projectName!}]</span>，相關檔案如附件。</p><br/>
    <p>LacrNo: <span class="red">${lacrNo!}</span></p><br/>
</div>

<div id="note">
    ${note!}
</div>

<div id="footer">
    <p>Best regards</p><br/>
    <p>${senderName!}</p><br/>
</div>
</body>
</html>
