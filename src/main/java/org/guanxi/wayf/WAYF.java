//: "The contents of this file are subject to the Mozilla Public License
//: Version 1.1 (the "License"); you may not use this file except in
//: compliance with the License. You may obtain a copy of the License at
//: http://www.mozilla.org/MPL/
//:
//: Software distributed under the License is distributed on an "AS IS"
//: basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//: License for the specific language governing rights and limitations
//: under the License.
//:
//: The Original Code is Guanxi (http://www.guanxi.uhi.ac.uk).
//:
//: The Initial Developer of the Original Code is Alistair Young alistair@smo.uhi.ac.uk.
//: Portions created by SMO WWW Development Group are Copyright (C) 2005 SMO WWW Development Group.
//: All Rights Reserved.
//:

package org.guanxi.wayf;

import org.guanxi.xal.idp.IdpListDocument;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * <p>WAYF</p>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
@SuppressWarnings("serial")
public class WAYF extends HttpServlet {
  public void init() throws ServletException {
    getServletContext().setAttribute("idpList", buildIDPList());
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Are we getting a form submission from the WAYF list?
    if (request.getParameter("mode") != null) {
      if (request.getParameter("mode").equalsIgnoreCase("dispatch")) {
        // Base IdP URL...
        String idpURL = request.getParameter("idp");
        
        // ...and add on the Shibboleth specific parameters
        String name, value;
        boolean firstShibbParam = true;
        Hashtable<String, String> requestParams = getRequestParameters(request);
        Enumeration<String> e = requestParams.keys();
        while (e.hasMoreElements()) {
          // Get a parameter from the request
          name = e.nextElement();
          value = requestParams.get(name);

          // If it's a Shibboleth parameter, remove the marker the JSP added...
          if (name.indexOf("shibb_") != -1) {
            name = name.replaceAll("shibb_", "");

            // ...and add it to the IdP's URL
            if (firstShibbParam) {
              idpURL += "?";
              firstShibbParam = false;
            }
            else
              idpURL += "&";
            idpURL += name + "=" + value;
          }
        }
        response.sendRedirect(idpURL);
      }
    }
    else {
      // Initial forward to the WAYF page from a Shibboleth SP GET request
      request.getRequestDispatcher("/WEB-INF/jsp/wayf.jsp").forward(request, response);
    }
  }

  private Hashtable<String, String> buildIDPList() {
    Hashtable<String, String> sites = new Hashtable<String, String>();

    // Build the path to the sites XML file...
    String sitesFile = getServletContext().getRealPath(getServletConfig().getInitParameter("sitesFile"));

    try {
      // Load the config file...
      IdpListDocument idpListDoc = IdpListDocument.Factory.parse(new File(sitesFile));
      IdpListDocument.IdpList idpList = idpListDoc.getIdpList();

      // ...and build up the list of IdPs we recognise
      for (int count = 0; count < idpList.getIdpArray().length; count++) {
        org.guanxi.xal.idp.WAYF wayf = idpList.getIdpArray(count);
        sites.put(wayf.getName(), wayf.getUrl());
      }
    }
    catch(Exception e) {
      sites.put("error", e.getMessage());
    }

    return sites;
  }

  @SuppressWarnings("unchecked")
  public static Hashtable<String, String> getRequestParameters(HttpServletRequest request) {
    Hashtable<String, String> params = new Hashtable<String, String>();
    Enumeration<String> e = request.getParameterNames();
    String name,value;

    while (e.hasMoreElements()) {
      name = e.nextElement();
      value = request.getParameter(name);
      params.put(name, value);
    }

    return params;
  }
}
