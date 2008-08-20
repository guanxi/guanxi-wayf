/**
 * 
 */
package org.guanxi.wayf.metadata.file;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.guanxi.xal.idp.IdpListDocument;

/**
 * This is responsible for providing the IdPMetadata objects on demand.
 * Since this will be used to create lists of IdPMetadata objects that
 * will be used to create webpages it currently only has a single method.
 * 
 * @author matthew
 *
 */
public class MetadataManager extends org.guanxi.wayf.metadata.MetadataManager {
  
  /**
   * This is the logger to use for this class.
   */
  private static final Logger logger = Logger.getLogger(MetadataManager.class.getName());
  
  /**
   * This is the sorted list which is created at initialisation time
   * and not updated.
   */
  private static SortedSet<IdPMetadata> idpList;
  
  /**
   * This will initialise this MetadataManager and set it as the manager to use.
   * This must not be called if another MetadataManager has already been initialised.
   * 
   * @throws IllegalStateException        If another MetadataManager has already been set.
   * @throws ExceptionInInitializerError  If there is a problem loading the IdP list
   */
  public static void init(ServletContext context, ServletConfig configuration) throws IllegalStateException, ExceptionInInitializerError {
    if ( idpList != null ) {
      // this method has already been called - lets just exit
      return;
    }
    
    // call this first to trigger the IllegalStateException early
    setMetadataManager(new MetadataManager());
    
    try {
      buildIdPList(context, configuration);
    }
    catch ( Exception e ) {
      logger.error("Loading the IdP list failed.", e);
      throw new ExceptionInInitializerError(e);
    }
  }
  
  /**
   * This gets the sorted list of the IdPs. This list
   * must be sorted alphabetically by the name of the
   * IdP.
   * 
   * @return  An alphabetically sorted list of IdPs.
   */
  public SortedSet<IdPMetadata> getMetadata() {
    return idpList;
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
  public static void buildIdPList(ServletContext context, ServletConfig configuration) throws IOException, XmlException {
    File sitesFile;
    IdpListDocument idpListDoc;
    IdpListDocument.IdpList idpList;
    SortedSet<IdPMetadata> result;

    // Build the path to the sites XML file...
    sitesFile = new File(context.getRealPath(configuration.getInitParameter("sitesFile")));
      
    // Load the configuration file...
    idpListDoc = IdpListDocument.Factory.parse(sitesFile);
    idpList = idpListDoc.getIdpList();

    // ...and build up the list of IdPs we recognise
    result = new TreeSet<IdPMetadata>(new IdPMetadata.IdPMetadataComparator());
    for (org.guanxi.xal.idp.WAYF current : idpList.getIdpArray()) {
      result.add(new IdPMetadata(current.getName(), current.getUrl()));
    }
    
    MetadataManager.idpList = result;
  }

  /**
   * This gets a specific IdP by the human readable name.
   * If there is no such IdP this will return null.
   * 
   * @param name  This is the name of the IdP to retrieve
   * @return      The IdP associated with the name, or null if no such IdP exists.
   */
  public org.guanxi.wayf.metadata.IdPMetadata getMetadataByName(String name) {
    for ( IdPMetadata current : idpList ) {
      if ( current.getName().equals(name) ) {
        return current;
      }
    }
    return null;
  }
}
