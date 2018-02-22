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
public class AppPropertiesServiceImpl implements AppPropertiesService{

	private Properties properties;
	private String configPath = "/config/application.properties"; 
	
	@Produces
	@AppProperties
	@Override
	public String produceProperties(final InjectionPoint propName) {
		return  this.properties.getProperty(getNameKey(propName));
	}

//	@Produces
//	@AppProperties
//	@Override
//	public String produceProperties(final InjectionPoint propName, final InjectionPoint confPath) {
//		this.configPath = getPathKey(confPath);
//		init();
//		return produceProperties(propName) ;
//	}

//	private String getPathKey(final InjectionPoint ip) {
//        return (ip.getAnnotated().isAnnotationPresent(AppProperties.class) 
//        		&& !ip.getAnnotated().getAnnotation(AppProperties.class).
//                    path().isEmpty()) ? ip.getAnnotated()
//                                           .getAnnotation(AppProperties.class)
//                                           .path() 
//                                       : ip.getMember()
//                                           .getName();
//
//    }
	
	private String getNameKey(final InjectionPoint ip) {
        return (ip.getAnnotated().isAnnotationPresent(AppProperties.class) 
        		&& !ip.getAnnotated().getAnnotation(AppProperties.class).
                    name().isEmpty()) ? ip.getAnnotated()
                                           .getAnnotation(AppProperties.class)
                                           .name() 
                                       : ip.getMember()
                                           .getName();

    }
	
    @PostConstruct
	public void init() {
		this.properties = new Properties();
		try (final InputStream stream = AppPropertiesServiceImpl.class.getResourceAsStream(configPath)) {
//			super.load(stream);
			properties.load(stream);
		} catch (IOException e) {
			System.err.println("Could not read properties from file " + configPath + " in classpath. " + e);
		}
	}
}
