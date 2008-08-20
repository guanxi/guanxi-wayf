/**
 * 
 */
package org.guanxi.wayf.metadata.hibernate;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
   * This is the session factory which creates sessions which can be used
   * to access or update the database.
   */
  private static SessionFactory sessionFactory;

  /**
   * This will initialise this MetadataManager and set it as the manager to use.
   * This must not be called if another MetadataManager has already been initialised.
   * 
   * @throws IllegalStateException        If another MetadataManager has already been set.
   * @throws ExceptionInInitializerError
   */
  public static void init() throws IllegalStateException, ExceptionInInitializerError {
    if ( sessionFactory != null ) {
      // this method has already been called - lets just exit
      return;
    }
    
    // call this first to trigger the IllegalStateException early
    setMetadataManager(new MetadataManager());
    
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      sessionFactory = new Configuration().configure("/org/guanxi/wayf/metadata/hibernate/hibernate.cfg.xml").buildSessionFactory();
    }
    catch (Throwable e) {
      // Make sure you log the exception, as it might be swallowed
      logger.error("Initial SessionFactory creation failed.", e);
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * This gets the session factory which is used to create
   * sessions which can then be used to access or update the
   * database.
   * 
   * @return
   */
  private static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
  
  /**
   * This gets the sorted list of the IdPs. This list
   * must be sorted alphabetically by the name of the
   * IdP.
   * 
   * @return  An alphabetically sorted list of IdPs.
   */
  public SortedSet<IdPMetadata> getMetadata() {
    Session                session;
    List<IdPMetadata>      idpList;
    SortedSet<IdPMetadata> result;
    
    session = getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    idpList = session.createQuery("from IdPMetadata").list();
    result  = new TreeSet<IdPMetadata>(new IdPMetadata.IdPMetadataComparator());
    result.addAll(idpList);
    
    session.getTransaction().commit();
    
    return result;
  }

  /**
   * This gets a specific IdP by the human readable name.
   * If there is no such IdP this will return null.
   * 
   * @param name  This is the name of the IdP to retrieve
   * @return      The IdP associated with the name, or null if no such IdP exists.
   */
  public org.guanxi.wayf.metadata.IdPMetadata getMetadataByName(String name) {
    Session     session;
    IdPMetadata metadata;
    
    session = getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    metadata = (IdPMetadata)session
               .createQuery("select i from Person i where i.name = :name")
               .setParameter("name", name)
               .uniqueResult();
    
    session.getTransaction().commit();

    return metadata;
  }
}
