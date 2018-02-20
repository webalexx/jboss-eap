package tokenmanagement.extension;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import java.io.IOException;
import java.util.Properties;

/**
 * Tests all management expects for subsystem, parsing, marshaling, model
 * definition and other Here is an example that allows you a fine grained
 * controller over what is tested and how. So it can give you ideas what can be
 * done and tested. If you have no need for advanced testing of subsystem you
 * look at {@link AbstractSubsystemBaseTest} that testes same stuff but most of
 * the code is hidden inside of test harness
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author Tomaz Cerar
 * @author Marko Strukelj
 */
public class SubsystemParsingTestCase2 extends AbstractSubsystemBaseTest {

	public SubsystemParsingTestCase2() {
		super(SubsystemExtension.SUBSYSTEM_NAME, new SubsystemExtension());
	}

	@Override
	protected Properties getResolvedProperties() {
		Properties properties = new Properties();
		properties.put("jboss.home.dir", System.getProperty("java.io.tmpdir"));
		properties.put("keycloak.jta.lookup.provider", "jboss");
		return properties;
	}

	@Override
	protected String getSubsystemXml() throws IOException {
		return readResource("tokenmanagement.xml");
	}

	@Override
	protected String getSubsystemXsdPath() throws Exception {
		return "schema/tokenmanagement.xsd";
	}

	@Override
	protected String[] getSubsystemTemplatePaths() throws IOException {
		return new String[] { "/schema/tokenmanagement.xml" };
	}
}
