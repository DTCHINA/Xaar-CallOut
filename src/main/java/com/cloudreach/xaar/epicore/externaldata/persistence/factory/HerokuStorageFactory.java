package com.cloudreach.xaar.epicore.externaldata.persistence.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

import com.cloudreach.xaar.epicore.application.exceptions.HerokuClientStartupException;
import com.cloudreach.xaar.epicore.externaldata.persistence.HerokuStorage;
import com.cloudreach.xaar.epicore.externaldata.rdbms.config.JDBCConfig;
import com.cloudreach.xaar.epicore.externaldata.rdbms.config.ODBCConfig;
//import com.cloudreach.xaar.epicore.externaldata.rdbms.config.PostgreSQLConfig;
import com.cloudreach.xaar.epicore.externaldata.rdbms.config.PostgreSQLConfig;

/**
 * @author alirezafallahi
 */
public class HerokuStorageFactory {
	
	public static HerokuStorage createStorage() {
		
			try {
				Class<?> cl = Class.forName("com.cloudreach.xaar.epicore.externaldata.persistence.HerokuStorageImpl");
				//The URL below was for testing purposes on Heroku system.
				String URL = "jdbc:postgresql://ec2-54-247-79-142.eu-west-1.compute.amazonaws.com:5432/d76k08pu8qgm0e?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
				Constructor constructor = cl.getDeclaredConstructor(JDBCConfig.class);
				constructor.setAccessible(true);
				return  (HerokuStorage) constructor.newInstance(new PostgreSQLConfig(URL, "ccwajcjejzodrl", "qLRCil0fT5H9Ms8-d3f65SnNqg", "org.postgresql.Driver"));
				//INSTANCE =  (EpiCoreStorage) constructor.newInstance(new ODBCConfig(URL, "xaargbl\\cloudreach", "C10ud234ch", "com.microsoft.sqlserver.jdbc.SQLServerDriver"));
			} catch (ClassNotFoundException e) {
				throw new HerokuClientStartupException(e);
			}
			catch (NoSuchMethodException | SecurityException  e) {
				throw new HerokuClientStartupException(e);
			}
			catch (InstantiationException e) {
				throw new HerokuClientStartupException(e);
			} catch (IllegalAccessException e) {
				throw new HerokuClientStartupException(e);
			} catch (IllegalArgumentException e) {
				throw new HerokuClientStartupException(e);
			} catch (InvocationTargetException e) {
				throw new HerokuClientStartupException(e.getTargetException());
			}
	}
}
