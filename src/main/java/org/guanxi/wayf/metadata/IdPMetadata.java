/**
 * 
 */
package org.guanxi.wayf.metadata;

/**
 * @author matthew
 *
 */
public class IdPMetadata {
  /**
   * This is the name that will be displayed on the drop down list
   */
  private String name;
  /**
   * This is the URL which the user will be redirected to upon selecting
   * the idp.
   */
  private String ssoURL;
  
  public IdPMetadata(String name, String ssoURL) {
    this.name   = name;
    this.ssoURL = ssoURL;
  }

  public IdPMetadata() {}
  
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
   * @return the ssoURL
   */
  public String getSsoURL() {
    return ssoURL;
  }

  /**
   * @param ssoURL the ssoURL to set
   */
  public void setSsoURL(String ssoURL) {
    this.ssoURL = ssoURL;
  }
}
