<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ResourceBundle msg = ResourceBundle.getBundle("messages.wayf_index", new Locale(request.getHeader("Accept-Language")));
%>
<html>
  <head>
    <title><%= msg.getString("ID_PAGE_TITLE")%></title>
      <style type="text/css">
        <!--
        body {
          background-color: #FFFFFF;
          margin-left: 20px;
          margin-top: 20px;
          margin-right: 20px;
          margin-bottom: 20px;
          font-family:Verdana, Arial, Helvetica, sans-serif;
          background-image: url(../../images/watermark.gif);
        }
        -->
      </style>
  </head>
  <body>
  <a href="wayf.htm"><%= msg.getString("ID_WAYF_DOC_LINK")%></a>
  </body>
</html>
