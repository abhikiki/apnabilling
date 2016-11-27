package com.abhishek.fmanage.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesFileReader {

	private Logger logger = LoggerFactory.getLogger(PropertiesFileReader.class);
	private static volatile PropertiesFileReader instance = null;
	private Properties prop = new Properties();

	private PropertiesFileReader() {
	}

	public static PropertiesFileReader getInstance() {
		
		if (instance == null) {
			synchronized (PropertiesFileReader.class) {
				if (instance == null) {
					instance = new PropertiesFileReader();
				}
			}
		}
		return instance;
	}

	public String getProperty(final String key) {
		loadProperties();
		return prop.getProperty(key);
	}

	private void loadProperties() {
		InputStream propertiesInputStream = getClass().getClassLoader()
				.getResourceAsStream("restapi.properties");
		try {
			if (propertiesInputStream != null) {
				prop.load(propertiesInputStream);
			}
		} catch (IOException e) {
			logger.error("Error loading property file", e);
		}
	}
}
