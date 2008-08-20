/**
 * 
 */
package org.guanxi.wayf.metadata.hibernate;

/**
 * @author matthew
 *
 */
public class IdPMetadata implements org.guanxi.wayf.metadata.IdPMetadata {
  /**
   * This is the human readable name of the IdP.
   * This is the primary key in the database and as
   * such it must be unique.
   */
  private String name;
  /**
   * This is the SSO URL for the IdP
   */
  private String url;
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }
  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
