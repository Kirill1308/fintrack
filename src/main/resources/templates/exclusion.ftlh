<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Exclusion from wallet</title>
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
        <h1 style="text-align: center">Exclusion Notification</h1>
        <p>Hello, ${name}!</p>
        <p>We regret to inform you that you have been excluded from the wallet ${walletName}. If you believe this is a mistake, please contact the wallet owner.</p>
        <p>With best regards, <br> Fintrack Company</p>
    </div>
</div>
</body>
</html>
