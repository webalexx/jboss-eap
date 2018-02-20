package tokenmanagement.rest;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ApplicationPath("/pseudo")
public class RestApp extends Application {

	public static final String TOKENMANAGEMENT_CONFIG_PARAM_NAME = "com.commerzunternahmen.tokenmanagement-subsystem.Config";
	private static final Logger logger = Logger.getLogger(RestApp.class);
	protected Set<Object> singletons = new HashSet<Object>();
	protected Set<Class<?>> classes = new HashSet<Class<?>>();

	public RestApp(@Context ServletContext context, @Context Dispatcher dispatcher) {
		logger.info("--- Start RestApp ---");
		dispatcher.getDefaultContextObjects().put(RestApp.class, this);
		ResteasyProviderFactory.pushContext(RestApp.class, this); // for injection
	}
}
