/**
 * 
 */
package tokenmanagement.extension;

import javax.inject.Inject;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

/**
 * @author web-w
 *
 */
@RunWith(Arquillian.class)
class MyTestAll {

	@Inject
	public MyCDIBean bean;

	/*@WeldSetup
	public WeldInitiator weld = WeldInitiator.of(MyCDIBeanDev.class);*/

/*	@WeldSetup
	private WeldInitiator weld1 = WeldInitiator.of(new Weld().addBeanClass(MyCDIBeanDev.class));
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
	void test() {
//		final MyCDIBeanDev foo = weld.select(MyCDIBeanDev.class).get();
		Assertions.assertNotNull(bean);
		bean.ping();
	}
}
