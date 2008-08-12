<%@ page import="java.util.Map,
                 java.util.Enumeration,
                 java.util.Locale,
                 java.util.ResourceBundle,
                 org.guanxi.wayf.WAYF,
                 org.guanxi.wayf.Util" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page autoFlush="false" buffer="20kb" %>
<%
  ResourceBundle msg = ResourceBundle.getBundle("messages.wayf", request.getLocale());
  ResourceBundle siteMsg = ResourceBundle.getBundle("messages.common", request.getLocale());
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

    <div style="margin: 0 auto; text-align: center; width:400px; height:80px;">
      <%= msg.getString("ID_CHOOSE_INSTITUTION")%>
    </div>

    <div style="border:1px solid black; width:400px; height:65; background-image:url(images/formback.gif); background-repeat:repeat-x repeat-y; margin: 0 auto; text-align: center;">
      <div style="padding:20px; margin: 0 auto;">
        <form name="wayfForm" method="POST" action="WAYF">
          <select name="<%= WAYF.idpKey %>">
            <%
              String idpURL;
              Map<String, String> sites;
                
              sites = (Map<String, String>)application.getAttribute(Util.idpListKey);
                
              for ( String idpName : sites.keySet() ) {
                idpURL = sites.get(idpName);
                %>
                <option value="<%= idpURL %>"><%= idpName %></option>
                <%
              }
            %>
          </select>
          <input type="submit" name="submit" value="<%= msg.getString("ID_BUTTON_PROCEED")%>" />
          <%
            Map<String, String[]> requestParameters;
          
            requestParameters = request.getParameterMap();
                  
            for ( String key : requestParameters.keySet() ) {
              
              for ( String value : requestParameters.get( key ) ) {
                %>
                <input type="hidden" name="<%= WAYF.parameterPrefix + key %>" value="<%= value %>" />
                <%
              }
            }
          %>
        </form>
      </div>
    </div>
    <div style="width:400px; margin: 0 auto;">
      <div align="left"><strong>Guanxi@<%= siteMsg.getString("institution.display.name")%></strong></div>
    </div>
  </body>
</html>