package tokenmanagement.extension;

import java.io.IOException;

import org.jboss.as.controller.Extension;
import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WeldJunit5Extension.class)
public class SubsystemParsingTestCase2 extends AbstractSubsystemBaseTest {
	
	

	public SubsystemParsingTestCase2(String mainSubsystemName, Extension mainExtension) {
		super(mainSubsystemName, mainExtension);
	}

//	@Inject
//	@AppProperties(name = "subsystem.rest.root")
//	public static String SUBSYSTEM_ROOT1;

	@Test
	public void testXmlVerification() throws Exception {
//		String s = SUBSYSTEM_ROOT1;
		String s = "";
	}

	@Override
	protected String getSubsystemXml() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
