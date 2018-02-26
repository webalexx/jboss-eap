package tokenmanagement.common;

import tokenmanagement.common.IMyCDIBean;


public class MyCDIBeanDev implements IMyCDIBean {
	
	public String ping() {
        return MyCDIBeanDev.class.getSimpleName();
    }

}
