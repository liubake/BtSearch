<%--
  Created by IntelliJ IDEA.
  User: Erola
  Date: 2017/10/9
  Time: 00:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Redis Test</title>
        <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            .main {padding-bottom: 3rem}
            .item-row {margin-top: 1rem}
        </style>
    </head>
    <body>
        <div class="container main">
            <div class="row">
                <div class="col-xs-12 page-header">
                    <h4><strong>Redis test page </strong><small><em>erola</em></small></h4>
                </div>
            </div>
            <div class="row panel panel-danger">
                <div class="col-xs-12 panel-heading">
                    <div class="col-xs-5 panel-title">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除
                    </div>
                </div>
                <div class="col-xs-12 panel-body">
                    <div class="row">
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2">Key：</div>
                            <div class="col-xs-10">
                                <input type="text" class="form-control" placeholder="Type key">
                            </div>
                        </div>
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2"></div>
                            <div class="col-xs-10 text-right">
                                <input class="btn btn-danger" type="button" value="Del">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row panel panel-info">
                <div class="col-xs-12 panel-heading">
                    <div class="col-xs-5 panel-title">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> 查询
                    </div>
                </div>
                <div class="col-xs-12 panel-body">
                    <div class="row">
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2">Key：</div>
                            <div class="col-xs-10">
                                <input type="text" class="form-control" placeholder="Type key">
                            </div>
                        </div>
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2">Value：</div>
                            <div class="col-xs-10">
                                <input type="text" class="form-control" placeholder="The value is..." disabled>
                            </div>
                        </div>
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2"></div>
                            <div class="col-xs-10 text-right">
                                <input class="btn btn-info" type="button" value="Get">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row panel panel-success">
                <div class="col-xs-12 panel-heading">
                    <div class="col-xs-5 panel-title">
                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 添加
                    </div>
                </div>
                <div class="col-xs-12 panel-body">
                    <div class="row form-group">
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2">Key：</div>
                            <div class="col-xs-10">
                                <input type="text" class="form-control" placeholder="Type key">
                            </div>
                        </div>
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2">Value：</div>
                            <div class="col-xs-10">
                                <input type="text" class="form-control" placeholder="Type value">
                            </div>
                        </div>
                        <div class="col-xs-12 item-row">
                            <div class="col-xs-2"></div>
                            <div class="col-xs-10 text-right">
                                <input class="btn btn-success" type="button" value="Add">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <nav class="navbar navbar-fixed-bottom">
            <div class="col-xs-10">
                <p class="navbar-text navbar-right">
                    <small style="color: #777"><em>2018 . erola</em></small>
                </p>
            </div>
        </nav>
    </body>
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript">






    </script>
</html>