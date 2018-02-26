package tokenmanagement.extension;

import javax.inject.Inject;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tokenmanagement.common.AppProperties;

@ExtendWith(WeldJunit5Extension.class)
public class SubsystemParsingTestCase2 {

	
	 
	 @Inject
	 @AppProperties(name = "subsystem.rest.root")
	 public static String SUBSYSTEM_ROOT1;
		
	/*@WeldSetup
	public WeldInitiator weld = WeldInitiator.from(AppPropertiesServiceImpl.class)
			.activate(RequestScoped.class, SessionScoped.class).build();*/

	@BeforeEach
	void initTest() {
		
	}
	
	@AfterEach
	void finishTest() {
		
	}
	
	@Test
	public void testXmlVerification() throws Exception {
	String s = SUBSYSTEM_ROOT1;
	}

}
