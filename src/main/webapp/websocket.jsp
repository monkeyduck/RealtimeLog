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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"/>

    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

    <!-- DatePicker-->
    <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css"/>
    <style type="text/css">
        .vcenter {
            display: inline-block;
            vertical-align: middle;
            float: none;
        }

        .col-centered {
            float: none;
            margin: 0 auto;
        }
    </style>
</head>

<body>
<header>
    <div class="container" style="text-align: center">
        <h3>线上实时日志</h3>
    </div>
</header>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-1 vcenter"></div>
        <div class="col-md-7 vcenter">
            <div style="float: left">
                <input type="text" class="form-control" name="dateRangeBegin" id="dateRangeBegin"/>
            </div>
            <div style="float: left">
                <select class="selectpicker" id="moduleSelect">
                    <option value="">Module</option>
                    <option value="GeneralGame">GeneralGame</option>
                    <option value="dialog">dialog</option>
                    <option value="WorldView">WorldView</option>
                    <option value="handworkGuide">handworkGuide</option>
                    <option value="cartoon">cartoon</option>
                    <option value="music">music</option>
                    <option value="story">story</option>
                    <option value="FrontEnd">FrontEnd</option>
                    <option value="preprocess">Preprocess</option>
                    <option value="All">All</option>
                </select>
            </div>
            <div style="float: left">
                <select class="selectpicker" id="difficulty">
                    <option value="simpleLog">Simple Log</option>
                    <option value="complexLog">Complex Log</option>
                </select>
            </div>
            <div style="float: left">
                <button type="button" onclick="displayLog()" class="btn btn-default">Display</button>

            </div>

            <div style="float:left">
                <button type="button" onclick="closeWebSocket()" class="btn btn-default">Close</button>

            </div>

           <div>
               <input id="member_id" type="text" class="form-control"
                      placeholder="输入要查询的member id"/>

           </div>
            <div style="float:left">
                <button type="button" onclick="simpleDownload()" class="btn btn-default">下载简单日志</button>

            </div>
            <div style="float:left">
                <button type="button" onclick="complexDownload()" class="btn btn-default">下载复杂日志</button>
            </div>

        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-1 vcenter"></div>
        <div class="col-md-7 vcenter">
            <table id="message" style="width: 80%"></table>
        </div>
    </div>


</div>
</body>

<script type="text/javascript">
    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://123.56.237.250:80/websocket/websocket");
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
        setMessageInnerHTML("Closed connection.");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        console.info(innerHTML);
        document.getElementById('message').innerHTML += '<tr>'+innerHTML+'</tr>';
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    function displayLog() {
        var id = document.getElementById('member_id').value;
        var module = document.getElementById('moduleSelect').value;
        var type = document.getElementById('difficulty').value;
        websocket.send("{\"member_id\":\""+id+"\",\"module\":\""+module+"\",\""+type+"\":1}");

    }

    $(function () {
        var startDate = new Date()
        startDate.setDate(startDate.getDate() - 1)
        var endDate = startDate
        var dateRangeBegin = $('input[name="dateRangeBegin"]')
        dateRangeBegin.daterangepicker({
            startDate: startDate,
            singleDatePicker: true,
            "maxDate": endDate,
            showDropdowns: true,
            locale: {
                format: 'YYYY-MM-DD'
            }
        });
    })

    function simpleDownload() {
        var member_id = document.getElementById('member_id').value;
        var date = document.getElementById('dateRangeBegin').value;
        window.location.href = '<%=basePath%>downloadSimple?member_id='+member_id+'&date='+date;
    }

    function complexDownload() {
        var member_id = document.getElementById('member_id').value;
        var date = document.getElementById('dateRangeBegin').value;
        window.location.href = '<%=basePath%>downloadComplex?member_id='+member_id+'&date='+date;
    }

</script>
</html>
