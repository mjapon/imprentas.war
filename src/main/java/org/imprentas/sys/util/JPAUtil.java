package org.imprentas.sys.util;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author imssbora
 */
public class JPAUtil {
  //private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";
	
  private static EntityManagerFactory factoryComp;
  //private static EntityManagerFactory factoryInit;
  
  public static EntityManagerFactory getEntityManagerFactoryComp() {	   
    if (factoryComp == null) {
    	factoryComp = Persistence.createEntityManagerFactory("COMP_UNIT");
    }
    return factoryComp;
  }

  /*
  public static EntityManagerFactory getEntityManagerFactoryInit() {	   
	    if (factoryInit == null) {
	    	factoryInit = Persistence.createEntityManagerFactory("INIT_UNIT");
	    }
	    return factoryInit;
	  }
	  */

  public static void shutdown() {
    if (factoryComp != null) {
    	factoryComp.close();
    }
    /*if (factoryInit != null){
    	factoryInit.close();
    }*/
  }
}
