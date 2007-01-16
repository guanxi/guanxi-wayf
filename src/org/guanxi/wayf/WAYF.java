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
/* CVS Header
   $Id$
   $Log$
   Revision 1.12  2007/01/16 11:44:33  alistairskye
   Added getRequestParameters()

   Revision 1.11  2007/01/16 11:31:16  alistairskye
   Updated to use XMLBeans

   Revision 1.10  2005/07/19 14:21:33  alistairskye
   Modified buildIDPList() to use new namespace aware org.guanxi.samuel.utils.XUtils

   Revision 1.9  2005/07/11 12:05:56  alistairskye
   Package restructure

   Revision 1.8  2005/06/20 11:16:51  alistairskye
   Removed org.Guanxi.SAMUEL.Exception.ParseException

   Revision 1.7  2005/06/10 10:39:05  alistairskye
   Modified buildIDPList to use ParserPoolException

   Revision 1.6  2005/05/23 11:38:38  alistairskye
   Changed buildIDPList to look for idp nodes

   Revision 1.5  2005/05/23 08:41:04  alistairskye
   Updated buildIDPList() to fix idp-list display bug

   Revision 1.4  2005/05/04 13:16:36  alistairskye
   Changed to use org.Guanxi.SAMUEL.Exception package

   Revision 1.3  2005/05/02 10:58:13  alistairskye
   Changed to use latest version of org.Guanxi.SAMUEL.Utils.XUtils.getIterator()

   Revision 1.2  2005/04/15 10:05:56  alistairskye
   License updated

*/

package org.guanxi.wayf;

import org.guanxi.common.GuanxiException;
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
public class WAYF extends HttpServlet {
  public void init() throws ServletException {
    try {
      getServletContext().setAttribute("idpList", buildIDPList());
    }
    catch(GuanxiException ge) {
      throw new ServletException(ge);
    }
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
        Hashtable requestParams = getRequestParameters(request);
        Enumeration e = requestParams.keys();
        while (e.hasMoreElements()) {
          // Get a parameter from the request
          name = (String)e.nextElement();
          value = (String)requestParams.get(name);

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

  private Hashtable buildIDPList() throws GuanxiException {
    Hashtable sites = new Hashtable();

    // Build the path to the sites XML file...
    String sitesFile = getServletContext().getRealPath(getServletConfig().getInitParameter("sitesFile"));

    IdpListDocument.IdpList idpList = null;
    try {
      IdpListDocument idpListDoc = IdpListDocument.Factory.parse(new File(sitesFile));
      idpList = idpListDoc.getIdpList();
    }
    catch(Exception e) {
      sites.put("error", e.getMessage());
      throw new GuanxiException(e);
    }

    // Build up the list of IdPs we recognise
    for (int count = 0; count < idpList.getIdpArray().length; count++) {
      org.guanxi.xal.idp.WAYF wayf = idpList.getIdpArray(count);
      sites.put(wayf.getName(), wayf.getUrl());
    }

    return sites;
  }

  public static Hashtable getRequestParameters(HttpServletRequest request) {
    Hashtable params = new Hashtable();
    Enumeration e = request.getParameterNames();
    String name,value;

    while (e.hasMoreElements()) {
      name = (String)e.nextElement();
      value = request.getParameter(name);
      params.put(name, value);
    }

    return params;
  }
}
