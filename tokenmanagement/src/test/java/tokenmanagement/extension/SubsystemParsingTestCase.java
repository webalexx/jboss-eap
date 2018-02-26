package tokenmanagement.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.dmr.ModelNode;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/*
 * https://github.com/weld/weld-junit/blob/master/junit5/README.md
 * https://www.novatec-gmbh.de/uploads/media/JavaSpektrum_Bean_Testing_2013.pdf
 * https://junit.org/junit5/docs/current/user-guide/
 * patching wildfly with http://www.cdi-spec.org/news/2017/05/15/CDI_2_is_released/
 * 
 */

//@RunWith(CdiRunner.class)
//@RunWith(WeldJUnit4Runner.class)
@ExtendWith(WeldJunit5Extension.class)
class SubsystemParsingTestCase extends AbstractSubsystemBaseTest {

	public SubsystemParsingTestCase(String mainSubsystemName, Extension mainExtension) {
		super(mainSubsystemName, mainExtension);
	}

	// @Inject
	// @AppProperties(name = "subsystem.rest.root")
	// public static String SUBSYSTEM_ROOT1;

	@Test
	void myFirstTest() {
		assertEquals(2, 1 + 1);
	}

	@Test
	public void testXmlVerification() throws Exception {
		// String s = SUBSYSTEM_ROOT1;
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
	}

	@Override
	protected String getSubsystemXml() throws IOException {
		return readResource("tokenmanagement.xml");
	}

	protected String getUsedXml() throws Exception {
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
