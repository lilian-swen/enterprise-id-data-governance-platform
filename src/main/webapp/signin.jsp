<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html lang="zh-CN">

<%
    String ret = request.getParameter("ret");
    if(StringUtils.isNotBlank(ret)) {
        ret = URLEncoder.encode(ret);
    } else {
        ret = "";
    }
%>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Log In | Enterprise Identity Platform</title>

    <link href="bootstrap3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            /* Replace the URL below with a real image of data nodes or abstract blue geometry */
            background: linear-gradient(rgba(18, 24, 58, 0.85), rgba(18, 24, 58, 0.85)),
                        url('https://images.unsplash.com/photo-1550751827-4bd374c3f58b?auto=format&fit=crop&w=1920&q=80');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            height: 100vh;
            display: flex;
            align-items: center;
            font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        }

        .login-wrapper {
            width: 100%;
            max-width: 450px;
            padding: 15px;
            margin: auto;
        }

        .project-header {
            text-align: center;
            color: #ffffff;
            margin-bottom: 30px;
        }

        .project-header h1 {
            font-size: 28px;
            font-weight: 900;
            letter-spacing: 1px;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
            margin-bottom: 10px;
            color: #e0e6ed;                /* A softer, cool light grey-blue */
        }

        .project-header p {
            color: #a5b1c2;
            font-size: 16px;
            font-weight: 500;
            text-transform: uppercase;
        }

        .project-header .pill-container {
            margin-top: 15px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .pill {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: #00d4ff; /* A cyan/electric blue for a tech highlight */
            backdrop-filter: blur(5px);
            transition: all 0.3s ease;
        }

        .pill:hover {
            background: rgba(0, 212, 255, 0.2);
            border-color: #00d4ff;
            transform: translateY(-2px);
        }

        .form-signin {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
        }

        .form-signin-heading {
            margin-bottom: 25px;
            font-weight: 600;
            color: #333;
            text-align: center;
            font-size: 22px;
        }

        .form-control {
            height: auto;
            padding: 12px;
            font-size: 16px;
            margin-bottom: 15px;
            border-radius: 4px;
        }

        .btn-login {
            background-color: #2c3e50;
            border: none;
            padding: 12px;
            font-size: 18px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-login:hover {
            background-color: #34495e;
            transform: translateY(-1px);
        }

        .error-msg {
            color: #e74c3c;
            text-align: center;
            margin-bottom: 15px;
            font-weight: bold;
        }
    </style>
</head>

<body>

<div class="login-wrapper">
    <div class="project-header">
        <h1>Distributed Enterprise Identity & Data Governance Platform</h1>
        <p>Security  •  Governance  •  Scalability</p>
    </div>

    <form class="form-signin" action="/login.page?ret=<%=ret%>" method="post">
        <h2 class="form-signin-heading">Account Login</h2>

        <div class="form-group">
            <label for="inputEmail" class="sr-only">Email / Phone</label>
            <input type="text" id="inputEmail" class="form-control" placeholder="Username / Email" name="username" required autofocus value="${username}">
        </div>

        <div class="form-group">
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" id="inputPassword" class="form-control" placeholder="Password" name="password" required >
        </div>

        <div class="error-msg">${error}</div>

        <button class="btn btn-lg btn-primary btn-block btn-login" type="submit">Secure Log In</button>

        <p class="text-muted text-center" style="margin-top: 20px; font-size: 12px;">
            &copy; Lilian Kerr Project Showcase © 2026. MIT License.
        </p>
    </form>
</div>

</body>
</html>