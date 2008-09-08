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
   Revision 1.1.2.3  2008/09/08 14:46:07  matthewfranglen
   This makes the WAYF support quartz jobs and adds a file job that will load the list of IdPs from the configuration file.

   Revision 1.1.2.2  2008/08/12 14:25:34  matthewfranglen
   This change to the WAYF does the following things:

   1) Supports multiple parameters with the same key in the get / post.

   2) Adds DS functionality using a new mapping. This can automatically forward the user to an appropriate idp based upon parameters passed to the initial request. While the functionality of this appears simple the idea is to create URLs that are provided to students of a given university. Those URLs will automatically forward said students to the IdP of the university. The coding has been done in a fairly expandable way so it should be easy to add other DS functionality.

   3) Removes all hardcoded values with references to final variables holding the hardcoded values. This should reduce the chance of error should those values have to change.


   Alistair before integrating this with the trunk you should review the changes and be sure they are what you want.

   Revision 1.1.2.1  2008/07/17 10:45:15  matthewfranglen
   Adding generic types where possible, suppressing warnings where not possible.

   Revision 1.1  2008/03/14 17:20:03  alistairskye
   Mavenised

   Revision 1.14  2007/01/16 11:48:18  alistairskye
   Tidied up

   Revision 1.13  2007/01/16 11:47:53  alistairskye
   Updated buildIDPList() to remove GuanxiException

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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>WAYF</p>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
@SuppressWarnings("serial")
public class WAYF extends HttpServlet {
  /**
   * This is the parameter key for the URL of the selected
   * IdP.
   */
  public static final String idpKey          = "idp";
  /**
   * This is the prefix that is appended to the parameters to
   * ensure that they do not conflict with the ones that are
   * added by the WAYF.
   */
  public static final String parameterPrefix = "shibb_";
  
  /**
   * This will initialise the IdP list for the WAYF.
   * 
   * @throws ServletException   If there is a problem loading the IdP list.
   */
  public void init() throws ServletException {
  }

  /**
   * This handles a get request to this Servlet. Considering that the form used
   * to select the IdP uses a post this method can be assumed to be restricted
   * to visitors to the WAYF that have yet to select an IdP.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Initial forward to the WAYF page from a Shibboleth SP GET request
    request.getRequestDispatcher("/WEB-INF/jsp/wayf.jsp").forward(request, response);
  }

  /**
   * This handles post requests to this Servlet. The posts are handled exactly the
   * same as the gets, although the mere fact that this is a post could be used
   * to assume that the IdP has been selected.
   */
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Map<String, String[]> requestParameters;
    String[] idpURLList;
    String idpURL;
    StringBuilder idpParameters;
    
    requestParameters = request.getParameterMap();
    idpURLList        = requestParameters.get(idpKey);
    
    if ( idpURLList == null ) {
      // if there is no URL to redirect to then the user
      // must submit the form again.
      doGet(request, response);
      
      return;
    }
    idpURL = idpURLList[0];
    
    idpParameters = new StringBuilder();
    for ( String key : requestParameters.keySet() ) {
      if ( key.startsWith(parameterPrefix) ) {
        String originalKey;

        originalKey = key.substring(parameterPrefix.length());
        
        for ( String value : requestParameters.get(key) ) {
          idpParameters.append( idpParameters.length() == 0 ? "?" : "&" );
          idpParameters.append(originalKey).append("=").append(value);
        }
      }
    }
    
    idpURL += idpParameters.toString();
    response.sendRedirect(idpURL);
  }
}
