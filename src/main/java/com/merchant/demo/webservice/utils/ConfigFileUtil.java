package com.merchant.demo.webservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.merchant.demo.constants.MerchantConstants;

public class ConfigFileUtil 
{
	static Properties properties = null;
	private static String configFileName=null;
	public static HashMap<Object,Object> hmap=null;
	
	public static Properties loadProperties()
 	{
		properties = new Properties();
		FileInputStream inStream = null;
		File inputFile = null;
		hmap=new HashMap<Object,Object>();
 		try 
 		{
 			inputFile = new File(MerchantConstants.config_properties_file);
			inStream = new FileInputStream(inputFile);
			properties.load(inStream);
			if(properties!=null)
			{
				for (final String name: properties.stringPropertyNames())
				{
					hmap.put(name, properties.getProperty(name));
					System.out.println(name+":"+properties.getProperty(name));
				}
			}
			System.out.println("Config File Loaded successfully...");
			return properties;
		}
 		catch (Exception e) 
 		{
 			e.printStackTrace();
			System.out.println("Problem occured! while loading Config properties for configuration file");
			return null;
		}
 		finally
 		{
 			try 
 			{
 				if(inStream!=null)
 				{
 					inStream.close();
 					inStream = null;
 				}
 				if (inputFile != null)
 				{
 					inputFile = null;
 				}
 					
			}
 			catch (Exception e) 
 			{
 				e.printStackTrace();
 				System.out.println("Problem occured in loading config properties! closing the file input stream");
			}
 		}
 	}
	
	public static Properties getProperties()
 	{
		if(properties==null)
		{
			return loadProperties();
		}
		else
		{
			return properties;			
		}
 	}
	
	public static int refreshProperty()
 	{
		int status = 1;
		if(properties!=null)
		{
			properties=null;
			properties = loadProperties();
			status = MerchantConstants.reload_status_success;
			if(properties == null)
			{
			status = MerchantConstants.reload_status_fail;
			}
		}
		else if(properties == null)
		{
			status = MerchantConstants.reload_status_success;
		}
		return status; 
 	}
	
	public static String getProperty(String key)
 	{
		StringBuilder s = null;
		String path = "%CONFIG_HOME%";
		
		if(properties==null)
		{
			loadProperties();
			
			if (hmap.get(key) == null)
				return null;
			
			s = new StringBuilder((String)hmap.get(key));
			
			if (hmap.get(key) != null && hmap.get(key).toString().indexOf(path) != -1)
			{
				return s.replace(s.indexOf(path), s.indexOf(path)+path.length(), (String) hmap.get("CONFIG_HOME")).toString();
			}
			else
			{
				return s.toString();
			}
		}
		else
		{
			if (hmap.get(key) == null)
				return null;
			
			s = new StringBuilder((String)hmap.get(key));
			
			if (hmap.get(key) != null && hmap.get(key).toString().indexOf(path) != -1)
			{
				return s.replace(s.indexOf(path), s.indexOf(path)+path.length(), (String) hmap.get("CONFIG_HOME")).toString();
			}
			else
			{
				return s.toString();
			}
		}
 	}

	static
	{
		Properties _properties = new Properties();
        java.io.InputStream stream = (com.merchant.demo.webservice.utils.ConfigFileUtil.class).getClassLoader().getResourceAsStream("location.properties");
        try
        {
			_properties.load(stream);
		}
        catch (IOException e)
        {
			System.out.println("Could Not Find Location Properties File");
		}
        if(stream == null)
            System.out.println("location.properties not loaded");
        configFileName = _properties.getProperty("location");
        MerchantConstants.config_properties_file= configFileName;
        _properties = null;
        loadProperties();
	}
	
}