package com.cloudreach.xaar.epicore.externaldata.rdbms.config;

import java.net.URLClassLoader;

/**
 * @author alirezafallahi
 */
public class ODBCConfig extends JDBCConfig {
	
	private URLClassLoader loader;
	public ODBCConfig(String url, String logon_id, String password,
			String driverClass) {
		super(url, logon_id, password, driverClass);
		// TODO Auto-generated constructor stub
	}
	public ODBCConfig(URLClassLoader loader, String url, String logon_id, String password,
			String driverClass) {
		super(url, logon_id, password, driverClass);
		this.loader = loader;
	}
	@Override
	public URLClassLoader loader(){
		return loader;
	}
}
