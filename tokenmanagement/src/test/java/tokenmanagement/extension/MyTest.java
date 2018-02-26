/**
 * 
 */
package tokenmanagement.extension;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tokenmanagement.common.MyCDIBeanDev;
import tokenmanagement.common.SomeBean;

/**
 * @author web-w
 *
 */
@ExtendWith(WeldJunit5Extension.class)
class MyTest {

	/*
	 * @Inject public MyCDIBean bean;
	 */
	private static EJBContainer ec;
	private static Context ctx;
	/*
	 * @WeldSetup public WeldInitiator weld = WeldInitiator.of(MyCDIBeanDev.class);
	 */

	/*
	 * @WeldSetup private WeldInitiator weld1 = WeldInitiator.of(new
	 * Weld().addBeanClass(MyCDIBeanDev.class));
	 */
	/*
	 * @Inject
	 * 
	 * @AppProperties
	 */
	String greeting;

	@BeforeEach
	void init() {

		/*
		 * weld = new Weld(); container = weld.initialize();
		 */
		// MyCDIBean superService = container.select(MyCDIBean.class).get();
	}

	@After
	void dismis() {
		// weld.shutdown();
	}

	@Test
	void test() throws NamingException {
		
		WeldInitiator weld = WeldInitiator.of(SomeBean.class);
		 WeldContainer weld2 = new Weld().initialize();
		 SomeBean hello = weld2.instance().select(SomeBean.class).get();
		
		 String s = "";
//	        assertEquals("should say Hello World !!!", "Hello World !!!", mainEjb.ping());

		// final MyCDIBeanDev foo = weld.select(MyCDIBeanDev.class).get();
		/*
		 * Assertions.assertNotNull(bean); bean.ping();
		 */
	}
}
