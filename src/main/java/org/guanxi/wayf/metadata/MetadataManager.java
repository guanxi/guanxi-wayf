/**
 * 
 */
package org.guanxi.wayf.metadata;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author matthew
 *
 */
public class MetadataManager {
  /**
   * This is set during the initialisation method
   * and can only be set once. This is the way in
   * which the method of metadata management is
   * chosen.
   */
  private static MetadataManager singleton;
  /**
   * This is the store of metadata that this manager holds.
   * The key of this map is the name of the IdP.
   */
  private Map<String, IdPMetadata> metadata;
  
  /**
   * This gets the MetadataManager. 
   * 
   * @return  The MetadataManager to use for the IdP details.
   */
  public static MetadataManager getMetadataManager() {
    if ( singleton == null ) {
      singleton = new MetadataManager();
    }
    return singleton;
  }
  
  private MetadataManager() {
    metadata = new TreeMap<String, IdPMetadata>();
  }
  
  /**
   * This gets the sorted list of the IdPs. This list
   * must be sorted alphabetically by the name of the
   * IdP.
   * 
   * @return  An alphabetically sorted list of IdPs.
   */
  public SortedSet<IdPMetadata> getMetadata() {
    SortedSet<IdPMetadata> result;
    
    result = new TreeSet<IdPMetadata>(new IdPMetadataComparator());
    result.addAll(metadata.values());
    
    return result;
  }
  
  /**
   * This gets a specific IdP by the human readable name.
   * If there is no such IdP this will return null.
   * 
   * @param name  This is the name of the IdP to retrieve
   * @return      The IdP associated with the name, or null if no such IdP exists.
   */
  public IdPMetadata getMetadata(String name) {
    return metadata.get(name);
  }
  
  /**
   * This removes all of the metadata and adds the collection that
   * has been passed in.
   * 
   * @param metadata
   */
  public void setMetadata(Collection<IdPMetadata> metadata) {
    this.metadata.clear();
    
    for ( IdPMetadata current : metadata ) {
      this.metadata.put(current.getName(), current);
    }
  }
  
  private static class IdPMetadataComparator implements Comparator<IdPMetadata> {

    public int compare(IdPMetadata one, IdPMetadata two) {
      return one.getName().compareTo(two.getName());
    }
    
  }
}
