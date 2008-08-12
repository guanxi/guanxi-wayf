/**
 * 
 */
package org.guanxi.wayf;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.xmlbeans.XmlException;
import org.guanxi.xal.idp.IdpListDocument;

/**
 * @author matthew
 *
 */
public class Util {

  /**
   * This is the key used to store the IdP list in the servlet context.
   */
  public static final String idpListKey = "idpList";
  
  /**
   * This method can be called from the init methods of the WAYF and DS.
   * This will load the IdP list, and will do so only once.
   * 
   * @param context           The servlet context which will store the loaded list
   * @throws ServletException If there is a problem loading the IdP list
   */
  public static void init(ServletContext context, ServletConfig configuration) throws ServletException {
    if ( context.getAttribute(idpListKey) == null ) {
      try {
        context.setAttribute(idpListKey, Util.buildIDPList(context, configuration));
      }
      catch ( Exception e ) {
        throw new ServletException("Unable to load IdP List", e);
      }
    }
  }
  
  /**
   * This loads the IdP List from the XML file.
   * 
   * @param context
   * @param configuration
   * @return
   * @throws IOException  If there is a problem reading the XML file
   * @throws XmlException If there is a problem parsing the XML file
   */
  public static Map<String, String> buildIDPList(ServletContext context, ServletConfig configuration) throws IOException, XmlException {
    Map<String, String> sites;
    File sitesFile;
    IdpListDocument idpListDoc;
    IdpListDocument.IdpList idpList;

    // Build the path to the sites XML file...
    sitesFile = new File(context.getRealPath(configuration.getInitParameter("sitesFile")));
    sites = new TreeMap<String, String>();
      
    // Load the configuration file...
    idpListDoc = IdpListDocument.Factory.parse(sitesFile);
    idpList = idpListDoc.getIdpList();

    // ...and build up the list of IdPs we recognise
    for (int count = 0; count < idpList.getIdpArray().length; count++) {
      org.guanxi.xal.idp.WAYF wayf;
      
      wayf = idpList.getIdpArray(count);
      sites.put(wayf.getName(), wayf.getUrl());
    }

    return sites;
  }
}
