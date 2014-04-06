package com.qileyuan.tatala.util;

import java.util.Properties;

/**
 * This is the configuration class which reads the properties from the properties file.
 */
public class Configuration {
    
    public static Properties _properties;
    
    
    /** Creates a new instance of Configuration */
    public Configuration() {
    	
    }
    
    /**
     * Sets the properties
     * @param properties - The properties from the properties file
     */
    public static void setProperties(Properties properties) {
        if(properties==null) {
            try{
                Properties prop = new Properties();
                prop.load(Configuration.class.getClassLoader().getResourceAsStream("tatala.properties"));
                _properties = prop;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            _properties=properties;
        }
    }
    
    /**
     * Gets the property value associated with the key parameter.
     * @param key - This is the key that is used to retrieve the value from the properties file.
     * @return property - This is the value for the key from the properties file.
     */
    public static String getProperty(String key) {
        if(_properties==null){
            setProperties(null);
        }
        return _properties.getProperty(key);
    }
    
    /**
     * Gets the integer value of the property associated with the key parameter
     * @param key - This is the key that is used to retrieve the value from the properties file.
     * @return property - This is the value for the key from the properties file.
     */
    public static int getIntProperty(String key) {
        if(_properties==null){
            setProperties(null);
        }
        return Integer.parseInt(_properties.getProperty(key.trim()));
    }
    
    public static int getIntProperty(String key, int defaultValue) {
    	int retureValue = defaultValue;
    	try {
			retureValue = getIntProperty(key);
		} catch (Exception e) {			
		}
    	return retureValue;
    }
}
