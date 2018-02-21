package tokenmanagement.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;


/*
 * class to read a properties from configs on a class path
 */
@PropertiesFromFile
@Stateless
public class PropertiesReader extends Properties {

	private Properties properties;
	
	
	
	@Produces
	@PropertiesFromFile
	public Properties produceProperties(final InjectionPoint ip) {
		init();
		return this.properties;
	}

	@PropertiesFromFile
	@Produces
	public String produceString(final InjectionPoint ip) {
		return this.properties.getProperty(getKey(ip));
	}

	@PropertiesFromFile
	@Produces
	public int produceInt(final InjectionPoint ip) {
		return Integer.valueOf(this.properties.getProperty(getKey(ip)));
	}

	@PropertiesFromFile
	@Produces
	public boolean produceBoolean(final InjectionPoint ip) {
		return Boolean.valueOf(this.properties.getProperty(getKey(ip)));

	}

	private String getKey(final InjectionPoint ip) {
        return (ip.getAnnotated().isAnnotationPresent(PropertiesFromFile.class) 
        		&& !ip.getAnnotated().getAnnotation(PropertiesFromFile.class).
                    value().isEmpty()) ? ip.getAnnotated()
                                           .getAnnotation(PropertiesFromFile.class)
                                           .value() 
                                       : ip.getMember()
                                           .getName();

    }
	
	

	public Properties getProperties() {
		init();
		return properties;
	}


	@PostConstruct
	public void init() {
		this.properties = new Properties();

		String filename = "/config/application.properties";
		try (final InputStream stream = PropertiesReader.class.getResourceAsStream(filename)) {
//			super.load(stream);
			properties.load(stream);
		} catch (IOException e) {
			System.err.println("Could not read properties from file " + filename + " in classpath. " + e);
		}

	}
}
