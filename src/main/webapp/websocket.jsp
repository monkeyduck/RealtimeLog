<%--
  Created by IntelliJ IDEA.
  User: llc
  Date: 16/6/30
  Time: 下午2:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>

<html lang="en">
<head>
    <base href="<%=basePath%>">
    <title>My WebSocket</title>
</head>

<body>
<input id="search_id" type="text" />
<div class="form-group">
    <select class="form-control input-lg" id="moduleSelect">
        <option value="">Module</option>
        <option value="GeneralGame">GeneralGame</option>
        <option value="Dialog">Dialog</option>
        <option value="WorldView">WorldView</option>
        <option value="handworkGuide">handworkGuide</option>
        <option value="cartoon">cartoon</option>
        <option value="music">music</option>
        <option value="story">story</option>
        <option value="FrontEnd">FrontEnd</option>
        <option value="Preprocess">Preprocess</option>
        <option value="All">All</option>
    </select>
</div>
<div class="form-select-button">
    <select class="form-select-button" id="difficulty">
        <option value="simpleLog">Simple Log</option>
        <option value="complexLog">Complex Log</option>
    </select>
</div>
<button onclick="displayLog()">Display</button>
<button onclick="closeWebSocket()">Close</button>
<div id="message">
</div>
</body>

<script type="text/javascript">
    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://localhost:8080/websocket");
    }
    else{
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){
        setMessageInnerHTML("Start to display logs");
    }

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function(){
        setMessageInnerHTML("close");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    function displayLog() {
        var id = document.getElementById('search_id').value;
        var module = document.getElementById('moduleSelect').value;
        var type = document.getElementById('difficulty').value;
        websocket.send("{\"search_id\":\""+id+"\",\"module\":\""+module+"\",\""+type+"\":1}");

    }

</script>
</html>
