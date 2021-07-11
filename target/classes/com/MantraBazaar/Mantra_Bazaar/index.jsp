<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
    <html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Import Excel File in Spring MVC</title>
    </head>

    <body>

        <h3>Import Excel File</h3>
        <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath }/product/process">
            <input type="file" name="file">
            <br>
            <input type="submit" value="Import">
        </form>

    </body>

    </html>