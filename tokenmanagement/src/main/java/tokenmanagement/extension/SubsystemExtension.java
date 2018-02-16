package tokenmanagement.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static tokenmanagement.logging.TokenManagementLogger.ROOT_LOGGER;

import java.util.List;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemExtension implements Extension {

	/**
	 * The name space used for the {@code substystem} element
	 */
	public static final String NAMESPACE = "urn:com.commerzunternahmen.tokenmanagement-subsystem:1.0";

	/**
	 * The name of our subsystem within the model.
	 */
	public static final String SUBSYSTEM_NAME = "tokenmanagement-subsystem";
	private static final ModelVersion MGMT_API_VERSION = ModelVersion.create(1, 0, 0);

	/**
	 * The parser used for parsing our subsystem
	 */
	private final SubsystemParser SUBSYS_PARSER = new SubsystemParser();

	protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);
	private static final String RESOURCE_NAME = SubsystemExtension.class.getPackage().getName() + ".LocalDescriptions";

	static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
		String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
		return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, SubsystemExtension.class.getClassLoader(),
				true, false);
	}

	@Override
	public void initialize(ExtensionContext context) {
//		ROOT_LOGGER.debug("Activating Token Management Extension");
		final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MGMT_API_VERSION);
		final ManagementResourceRegistration registration = subsystem
				.registerSubsystemModel(SubsystemDefinition.INSTANCE);
		registration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION,
				GenericSubsystemDescribeHandler.INSTANCE);
		Supplier<XMLElementWriter<SubsystemMarshallingContext>> supplier = () -> SUBSYS_PARSER;
		subsystem.registerXMLElementWriter(SUBSYS_PARSER);
//		subsystem.registerXMLElementWriter(supplier);
	}

	@Override
	public void initializeParsers(ExtensionParsingContext context) {
		context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, SUBSYS_PARSER);
	}

	// @Override
	// public void initializeParsers(ExtensionParsingContext context) {
	// Supplier<XMLElementReader<List<ModelNode>>> supplier = () -> parser;
	// context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, supplier);
	// }

	private static ModelNode createAddSubsystemOperation() {
		final ModelNode subsystem = new ModelNode();
		subsystem.get(OP).set(ADD);
		subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);
		return subsystem;
	}

	/**
	 * The subsystem parser, which uses stax to read and write to and from xml
	 */
	private static class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>,
			XMLElementWriter<SubsystemMarshallingContext> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context)
				throws XMLStreamException {
			context.startSubsystemElement(SubsystemExtension.NAMESPACE, false);
			writer.writeEndElement();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
			// Require no content
			ParseUtils.requireNoContent(reader);
			list.add(createAddSubsystemOperation());
		}
	}

}
