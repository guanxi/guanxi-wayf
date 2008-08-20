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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guanxi.wayf.metadata.IdPMetadata;
import org.guanxi.wayf.metadata.MetadataManager;

/**
 * DS
 * 
 * This is an extremely simple Discovery Service. This relies upon the 
 * destination IdP being passed as the IdP parameter. This will then simply
 * forward the user to that IdP. While this seems almost pointless it can
 * demonstrate how to create a service that deduces the IdP to use and then
 * forwards the user without interaction.
 * 
 * This also supports forwarding according to the readable IdP name. This can
 * be useful if the IdP URL is prone to change. Currently due to the IdP loading
 * mechanism any IdP URL change would force a servlet reload in order to pick up
 * the change. This can be fixed by altering the resolution of the IdP Name to URL
 * map, so that it is determined using the most up to date information.
 *
 * @author matthew
 */
@SuppressWarnings("serial")
public class DS extends HttpServlet {
  /**
   * This is the parameter key for the URL of the selected
   * IdP.
   */
  public static final String idpURLKey  = "idp";
  /**
   * This is the alternate parameter that can be used to select
   * the IdP by the human readable name.
   */
  public static final String idpNameKey = "name";
  
  /**
   * This will initialise the IdP list for the DS.
   * 
   * @throws ServletException   If there is a problem loading the IdP list.
   */
  public void init() throws ServletException {
    Util.init(getServletContext(), getServletConfig());
  }

  /**
   * Posts are not treated differently from Gets because this service
   * does not rely on a form submission like the WAYF.
   * 
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  /**
   * This will forward the user to the IdP they have indicated with the idp parameter.
   * If no such parameter exists then they will be redirected to the WAYF service so
   * they can select an IdP.
   * 
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Map<String, String[]> requestParameters;
    String[] idpURLList, idpNameList;
    String idpURL;
    StringBuilder idpParameters;
    
    requestParameters = request.getParameterMap();
    idpURLList        = requestParameters.get(idpURLKey);
    idpURL            = null;
    
    // validation of the potential URLs could be performed here
    if ( idpURLList == null ) {
      idpNameList = requestParameters.get(idpNameKey);
      
      if ( idpNameList != null ) {
        for ( String idpName : idpNameList ) {
          IdPMetadata metadata;
          
          metadata = MetadataManager.getMetadataManager().getMetadataByName(idpName);
          
          if ( metadata != null ) {
            idpURL = metadata.getUrl();
            break;
          }
        }
      }
    }
    else {
      idpURL = idpURLList[0];
    }
    
    if ( idpURL == null ) {
      // if there is no URL to redirect to then the user
      // must select the IdP using the WAYF service.
      request.getRequestDispatcher("/WEB-INF/jsp/wayf.jsp").forward(request, response);
      
      return;
    }
    
    idpParameters = new StringBuilder();
    for ( String key : requestParameters.keySet() ) {
      if ( !key.equals(idpURLKey) && !key.equals(idpNameKey) ) { // don't pass the DS controlling parameters on
        for ( String value : requestParameters.get(key) ) {
          idpParameters.append( idpParameters.length() == 0 ? "?" : "&" );
          idpParameters.append(key).append("=").append(value);
        }
      }
    }
    
    idpURL += idpParameters.toString();
    response.sendRedirect(idpURL);
  }
}
