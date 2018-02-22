package tokenmanagement.common;

import javax.enterprise.inject.spi.InjectionPoint;

public interface AppPropertiesService {
	
	String produceProperties(final InjectionPoint propName);
//	String produceProperties(final InjectionPoint confPath, final InjectionPoint propName);
}
