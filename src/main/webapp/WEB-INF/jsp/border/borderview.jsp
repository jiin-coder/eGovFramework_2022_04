<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    
    <style>
        .mytable
        {
            border: 1px solid black;
            width: 800px;
            margin:50px auto 0px auto;
        }
        
        .mytable .td1
        {
            width:100px;
        }
        .mytable .td2
        {
            width:700px;
        }
        .mytable .td3
        {
            vertical-align: 0px;
        }
        .mytable .td4
        {
            text-align: right;
        }

        .mytable .mytextarea
        {
            height:500px;
            resize: none;
        }
    </style>
</head>
<body>
    <table class="mytable">
        <tr>
            <td class="td1">작성자</td>
            <td class="td2">${resultList[0].NICKNAME}</td>
        </tr>
        <tr>
            <td class="td1">제목</td>
            <td><input type="text" class="td2" name="title" readonly value="${resultList[0].TITLE}"></td>
        </tr>
        <tr>
            <td class="td1 td3">내용</td>
            <td><textarea class="td2 mytextarea" name="mytextarea" readonly>${resultList[0].BORDERTEXT}</textarea></td>
        </tr>
        <tr>
            <td colspan="2" class="td4">
                <a href="borderEdit.do?no=${resultList[0].BORDERID}"><input type="button" value="수정""></a>
                <input type="button" value="삭제">
                <a href="borderReply.do?no=${resultList[0].BORDERID}"><input type="button" value="답글"></a>
                <a href="borderList.do"><input type="button" value="목록보기"></a>
            </td>
        </tr>
    </table>
</body>
</html>
