/**
 * 
 */
package org.guanxi.wayf.metadata;

import java.util.Comparator;

/**
 * This represents a single IdP entry in the WAYF list.
 * 
 * @author matthew
 */
public interface IdPMetadata {

  /**
   * @return the name
   */
  public String getName();
  
  /**
   * @return the url
   */
  public String getUrl();
  
  /**
   * This comparator allows the IdPMetadata elements to be ordered
   * so that the displayed list is alphabetically sorted and is thus
   * usable when the number of IdPs grows.
   * 
   * @author matthew
   */
  public static class IdPMetadataComparator implements Comparator<IdPMetadata> {
    public int compare(IdPMetadata one, IdPMetadata two) {
      return one.getName().compareTo(two.getName());
    }
  }
}
