package tokenmanagement.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ApplicationPath("${subsystem.rest.root}")
public class RestApp extends Application {

	private static final Logger logger = Logger.getLogger(RestApp.class);

	public RestApp(@Context ServletContext context, @Context Dispatcher dispatcher) {
		logger.info("--- Start RestApp ---");
		dispatcher.getDefaultContextObjects().put(RestApp.class, this);
		ResteasyProviderFactory.pushContext(RestApp.class, this); // for injection
	}
}
