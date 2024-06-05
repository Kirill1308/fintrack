<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Invitation to join wallet</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Invitation</h1>
        <p>Hello, ${name}!</p>
        <p>You have been invited by ${sender} to join a wallet. Please click the link below to accept the invitation:</p>
        <a href="${invitationLink}">Accept Invitation</a>
        <p>With best regards, <br> Fintrack Company</p>
    </div>
</div>
</body>
</html>