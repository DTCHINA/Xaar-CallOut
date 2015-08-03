package com.cloudreach.xaar.epicore.externaldata.rdbms.config;

import java.net.URLClassLoader;
import java.util.Properties;

/**
 * @author alirezafallahi
 */
public abstract class JDBCConfig {
	
	private final String url;
	private final String logon_id;
	private final String password;
	private final String driverClass;
	
	public JDBCConfig(String url, String logon_id, String password, String driverClass){
		this.url = url;
		this.logon_id = logon_id;
		this.password = password;
		this.driverClass = driverClass;
	}
	public Properties getConnectionProperties() {
		Properties props = new Properties();
		props.put("user", getLogon_id());
		props.put("password", getPassword());
		return props;
	}
	protected String getLogon_id() {
		return logon_id;
	}
	protected String getPassword() {
		return password;
	}
	public String getDriverClass() {
		return driverClass;
	}	
	public String getUrl() {
		return url;
	}
	public URLClassLoader loader() {
		return null;
	}
}
