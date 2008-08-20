/**
 * 
 */
package org.guanxi.wayf.metadata.file;

/**
 * @author matthew
 */
public class IdPMetadata implements org.guanxi.wayf.metadata.IdPMetadata {
  /**
   * This is the human readable name of the IdP.
   */
  private final String name;
  /**
   * This is the SSO URL for the IdP
   */
  private final String url;
  
  public IdPMetadata(String name, String url) {
    this.name = name;
    this.url  = url;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }
}
