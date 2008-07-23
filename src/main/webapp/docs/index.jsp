<%@ page import="java.io.File"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  /* This is a simple router page to redirect to the correct language documentation.
   * Start by getting the languages supported by the browser.
   */
  String[] langs = request.getHeader("Accept-Language").split(",");
  String lang = null;
  File docFile = null;

  // Find documentation in the first language supported
  for (int c=0; c < langs.length; c++) {
    // Only support the major language, not the variants.
    if (langs[c].indexOf("-") != -1)
      lang = lang.split("-")[0];
    else
      lang = langs[c];

    docFile = new File(lang + "/index.jsp");

    // If the localised file exists, go to it
    if (docFile.exists()) {
      response.sendRedirect(lang + "/index.jsp");
      return;
    }
  }

  // If we didn't find the correct language, default to English
  response.sendRedirect("en/index.jsp");
%>
