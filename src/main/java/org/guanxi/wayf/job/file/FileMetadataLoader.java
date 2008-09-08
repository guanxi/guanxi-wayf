/**
 * 
 */
package org.guanxi.wayf.job.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.guanxi.wayf.metadata.IdPMetadata;
import org.guanxi.wayf.metadata.MetadataManager;
import org.guanxi.xal.idp.IdpListDocument;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.ServletContextAware;

/**
 * @author matthew
 *
 */
public class FileMetadataLoader implements ServletContextAware {
  
  private static final Logger logger = Logger.getLogger(FileMetadataLoader.class.getName());
  /**
   * The servlet context is used to generate the InputStream for the metadata file.
   * getResourceAsStream is used in case the webapp has not been expanded.
   */
  private ServletContext servletContext;
  /**
   * This is the path to the metadata file. This may represent a resource inside
   * a war file rather than a file on disk. This will be relative to the webapp,
   * not absolute.
   */
  private String metadataFile;

  /**
   * This reloads the metadata from the metadata file.
   */
  public void run() {
    try {
      buildIdPList(servletContext.getResourceAsStream(metadataFile));
      logger.info("Reloaded file based metadata");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    catch (XmlException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This parses the metadata file, loading the parsed metadata or throwing
   * an exception.
   * 
   * @param in
   * @throws IOException
   * @throws XmlException
   */
  public static void buildIdPList(InputStream in) throws IOException, XmlException {
    IdpListDocument idpListDoc;
    IdpListDocument.IdpList idpList;
    Collection<IdPMetadata> result;
    
    // Load the configuration file...
    try {
      idpListDoc = IdpListDocument.Factory.parse(in);
      idpList = idpListDoc.getIdpList();
  
      // ...and build up the list of IdPs we recognise
      result = new ArrayList<IdPMetadata>();
      for (org.guanxi.xal.idp.WAYF current : idpList.getIdpArray()) {
        result.add(new IdPMetadata(current.getName(), current.getUrl()));
      }
      
      MetadataManager.getMetadataManager().setMetadata(result);
    }
    finally {
      in.close();
    }
  }

  /**
   * @param metadataFile the metadataFile to set
   */
  @Required
  public void setMetadataFile(String metadataFile) {
    this.metadataFile = metadataFile;
  }


  /**
   * @param servletContext the servletContext to set
   */
  @Required
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
}
