package com.cloudreach.xaar.epicore.upload.jar.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import com.cloudreach.alumina.PluginClassLoader;
import com.cloudreach.alumina.framework.RestService;
import com.cloudreach.alumina.utils.HDBTask;
import com.cloudreach.alumina.utils.UT;

public class JarPersistor {
	
	//public static final String DATABASE_URL = "postgres://u5hr7g2blp830g:p79pvtv8m0of4a17eca7em9bhg@ec2-54-247-172-188.eu-west-1.compute.amazonaws.com:5542/dcjk3vlv81c69j?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	
	public static final String DATABASE_URL = "postgres://u5hr7g2blp830g:p79pvtv8m0of4a17eca7em9bhg@ec2-54-247-172-188.eu-west-1.compute.amazonaws.com:5542/dcjk3vlv81c69j?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	
	static {
		System.setProperty(UT.SYS_DATABASE_URL, DATABASE_URL);
	}
	
	public static void main(String[] args) throws Exception{
		UT.HDB(new HDBTask() {
			
			public void run(final Connection arg0) throws Exception {
			
				String currentDir = System.getProperty("user.dir");
				//Path p = Paths.get(currentDir,"src","main","resources","rt.jar");
				Path p = Paths.get(currentDir,"target","callout-to-epicor-from-salesforce-1.0-jar-with-dependencies.jar");
				JarFile jar = new JarFile(p.toFile());
				Enumeration<JarEntry> entries = jar.entries();
				PreparedStatement pstm = null;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JarOutputStream outputStream = new JarOutputStream(byteArrayOutputStream);
				while(entries.hasMoreElements()){
					JarEntry jarEntry = (JarEntry) entries.nextElement();
					if(!jarEntry.isDirectory() && jarEntry.getName().contains(".class")){
						InputStream jarInputStream = jar.getInputStream(jarEntry);
						byte[] bytifiedClassFile = UT.getBytes(jarInputStream);
						outputStream.putNextEntry(jarEntry);
						outputStream.write(bytifiedClassFile);
					
						
						PluginClassLoader pl = new PluginClassLoader(bytifiedClassFile);
//						pstm = arg0.prepareStatement("insert into console.plugin_classes(plugin_id,classname,isexposable) values (?,?,?)");
//						pstm.setInt(1, 63);
						String qualifiedClassName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf(".class"));
						qualifiedClassName = qualifiedClassName.replace('/', '.');
						if(qualifiedClassName.equals("com.cloudreach.alumina.framework.RestService")){
							Class<?> plc = pl.loadClass(qualifiedClassName);
							System.out.println(plc);
						}
						if(qualifiedClassName.equals("com.cloudreach.xaar.epicore.integration.ws.EpiCorSyncAccountWebService")){
							Class<?> plc = pl.loadClass(qualifiedClassName);
							System.out.println(plc);
						}
						
						
//						pstm.setString(2, qualifiedClassName);
//						pstm.setBoolean(3, RestService.class.isAssignableFrom(plc));
//						pstm.execute();
						
//						pstm = arg0.prepareStatement("insert into console.plugin_classes(plugin_id,classname,isexposable) values(?,?,?) ");
//						String qualifiedClassName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf(".class"));
//						qualifiedClassName = qualifiedClassName.replace('/', '.');
//						pstm.setInt(1,63);
//						pstm.setString(2,qualifiedClassName);
//						pstm.setBoolean(3,false);
//						pstm.execute();
//						arg0.commit();
						
					}
				}
				outputStream.close();
//				pstm = arg0.prepareStatement("insert into console.plugins(name,version,description,plugin_code) "
//						+ "values (?,?,?,?)");
				
//				pstm = arg0.prepareStatement("update console.plugins set plugin_code=? where id = 63 ");
//				pstm.setString(1,"Xaar - EpiCore - Callouts");
//				pstm.setString(2,"1.0");
//				pstm.setString(3,"This plugin is used in order to allow us to make ODBC connection from Cloudreach Connect to other databases");
//				pstm.setBytes(4, byteArrayOutputStream.toByteArray());
//				pstm.setBytes(1, byteArrayOutputStream.toByteArray());
//				pstm.execute();
				jar.close();
				if(pstm!=null){
					pstm.close();
				}	
			}
		});
		
		/*LOCAL CHECK
		UT.DB("GHD-HT-PROD", new HDBTask() {
			
			public void run(final Connection arg0) throws Exception {
			
				String currentDir = System.getProperty("user.dir");
				Path p = Paths.get(currentDir,"src","main","resources","rt.jar");
				JarFile jar = new JarFile(p.toFile());
				Enumeration<JarEntry> entries = jar.entries();
				PreparedStatement pstm = null;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JarOutputStream outputStream = new JarOutputStream(byteArrayOutputStream);
				while(entries.hasMoreElements()){
					JarEntry jarEntry = (JarEntry) entries.nextElement();
					if(!jarEntry.isDirectory() && jarEntry.getName().contains(".class")){
						InputStream jarInputStream = jar.getInputStream(jarEntry);
						byte[] bytifiedClassFile = UT.getBytes(jarInputStream);
						outputStream.putNextEntry(jarEntry);
						outputStream.write(bytifiedClassFile);		
						
					}
				}
				outputStream.close();
				pstm = arg0.prepareStatement("insert into public.plugins(name,version,description,plugin_code) "
						+ "values (?,?,?,?)");	
				pstm.setString(1,"Xaar - EpiCore - Callouts");
				pstm.setString(2,"1.0");
				pstm.setString(3,"This plugin is used in order to make ODBC connection from Cloudreach Connect");
				pstm.setBytes(4, byteArrayOutputStream.toByteArray());
				pstm.execute();
				jar.close();
				if(pstm!=null){
					pstm.close();
				}	
			}
		});
		*/
	}
}
