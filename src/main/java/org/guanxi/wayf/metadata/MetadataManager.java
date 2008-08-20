/**
 * 
 */
package org.guanxi.wayf.metadata;

import java.util.SortedSet;

/**
 * @author matthew
 *
 */
public abstract class MetadataManager {
  
  /**
   * This is set during the initialisation method
   * and can only be set once. This is the way in
   * which the method of metadata management is
   * chosen.
   */
  private static MetadataManager singleton;
  
  /**
   * This sets the MetadataManager. If the MetadataManager has previously
   * been set then this will throw an IllegalStateException. This means that
   * only one source of Metadata will be used.
   * 
   * @param manager                 The MetadataManager to set.
   * @throws IllegalStateException  If the MetadataManager has been set already this will be thrown. 
   */
  public static void setMetadataManager(MetadataManager manager) throws IllegalStateException {
    if ( singleton != null ) {
      throw new IllegalStateException("The MetadataManager has already been set");
    }
    MetadataManager.singleton = manager;
  }
  
  /**
   * This gets the MetadataManager. 
   * 
   * @return  The MetadataManager to use for the IdP details.
   */
  public static MetadataManager getMetadataManager() {
    return singleton;
  }
  
  /**
   * This gets the sorted list of the IdPs. This list
   * must be sorted alphabetically by the name of the
   * IdP.
   * 
   * @return  An alphabetically sorted list of IdPs.
   */
  public abstract SortedSet<? extends IdPMetadata> getMetadata();
  
  /**
   * This gets a specific IdP by the human readable name.
   * If there is no such IdP this will return null.
   * 
   * @param name  This is the name of the IdP to retrieve
   * @return      The IdP associated with the name, or null if no such IdP exists.
   */
  public abstract IdPMetadata getMetadataByName(String name);
}
