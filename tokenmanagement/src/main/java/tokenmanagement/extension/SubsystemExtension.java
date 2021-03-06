package tokenmanagement.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import javax.inject.Inject;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;

import tokenmanagement.common.AppProperties;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemExtension implements Extension {
	
    @Inject
	@AppProperties(name="subsystem.rest.root")
    public static String SUBSYSTEM_ROOT;
	
//	@Inject
//	@AppProperties(name="subsystem.namespace")
	public static String SUBSYSTEM_NAME = "";
	
//	@Inject
//	@AppProperties(name="subsystem.name") 
	public static String NAMESPACE = "";

	private static final ModelVersion MGMT_API_VERSION = ModelVersion.create(1, 0, 0);
	private static final SubsystemParser PARSER = new SubsystemParser();
	protected static final PathElement PATH_SUBSYSTEM = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);
	private static final String RESOURCE_NAME = SubsystemExtension.class.getPackage().getName() + ".LocalDescriptions";

	static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
		String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
		return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, SubsystemExtension.class.getClassLoader(),
				true, false);
	}

	@Override
	public void initialize(ExtensionContext context) {
		final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MGMT_API_VERSION);
		final ManagementResourceRegistration registration = subsystem
				.registerSubsystemModel(SubsystemDefinition.INSTANCE);
		subsystem.registerXMLElementWriter(PARSER);
	}

	@Override
	public void initializeParsers(ExtensionParsingContext context) {
		System.out.println("---------initializeParsers-----------");
		 context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, PARSER);
	}

	private static ModelNode createAddSubsystemOperation() {
		System.out.println("---------createAddSubsystemOperation-----------");
		final ModelNode subsystem = new ModelNode();
		subsystem.get(OP).set(ADD);
		subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);
		return subsystem;
	}
}
