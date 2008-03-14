<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ResourceBundle msg = ResourceBundle.getBundle("messages.wayf_index", new Locale(request.getHeader("Accept-Language")));
  ResourceBundle siteMsg = ResourceBundle.getBundle("messages.site", new Locale(request.getHeader("Accept-Language")));
%>
<html>
  <head><title><%= msg.getString("ID_PAGE_TITLE")%></title>
    <style type="text/css">
      <!--
      body {
        background-color: #FFFFFF;
        margin-left: 20px;
        margin-top: 20px;
        margin-right: 20px;
        margin-bottom: 20px;
        font-family:Verdana, Arial, Helvetica, sans-serif;
        background-image: url(images/watermark.gif);
      }
      -->
    </style>
  </head>
  <body>
    <div style="width:167; height:91; margin: 0 auto; background-image:url(images/logo.gif);"></div>
    <br>
    <div style="border:1px solid black; width:30%; height:20%; background-image:url(images/formback.gif); background-repeat:repeat-x repeat-y; margin: 0 auto; text-align: center;">
    <div style="padding:20px; margin: 0 auto;">
    <%= msg.getString("ID_WAYF_INFO")%>
    </div>
    </div>
    <div style="width:30%; margin: 0 auto;">
      <div align="left"><strong>Guanxi@<%= siteMsg.getString("ID_INSTITUTION")%></strong></div>
    </div>
  </body>
</html>