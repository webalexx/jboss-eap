package tokenmanagement.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.dmr.ModelNode;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import tokenmanagement.common.PropertiesFromFile;
import tokenmanagement.common.PropertiesReader;

/**
 * Tests all management expects for subsystem, parsing, marshaling, model
 * definition and other Here is an example that allows you a fine grained
 * controler over what is tested and how. So it can give you ideas what can be
 * done and tested. If you have no need for advanced testing of subsystem you
 * look at {@link SubsystemBaseParsingTestCase} that testes same stuff but most
 * of the code is hidden inside of test harness
 *
 * @author A.Karpachev
 */
public class SubsystemParsingTestCase extends AbstractSubsystemBaseTest {

	private PropertiesReader properties;
	private static Weld weld;
	
//	@Inject
//	@PropertiesFromFile("sdfsdf")
//	Properties appProperties;
//
//	@BeforeClass
//	public static void setupClass() {
//		weld = new Weld();
//		try (WeldContainer container = new Weld().initialize()) {
//			container.select(PropertiesReader.class).get();
//		}
//	}
//
//	@AfterClass
//	public static void teardownClass() {
//		weld.shutdown();
//	}
	
	

	public SubsystemParsingTestCase() {
		super(SubsystemExtension.SUBSYSTEM_NAME, (Extension) new SubsystemExtension());
	}

	@Test
	public void testXmlVerification() throws Exception {
		assertTrue(getSubsystemXml().contains(getGeneratedXmlForVeriefication()));
	}

	/**
	 * Tests that the xml is parsed into the correct operations
	 */
	@Test
	public void testParseSubsystem() throws Exception {
		// Parse the subsystem xml into operations
		String subsystemXml = getUsedXml();
		List<ModelNode> operations = super.parse(subsystemXml);

		/// Check that we have the expected number of operations
		Assert.assertEquals(1, operations.size());

		// Check that each operation has the correct content
		ModelNode addSubsystem = operations.get(0);
		Assert.assertEquals(ADD, addSubsystem.get(OP).asString());
		PathAddress addr = PathAddress.pathAddress(addSubsystem.get(OP_ADDR));
		Assert.assertEquals(1, addr.size());
		PathElement element = addr.getElement(0);
		Assert.assertEquals(SUBSYSTEM, element.getKey());
		Assert.assertEquals(SubsystemExtension.SUBSYSTEM_NAME, element.getValue());
	}

	/**
	 * Test that the model created from the xml looks as expected
	 */
	@Test
	public void testInstallIntoController() throws Exception {
		// Parse the subsystem xml and install into the controller
		String subsystemXml = getUsedXml();
		KernelServices services = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();

		// Read the whole model and make sure it looks as expected
		ModelNode model = services.readWholeModel();
		Assert.assertTrue(model.get(SUBSYSTEM).hasDefined(SubsystemExtension.SUBSYSTEM_NAME));
	}

	/**
	 * Starts a controller with a given subsystem xml and then checks that a second
	 * controller started with the xml marshalled from the first one results in the
	 * same model
	 */
	@Test
	public void testParseAndMarshalModel() throws Exception {
		// Parse the subsystem xml and install into the first controller
		String subsystemXml = getUsedXml();
		KernelServices servicesA = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();
		// Get the model and the persisted xml from the first controller
		ModelNode modelA = servicesA.readWholeModel();
		String marshalled = servicesA.getPersistedSubsystemXml();

		// Install the persisted xml from the first controller into a second controller
		KernelServices servicesB = super.createKernelServicesBuilder(null).setSubsystemXml(marshalled).build();
		ModelNode modelB = servicesB.readWholeModel();

		// Make sure the models from the two controllers are identical
		super.compare(modelA, modelB);
	}

	/**
	 * Tests that the subsystem can be removed
	 */
	@Test
	public void testSubsystemRemoval() throws Exception {
		// Parse the subsystem xml and install into the first controller

		String subsystemXml = getUsedXml();
		KernelServices services = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();
		// Checks that the subsystem was removed from the model
		super.assertRemoveSubsystemResources(services);

		// TODO Chek that any services that were installed were removed here
	}

	@Override
	protected String getSubsystemXml() throws IOException {
		// return "<subsystem
		// xmlns\='urn\:com\.commerzunternahmen.tokenmanagement-subsystem\:1.0'><web-context>pseudo</web-context></subsystem>\u0000";
		return readResource("tokenmanagement.xml");
	}

	protected String getUsedXml() throws Exception {
		// Variant 2 return getGeneratedXmlForVeriefication() + "</subsystem>";
		return getSubsystemXml();
	}

	private String getGeneratedXmlForVeriefication() {
		return "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\">";
	}

	@Override
	protected String getSubsystemXsdPath() throws Exception {
		return "schema/tokenmanagement.xsd";
	}

}
