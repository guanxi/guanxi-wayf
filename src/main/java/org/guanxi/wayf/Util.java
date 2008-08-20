/**
 * 
 */
package org.guanxi.wayf;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author matthew
 *
 */
public class Util {

  /**
   * This method can be called from the init methods of the WAYF and DS.
   * This will load the MetadataManager. There is no protection against
   * the WAYF and the DS having different values for the type of manager
   * and each calling this method - that will cause an error. If they both
   * have the same type of manager then this can be called twice safely.
   * 
   * @param context           The servlet context which will store the loaded list
   * @throws ServletException If there is a problem loading the IdP list
   */
  public static void init(ServletContext context, ServletConfig configuration) throws ServletException {
    String metadataManager;
    
    try {
      metadataManager = configuration.getInitParameter("metadataManager");
      if ( metadataManager == null || metadataManager.equalsIgnoreCase("file")) {
        org.guanxi.wayf.metadata.file.MetadataManager.init(context, configuration);
      }
      else if ( metadataManager.equalsIgnoreCase("hibernate") ) {
        org.guanxi.wayf.metadata.hibernate.MetadataManager.init();
      }
      else {
        // default manager is the file backed one
        org.guanxi.wayf.metadata.file.MetadataManager.init(context, configuration);
      }
    }
    catch ( Exception e ) {
      throw new ServletException(e);
    }
  }
}
