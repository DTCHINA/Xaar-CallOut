package com.cloudreach.xaar.epicore.externaldata.rdbms.config;

/**
 * @author alirezafallahi
 */
public class DerbyConfig extends JDBCConfig {
	
	public DerbyConfig(String url, String logon_id, String password,
			String driverClass) {
		super(url, logon_id, password, driverClass);
	}
}
